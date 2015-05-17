package model.tools

import java.io.File
import java.nio.charset.CodingErrorAction
import scala.collection._
import model.data.{Paragraph, Document}

import scala.io.{Codec, Source}

/**
 * Created by mehmetgunturkun on 17/05/15.
 */

case class DocumentCollection(dir: String) extends Collection[Document] {
  var id: Int = 1
  def getCollectionSource(): List[Document] = {
    val builder: mutable.Builder[Document, immutable.List[Document]] = immutable.List.newBuilder[Document]
    val directory: File = new File(dir)
    if (directory.isDirectory) {
      val fileList: Array[File] = directory.listFiles()
      fileList.foreach(file => {
        val title: String = file.getName
        val document: Document = Document(id, title, file)
        builder += document
        id += 1
      })
      builder.result()
    } else {
      throw new NoSuchElementException(s"Given directory is not a directory. Dir = $dir")
    }
  }
}

case class ParagraphCollection(dir: String) extends Collection[Paragraph] {
  var id: Int = 1
  def getCollectionSource(): List[Paragraph] = {
    val builder: mutable.Builder[Paragraph, immutable.List[Paragraph]] = immutable.List.newBuilder[Paragraph]
    val directory: File = new File(dir)
    if (directory.isDirectory) {
      val fileList: Array[File] = directory.listFiles()

      fileList.foreach(file => {
        val title: String = file.getName
        val paragraphList = getParagraphs(title, file)
        builder ++= paragraphList
        id += 1
      })
      builder.result()
    } else {
      throw new NoSuchElementException(s"Given directory is not a directory. Dir = $dir")
    }
  }

  def getParagraphs(title: String, file: File): List[Paragraph] = {
    val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)
    val iterator = Source.fromFile(file)(codec)
    val lines = iterator.getLines()
    val builder: mutable.Builder[Paragraph, immutable.List[Paragraph]] = immutable.List.newBuilder[Paragraph]

    while (lines.hasNext) {
      val paragraphContent = lines.take(10).mkString(" ")
      val paragraph = Paragraph(id, title, paragraphContent)
      builder += paragraph
      id += 1
    }
    builder.result()
  }
}

trait Collection[T] {
  def getCollectionSource(): List[T]
}
