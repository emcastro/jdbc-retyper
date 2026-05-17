package fr.emcastro.jdbcretyper.demo.transform;

import fr.emcastro.jdbcretyper.demo.JsonBox;
import fr.emcastro.jdbcretyper.transform.TypeTransformer;
import org.duckdb.JsonNode; // TODO keep it until the problem of non-bidirectionality of types in DuckDB is solved (and validated by the user)

// TODO handle the problem on non-bidirectionallity of types in DuckDB
public class JsonBoxTypeTransformer implements TypeTransformer<JsonBox, Object> {

    @Override
    public Class<JsonBox> getAppType() {
        return JsonBox.class;
    }

    @Override
    public Class<Object> getSqlType() {
        return Object.class;
    }
/* // TODO keep until JsonNode probleme is solved (and validated by the user)
    @Override
    public Class<JsonNode> getSqlType() {
        return JsonNode.class;
    }
*/
    @Override
    public String toSql(JsonBox appValue) {
        return appValue.value();
    }

    @Override
    public JsonBox fromSql(Object sqlValue) {
        return new JsonBox(sqlValue.toString());
    }
}
