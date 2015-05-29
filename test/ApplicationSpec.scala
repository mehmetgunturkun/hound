import model.index.SearchEngine
import model.utils.ParagraphExtractor
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {



  "Application" should {

    "def" in {

//      ParagraphExtractor.extract("resources/test")

      val engine = new SearchEngine()
      val s1 = System.currentTimeMillis()
      engine.seed("resources", "gutenberg")
      val s2 = System.currentTimeMillis()
      println(s2-s1)
//      val query = model.search.query.MatchQuery("john robinson", model.search.query.OperationType.AND)
      val query = model.search.query.PhraseQuery("john robinson")
//      val sQuery = query.withScoring()

      val start = System.currentTimeMillis()
      val a = engine.search(query, "gutenberg")
      val stop = System.currentTimeMillis()
      println(stop-start, a.mkString("\n"))


      val index = engine.indexMap.get("gutenberg").get
      println(index.doSpellCheck("irane"))


      1 === 1
    }


    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "render the index page" in new WithApplication{
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain ("Your new application is ready.")
    }
  }
}
