package fr.emcastro.jdbctyper;

import fr.emcastro.jdbctyper.transform.TypeTransformer;

/**
 * Transforms {@link JsonBox} to/from {@code String} for JDBC operations.
 */
public class JsonBoxTypeTransformer implements TypeTransformer<JsonBox, String> {

    @Override
    public Class<JsonBox> getAppType() {
        return JsonBox.class;
    }

    @Override
    public Class<String> getSqlType() {
        return String.class;
    }

    @Override
    public String toSql(JsonBox value) {
        return value.value();
    }

    @Override
    public JsonBox fromSql(String value) {
        return new JsonBox(value);
    }
}
