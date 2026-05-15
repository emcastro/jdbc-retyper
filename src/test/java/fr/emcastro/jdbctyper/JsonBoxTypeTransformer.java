package fr.emcastro.jdbctyper;

import fr.emcastro.jdbctyper.exception.TypeConversionException;
import fr.emcastro.jdbctyper.transform.TypeTransformer;

/**
 * Transforms {@link JsonBox} to/from {@code String} for JDBC operations.
 * Registered in {@code JdbcConfig} via {@code @PostConstruct}.
 */
public class JsonBoxTypeTransformer implements TypeTransformer<JsonBox> {

    @Override
    public Class<JsonBox> getType() {
        return JsonBox.class;
    }

    @Override
    public Class<?> getSqlType() {
        return String.class;
    }

    @Override
    public Object toSql(JsonBox value) {
        return value.value();
    }

    @Override
    public JsonBox fromSql(Object value) {
        if (value instanceof String str) {
            return new JsonBox(str);
        }
        throw new TypeConversionException("Cannot convert " + value.getClass() + " to JsonBox", null);
    }
}
