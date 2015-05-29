package model.analysis.analyzer

import model.analysis.filter.Filter
import model.analysis.tokenizer.Tokenizer
import model.common.Token

/**
 * Created by mehmetgunturkun on 23/05/15.
 */
trait Analyzer {
  val filters: Seq[Filter]
  val tokenizer: Tokenizer
  def analyze(content: String): Set[Token]
}
