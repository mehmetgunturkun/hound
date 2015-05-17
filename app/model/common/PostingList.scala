package model.common

import scala.collection.mutable.ArrayBuffer

/**
 * Created by mehmetgunturkun on 16/05/15.
 */
trait AbstractPostingList[T] {
  protected val list: ArrayBuffer[T]  = ArrayBuffer[T]()
  def += (item: T) = list += item
}

class PostingList[T](item: T) extends AbstractPostingList[T] {
  override protected val list = ArrayBuffer(item)
  override def toString() = s"PostingList(${list.mkString(", ")})"
}
