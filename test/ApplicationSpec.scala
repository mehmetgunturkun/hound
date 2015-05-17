import model.tokenizer.{BiGramTokenizer, WhitespaceTokenizer}
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

  val wTokenizer = WhitespaceTokenizer
  val bTokenizer = BiGramTokenizer()

  "Application" should {
    "send 404 on a bad request" in new WithApplication {
      println(wTokenizer.tokenize("mehmem cem gunturkun"))
      println(bTokenizer.tokenize("mehmem cem gunturkun"))
      println(bTokenizer.termDictionary)
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
