package fr.emcastro.jdbctyper.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import fr.emcastro.jdbctyper.transform.TypeTransformerRegistry;

public class MagicDatasource implements DataSource {

    private final DataSource dataSource;
    private final TypeTransformerRegistry registry;

    public MagicDatasource(DataSource dataSource, TypeTransformerRegistry registry) {
        this.dataSource = dataSource;
        this.registry = registry;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new MagicConnection(dataSource.getConnection(), registry);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return new MagicConnection(dataSource.getConnection(username, password), registry);
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

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(dataSource) || dataSource.isWrapperFor(iface);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(dataSource)) {
            return iface.cast(dataSource);
        }
        return dataSource.unwrap(iface);
    }
}
