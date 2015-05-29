package model.common
import scala.collection._

/**
 * Created by mehmetgunturkun on 23/05/15.
 */
case class Token(token: String, positionList: mutable.TreeSet[Int] = mutable.TreeSet.empty[Int]) {
  def + (newPosition: Int) = positionList += newPosition
  def - (other: Token) = positionList.head - other.positionList.head

}
