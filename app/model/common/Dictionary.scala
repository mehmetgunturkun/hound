package model.common
import scala.collection._
/**
 * Created by mehmetgunturkun on 16/05/15.
 */
case class Dictionary[R](source: mutable.Map[String, PostingList[R]] = mutable.Map.empty[String, PostingList[R]]) {

  def update(tokenSet: Set[String], item: R) = {
    tokenSet.foreach(token => {
      if (source.contains(token)) {
        val postingList = source.get(token).get
        postingList += item
      } else {
        val postingList = new PostingList(item)
        source += (token -> postingList)
      }
    })
  }
}
