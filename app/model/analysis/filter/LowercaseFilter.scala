package model.analysis.filter

/**
 * Created by mehmetgunturkun on 23/05/15.
 */
object LowercaseFilter extends Filter{
  override def filter(content: String): String = content.toLowerCase
}
