package fr.emcastro.jdbcretyper.transform;

/**
 * Type transformer for the writing direction: from application types to JDBC SQL values.
 *
 * <p>Implementations are registered in {@link TypeTransformerRegistry#registerWrite(WriteTypeTransformer)}
 * and are invoked automatically by {@code RetyperPreparedStatement} when writing parameters.
 *
 * <p>The write SQL type ({@link #getWriteSqlType()}) and the read SQL type
 * ({@link ReadTypeTransformer#getReadSqlType()}) can differ. This is intentional:
 * a driver may require a different type for {@code setObject()} (e.g. a plain {@code String}
 * for JSON columns) than what it returns when reading (e.g. DuckDB returns {@code JsonNode}).
 * The {@link TypeTransformerRegistry} dispatches each direction independently,
 * so these two types never need to match.
 *
 * @param <A> the application type (e.g. {@code JsonBox}, {@code Point})
 * @param <P> the JDBC parameter type used for {@code setObject()} (e.g. {@code String}, {@code byte[]})
 */
public interface WriteTypeTransformer<A, P> {

    /**
     * Returns the application type this transformer handles.
     * Used by the registry to find the correct transformer for a given value.
     *
     * @return the application class (e.g. {@code JsonBox.class})
     */
    Class<A> getAppType();

    /**
     * Returns the JDBC parameter type that this transformer writes to.
     * Used as a cast target in {@code TypeTransformerRegistry.toSql()} to
     * ensure the value returned by {@link #toSql(Object)} matches the declared
     * type.
     *
     * @return the parameter type class (e.g. {@code String.class})
     */
    Class<P> getWriteSqlType();

    /**
     * Converts an application value to its JDBC representation.
     * Called when using {@code RetyperPreparedStatement.setObject()} with a parameter
     * of type {@code A}.
     *
     * @param appValue the application value (never null)
     * @return the JDBC-compatible value of type {@code P}
     */
    P toSql(A appValue);
}
