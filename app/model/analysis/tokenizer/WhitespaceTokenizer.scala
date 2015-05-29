package model.analysis.tokenizer

import model.common.Token
import scala.collection._
/**
 * Created by mehmetgunturkun on 23/05/15.
 */
object WhitespaceTokenizer extends Tokenizer {

  override def tokenize(content: String): immutable.Set[Token] = {
    val map: mutable.Map[String, Token] = mutable.Map.empty[String, Token]
    val wordSet = content.split(" ")
    var position = 1
    wordSet.foreach(word => {
      val maybeToken = map.get(word)
      maybeToken match {
        case Some(token) => {
          token + position
        }
        case None => {
          val token = Token(word, mutable.TreeSet(position))
          map += word -> token
        }
      }
      position += 1
    })
    map.values.toSet
  }
}
