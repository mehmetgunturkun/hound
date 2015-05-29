package model.index

import java.io.File

import model.common.{Token, Document}
import model.search.query.Query
import model.search.scoring.ScoredDocument
import scala.collection._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by mehmetgunturkun on 23/05/15.
 */

object Engine extends SearchEngine

class SearchEngine() {
  val indexMap: Map[String, AbstractIndex] = Map("gutenberg" -> Index)

  def index(file: File, indexKey: String) = {
    val maybeIndex = indexMap.get(indexKey)
    maybeIndex match {
      case Some(index) => {
        index.insert(file)
      }
      case None => {
        throw new Exception(s"There is no existing index with name: $indexKey")
      }
    }
  }

  def seed(directory: String, indexKey: String) {
    withExistingIndex(indexKey)(index => {
      val dir: File = new File(directory)
      if (dir.isDirectory) {
        val fileList: List[java.io.File] = dir.listFiles().toList
        val fileGroupList = fileList.grouped(8)
        fileGroupList.foreach(fileList => {
          val futureResultList = fileList.map(file => {
            Future(index.getTokenDocPair(file))
          })
          val start = System.currentTimeMillis()
          val oneResult = Future.sequence(futureResultList)
          val resultList = Await.result(oneResult, Duration(30, SECONDS))
          resultList.foreach({
            case (doc, tokenSet) => {
              val tList = tokenSet.grouped(8)
              tList.foreach(t => {
                val tr = t.map(y => {
                  Future(index.dictionary +(doc, y))
                })
                val twoResult = Future.sequence(tr)
                Await.result(twoResult, Duration(30, SECONDS))
              })
            }
          })
          val stop = System.currentTimeMillis()
          println(stop - start)
        })
      } else {
        throw new Exception("Given directory is not valid!")
      }
      index.getStatistics
    })
  }

  def recover(): Unit = {
    indexMap.foreach({
      case (key, index) => index.recover()
    })
  }


  def search(query: Query, indexKey: String): List[ScoredDocument] = {
    val maybeIndex = indexMap.get(indexKey)
    maybeIndex match {
      case Some(index) => {
        val resultList = index.retrieve(query)
        println("RS", resultList.head._2.size())
        println("deleted", index.deletedSet.size)
        val r = query.mergeResult(resultList)
        println("FRS", r.size)
        r
      }
      case None => {
        throw new Exception(s"There is no existing index with name: $indexKey")
      }
    }
  }

  def delete(docId: Int, indexKey: String): Unit = {
    withExistingIndex(indexKey)(index => {
      index.delete(docId)
    })
  }

  def withExistingIndex[T](indexKey: String)(body: AbstractIndex => T): T = {
    val maybeIndex = indexMap.get(indexKey)
    maybeIndex match {
      case Some(index) => {
        body(index)
      }
      case None => {
        throw new Exception(s"There is no existing index with name: $indexKey")
      }
    }
  }
}
