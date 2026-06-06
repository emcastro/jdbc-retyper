package fr.emcastro.jdbcretyper.demo.spring.transform;

import fr.emcastro.jdbcretyper.demo.spring.JsonBox;
import fr.emcastro.jdbcretyper.transform.ReadTypeTransformer;
import org.duckdb.JsonNode;

/**
 * Reads {@link JsonBox} from JDBC {@link JsonNode} columns for the demo.
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
