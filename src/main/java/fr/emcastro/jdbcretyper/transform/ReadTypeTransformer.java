package fr.emcastro.jdbcretyper.transform;

import fr.emcastro.jdbcretyper.exception.TypeConversionException;

/**
 * Type transformer for the reading direction: from JDBC SQL values to application types.
 *
 * <p>Implementations are registered in {@link TypeTransformerRegistry#registerRead(ReadTypeTransformer)}
 * and are invoked automatically by {@code RetyperResultSet} when reading results.
 *
 * <p>The read SQL type ({@link #getReadSqlType()}) and the write SQL type
 * ({@link WriteTypeTransformer#getWriteSqlType()}) can differ. This is intentional:
 * a driver may return values in one form when reading (e.g. DuckDB returns {@code JsonNode}
 * for JSON columns) but accept a different form when writing (e.g. a plain {@code String}).
 * The {@link TypeTransformerRegistry} dispatches each direction independently,
 * so these two types never need to match.
 *
 * @param <A> the application type (e.g. {@code JsonBox}, {@code Point})
 * @param <S> the JDBC SQL type returned by the driver (e.g. {@code JsonNode}, {@code Geometry})
 */
public interface ReadTypeTransformer<A, S> {

    /**
     * Returns the application type this transformer handles.
     * Used by the registry to find the correct transformer for a given target type.
     *
     * @return the application class (e.g. {@code JsonBox.class})
     */
    Class<A> getAppType();

    /**
     * Returns the JDBC SQL type that this transformer reads from.
     * Used by {@code ResultSet.getObject(columnIndex, type)} to request the correct
     * underlying SQL type from the driver before applying {@link #fromSql}.
     *
     * @return the SQL type class (e.g. {@code String.class})
     */
    Class<S> getReadSqlType();

    /**
     * Whether this transformer supports typed {@code getObject(columnIndex, Class)}
     * calls. If false, the registry falls back to the driver's default return type.
     *
     * @return true if the transformer can receive a typed JDBC value
     */
    default boolean supportsTypedGetObject() {
        return true;
    }

    /**
     * Allows the transformer to accept or reject a specific SQL value during
     * dispatch. Called after {@link #getAppType()} and {@link #getReadSqlType()}
     * match, but before {@link #fromSql(Object)}. If false, the registry
     * continues searching for another transformer.
     *
     * @param sqlValue the SQL value from the JDBC driver (never null)
     * @return true if this transformer can convert the given value
     */
    default boolean canTransform(S sqlValue) {
        return true;
    }

    /**
     * Converts a JDBC value to the application type.
     * Called when using {@code RetyperResultSet.getObject(columnIndex, type)} after
     * the driver returns a value of the type declared by {@link #getReadSqlType()}.
     *
     * @param sqlValue the JDBC value (never null)
     * @return the application value of type {@code A}
     * @throws TypeConversionException if the value cannot be converted
     *                                  (e.g. parse failure, invalid format)
     */
    A fromSql(S sqlValue);
}
