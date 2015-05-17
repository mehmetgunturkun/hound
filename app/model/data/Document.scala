package model.data

import java.io.File

/**
 * Created by mehmetgunturkun on 16/05/15.
 */

case class Document(id: Int, title: String, source: File)  extends AbstractDocument {
  def getContent(): Iterator[String] = Iterator("")
}

case class Paragraph(id: Int, title: String, source: String) extends AbstractDocument {
  def getContent(): Iterator[String] = Iterator(source)
}

trait AbstractDocument {
  def getContent(): Iterator[String]
}


