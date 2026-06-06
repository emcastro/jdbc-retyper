package fr.emcastro.jdbcretyper.demo.postgis.transform;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;
import org.postgresql.util.PGobject;

import fr.emcastro.jdbcretyper.transform.ReadTypeTransformer;

public class GeometryReadTransformer implements ReadTypeTransformer<Geometry, PGobject> {

    private final WKBReader reader = new WKBReader();

    @Override
    public Class<Geometry> getAppType() {
        return Geometry.class;
    }

    @Override
    public Class<PGobject> getReadSqlType() {
        return PGobject.class;
    }

    @Override
    public boolean canTransform(PGobject sqlValue) {
        return "geometry".equals(sqlValue.getType());
    }

    @Override
    public Geometry fromSql(PGobject sqlValue) {
        try {
            return reader.read(WKBReader.hexToBytes(sqlValue.getValue()));
        } catch (ParseException e) {
            throw new IllegalStateException("Unable to convert PGobject to Geometry", e);
        }
    }
}
