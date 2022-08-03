package mihaichris.solr.search;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import mihaichris.solr.BaseSolrTestCase4J;

public class ExtractValueInListSourceParserTest extends BaseSolrTestCase4J {

    private ExtractValueInListSourceParser extractValueInListSourceParser;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.extractValueInListSourceParser = new ExtractValueInListSourceParser();
    }

    @Test
    @DisplayName("Ensure that parser is instance of the target.")
    public void instance() {
        assertTrue("object is not instance of class: " + ExtractValueInListSourceParser.class,
                extractValueInListSourceParser instanceof ExtractValueInListSourceParser);
    }
}
