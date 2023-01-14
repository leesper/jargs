package tdd.leesper.jargs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import tdd.leesper.jargs.exceptions.InsufficientArgumentException;
import tdd.leesper.jargs.exceptions.TooManyArgumentsException;

import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static tdd.leesper.jargs.BooleanOptionParserTest.option;

public class SingleValuedOptionParserTest {
    // Sad path
    @Test
    public void shouldNotAcceptExtraArgumentForSingleValuedOption() {
        TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
            new SingleValuedOptionParser<>(0, Integer::parseInt).parse(asList("-p", "8080", "8081"), option("p"));
        });
        assertEquals("p", e.getOption());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-p -l", "-p"})
    public void shouldNotAcceptInsufficientArgumentForSingleValuedOption(String arguments) {
        InsufficientArgumentException e = assertThrows(InsufficientArgumentException.class, () -> {
            new SingleValuedOptionParser<>(0, Integer::parseInt).parse(asList(arguments.split(" ")), option("p"));
        });
        assertEquals("p", e.getOption());
    }

    @Test
    public void shouldNotAcceptExtraArgumentForStringSingleValuedOption() {
        TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
            new SingleValuedOptionParser<>("", String::valueOf).parse(asList("-d", "/usr/logs", "/usr/vars"), option("d"));
        });
        assertEquals("d", e.getOption());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-d -l", "-d"})
    public void shouldNotAcceptInsufficientArgumentForStringSingleValuedOption(String arguments) {
        InsufficientArgumentException e = assertThrows(InsufficientArgumentException.class, () -> {
            new SingleValuedOptionParser<>("", String::valueOf).parse(asList(arguments.split(" ")), option("d"));
        });
        assertEquals("d", e.getOption());
    }

    // Default
    @Test
    public void shouldSetDefaultValueToEmptyStringForStringOption() {
        assertEquals("", new SingleValuedOptionParser<>("", String::valueOf).parse(asList(), option("d")));
    }

    @Test
    public void shouldSetDefaultValueForSingleValuedOption() {
        Function<String, Object> whatever = (it) -> null;
        Object defaultValue = new Object();
        assertSame(defaultValue, new SingleValuedOptionParser<>(defaultValue, whatever).parse(asList(), option("p")));
    }

    // happy path
    @Test
    public void shouldParseValueIfFlagPresent() {
        Object parsed = new Object();
        Function<String, Object> parse = (it) -> parsed;
        Object whatever = new Object();
        assertSame(parsed, new SingleValuedOptionParser<>(whatever, parse).parse(asList("-p", "8080"), option("p")));
    }
}
