package org.example;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.Collections.emptyMap;
import static org.example.UuidMatcher.isUuid;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TemplateTest {

    private static final String TEXT_WITHOUT_EXPRESSIONS = "My name is Jan Kowalski";
    private static final String TEXT_WITH_EXPRESSIONS = "My name is ${firstName} ${lastName}";

    @Test
    void given_a_text_without_expressions_when_evaluating_then_returns_the_text() {
        var template = new Template(TEXT_WITHOUT_EXPRESSIONS);
        assertEquals(TEXT_WITHOUT_EXPRESSIONS, template.evaluate(emptyMap()));
    }

    @Test
    void given_a_text_with_non_unique_expressions_when_creating_template_then_throws_an_exception() {
        assertThrows(IllegalArgumentException.class, () -> new Template("My name is ${firstName} ${firstName}"));
    }

    @DisplayName("given a text with expressions")
    @Nested
    class GivenTextWithExpressions {

        @Test
        void when_evaluating_then_returns_the_text_with_substituted_values() {
            var template = new Template(TEXT_WITH_EXPRESSIONS);
            var parameters = Map.of("firstName", "Jan", "lastName", "Kowalski");
            assertEquals(TEXT_WITHOUT_EXPRESSIONS, template.evaluate(parameters));
        }

        @Test
        void when_evaluating_without_providing_all_values_then_thorws_an_exception() {
            var template = new Template(TEXT_WITH_EXPRESSIONS);
            assertThrows(IllegalArgumentException.class, () -> template.evaluate(emptyMap()));
        }

        @Test
        void when_evaluating_with_providing_non_alphanumeric_values_then_thorws_an_exception() {
            var template = new Template(TEXT_WITH_EXPRESSIONS);
            var parameters = Map.of("firstName", "@@", "lastName", "Kowalski");
            assertThrows(IllegalArgumentException.class, () -> template.evaluate(parameters));
        }

    }


    @BeforeEach
    void beforeEach() {
        System.out.println("Before each");
    }

    @AfterEach
    void afterEach() {
        System.out.println("After each");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Before all");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("After all");
    }

    @ValueSource(ints = {0, 1 ,2})
    @ParameterizedTest(name = "Index: {0}")
    void parameterized_test_example(int value) {
        assertThat("1f59058d-ab49-46b6-b91f-b96ff03f1750", isUuid());
    }

    @CsvFileSource(resources = "/data.csv")
    //@MethodSource("keysAndValues")
    @ParameterizedTest
    void other_parameterized_test_example(String name, int value) {

    }

    static Stream<Arguments> keysAndValues() {
        return Stream.of(
                Arguments.of("value", 1),
                Arguments.of("key", 3)
        );
    }

}
