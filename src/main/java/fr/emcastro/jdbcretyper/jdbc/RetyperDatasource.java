package fr.emcastro.jdbcretyper.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import fr.emcastro.jdbcretyper.transform.TypeTransformerRegistry;

/**
 * A JDBC {@link DataSource} wrapper that returns {@link RetyperConnection}
 * instances and delegates all other operations to the underlying data source.
 */
public class RetyperDatasource implements DataSource {

    private final DataSource dataSource;
    private final TypeTransformerRegistry registry;

    public RetyperDatasource(DataSource dataSource, TypeTransformerRegistry registry) {
        this.dataSource = dataSource;
        this.registry = registry;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new RetyperConnection(dataSource.getConnection(), registry);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return new RetyperConnection(dataSource.getConnection(username, password), registry);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return dataSource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        dataSource.setLogWriter(out);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return dataSource.getLoginTimeout();
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        dataSource.setLoginTimeout(seconds);
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return dataSource.getParentLogger();
    }

    /**
     * {@inheritDoc}
     * <p>Follows the HikariCP wrapper pattern: returns {@code true} if this wrapper itself
     * implements the requested type, or delegates to the underlying JDBC driver.</p>
     */
    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(dataSource) || dataSource.isWrapperFor(iface);
    }

    /**
     * {@inheritDoc}
     * <p>Follows the HikariCP wrapper pattern: unwraps to this wrapper if it implements the
     * requested type, otherwise delegates to the underlying JDBC driver recursively.</p>
     */
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(dataSource)) {
            return iface.cast(dataSource);
        }
        return dataSource.unwrap(iface);
    }
}
