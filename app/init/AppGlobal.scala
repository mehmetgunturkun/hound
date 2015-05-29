package init

import model.index.Engine
import play.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by mehmetgunturkun on 24/05/15.
 */
object AppGlobal extends GlobalSettings {
  override def onStart(app: Application): Unit = {
    super.onStart(app)
    println("<<< APP IS STARTING >>>")
    val engine = Engine

    engine.recover()
    println("Done")

//    val seedResult = Future {
//      engine.seed("/Users/mehmetgunturkun/MasterThesis/ceng543-informationretrieval/collection/paragraph", "gutenberg")
//    }
//    seedResult.onSuccess({
//      case _ => {
//        println("Indexing Done")
//      }
//    })
    println("<<< APP IS STARTED >>>")
  }
}
