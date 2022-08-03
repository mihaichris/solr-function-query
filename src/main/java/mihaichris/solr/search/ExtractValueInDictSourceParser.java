package mihaichris.solr.search;

import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.valuesource.LiteralValueSource;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.search.FunctionQParser;
import org.apache.solr.search.SyntaxError;
import org.apache.solr.search.ValueSourceParser;

import mihaichris.solr.search.function.ExtractValueInDictFunction;

public class ExtractValueInDictSourceParser extends ValueSourceParser {

    public void init(NamedList namedList) {
    }

    @Override
    public ValueSource parse(FunctionQParser fp) throws SyntaxError {
        ValueSource list = fp.parseValueSource();
        ValueSource key = fp.parseValueSource();
        ValueSource defaultValue = parseDefaultValue(fp);
        return new ExtractValueInDictFunction(list, key, defaultValue);
    }

    private ValueSource parseDefaultValue(FunctionQParser fp) throws SyntaxError {
        ValueSource defaultValue;
        if (fp.hasMoreArguments()) {
            defaultValue = fp.parseValueSource();
        } else {
            defaultValue = new LiteralValueSource("0");
        }
        return defaultValue;
    }
}
