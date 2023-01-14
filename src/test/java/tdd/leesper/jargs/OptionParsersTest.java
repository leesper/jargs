package tdd.leesper.jargs;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import tdd.leesper.jargs.exceptions.InsufficientArgumentException;
import tdd.leesper.jargs.exceptions.TooManyArgumentsException;

import java.lang.annotation.Annotation;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static tdd.leesper.jargs.OptionParsersTest.BooleanOptionParserTest.option;
import static tdd.leesper.jargs.OptionParsers.unary;

public class OptionParsersTest {
    @Nested
    class UnaryOptionParserTest {
        // Sad path
        @Test
        public void shouldNotAcceptExtraArgumentForSingleValuedOption() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
                unary(0, Integer::parseInt).parse(asList("-p", "8080", "8081"), option("p"));
            });
            assertEquals("p", e.getOption());
        }

        @ParameterizedTest
        @ValueSource(strings = {"-p -l", "-p"})
        public void shouldNotAcceptInsufficientArgumentForSingleValuedOption(String arguments) {
            InsufficientArgumentException e = assertThrows(InsufficientArgumentException.class, () -> {
                unary(0, Integer::parseInt).parse(asList(arguments.split(" ")), option("p"));
            });
            assertEquals("p", e.getOption());
        }

        @Test
        public void shouldNotAcceptExtraArgumentForStringSingleValuedOption() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
                unary("", String::valueOf).parse(asList("-d", "/usr/logs", "/usr/vars"), option("d"));
            });
            assertEquals("d", e.getOption());
        }

        @ParameterizedTest
        @ValueSource(strings = {"-d -l", "-d"})
        public void shouldNotAcceptInsufficientArgumentForStringSingleValuedOption(String arguments) {
            InsufficientArgumentException e = assertThrows(InsufficientArgumentException.class, () -> {
                unary("", String::valueOf).parse(asList(arguments.split(" ")), option("d"));
            });
            assertEquals("d", e.getOption());
        }

        // Default
        @Test
        public void shouldSetDefaultValueToEmptyStringForStringOption() {
            assertEquals("", unary("", String::valueOf).parse(asList(), option("d")));
        }

        @Test
        public void shouldSetDefaultValueForSingleValuedOption() {
            Function<String, Object> whatever = (it) -> null;
            Object defaultValue = new Object();
            assertSame(defaultValue, unary(defaultValue, whatever).parse(asList(), option("p")));
        }

        // happy path
        @Test
        public void shouldParseValueIfFlagPresent() {
            Object parsed = new Object();
            Function<String, Object> parse = (it) -> parsed;
            Object whatever = new Object();
            assertSame(parsed, unary(whatever, parse).parse(asList("-p", "8080"), option("p")));
        }
    }

    @Nested
    class BooleanOptionParserTest {
        // Sad path
        @Test
        public void shouldNotAcceptExtraArgumentForBooleanOption() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
                OptionParsers.bool().parse(asList("-l", "t"), option("l"));
            });

            assertEquals("l", e.getOption());
        }

        @Test
        public void shouldNotAcceptExtraArgumentsForBooleanOption() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
                OptionParsers.bool().parse(asList("-l", "t", "f"), option("l"));
            });

            assertEquals("l", e.getOption());
        }

        static Option option(String value) {
            return new Option() {

                @Override
                public Class<? extends Annotation> annotationType() {
                    return Option.class;
                }

                @Override
                public String value() {
                    return value;
                }
            };
        }

        // Default
        @Test
        public void shouldSetDefaultValueToFalseIfOptionNotPresent() {
            assertFalse(OptionParsers.bool().parse(asList(), option("l")));
        }

        // Happy path
        @Test
        public void shouldSetValueToTrueIfOptionPresent() {
            assertTrue(OptionParsers.bool().parse(asList("-l"), option("l")));
        }
    }
}
