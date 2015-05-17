package model.query

/**
 * Created by mehmetgunturkun on 17/05/15.
 */
case class MatchQuery(query: String, operatorType: OperatorType = OperatorType.OR) extends Query

sealed trait OperatorType
object OperatorType {
  case object AND extends OperatorType
  case object OR extends OperatorType
}

