package model.tokenizer

import model.analyze.Tokenizer

/**
 * Created by mehmetgunturkun on 17/05/15.
 */
object WhitespaceTokenizer extends Tokenizer {
  override def tokenize(content: String): Set[String] = {
    val tokenSet: Set[String] = content.split(" ").toSet
    tokenSet
  }
}
