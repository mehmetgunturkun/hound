package model.common

import java.io.File
import java.nio.charset.CodingErrorAction

import scala.io.{BufferedSource, Codec, Source}

/**
 * Created by mehmetgunturkun on 23/05/15.
 */
class Document(val id: Int, val title: String, val file: File) {
  def getContent(): String = {
    val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)
    val buffer: BufferedSource = Source.fromFile(file)(codec)
    val string = buffer.mkString
    buffer.close()
    string
  }

  override def toString(): String = s"Document($id, $title)"
}


