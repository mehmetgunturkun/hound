package model.common
import scala.collection._
/**
 * Created by mehmetgunturkun on 23/05/15.
 */
case class DictionaryOnMemory() extends Dictionary {

  var documentCount: Int = 0
  val dictionary: mutable.Map[String, PostingList] = mutable.Map.empty[String, PostingList]
  def +(doc: Document, token: Token): Unit = {
    val maybePostingList = dictionary.get(token.token)
    maybePostingList match {
      case Some(postingList) => {
        postingList + (doc, token)
      }
      case None => {
        documentCount += 1
        val postingList = PostingList()
        postingList + (doc, token)
        dictionary += (token.token -> postingList)
      }
    }
  }
  def size(): Int = dictionary.size
  def get(token: Token): Option[PostingList] = dictionary.get(token.token)
  def recover(): Unit = {}
  override def toString(): String = s"""Dictionary(${dictionary.mkString(", ")})"""

}

trait Dictionary {
  def get(token: Token): Option[PostingList]
  def +(doc: Document, token: Token): Unit
  def size(): Int
  def recover(): Unit
}
