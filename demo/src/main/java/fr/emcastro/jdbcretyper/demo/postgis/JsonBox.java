package fr.emcastro.jdbcretyper.demo.postgis;

/**
 * Wrapper for a JSON string.
 * <p>
 * The same record exists in the test source tree
 * ({@code fr.emcastro.jdbcretyper.JsonBox}) — this duplication is intentional:
 * the demo is a standalone subproject and must not depend on
 * {@code src/test}.
 */
public record JsonBox(String value) {
    
}
