package fr.emcastro.jdbcretyper.demo.postgis.transform;

import org.locationtech.jts.geom.Geometry;
import org.postgresql.util.PGobject;

import fr.emcastro.jdbcretyper.transform.WriteTypeTransformer;
import org.locationtech.jts.io.WKBWriter;

public class GeometryPostgisWriteTransformer implements WriteTypeTransformer<Geometry, PGobject> {

    private final WKBWriter writer = new WKBWriter();
    
    @Override
    public Class<Geometry> getAppType() {
        return Geometry.class;
    }

    @Override
    public Class<PGobject> getWriteSqlType() {
        return PGobject.class;
    }

    @Override
    public PGobject toSql(Geometry appValue) {
        try {
            PGobject pgObject = new PGobject();
            pgObject.setType("geometry");
            pgObject.setValue(WKBWriter.toHex(writer.write(appValue)));
            return pgObject;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to convert Geometry to PGobject", e);
        }
    }
}
