package fr.emcastro.jdbcretyper.demo.postgis.transform;

import org.postgresql.util.PGobject;

import fr.emcastro.jdbcretyper.demo.postgis.JsonBox;
import fr.emcastro.jdbcretyper.transform.WriteTypeTransformer;

public class JsonBoxPostgresWriteTransformer implements WriteTypeTransformer<JsonBox, PGobject> {

    @Override
    public Class<JsonBox> getAppType() {
        return JsonBox.class;
    }

    @Override
    public Class<PGobject> getWriteSqlType() {
        return PGobject.class;
    }

    @Override
    public PGobject toSql(JsonBox appValue) {
        try {
            PGobject pgObject = new PGobject();
            pgObject.setType("jsonb");
            pgObject.setValue(appValue.value());
            return pgObject;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to convert JsonBox to PGobject", e);
        }
    }
}
