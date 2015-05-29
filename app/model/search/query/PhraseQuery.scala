package model.search.query

import model.common.{Token, Document, PostingList}
import model.search.scoring.ScoredDocument

import scala.collection.mutable.ArrayBuffer

/**
 * Created by mehmetgunturkun on 23/05/15.
 */
case class PhraseQuery(query: String) extends Query {
  def mergeResult(resultList: Map[Token, PostingList]): List[ScoredDocument] = {
    val map: scala.collection.mutable.Map[Int, PositionalDocument] = scala.collection.mutable.Map.empty[Int, PositionalDocument]
    resultList.foreach({
      case (token, postingList) => {
        postingList.foreach(entry => {
          val document = entry.doc
          val maybePosition = map.get(document.id)
          maybePosition match {
            case Some(positional) => {
              positional.add(document, token.token, entry.positionalList)
            }
            case None => {
              val positional = new PositionalDocument(document, scala.collection.mutable.Map(token.token -> entry.positionalList))
              map += document.id -> positional
            }
          }
        })
      }
    })
    val tokenSet =resultList.keySet
    val arrayBuffer = ArrayBuffer[ScoredDocument]()
    map.foreach({
      case (docId, positionalDoc) if positionalDoc.containsAll(resultList.keySet.map(_.token)) => {
        val isOk = positionalDoc.checkPosition(tokenSet.toList)
        if (isOk) arrayBuffer += ScoredDocument(positionalDoc.doc, 1.0)
      }
      case _ => {}
    })
    arrayBuffer.toList
  }

  def rawMergeResult(resultList: Map[Token, PostingList]): PostingList = {
    resultList.tail.foldLeft(resultList.head._2)((mergedPostings, nextEntry) => {
      nextEntry match {
        case (token, nextPostingList) => {
          mergedPostings.intersect(nextPostingList)
        }
        case _ => {
          PostingList.empty
        }
      }
    })
  }
}

case class PositionalDocument(doc: Document, map: scala.collection.mutable.Map[String, scala.collection.mutable.Set[Int]]) {
  def add(doc: Document, token: String, positionSet: scala.collection.mutable.Set[Int]) = {
    val maybeSet = map.get(token)
    maybeSet match {
      case Some(set) => set ++= positionSet
      case None => map += token -> positionSet
    }
  }

  def containsAll(tokenSet: Set[String]) = tokenSet.forall(token => map.contains(token))

  def checkPosition(tokenSet: List[Token]): Boolean = {
    tokenSet match {
      case Nil => false
      case x ::  Nil => true
      case x :: y :: Nil => {
        val firstPosition = map.get(x.token).get
        val secondPosition = map.get(y.token).get
        val nextPosition = getNextPositions(x, y, firstPosition.toSet)
        val i = secondPosition.intersect(nextPosition).size
        i > 0
      }
      case x :: y :: z => {
        val firstPosition = map.get(x.token).get
        val secondPosition = map.get(y.token).get
        val nextPosition = getNextPositions(x, y, firstPosition.toSet)
        val i = secondPosition.intersect(nextPosition).size
        (i > 0) && checkPosition(y :: z)
      }
    }
  }

  def getNextPositions(firstToken: Token, secondToken: Token, firstPositions: Set[Int]): Set[Int] = {
    val distance = secondToken - firstToken
    firstPositions.map(_ + distance)
  }

}