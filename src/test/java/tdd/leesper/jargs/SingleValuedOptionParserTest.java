package tdd.leesper.jargs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    public void shouldSetDefaultValueTo0ForIntOption() {
        assertEquals(0, new SingleValuedOptionParser<>(0, Integer::parseInt).parse(asList(), option("p")));
    }

    // Default
    @Test
    public void shouldSetDefaultValueToEmptyStringForStringOption() {
        assertEquals("", new SingleValuedOptionParser<>("", String::valueOf).parse(asList(), option("d")));
    }
}
