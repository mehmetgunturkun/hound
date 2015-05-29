package model.utils

import model.analysis.tokenizer.BiGramTokenizer
import model.common.Token

import scala.collection._

/**
 * Created by mehmetgunturkun on 24/05/15.
 */
object SpellingChecker extends AbstractSpellingChecker{
  val vocabulary: mutable.Map[String, mutable.TreeSet[String]] = mutable.Map.empty[String, mutable.TreeSet[String]]

  def merge(tokenSet: Set[Token]): Unit = {
    tokenSet.foreach(token => {
      val word = token.token
      val biGramTokenList = BiGramTokenizer.tokenize(word).toList
      biGramTokenList.foreach(token => {
        val maybeTermList = vocabulary.get(token.token)
        maybeTermList match {
          case Some(termList) => {
            termList += word
          }
          case None => {
            val termList = mutable.TreeSet(word)
            vocabulary += token.token -> termList
          }
        }
      })
    })
  }

  def check(word: String): immutable.Set[String] = {
    val biGramTokenList = BiGramTokenizer.tokenize(word).toList
    val combinationList = biGramTokenList.combinations(biGramTokenList.size - 2).toSet
    combinationList.flatMap({
      case tokenList => {
        tokenList.tail.foldLeft(vocabulary.get(tokenList.head.token).get)((prevTermList, nextToken) => {
          prevTermList.intersect(vocabulary.get(nextToken.token).get)
        })
      }
    })
  }

  private def intersection[T](s1: mutable.TreeSet[T], s2: mutable.TreeSet[T]) = {
    (s1, s2) match {
      case (r, p) if r.nonEmpty && p.nonEmpty => r.intersect(p)
      case (r, p) if r.isEmpty => p
      case (r, p) if p.isEmpty => r
    }
  }
}

trait AbstractSpellingChecker {
  def merge(tokenSet: Set[Token]): Unit
  def check(word: String): immutable.Set[String]
}
