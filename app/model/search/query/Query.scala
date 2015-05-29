package model.search.query

import model.common.{Token, Document, PostingList}
import model.search.scoring.{ScoredDocument, VectorSpaceModel, ScoringAlgorithm}

/**
 * Created by mehmetgunturkun on 23/05/15.
 */
trait Query {
  val query: String
  def isStopWord(token: Token): Boolean = false
  def mergeResult(resultList: Map[Token, PostingList]): List[ScoredDocument]
  def rawMergeResult(resultList: Map[Token, PostingList]): PostingList
  def withScoring(algorithm: ScoringAlgorithm = VectorSpaceModel) = ScoredQuery(this.query, algorithm)
}
