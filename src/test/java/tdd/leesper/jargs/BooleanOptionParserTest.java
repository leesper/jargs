package tdd.leesper.jargs;

import org.junit.jupiter.api.Test;
import tdd.leesper.jargs.exceptions.TooManyArgumentsException;

import java.lang.annotation.Annotation;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

public class BooleanOptionParserTest {
    // Sad path
    @Test
    public void shouldNotAcceptExtraArgumentForBooleanOption() {
        TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
            new BooleanOptionParser().parse(asList("-l", "t"), option("l"));
        });

        assertEquals("l", e.getOption());
    }

    @Test
    public void shouldNotAcceptExtraArgumentsForBooleanOption() {
        TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
            new BooleanOptionParser().parse(asList("-l", "t", "f"), option("l"));
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
        assertFalse(new BooleanOptionParser().parse(asList(), option("l")));
    }

    // Happy path
    @Test
    public void shouldSetValueToTrueIfOptionPresent() {
        assertTrue(new BooleanOptionParser().parse(asList("-l"), option("l")));
    }
}
