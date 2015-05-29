package model.search.scoring

import model.index.Index

/**
 * Created by mehmetgunturkun on 24/05/15.
 */
object VectorSpaceModel extends ScoringAlgorithm {
  def computeSimilarity(tf: Int, df: Int): Double = {
    val N = Index.idGenerator
    val score = (1 + scala.math.log(tf + 1))*scala.math.log(N/df)
    score
  }
}
