package model.analyze


/**
 * Created by mehmetgunturkun on 16/05/15.
 */
trait Tokenizer {
  def tokenize(content: String): Set[String]
}
