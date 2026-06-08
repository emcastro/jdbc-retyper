package fr.emcastro.jdbcretyper.exception;

/**
 * Thrown when a type transformer cannot convert a value during
 * {@code fromSql()} or {@code toSql()} operations.
 */
public class TypeConversionException extends RuntimeException {

    public TypeConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
