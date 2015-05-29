package model.search.scoring

import model.common.Document

/**
 * Created by mehmetgunturkun on 24/05/15.
 */
case class ScoredDocument(doc: Document, score: Double) {
  def updateScore(newScore: Double): ScoredDocument = ScoredDocument(doc, score + newScore)
}

object ScoredDocument {
  implicit val ord = new Ordering[ScoredDocument] {
    def compare(ac1: ScoredDocument, acc2: ScoredDocument): Int = {
      if (ac1.score < acc2.score) {
        1
      } else if (ac1.score > acc2.score) {
        -1
      } else {
        1
      }
    }
  }
}