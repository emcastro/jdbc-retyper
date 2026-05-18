package fr.emcastro.jdbcretyper.transform;

import org.duckdb.JsonNode;

import fr.emcastro.jdbcretyper.JsonBox;

/**
 * Reads {@link JsonBox} from JDBC {@link JsonNode} columns.
 *
 * <p>DuckDB's JDBC driver returns JSON columns as a {@link JsonNode}.
 */
public class JsonBoxReadTransformer implements ReadTypeTransformer<JsonBox, JsonNode> {

    @Override
    public Class<JsonBox> getAppType() {
        return JsonBox.class;
    }

    @Override
    public Class<JsonNode> getReadSqlType() {
        return JsonNode.class;
    }

    @Override
    public boolean supportsTypedGetObject() {
        return false;
    }

    @Override
    public JsonBox fromSql(JsonNode sqlValue) {
        return new JsonBox(sqlValue.toString());
    }
}
