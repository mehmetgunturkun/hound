package model.tools

import model.data.{Paragraph, Document, Index}

/**
 * Created by mehmetgunturkun on 17/05/15.
 */
object IndexDataSeeder {
  def seed(index: Index[Document], collection: DocumentCollection): Unit = {
    collection.getCollectionSource().foreach(document => {
      index.insert(document)
    })
  }

  def seedParagraphs(index: Index[Paragraph], collection: ParagraphCollection): Unit = {
    collection.getCollectionSource().foreach(paragraph => {
      index.insert(paragraph)
    })
  }
}
