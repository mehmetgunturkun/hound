package model.analysis.filter

/**
 * Created by mehmetgunturkun on 23/05/15.
 */
trait Filter {
  def filter(word: String): String
}
