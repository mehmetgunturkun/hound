package model.common
import scala.collection._
/**
 * Created by mehmetgunturkun on 23/05/15.
 */
case class PositionalEntry(doc: Document, positionalList: mutable.Set[Int]) {
  def tf() = positionalList.size
}

object PositionalEntry {
  implicit val ord = new Ordering[PositionalEntry] {
    def compare(ac1: PositionalEntry, acc2: PositionalEntry): Int = {
      if (ac1.doc.id > acc2.doc.id) {
        1
      } else if (ac1.doc.id < acc2.doc.id) {
        -1
      } else {
        0
      }
    }
  }
}