package mihaichris.solr.search;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import mihaichris.solr.BaseSolrTestCase4J;

public class ExtractValueInListTest extends BaseSolrTestCase4J {

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
    @DisplayName("Test get first value from list when first parameter is an autoindexed array.")
    public void getFirstValueFromFunctionWithAutoIndexArray() {

        assertQ("The function should return first value from the list.",
                req("q", "*:*",
                        "qt", "/select",
                        "fl", "vallist(\"[0.1, 0.2, 0.3]\",0)"),
                        "//*[@numFound='3']",
                "//result/doc[1]/float[@name='vallist(\"[0.1, 0.2, 0.3]\",0)'][.=0.1]");
    }

    @Test
    @DisplayName("Test get second value from list when first parameter is an autoindexed array.")
    public void getSecondValueFromFunctionWithAutoIndexArray() {

        assertQ("The function should return second value from the list.",
                req("q", "*:*",
                        "qt", "/select",
                        "fl", "vallist(\"[0.1, 0.2, 0.3]\",1)"),
                        "//*[@numFound='3']",
                "//result/doc[1]/float[@name='vallist(\"[0.1, 0.2, 0.3]\",1)'][.=0.2]");
    }

    @Test
    @DisplayName("Test get first value from list when first parameter is an autoindexed array. Value is returned as float.")
    public void getFirstStringValueFromFunctionWithAutoIndexArray() {

        assertQ("The function should return first value from the list.",
                req("q", "*:*",
                        "qt", "/select",
                        "fl", "vallist(\"[\\\"0.1\\\", 0.2, 0.3]\",0)"),
                        "//*[@numFound='3']",
                "//result/doc[1]/float[@name='vallist(\"[\\\"0.1\\\", 0.2, 0.3]\",0)'][.=0.1]");
    }

    @Test
    @DisplayName("Test throw error when call function wihtout params.")
    public void throwErrorWhenCallFunctionWithoutParams() {

        assertQEx("The function should throw exception.",
                req("q", "*:*",
                        "qt", "/select",
                        "fl", "vallist(\"[0.1, 0.2, 0.3]\")"),
                400);
    }

    @Test
    @DisplayName("Test throw error when get value from list when first parameter is an autoindexed array and index param is not specified.")
    public void throwErrorWhenGetValueFromFunctionWithAutoIndexArrayAndNoIndexParam() {

        assertQEx("The function should throw exception.",
                req("q", "*:*",
                        "qt", "/select",
                        "fl", "vallist(\"[0.1, 0.2, 0.3]\")"),
                400);
    }

    @Test
    @DisplayName("Test return 0.0 when index out of bound and default param is not set.")
    public void return0WhenIndexOutOfBoundAndDefaultValueIsNotSet() {

        assertQ("The function should return 0.0.",
        req("q", "*:*",
                "qt", "/select",
                "fl", "vallist(\"[0.1, 0.2, 0.3]\",10)"),
                "//*[@numFound='3']",
        "//result/doc[1]/float[@name='vallist(\"[0.1, 0.2, 0.3]\",10)'][.=0.0]");
    }

    @Test
    @DisplayName("Test return default value when index out of bound and default param is set.")
    public void getDefaultValueWhenIndexOutOfBoundAndDefaultValueIsSet() {

        assertQ("The function should return 0.0.",
        req("q", "*:*",
                "qt", "/select",
                "fl", "vallist(\"[0.1, 0.2, 0.3]\",10,1.3)"),
                "//*[@numFound='3']",
        "//result/doc[1]/float[@name='vallist(\"[0.1, 0.2, 0.3]\",10,1.3)'][.=1.3]");
    }

    @Test
    @DisplayName("Test throw exception when index out of bound and default value is not float value.")
    public void throwErrorWhenIndexOutOfBoundAndDefaultValueIsNotFloat() {

        assertQEx("The function should throw exception.",
        req("q", "*:*",
                "qt", "/select",
                "fl", "vallist(\"[0.1, 0.2, 0.3]\",10, \\\"\\\")"),
        400);
    }

    @Test
    @DisplayName("Test throw exception when index is not out of bound and default value is not float value.")
    public void throwErrorWhenIndexIsNotOutOfBoundAndDefaultValueIsNotFloat() {

        assertQEx("The function should throw exception.",
        req("q", "*:*",
                "qt", "/select",
                "fl", "vallist(\"[0.1, 0.2, 0.3]\",1, \\\"\\\")"),
        400);
    }

    @Test
    @DisplayName("Test get second value from list when second parameter is a field.")
    public void getFirstValueFromFunctionWithField() {

        assertQ("The function should return second value from the list. id=1",
                req("q", "*:*",
                        "qt", "/select",
                        "fl", "vallist(\"[0.1, 0.2, 0.3]\",id)"),
                        "//*[@numFound='3']",
                "//result/doc[1]/float[@name='vallist(\"[0.1, 0.2, 0.3]\",id)'][.=0.2]");
    }

    @Test
    @DisplayName("Test get second value from list when second parameter is a field and default value is set as value.")
    public void getFirstValueFromFunctionWithFieldAndDefaultValueIsSetAsValue() {

        assertQ("The function should return second value from the list. id=1",
                req("q", "*:*",
                        "qt", "/select",
                        "fl", "vallist(\"[0.1, 0.2, 0.3]\",id,0)"),
                        "//*[@numFound='3']",
                "//result/doc[1]/float[@name='vallist(\"[0.1, 0.2, 0.3]\",id,0)'][.=0.2]");
    }

    @Test
    @DisplayName("Test get second value from list when second parameter is a field and default value is set as field.")
    public void getFirstValueFromFunctionWithFieldAndDefaultValueIsSetAsField() {

        assertQ("The function should return second value from the list. id=1, product_id=1",
                req("q", "*:*",
                        "qt", "/select",
                        "fl", "vallist(\"[0.1, 0.2, 0.3]\",id,product_id)"),
                        "//*[@numFound='3']",
                "//result/doc[1]/float[@name='vallist(\"[0.1, 0.2, 0.3]\",id,product_id)'][.=0.2]");
    }

    @Test
    @DisplayName("Test return default value when second parameter is field and index is out of bound. id=1, product_id=1")
    public void returnDefaultValueWhenSecondParameterIsFieldAndIndexIsOutOfBound() {

        assertQ("The function should return first value from the list.",
                req("q", "*:*",
                        "qt", "/select",
                        "fl", "vallist(\"[0.1]\",id,product_id)"),
                        "//*[@numFound='3']",
                "//result/doc[1]/float[@name='vallist(\"[0.1]\",id,product_id)'][.=1.0]");
    }

    @Test
    @DisplayName("Test return 0.0 value when second parameter is field and index is out of bound and default value is not set. id=1")
    public void return0WhenSecondParameterIsFieldAndIndexIsOutOfBoundAndDefaultValueIsNotSet() {

        assertQ("The function should return first value from the list.",
                req("q", "*:*",
                        "qt", "/select",
                        "fl", "vallist(\"[0.1]\",id)"),
                        "//*[@numFound='3']",
                "//result/doc[1]/float[@name='vallist(\"[0.1]\",id)'][.=0.0]");
    }
}
