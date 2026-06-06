package fr.emcastro.jdbcretyper.demo.postgis.transform;

import org.postgresql.util.PGobject;

import fr.emcastro.jdbcretyper.demo.postgis.JsonBox;
import fr.emcastro.jdbcretyper.transform.ReadTypeTransformer;

public class JsonBoxReadTransformer implements ReadTypeTransformer<JsonBox, PGobject> {

    @Override
    public Class<JsonBox> getAppType() {
        return JsonBox.class;
    }

    @Override
    public Class<PGobject> getReadSqlType() {
        return PGobject.class;
    }

    @Override
    public boolean canTransform(PGobject sqlValue) {
        return "jsonb".equals(sqlValue.getType());
    }

    @Override
    public JsonBox fromSql(PGobject sqlValue) {
        return new JsonBox(sqlValue.getValue());
    }
}
