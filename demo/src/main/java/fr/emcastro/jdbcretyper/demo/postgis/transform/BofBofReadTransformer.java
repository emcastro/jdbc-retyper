package fr.emcastro.jdbcretyper.demo.postgis.transform;

import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.postgresql.util.PGobject;

import fr.emcastro.jdbcretyper.demo.postgis.JsonBox;
import fr.emcastro.jdbcretyper.transform.ReadTypeTransformer;

public class BofBofReadTransformer implements ReadTypeTransformer<Object, PGobject> {

    private final WKBReader reader = new WKBReader();

    @Override
    public Class<Object> getAppType() {
        return Object.class;
    }

    @Override
    public Class<PGobject> getReadSqlType() {
        return PGobject.class;
    }

    @Override
    public boolean canTransform(PGobject sqlValue) {
        return sqlValue.getType().equals("geometry") || sqlValue.getType().equals("jsonb");
    }

    @Override
    public Object fromSql(PGobject sqlValue) {
        if (sqlValue.getType().equals("geometry")) {
            try {
                return reader.read(WKBReader.hexToBytes(sqlValue.getValue()));
            } catch (ParseException e) {
                throw new IllegalStateException("Unable to convert PGobject to Geometry", e);
            }
        } else if (sqlValue.getType().equals("jsonb")) {
            return new JsonBox(sqlValue.getValue());
        }
        throw new IllegalStateException("Unable to convert PGobject of type " + sqlValue.getType());
    }
}
