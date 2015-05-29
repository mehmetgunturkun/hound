package model.utils

import java.io.File
import java.nio.charset.CodingErrorAction

import scala.io.{Source, BufferedSource, Codec}

/**
 * Created by mehmetgunturkun on 24/05/15.
 */
object U {
  def withIterator[T](file: File)(body: Iterator[String] => T) = {
    val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)
    val buffer: BufferedSource = Source.fromFile(file)(codec)
    val lines = buffer.getLines()
    body(lines)
  }
}
