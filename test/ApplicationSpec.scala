import model.analyze.StandardAnalyzer
import model.data.Paragraph
import model.tokenizer.{BiGramTokenizer, WhitespaceTokenizer}
import model.tools.{IndexDataSeeder, ParagraphCollection}
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
    "send 404 on a bad request" in new WithApplication {

      val collection = ParagraphCollection("resources/")
      val start = System.currentTimeMillis()
      val source = collection.getCollectionSource()

      val index = model.data.Index[Paragraph](StandardAnalyzer)
      println(index.dictionary.get("will"))
      IndexDataSeeder.seedParagraphs(index, collection)

      val stop = System.currentTimeMillis()
      println(stop - start)
      println(index.dictionary.get("will"))
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "render the index page" in new WithApplication {
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain ("Your new application is ready.")
    }
  }
}
