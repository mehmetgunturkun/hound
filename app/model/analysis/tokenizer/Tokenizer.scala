package model.analysis.tokenizer

import model.common.Token

/**
 * Created by mehmetgunturkun on 23/05/15.
 */
trait Tokenizer {
  def tokenize(line: String): Set[Token]
}
