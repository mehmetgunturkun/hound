package model.common

import java.io.{PrintWriter, File}
import scala.collection.mutable
import scala.io.Source
import scala.util.Random

/**
 * Created by mehmetgunturkun on 25/05/15.
 */
class DictionaryOnDisk extends Dictionary {
  var documentCount: Int = 0
  val DataDirectory: String = "/tmp/backup"
  val DocumentDirectory: String = "/Users/mehmetgunturkun/MasterThesis/ceng543-informationretrieval/collection/paragraph"
  val dictionary: mutable.Map[String, File] = mutable.Map.empty[String, File]
  val cache: Cache = new Cache()

  def +(doc: Document, token: Token): Unit = {
    if (token.token.nonEmpty) {
      try {
        val maybePostingFile = dictionary.get(token.token)
        maybePostingFile match {
          case Some(postingFile) => {
            val postingList = deserialize(postingFile)
            postingList +(doc, token)
            serialize(postingList, postingFile)
          }
          case None => {
            documentCount += 1
            val postingList = PostingList()
            postingList +(doc, token)
            val postingFile = new File(s"$DataDirectory/${token.token}")
            serialize(postingList, postingFile)
            dictionary += (token.token -> postingFile)
          }
        }
      } catch {
        case ex: Exception => {
          ex.printStackTrace()
          println("=" * 100)
          println(token)
          println("=" * 100)
        }
      }
    }
  }

  def get(token: Token): Option[PostingList] =  {
    if (token.token.nonEmpty) {
      val maybePostingListOnCache = cache.get(token)
      maybePostingListOnCache match {
        case Some(postingListOnCache) => {
          println(s"Cache Hit(${token.token})")
          Some(postingListOnCache)
        }
        case None => {
          println(s"Cache Miss(${token.token})")
          val maybePostingFile = dictionary.get(token.token)
          maybePostingFile match {
            case Some(postingFile) => {
              val plist = deserialize(postingFile)
              cache.add(token, plist)
              Some(plist)
            }
            case None => {
              None
            }
          }
        }
      }
    } else {
      None
    }
  }
  def size(): Int = dictionary.size

  def deserialize(file: File): PostingList = {
    val regex = """\$(.*)\$(.*)\$(.*)\$""".r
    val buffer = Source.fromFile(file)
    val content = buffer.getLines()
    val pList = PostingList()
    content.foreach(line => {
      val regex(id, title, list) = line
      val doc = new Document(id.toInt, title, new File(s"$DocumentDirectory/$title") )
      val intList = list.split(",").toSeq.map(_.toInt)
      val set = mutable.Set[Int](intList:_*)
      val entry = PositionalEntry(doc, set)
      pList.source += entry
    })
    buffer.close()
    pList
  }

  def serialize(postingList: PostingList, file: File) = {
    val stringBuilder = new mutable.StringBuilder()
    postingList.foreach(e => {
      val header = s"$$${e.doc.id}$$${e.doc.title}"
      val list = e.positionalList.mkString(",")

      stringBuilder append header
      stringBuilder append s"$$$list$$"
      stringBuilder append "\n"
    })
    val printer = new PrintWriter(file)
    printer.print(stringBuilder.result())
    printer.close()
  }

  def recover(): Unit = {
    val directory = new File(DataDirectory)
    val dataStore = directory.listFiles().toList.map(file => {
      file.getName -> file
    }).toMap
    dictionary ++= dataStore
  }

  override def toString(): String = s"""Dictionary(${dictionary.mkString(", ")})"""

}

class Cache() {
  val CACHE_SIZE = 10000
  var entryCount = 0
  val source: mutable.Map[String, PostingList] = mutable.Map.empty[String, PostingList]

  def add(doc: Document, token: Token): Unit = {
    if (entryCount == CACHE_SIZE) {
      purge()
    } else {
      val maybePostingList = source.get(token.token)
      maybePostingList match {
        case Some(postingList) => {
          postingList + (doc, token)
        }
        case None => {
          entryCount += 1
          val postingList = PostingList()
          postingList + (doc, token)
          source += (token.token -> postingList)
        }
      }
    }
  }

  def add(token: Token, pList: PostingList): Unit = {
    if (entryCount == CACHE_SIZE) {
      purge()
    } else {
      source += (token.token -> pList)
    }
  }

  def get(token: Token): Option[PostingList] = source.get(token.token)
  def purge() = {
    val keyList = source.keySet.toList
    val rand = Random.nextInt(keyList.size)
    val token = keyList(rand)
    source.remove(token)
  }
}