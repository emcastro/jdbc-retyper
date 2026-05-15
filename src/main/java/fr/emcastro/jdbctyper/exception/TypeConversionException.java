package fr.emcastro.jdbctyper.exception;

public class TypeConversionException extends RuntimeException {

    public TypeConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
