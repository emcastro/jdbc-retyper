# Testing Strategy

## Framework & Tools

- JUnit 5 + Mockito (`mockito-junit-jupiter`)
- DuckDB in-memory for integration tests
- Maven Surefire 3.5.5 for test execution
- Spotless / Palantir for formatting

## Test Architecture

| Package            | Type           | Description                                                                                   |
|--------------------|----------------|-----------------------------------------------------------------------------------------------|
| `jdbc/`            | Unit (Mockito) | `*Test.java` — wrapper-logic tests (unwrap, isWrapperFor)                                     |
| `jdbc/delegation/` | Unit (Mockito) | `*DelegationTest.java` — pure delegation checks (every JDBC method forwarded to the delegate) |
| `transform/`       | Unit           | `TypeTransformerRegistryTest` — SQL-to-application type conversion logic                      |
| root               | Integration    | `*DuckDBTest.java` — end-to-end tests extending `DuckDBTestBase`                              |

## Conventions

- `DuckDBTestBase`: creates an in-memory DuckDB `RetyperConnection`, registers transformers (Geometry, JsonBox), loads the spatial extension
- Pure delegation tests go in `jdbc/delegation/`; wrapper-logic tests (`unwrap`, `isWrapperFor`) go in the matching `jdbc/*Test.java`
- Precede each test method with a `// Check that...` comment
- Maintain 100% coverage (instructions, branches, lines, complexity, methods)

## Typical Mockito Pattern

```java
@ExtendWith(MockitoExtension.class)
class RetyperFooTest {
    @Mock private Foo mockFoo;
    private RetyperFoo retyper;

    @BeforeEach void setUp() { retyper = new RetyperFoo(mockFoo, registry); }

    @Test
    // Check that methodX() delegates to the underlying Foo without transformation.
    void methodX_delegates() throws SQLException {
        when(mockFoo.methodX()).thenReturn(value);
        assertEquals(value, retyper.methodX());
        verify(mockFoo).methodX();
    }
}
```

## Typical DuckDB Integration Pattern

```java
class SomeDuckDBTest extends DuckDBTestBase {
    @BeforeEach @Override protected void setUp() throws SQLException {
        super.setUp();
        // create tables
    }
    @AfterEach @Override protected void tearDown() throws SQLException {
        // DROP tables
        super.tearDown();
    }
    @Test void insertAndRead() { ... }
}
```
