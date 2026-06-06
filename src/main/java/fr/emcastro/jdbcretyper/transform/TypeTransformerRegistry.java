package fr.emcastro.jdbcretyper.transform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.emcastro.jdbcretyper.exception.TypeConversionException;

/**
 * Maintains two independent lists of type transformers — one for reading (JDBC
 * → application) and one for writing (application → JDBC).
 *
 * <p>The read path ({@link #fromSql(Object, Class)}) matches by both the
 * driver's return type and the target application type. This is necessary
 * because different SQL types may require different transformers even for the
 * same application type.
 *
 * <p>The write path ({@link #toSql(Object)}) matches by application type only.
 * The transformer's declared write SQL type is used purely as a cast target —
 * the registry does not inspect the value's runtime type on the write side.
 *
 * <p>The read SQL type ({@link ReadTypeTransformer#getReadSqlType()}) and the
 * write SQL type ({@link WriteTypeTransformer#getWriteSqlType()}) can differ for
 * a given application type. This asymmetry is handled transparently by the
 * two-list dispatch above.
 */
public class TypeTransformerRegistry {

    private final List<ReadTypeTransformer<?, ?>> readTransformers = new ArrayList<>();
    private final List<WriteTypeTransformer<?, ?>> writeTransformers = new ArrayList<>();

    /**
     * Registers a read transformer.
     * <p>
     * This method is not thread-safe and must only be called during
     * initialization, before the registry is shared across threads.
     *
     * @param transformer the read transformer to register
     */
    public void registerRead(ReadTypeTransformer<?, ?> transformer) {
        readTransformers.add(transformer);
    }

    /**
     * Registers a write transformer.
     * <p>
     * This method is not thread-safe and must only be called during
     * initialization, before the registry is shared across threads.
     *
     * @param transformer the write transformer to register
     */
    public void registerWrite(WriteTypeTransformer<?, ?> transformer) {
        writeTransformers.add(transformer);
    }

    /**
     * Converts an application value to its JDBC representation.
     * Returns the first matching write transformer's SQL value, or the original
     * value if none matches.
     *
     * @param appValue the application value, may be null
     * @return the JDBC-compatible value, or the original value if unregistered
     */
    @SuppressWarnings("unchecked")
    public Object toSql(Object appValue) {
        if (appValue == null) return null;
        for (WriteTypeTransformer<?, ?> t : writeTransformers) {
            if (t.getAppType().isInstance(appValue)) {
                return t.getWriteSqlType().cast(((WriteTypeTransformer<Object, Object>) t).toSql(appValue));
            }
        }
        return appValue;
    }

    /**
     * Converts an SQL value from the JDBC driver to a specific application type.
     *
     * @param sqlValue the SQL value from the JDBC driver, may be null
     * @param appType  the target application type
     * @param <T>      the target application type
     * @return the transformed application value, or the raw value if already of the
     *         target type
     * @throws TypeConversionException if no transformer matches and the value is
     *                                 not already of the target type
     */
    @SuppressWarnings("unchecked")
    public <T> T fromSql(Object sqlValue, Class<T> appType) {
        if (sqlValue == null) return null;
        for (ReadTypeTransformer<?, ?> t : readTransformers) {
            if (t.getAppType().isAssignableFrom(appType) && t.getReadSqlType().isInstance(sqlValue)) {
                if (((ReadTypeTransformer<Object, Object>) t).canTransform(sqlValue)) {
                    return appType.cast(((ReadTypeTransformer<T, Object>) t).fromSql(sqlValue));
                }
            }
        }
        if (appType.isInstance(sqlValue)) {
            return (T) sqlValue;
        }
        throw new TypeConversionException(
                "Unsupported conversion from " + sqlValue.getClass() + " to " + appType.getName(), null);
    }

    /**
     * Transforms an SQL value to its application type using the first matching read
     * transformer.
     * Falls back to the raw SQL value if no transformer matches.
     *
     * @param sqlValue the SQL value from the JDBC driver, may be null
     * @return the transformed application value, or the raw SQL value if no
     *         transformer matches
     */
    @SuppressWarnings("unchecked")
    public Object fromSqlDefaultType(Object sqlValue) {
        if (sqlValue == null) return null;
        for (ReadTypeTransformer<?, ?> t : readTransformers) {
            if (t.getReadSqlType().isInstance(sqlValue)) {
                if (((ReadTypeTransformer<Object, Object>) t).canTransform(sqlValue)) {
                    return t.getAppType().cast(((ReadTypeTransformer<Object, Object>) t).fromSql(sqlValue));
                }
            }
        }
        return sqlValue;
    }

    /**
     * Returns the JDBC SQL type that corresponds to the given application type for
     * reading.
     * Used to inform the JDBC driver of the expected SQL type when calling
     * {@code ResultSet.getObject(columnIndex, Class)}.
     *
     * @param appType the application type
     * @param <T>     the application type
     * @return the mapped SQL type, or the original type if no transformer is
     *         registered
     */
    @SuppressWarnings("unchecked")
    public <T> Class<T> mapType(Class<T> appType) {
        for (ReadTypeTransformer<?, ?> t : readTransformers) {
            if (t.getAppType().isAssignableFrom(appType)) {
                if (t.supportsTypedGetObject()) {
                    return (Class<T>) t.getReadSqlType();
                } else {
                    return null; // getObject will be used without type hint
                }
            }
        }
        return appType;
    }

    /**
     * Converts a type map by replacing each application class with its
     * corresponding JDBC SQL type.
     *
     * @param map the original type map from the caller
     * @return a new map with application types replaced by their SQL types, or null
     *         if the input is null
     */
    public Map<String, Class<?>> fromSqlMap(Map<String, Class<?>> map) {
        if (map == null) return null;
        Map<String, Class<?>> result = new HashMap<>(map.size());
        for (Map.Entry<String, Class<?>> entry : map.entrySet()) {
            var sqlType = mapType(entry.getValue());
            if (sqlType != null) {
                result.put(entry.getKey(), sqlType);
            }
        }
        return result;
    }
}
