package fr.emcastro.jdbctyper.transform;

import fr.emcastro.jdbctyper.exception.TypeConversionException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TypeTransformerRegistry {

    private static final List<TypeTransformer<?>> transformers = new CopyOnWriteArrayList<>();

    private TypeTransformerRegistry() {
    }

    public static <T> void register(TypeTransformer<T> transformer) {
        transformers.add(transformer);
    }

    @SuppressWarnings("unchecked")
    public static <T> Object toSql(T value) {
        if (value == null) {
            return null;
        }
        for (TypeTransformer<?> t : transformers) {
            if (t.getType().isInstance(value)) {
                return ((TypeTransformer<T>) t).toSql(value);
            }
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromSql(Object value, Class<T> type) {
        if (value == null) {
            return null;
        }
        for (TypeTransformer<?> t : transformers) {
            if (t.getType() == type) {
                return ((TypeTransformer<T>) t).fromSql(value);
            }
        }
        if (type.isInstance(value)) {
            return (T) value;
        }
        throw new TypeConversionException(
            "Unsupported conversion from " + value.getClass() + " to " + type.getName(),
            null
        );
    }

    public static Object defaultFromSql(Object value) {
        if (value == null) {
            return null;
        }
        for (TypeTransformer<?> t : transformers) {
            try {
                Object result = t.fromSql(value);
                if (result != null) {
                    return result;
                }
            } catch (Exception e) {
                // TODO: silently swallowing exceptions hides errors from incompatible transformers.
                // Consider checking input type before calling fromSql, or catching only specific exceptions.
            }
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> mapType(Class<T> type) {
        for (TypeTransformer<?> t : transformers) {
            if (t.getType() == type) {
                return (Class<T>) t.getSqlType();
            }
        }
        return type;
    }
}
