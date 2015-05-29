package controllers

import model.common.Document
import model.search.query.{ScoredQuery, PhraseQuery, OperationType, MatchQuery}
import model.search.scoring.{ScoredDocument, VectorSpaceModel}
import play.api.libs.json._
import play.api.mvc._

object Application extends Controller {

  implicit val writeDocumentwithoutcontent: Writes[Document] = new Writes[Document] {
    def writes(c: Document): JsValue = {
      Json.obj(
        "id" -> Json.toJson(c.id),
        "title" -> Json.toJson(c.title),
        "content" -> Json.toJson(c.getContent())
      )
    }
  }

//  implicit val writeDocument: Writes[Document] = new Writes[Document] {
//    def writes(c: Document): JsValue = {
//      Json.obj(
//        "id" -> Json.toJson(c.id),
//        "title" -> Json.toJson(c.title),
//        "content" -> c.getContent()
//      )
//    }
//  }

  implicit val writeScoredDocument: Writes[ScoredDocument] = new Writes[ScoredDocument] {
    def writes(c: ScoredDocument): JsValue = {
      Json.obj(
        "document" -> Json.toJson(c.doc),
        "score" -> Json.toJson(c.score)
      )
    }
  }

  val engine = model.index.Engine

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def pageAnd = Action {
    Ok(views.html.and())
  }

  def pageOr = Action {
    Ok(views.html.or())
  }

  def pagePhrase = Action {
    Ok(views.html.phrase())
  }

  def pageScore = Action {
    Ok(views.html.score())
  }

  def searchWithAnd = Action(parse.json) {
    request => {
      val queryJson = request.body \ "query"
      queryJson match {
        case JsString(queryString) => {
          val query = MatchQuery(queryString, OperationType.AND)
          val startTime = System.currentTimeMillis()
          val list = engine.search(query, "gutenberg")
          println(System.currentTimeMillis() - startTime)
          Ok(Json.toJson(list))
        }
        case _ => {
          BadRequest
        }
      }
    }
  }

  def searchWithOr = Action(parse.json) {
    request => {
      val queryJson = request.body \ "query"
      queryJson match {
        case JsString(queryString) => {
          val query = MatchQuery(queryString, OperationType.OR)
          val list = engine.search(query, "gutenberg")
          Ok(Json.toJson(list))
        }
        case _ => {
          BadRequest
        }
      }
    }
  }

  def searchWithPhrase = Action(parse.json) {
    request => {
      val queryJson = request.body \ "query"
      queryJson match {
        case JsString(queryString) => {
          val query = PhraseQuery(queryString)
          val list = engine.search(query, "gutenberg")
          Ok(Json.toJson(list))
        }
        case _ => {
          BadRequest
        }
      }
    }
  }

  def searchWithScore = Action(parse.json) {
    request => {
      val queryJson = request.body \ "query"
      queryJson match {
        case JsString(queryString) => {
          val query = ScoredQuery(queryString, VectorSpaceModel)
          val list = engine.search(query, "gutenberg")
          Ok(Json.toJson(list.take(20)))
        }
        case _ => {
          BadRequest
        }
      }
    }
  }

  def spellCorrection = Action(parse.json) {
    request => {
      val termJson = request.body \ "term"
      termJson match {
        case JsString(term) => {
          val correction = engine.indexMap.head._2.doSpellCheck(term)
          Ok(Json.toJson(correction))
        }
        case _ => {
          BadRequest
        }
      }
    }
  }

  def delete = Action(parse.json) {
    request => {
      val docIdJson = request.body \ "docId"
      docIdJson match {
        case JsNumber(docId) => {
          engine.delete(docId.toInt, "gutenberg")
          Ok
        }
        case _ => {
          BadRequest
        }
      }
    }
  }

}