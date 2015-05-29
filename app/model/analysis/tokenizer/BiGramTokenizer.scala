package model.analysis.tokenizer

import model.common.Token

/**
 * Created by mehmetgunturkun on 24/05/15.
 */
object BiGramTokenizer extends Tokenizer {
  def tokenize(content: String): Set[Token] = {
    val extendedContent = s"$$$content$$"
    val tokenSet = extendedContent.sliding(2).toSet
    tokenSet.map(token => Token(token))
  }
}
