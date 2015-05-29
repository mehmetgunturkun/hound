import model.utils.ParagraphExtractor
import org.specs2.mutable.Specification

/**
 * Created by mehmetgunturkun on 24/05/15.
 */
class ParagraphExtraction extends Specification {

  "Paragraph extraction" should {
    "create paragraphs from files" in {
      ParagraphExtractor.extract("/Users/mehmetgunturkun/MasterThesis/ceng543-informationretrieval/collection")
      1 === 1
    }
  }
}
