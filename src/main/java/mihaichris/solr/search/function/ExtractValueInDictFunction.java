package mihaichris.solr.search.function;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.docvalues.FloatDocValues;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class ExtractValueInDictFunction extends ValueSource {

    private final class FloatDocValuesExtension extends FloatDocValues {
        private FloatDocValuesExtension(ValueSource vs) {
            super(vs);
        }

        @Override
        public float floatVal(int doc) throws IOException {
            return ExtractValueInDictFunction.this.func(doc);
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(ExtractValueInDictFunction.class);

    protected ValueSource list;
    protected ValueSource key;
    protected ValueSource defaultValue;

    private ObjectMapper objectMapper;

    private FunctionValues functionValueList;
    private FunctionValues functionValueKey;
    private FunctionValues functionValueDefaultValue;

    public ExtractValueInDictFunction(ValueSource list, ValueSource key, ValueSource defaultValue) {
        this.list = list;
        this.key = key;
        this.defaultValue = defaultValue;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public FunctionValues getValues(Map context, LeafReaderContext readerContext) throws IOException {
        functionValueList = this.list.getValues(context, readerContext);
        functionValueKey = this.key.getValues(context, readerContext);
        functionValueDefaultValue = this.defaultValue.getValues(context, readerContext);
        return new FloatDocValuesExtension(this);
    }

    public float func(int doc) throws IOException {
        try {
            String key = functionValueKey.objectVal(doc).toString();
            Map<String, Object> dict = parseAsMap(functionValueList.strVal(doc));
            return Float.valueOf(dict.get(key).toString());
        } catch (Exception exception) {
            LOG.error(exception.toString());
        }
        return Float.valueOf(functionValueDefaultValue.strVal(doc));
    }

    @Override
    public int hashCode() {
        return this.list.hashCode() + this.key.hashCode() + this.defaultValue.hashCode();
    }

    public String name() {
        return "valdict";
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public String description() {
        return "The function can be valdict(list, key) or valdict(list, key, defaultValue). The function returns float values from a dictionary by key.";
    }

    @Override
    public boolean equals(Object o) {
        if (this.getClass() != o.getClass())
            return false;
        ExtractValueInListFunction other = (ExtractValueInListFunction) o;
        return this.name().equals(other.name()) && this.list.equals(other.list) && this.key.equals(other.index)
                && this.defaultValue.equals(other.defaultValue);
    }

    private Map<String, Object> parseAsMap(String stringList) throws Exception {
        return objectMapper.readValue(stringList, new TypeReference<Map<String, Object>>() {
        });
    }
}
