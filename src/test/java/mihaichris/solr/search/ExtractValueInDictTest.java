package mihaichris.solr.search;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import mihaichris.solr.BaseSolrTestCase4J;

public class ExtractValueInDictTest extends BaseSolrTestCase4J {

        @Override
        @Before
        public void setUp() throws Exception {
                super.setUp();
                clearIndexDocuments();
                indexSampleDocuments();
        }

        @Override
        @After
        public void tearDown() throws Exception {
                super.tearDown();
                clearIndexDocuments();
        }

        @Test
        @DisplayName("Test get first value from object by key.")
        public void getFirstValueFromFunctionWithJsonObject() {
                assertQ("The function should return first value from the object.",
                                req("q", "*:*",
                                                "qt", "/select",
                                                "fl", "valdict(\"{\\\"93\\\":0.9}\",\"93\")"),
                                                "//*[@numFound='3']",
                                "//result/doc[1]/float[@name='valdict(\"{\\\"93\\\":0.9}\",\"93\")'][.=0.9]");
        }

        @Test
        @DisplayName("Test get second value from object by key.")
        public void getSecondValueFromFunctionWithJsonObject() {
                assertQ("The function should return second value from the object.",
                                req("q", "*:*",
                                                "qt", "/select",
                                                "fl", "valdict(\"{\\\"93\\\":0.9, \\\"91\\\":0.3}\",\"91\")"),
                                                "//*[@numFound='3']",
                                "//result/doc[1]/float[@name='valdict(\"{\\\"93\\\":0.9, \\\"91\\\":0.3}\",\"91\")'][.=0.3]",
                                "//result/doc[2]/float[@name='valdict(\"{\\\"93\\\":0.9, \\\"91\\\":0.3}\",\"91\")'][.=0.3]");
        }

        @Test
        @DisplayName("Test get value when second param is not string.")
        public void getValuesFromFunctionWithJsonObjectWithSecondParamIsNotString() {
                assertQ("The function should return first value.",
                                req("q", "*:*",
                                                "qt", "/select",
                                                "fl",
                                                "valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.3, \\\"3\\\":0.1}\",1)"),
                                                "//*[@numFound='3']",
                                "//result/doc[1]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.3, \\\"3\\\":0.1}\",1)'][.=0.9]",
                                "//result/doc[2]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.3, \\\"3\\\":0.1}\",1)'][.=0.9]",
                                "//result/doc[3]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.3, \\\"3\\\":0.1}\",1)'][.=0.9]");
        }

        @Test
        @DisplayName("Test throw error when no param is set.")
        public void throwErrorWhenNoParamIsSet() {
                assertQEx("The function should throw an error.",
                                req("q", "*:*", "qt", "/select", "fl", "valdict()"), 400);
        }

        @Test
        @DisplayName("Test throw error when second param is not set.")
        public void throwErrorWhenSecondParamIsNotSet() {
                assertQEx("The function should throw an error.",
                                req("q", "*:*", "qt", "/select", "fl", "valdict(\"{\\\"93\\\":0.9}\")"), 400);
        }

        @Test
        @DisplayName("Return 0 when key not found in object.")
        public void return0WhenKeyNotFoundInObject() {
                assertQ("The function should return 0 value.",
                                req("q", "*:*",
                                                "qt", "/select",
                                                "fl",
                                                "valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.3, \\\"3\\\":0.1}\",\"4\")"),
                                                "//*[@numFound='3']",
                                "//result/doc[1]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.3, \\\"3\\\":0.1}\",\"4\")'][.=0.0]",
                                "//result/doc[2]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.3, \\\"3\\\":0.1}\",\"4\")'][.=0.0]",
                                "//result/doc[3]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.3, \\\"3\\\":0.1}\",\"4\")'][.=0.0]");
        }

        @Test
        @DisplayName("Test get default value when key not found and default value is set.")
        public void getDefaultValueWhenKeyNotFoundAndDefaultValueIsSet() {
                assertQ("The function should return default value.",
                                req("q", "*:*",
                                                "qt", "/select",
                                                "fl",
                                                "valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.4, \\\"3\\\":0.1}\",\"4\", 0.3)"),
                                                "//*[@numFound='3']",
                                "//result/doc[1]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.4, \\\"3\\\":0.1}\",\"4\", 0.3)'][.=0.3]",
                                "//result/doc[2]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.4, \\\"3\\\":0.1}\",\"4\", 0.3)'][.=0.3]",
                                "//result/doc[3]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.4, \\\"3\\\":0.1}\",\"4\", 0.3)'][.=0.3]");
        }

