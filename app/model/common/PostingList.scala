package model.common
import scala.collection._

/**
 * Created by mehmetgunturkun on 23/05/15.
 */
case class PostingList(source: mutable.TreeSet[PositionalEntry] = mutable.TreeSet.empty[PositionalEntry]) extends AbstractPostingList {

  def size(): Int = source.size
  def df(): Int = source.size

  def + (doc: Document, token: Token): Unit = {
    val entry = PositionalEntry(doc, token.positionList)
    source += entry
  }

  def intersect(other: PostingList): PostingList = {
    (source, other.source) match {
      case (r, p) if r.nonEmpty && p.nonEmpty => {
        val i = r.intersect(p)
        PostingList(i)
      }
      case (r, p) if r.isEmpty => PostingList(p)
      case (r, p) if p.isEmpty => PostingList(r)
    }
  }

  def union(other: PostingList): PostingList = {
    (source, other.source) match {
      case (r, p) if r.nonEmpty && p.nonEmpty => PostingList(r.union(p))
      case (r, p) if r.isEmpty => PostingList(p)
      case (r, p) if p.isEmpty => PostingList(r)
    }
  }

  def map[T](f: PositionalEntry => T): Set[T] = source.map(f)
  def foreach[T](f: PositionalEntry => Unit): Unit = source.foreach(f)
  def filter(f: PositionalEntry => Boolean): Set[PositionalEntry] = source.filter(f)
  override def toString(): String = s"""PostingList($source)"""

}

object PostingList {
  def empty = PostingList()
}

trait AbstractPostingList {
  def size(): Int
  def df(): Int
  def + (doc: Document, token: Token)
  def intersect(other: PostingList): PostingList
  def union(other: PostingList): PostingList
  def map[T](f: PositionalEntry => T): Set[T]
  def filter(f: PositionalEntry => Boolean): Set[PositionalEntry]
  override def toString(): String
}

