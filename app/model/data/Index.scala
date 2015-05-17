package model.data

import model.analyze.{StandardAnalyzer, Analyzer}
import model.query.{Query, SearchResult}

/**
 * Created by mehmetgunturkun on 17/05/15.
 */
case class Index[T <: AbstractDocument](override val analyzer: Analyzer) extends AbstractIndex[T] {
  override val dictionary: Dictionary[T] = Dictionary[T]()
}

abstract class AbstractIndex[R <: AbstractDocument]  {

  protected val analyzer: Analyzer
  protected val dictionary: Dictionary[R]

  def search(query: Query): SearchResult = ???
  def insert(doc: R): Unit = {
    val content = doc.getContent()
    content.foreach(subContent => {
      val tokenSet = analyzer.analyze(subContent)
      dictionary.update(tokenSet, doc)
    })
  }
}
