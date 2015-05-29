package model.search.query

import model.common.{PositionalEntry, Document, PostingList, Token}
import model.search.scoring.{ScoredDocument, VectorSpaceModel, ScoringAlgorithm}
import scala.collection._
import scala.collection.immutable.TreeSet

/**
 * Created by mehmetgunturkun on 24/05/15.
 */
case class ScoredQuery(query: String, algorithm: ScoringAlgorithm) extends Query {
  override def isStopWord(token: Token): Boolean = {
    ScoredQuery.stopWords.contains(token.token)
  }
  def mergeResult(resultList: immutable.Map[Token, PostingList]): List[ScoredDocument] = {
    val map: mutable.Map[Int, ScoredDocument] = mutable.Map.empty[Int, ScoredDocument]
    println(resultList.size)
    resultList.foreach({
      case (token, postingList) => {
        println(token.token, postingList.size())
        postingList.foreach(entry => {

          val doc = entry.doc
          val tf = entry.tf()
          val df = postingList.df()
          val score = algorithm.computeSimilarity(tf, df)

          val maybeDocument = map.get(doc.id)
          val scoredDocument = maybeDocument match {
            case Some(document) => {
              document.updateScore(score)
            }
            case None => {
              ScoredDocument(doc, score)
            }
          }
          map += (doc.id -> scoredDocument)
        })
      }
    })
    println("Map", map.size)
    map.values.toList.sortBy(t => t.score).reverse
  }

  override def withScoring(algorithm: ScoringAlgorithm = VectorSpaceModel) = this

  def rawMergeResult(resultList: immutable.Map[Token, PostingList]): PostingList = ???
}

object ScoredQuery {
  val stopWords = Set("the")
}


