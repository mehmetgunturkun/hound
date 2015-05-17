package model.analyze

import model.tokenizer.{BiGramTokenizer, WhitespaceTokenizer}

/**
 * Created by mehmetgunturkun on 16/05/15.
 */
case class Analyzer(name: String,
                    filters: List[Filter],
                    tokenizer: Tokenizer) {
  def analyze(content: String): Set[String] = {
    val tokenSet: Set[String] = tokenizer.tokenize(content)
    val filteredTokenSet: Set[String] = filters.foldLeft(tokenSet)((filteredTokens, nextFilter) => {
      filteredTokens.map(token => nextFilter.filter(token))
    })
    filteredTokenSet
  }
}

object StandardAnalyzer extends Analyzer("Standard", List.empty[Filter], WhitespaceTokenizer)
