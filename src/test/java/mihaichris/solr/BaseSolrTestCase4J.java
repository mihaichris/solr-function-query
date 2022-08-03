package mihaichris.solr;

import java.util.Arrays;
import java.util.List;

import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.SolrInputDocument;
import org.junit.BeforeClass;
import org.xml.sax.SAXException;

public abstract class BaseSolrTestCase4J extends SolrTestCaseJ4 {

        @BeforeClass
        public static void beforeClass() throws Exception {
                initCore("solrconfig.xml", "schema.xml", "test-files/solr");
        }

        public static void clearIndexDocuments() throws SAXException {
                assertNull(h.validateUpdate(delQ("*:*")));
                assertNull(h.validateUpdate(commit()));
        }

        public static void indexSampleDocuments() throws SAXException {
                SolrInputDocument doc1 = new SolrInputDocument();
                doc1.addField("id", "1");
                doc1.addField("product_id", "1");
                List<String> tags_category_traffic1 = Arrays.asList("set cadou", "aparat ras", "aparate ras", "ras",
                                "set cadou barbati", "aparat barbierit", "set barbierit", "aparat", "gilet", "gillet",
                                "lama ras",
                                "set barbati", "spuma ras", "barbierit", "big", "bricheta", "lame ras", "portofel",
                                "rezerve aparat ras");
                doc1.addField("tags_category_traffic", tags_category_traffic1);
                assertNull(h.validateUpdate(adoc(doc1)));

                SolrInputDocument doc2 = new SolrInputDocument();
                doc2.addField("id", "2");
                doc2.addField("product_id", "2");
                List<String> tags_category_traffic2 = Arrays.asList("set cadou", "aparat ras", "aparate ras", "ras",
                                "set cadou barbati", "aparat barbierit", "set barbierit", "aparat", "gilet", "gillet",
                                "lama ras",
                                "set barbati", "spuma ras", "barbierit", "big", "bricheta", "lame ras", "portofel",
                                "rezerve aparat ras");
                doc2.addField("tags_category_traffic", tags_category_traffic2);
                assertNull(h.validateUpdate(adoc(doc2)));

                SolrInputDocument doc3 = new SolrInputDocument();
                doc3.addField("id", "3");
                doc3.addField("product_id", "3");
                List<String> tags_category_traffic3 = Arrays.asList("lame ras", "aparat ras", "aparat ras clasic",
                                "barbierit",
                                "razor", "safety razor", "aparat barbierit", "muhle", "muhle r89", "pamatuf", "ras",
                                "set cadou barbati", "aparat barbierit", "set barbierit", "aparat", "gilet", "gillet");
                doc3.addField("tags_category_traffic", tags_category_traffic3);
                assertNull(h.validateUpdate(adoc(doc3)));
                
                assertNull(h.validateUpdate(commit()));
        }
}
