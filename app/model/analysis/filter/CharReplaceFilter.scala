package model.analysis.filter

/**
 * Created by mehmetgunturkun on 23/05/15.
 */
case class CharReplaceFilter(regex: String, target: String) extends Filter {
  def filter(content: String): String = {
    content.replaceAll(regex, target)
  }
}
