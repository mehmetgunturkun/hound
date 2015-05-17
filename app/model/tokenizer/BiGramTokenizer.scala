package model.tokenizer

import model.analyze.Tokenizer
import model.data.Dictionary

import scala.collection._
/**
 * Created by mehmetgunturkun on 17/05/15.
 */
case class BiGramTokenizer() extends Tokenizer {
  val termDictionary: Dictionary[String] = Dictionary[String]()

  override def tokenize(content: String): immutable.Set[String] = {
    val builder: mutable.Builder[String, immutable.Set[String]] = immutable.Set.newBuilder[String]
    content.split(" ").foreach(token => {
      val biGrams = getBiGramTokens(token)
      termDictionary.update(biGrams, token)
      builder += token
    })
    builder.result()
  }

  def getBiGramTokens(word: String): Set[String] = {
    s"$$$word$$".sliding(2).toSet
  }
}
