package model.analysis.analyzer

import model.analysis.filter.{CharReplaceFilter, Filter}
import model.analysis.tokenizer.Tokenizer
import model.common.Token

/**
 * Created by mehmetgunturkun on 23/05/15.
 */
object StandardAnalyzer extends Analyzer {
  val filters: Seq[Filter] = Seq(model.analysis.filter.LowercaseFilter,
    CharReplaceFilter("[^a-zA-Z\\d]", " "))
  val tokenizer: Tokenizer= model.analysis.tokenizer.WhitespaceTokenizer
  val stopWords = Set("the","that", "is", "are")

  override def analyze(content: String): Set[Token] = {
    val filteredContent: String = applyFilters(content)
    val tokenSet: Set[Token] = tokenizer.tokenize(filteredContent)
    tokenSet
  }

  def applyFilters(content: String): String = {
    filters.foldLeft(content)((filteredContent, nextFilter) => {
      nextFilter.filter(filteredContent)
    })
  }
}
