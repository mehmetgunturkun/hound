package model.search.scoring


/**
 * Created by mehmetgunturkun on 24/05/15.
 */
trait ScoringAlgorithm {
  def computeSimilarity(tf: Int, df: Int): Double
}