        @Test
        @DisplayName("Test get default value when key not found and default value is set and default value is not float.")
        public void getDefaultValueWhenKeyNotFoundAndDefaultValueIsSetAndValueIsNotFloat() {
                assertQ("The function should return default value.",
                                req("q", "*:*",
                                                "qt", "/select",
                                                "fl",
                                                "valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.4, \\\"3\\\":0.1}\",\"4\", \"0.3\")"),
                                                "//*[@numFound='3']",
                                "//result/doc[1]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.4, \\\"3\\\":0.1}\",\"4\", \"0.3\")'][.=0.3]",
                                "//result/doc[2]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.4, \\\"3\\\":0.1}\",\"4\", \"0.3\")'][.=0.3]",
                                "//result/doc[3]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.4, \\\"3\\\":0.1}\",\"4\", \"0.3\")'][.=0.3]");
        }

        @Test
        @DisplayName("Test get value from object by key with second param as field.")
        public void getValuesFromFunctionWithJsonObjectWithSecondParamAsField() {
                assertQ("The function should return value by id.",
                                req("q", "*:*",
                                                "qt", "/select",
                                                "fl",
                                                "valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.3, \\\"3\\\":0.1}\",id)"),
                                                "//*[@numFound='3']",
                                "//result/doc[1]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.3, \\\"3\\\":0.1}\",id)'][.=0.9]",
                                "//result/doc[2]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.3, \\\"3\\\":0.1}\",id)'][.=0.3]",
                                "//result/doc[3]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.3, \\\"3\\\":0.1}\",id)'][.=0.1]");
        }

        @Test
        @DisplayName("Test get value from object by key with second param as field and default value.")
        public void getValuesFromFunctionWithJsonObjectWithSecondParamAsFieldAndDefaultValue() {
                assertQ("The function should return value by id.",
                                req("q", "*:*",
                                                "qt", "/select",
                                                "fl",
                                                "valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.3, \\\"3\\\":0.1}\",id, 0.3)"),
                                                "//*[@numFound='3']",
                                "//result/doc[1]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.3, \\\"3\\\":0.1}\",id, 0.3)'][.=0.9]",
                                "//result/doc[2]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.3, \\\"3\\\":0.1}\",id, 0.3)'][.=0.3]",
                                "//result/doc[3]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.3, \\\"3\\\":0.1}\",id, 0.3)'][.=0.1]");
        }

        @Test
        @DisplayName("Test get value from object by key with second param as field and default value and default value is not float.")
        public void getValuesFromFunctionWithJsonObjectWithSecondParamAsFieldAndDefaultValueAndDefaultValueIsNotFloat() {
                assertQ("The function should return value by id.",
                                req("q", "*:*",
                                                "qt", "/select",
                                                "fl",
                                                "valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.3, \\\"3\\\":0.1}\",id, \"0.3\")"),
                                                "//*[@numFound='3']",
                                "//result/doc[1]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.3, \\\"3\\\":0.1}\",id, \"0.3\")'][.=0.9]",
                                "//result/doc[2]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.3, \\\"3\\\":0.1}\",id, \"0.3\")'][.=0.3]",
                                "//result/doc[3]/float[@name='valdict(\"{\\\"1\\\":0.9, \\\"2\\\":0.3, \\\"3\\\":0.1}\",id, \"0.3\")'][.=0.1]");
        }

        @Test
        @DisplayName("Test get default value when key not found and default value is field.")
        public void getDefaultValueWhenKeyNotFoundAndDefaultValueIsField() {
                assertQ("The function should return value by id.",
                                req("q", "*:*",
                                                "qt", "/select",
                                                "fl",
                                                "valdict(\"{\\\"11\\\":0.9, \\\"22\\\":0.3, \\\"33\\\":0.1}\",id, product_id)"),
                                                "//*[@numFound='3']",
                                "//result/doc[1]/float[@name='valdict(\"{\\\"11\\\":0.9, \\\"22\\\":0.3, \\\"33\\\":0.1}\",id, product_id)'][.=1.0]",
                                "//result/doc[2]/float[@name='valdict(\"{\\\"11\\\":0.9, \\\"22\\\":0.3, \\\"33\\\":0.1}\",id, product_id)'][.=2.0]",
                                "//result/doc[3]/float[@name='valdict(\"{\\\"11\\\":0.9, \\\"22\\\":0.3, \\\"33\\\":0.1}\",id, product_id)'][.=3.0]");
        }
}
