package mihaichris.solr.search.function;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.function.FunctionValues;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.queries.function.docvalues.FloatDocValues;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class ExtractValueInListFunction extends ValueSource {

    private final class FloatDocValuesExtension extends FloatDocValues {
        private FloatDocValuesExtension(ValueSource vs) {
            super(vs);
        }

        @Override
        public float floatVal(int doc) throws IOException {
            return ExtractValueInListFunction.this.func(doc);
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(ExtractValueInListFunction.class);

    protected ValueSource list;
    protected ValueSource index;
    protected ValueSource defaultValue;

    private ObjectMapper objectMapper;

    private FunctionValues functionValueList;
    private FunctionValues functionValueIndex;
    private FunctionValues functionValueDefaultValue;

    public ExtractValueInListFunction(ValueSource list, ValueSource index, ValueSource defaultValue) {
        this.list = list;
        this.index = index;
        this.defaultValue = defaultValue;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public FunctionValues getValues(Map context, LeafReaderContext readerContext) throws IOException {
        functionValueList = this.list.getValues(context, readerContext);
        functionValueIndex = this.index.getValues(context, readerContext);
        functionValueDefaultValue = this.defaultValue.getValues(context, readerContext);
        return new FloatDocValuesExtension(this);
    }

    public float func(int doc) throws IOException {
        try {
            int index = Integer.valueOf(functionValueIndex.objectVal(doc).toString());
            List<Object> list = parseAsList(functionValueList.strVal(doc));
            return Float.valueOf(list.get(index).toString());
        } catch (Exception exception) {
            LOG.error(exception.toString());
        }

        return Float.valueOf(functionValueDefaultValue.strVal(doc));
    }

    public String name() {
        return "vallist";
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public String description() {
        return "The function can be vallist(list, index) or vallist(list, index, defaultValue). The function returns float values from a list by index.";
    }

    @Override
    public boolean equals(Object o) {
        if (this.getClass() != o.getClass())
            return false;
        ExtractValueInListFunction other = (ExtractValueInListFunction) o;
        return this.name().equals(other.name()) && this.list.equals(other.list) && this.index.equals(other.index)
                && this.defaultValue.equals(other.defaultValue);
    }

    @Override
    public int hashCode() {
        return this.list.hashCode() + this.index.hashCode() + this.defaultValue.hashCode();
    }

    private List<Object> parseAsList(String stringList) throws Exception {
        return objectMapper.readValue(stringList,
                new TypeReference<List<Object>>() {
                });
    }
}
