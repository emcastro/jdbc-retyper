package fr.emcastro.jdbctyper.transform;

/**
 * Bidirectional type transformer between application types and JDBC SQL types.
 *
 * Implementations are registered in {@link TypeTransformerRegistry} and are invoked
 * automatically by {@code TyperPreparedStatement} (when writing parameters) and
 * {@code TyperResultSet} (when reading results).
 *
 * <p>Example: a {@code JsonBox} transformer converts {@code JsonBox} to {@code String}
 * for {@code setObject()}, and {@code String} back to {@code JsonBox} for {@code getObject()}.
 *
 * @param <A> the application type handled by this transformer
 * @param <S> the JDBC SQL type this transformer maps to
 */
public interface TypeTransformer<A, S> {

    /**
     * Returns the application type this transformer handles.
     * Used by the registry to find the correct transformer for a given value or target type.
     *
     * @return the application class (e.g. {@code JsonBox.class})
     */
    Class<A> getAppType();

    /**
     * Returns the JDBC SQL type that this transformer maps to.
     * Used by {@code ResultSet.getObject(columnIndex, type)} to request the correct
     * underlying SQL type from the driver before applying {@link #fromSql}.
     *
     * @return the SQL type class (e.g. {@code String.class})
     */
    Class<S> getSqlType();

    /**
     * Converts an application value to its JDBC representation.
     * Called by {@code TyperPreparedStatement.setObject()} when a parameter
     * of type {@code A} is passed.
     *
     * @param appValue the application value (never null)
     * @return the JDBC-compatible value of type {@code S}
     */
    S toSql(A appValue);

    /**
     * Converts a JDBC value to the application type.
     * Called by {@code TyperResultSet.getObject(columnIndex, type)} after
     * the driver returns a value of the type declared by {@link #getSqlType}.
     *
     * @param sqlValue the JDBC value (never null)
     * @return the application value of type {@code A}
     */
    A fromSql(S sqlValue);
}
