package model.index

import java.io.File
import java.util.concurrent.atomic.AtomicReference

import model.analysis.analyzer.Analyzer
import model.common._
import model.search.query.Query
import model.utils.{AbstractSpellingChecker, SpellingChecker}

import scala.collection._

/**
 * Created by mehmetgunturkun on 23/05/15.
 */

object Index extends AbstractIndex {

  var idGenerator: Int = 40000
  val analyzer: Analyzer = model.analysis.analyzer.StandardAnalyzer
  val dictionary: Dictionary = new DictionaryOnDisk()
  val deletedSet: mutable.Set[Int] = mutable.Set.empty[Int]

  val checker: AbstractSpellingChecker = SpellingChecker

  def doSpellCheck(word: String): immutable.Set[String] = checker.check(word)

  def insert(file: File): Unit = {
    val document: Document = generateDocument(file)
    println(s"${file.getName} indexing started.")
    val tokenSet: Set[Token] = analyzer.analyze(document.getContent())
    checker.merge(tokenSet)
    tokenSet.foreach(token => {
      dictionary + (document, token)
    })
    println(s"${file.getName} indexed. Status = ${dictionary.size}")
  }

  def getTokenDocPair(file: File) = {
    val document: Document = generateDocument(file)
    println(s"${document.id} - ${file.getName} indexing started.")
    val tokenSet: Set[Token] = analyzer.analyze(document.getContent())
    (document, tokenSet)
  }

  def delete(docId: Int): Unit = deletedSet += docId

  def generateDocument(file: File): Document = {
    idGenerator += 1
    new Document(idGenerator, file.getName, file)
  }

  def getStatistics: Unit = {
    println(dictionary.size)
//    println(dictionary.dictionary.values.map(t => t.size()).max)
  }

  def recover() {
    dictionary.recover()
  }

  def retrieve(query: Query): immutable.Map[Token, PostingList] = {
    val tokenSet = analyzer.analyze(query.query)
    val setBuilder: mutable.Builder[(Token, PostingList), immutable.Map[Token, PostingList]] = immutable.Map.newBuilder[Token, PostingList]
    tokenSet.foreach(token => {
      if (!query.isStopWord(token)) {
        val maybePostingList = dictionary.get(token)
        maybePostingList match {
          case Some(postingList) => {
            setBuilder += ( token -> postingList)
          }
          case _ => {}
        }
      }
    })
    setBuilder.result()
  }
}

abstract class AbstractIndex {
  val analyzer: Analyzer
  val dictionary: Dictionary
  val deletedSet: mutable.Set[Int]

  def recover(): Unit
  def getTokenDocPair(file: File): (Document, Set[Token])
  def doSpellCheck(word: String): immutable.Set[String]
  def delete(docId: Int)
  def insert(file: File)
  def generateDocument(file: File): Document
  def getStatistics: Unit
  def retrieve(query: Query): immutable.Map[Token, PostingList]
}