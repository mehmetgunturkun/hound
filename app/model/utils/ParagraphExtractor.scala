package model.utils

import java.io.{PrintWriter, File}

/**
 * Created by mehmetgunturkun on 24/05/15.
 */
object ParagraphExtractor {

  def extract(directory: String): Unit = {
    val dir: File = new File(directory)
    if (dir.isDirectory) {
      val fileList: List[java.io.File] = dir.listFiles().toList
      fileList.foreach(file => {
        if (file.getName.endsWith("txt")) {
          var pId: Int = 1
          val fileName = file.getName
          val parentName = file.getParent
          U.withIterator(file)(lines => {
            while(lines.hasNext) {
              val paragraphName = s"$parentName/paragraph/P$pId-$fileName"
              val paragraphFile = new File(paragraphName)
              val paragraph = lines.take(30).mkString(" ")
              val pw = new PrintWriter(paragraphFile)
              pw.write(paragraph)
              pw.close()
              pId += 1
            }
          })
          println(s"$fileName is extracted into $pId Paragraphs.")
        }
      })
    } else {
      throw new Exception("Given directory is not valid!")
    }
  }
}
