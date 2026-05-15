package fr.emcastro.jdbctyper.transform;

/**
 * Interface for transforming types for database storage.
 * Implementations can convert application types to database-specific types.
 */
public interface TypeTransformer<T, S> {

    /**
     * Transforms the source object to a target object suitable for database storage.
     * 
     * @param source the source object to transform
     * @return the transformed object
     * @throws Exception if transformation fails
     */
    S transform(T source) throws Exception;
}