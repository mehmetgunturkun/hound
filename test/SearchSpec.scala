import model.index.{Engine, SearchEngine}
import model.search.query.{PhraseQuery, OperationType, MatchQuery}
import org.specs2.mutable.Specification

/**
 * Created by mehmetgunturkun on 24/05/15.
 */
class SearchSpec extends Specification {


  "Search Engine" should {
    val s1 = System.currentTimeMillis()
    println("Started")
    val engine = Engine
    engine.seed("../test/paragraph", "gutenberg")
    val s2 = System.currentTimeMillis()
    println(s2 - s1)

    "return results for match query" in {
      val query = MatchQuery("Henry the another Fourth", OperationType.AND)
      val result = engine.search(query, "gutenberg")
      println("*"*100)
      println(query)
      println("*"*100)
      println(result.mkString("\n"))
      println("*"*100)
      result.size mustEqual 1
    }

    "return results for match query" in {
      val query = PhraseQuery("Henry the Fourth")
      val result = engine.search(query, "gutenberg")
      println(query)
      println("*"*100)
      println(result.mkString("\n"))
      println("*"*100)
      result.size mustEqual 7
    }

    "return results for match query" in {
      val query = PhraseQuery("Henry the Fourth").withScoring()
      val result = engine.search(query, "gutenberg")
      println(query)
      println("*"*100)
      println(result.mkString("\n"))
      println("*"*100)
      result.size mustEqual 7
    }
  }
}
