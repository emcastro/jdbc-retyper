package fr.emcastro.jdbctyper.transform;

import fr.emcastro.jdbctyper.exception.TypeConversionException;

import java.util.ArrayList;
import java.util.List;

public class TypeTransformerRegistry {

    private final List<TypeTransformer<?, ?>> transformers = new ArrayList<>();

    /**
     * Registers a type transformer.
     * <p>
     * This method is not thread-safe and must only be called during
     * initialization, before the registry is shared across threads.
     *
     * @param transformer the transformer to register
     */
    public void register(TypeTransformer<?, ?> transformer) {
        transformers.add(transformer);
    }

    /**
     * Converts an application value to its JDBC representation.
     * Returns the first matching transformer's SQL value, or the original value if none matches.
     *
     * @param appValue the application value, may be null
     * @return the JDBC-compatible value, or the original value if unregistered
     */
    @SuppressWarnings("unchecked")
    public Object toSql(Object appValue) {
        if (appValue == null) return null;
        for (TypeTransformer<?, ?> t : transformers) {
            if (t.getAppType().isInstance(appValue)) {
                return t.getSqlType().cast(((TypeTransformer<Object, Object>) t).toSql(appValue));
            }
        }
        return appValue;
    }

    /**
     * Converts an SQL value to a specific application type.
     *
     * @param sqlValue the SQL value from the JDBC driver, may be null
     * @param appType the target application type
     * @return the transformed application value, or the raw value if already of the target type
     * @throws TypeConversionException if no transformer matches and the value is not already of the target type
     */
    @SuppressWarnings("unchecked")
    public <T> T fromSql(Object sqlValue, Class<T> appType) {
        if (sqlValue == null) return null;
        for (TypeTransformer<?, ?> t : transformers) {
            if (t.getAppType().isAssignableFrom(appType) && t.getSqlType().isInstance(sqlValue)) {
                return appType.cast(((TypeTransformer<T, Object>) t).fromSql(sqlValue));
            }
        }
        if (appType.isInstance(sqlValue)) {
            return (T) sqlValue;
        }
        throw new TypeConversionException(
                "Unsupported conversion from " + sqlValue.getClass() + " to " + appType.getName(), null);
    }

    /**
     * Transforms an SQL value to its application type using the first matching transformer.
     * Falls back to the raw SQL value if no transformer matches.
     */
    @SuppressWarnings("unchecked")
    public Object fromSqlDefaultType(Object sqlValue) {
        if (sqlValue == null) return null;
        for (TypeTransformer<?, ?> t : transformers) {
            if (t.getSqlType().isInstance(sqlValue)) {
                return t.getAppType().cast(((TypeTransformer<Object, Object>) t).fromSql(sqlValue));
            }
        }
        return sqlValue;
    }

    /**
     * Returns the JDBC SQL type that corresponds to the given application type.
     * Used to inform the JDBC driver of the expected SQL type when calling
     * {@code ResultSet.getObject(columnIndex, Class)}.
     *
     * @param appType the application type
     * @return the mapped SQL type, or the original type if no transformer is registered
     */
    @SuppressWarnings("unchecked")
    public <T> Class<T> mapType(Class<T> appType) {
        for (TypeTransformer<?, ?> t : transformers) {
            if (t.getAppType().isAssignableFrom(appType)) {
                return (Class<T>) t.getSqlType();
            }
        }
        return appType;
    }
}
