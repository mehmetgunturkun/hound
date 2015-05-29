package model.search.query

import model.common.{Token, Document, PostingList}
import model.search.scoring.ScoredDocument


/**
 * Created by mehmetgunturkun on 23/05/15.
 */
case class MatchQuery(query: String, operation: OperationType) extends Query {
  def mergeResult(resultList: Map[Token, PostingList]): List[ScoredDocument] = {
    if (resultList.nonEmpty) {
      val finalPostingList = rawMergeResult(resultList)
      finalPostingList.map(entry => ScoredDocument(entry.doc, 1.0)).toList
    } else {
      List.empty[ScoredDocument]
    }
  }

  def rawMergeResult(resultList: Map[Token, PostingList]): PostingList = {
    val finalPostingList = operation match {
      case OperationType.AND => {
        resultList.foldLeft(PostingList.empty)((mergedPostings, nextEntry) => {
          nextEntry match {
            case (token, nextPostingList) => {
              mergedPostings.intersect(nextPostingList)
            }
            case _ => {
              println("Match Failure")
              PostingList.empty
            }
          }
        })
      }
      case OperationType.OR => {
        resultList.tail.foldLeft(resultList.head._2)((mergedPostings, nextEntry) => {
          nextEntry match {
            case (token, nextPostingList) => {
              mergedPostings.union(nextPostingList)
            }
            case _ => {
              PostingList.empty
            }
          }
        })
      }
    }
    finalPostingList
  }
}

sealed trait OperationType
object OperationType {
  case object OR extends OperationType
  case object AND extends OperationType
}

