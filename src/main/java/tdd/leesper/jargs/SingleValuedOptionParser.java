package tdd.leesper.jargs;

import tdd.leesper.jargs.exceptions.IllegalValueException;
import tdd.leesper.jargs.exceptions.InsufficientArgumentException;
import tdd.leesper.jargs.exceptions.TooManyArgumentsException;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

class SingleValuedOptionParser<T> implements OptionParser<T> {
    Function<String, T> valueParser;
    T defaultValue;

    public SingleValuedOptionParser(T defaultValue, Function<String, T> valueParser) {
        this.defaultValue = defaultValue;
        this.valueParser = valueParser;
    }

    @Override
    public T parse(List<String> arguments, Option option) {
        int index = arguments.indexOf("-" + option.value());

        if (index == -1) return defaultValue;

        List<String> values = values(arguments, index);

        if (values.size() < 1) throw new InsufficientArgumentException(option.value());
        if (values.size() > 1) throw new TooManyArgumentsException(option.value());

        String value = arguments.get(index + 1);
        try {
            return valueParser.apply(value);
        } catch (Exception e) {
            throw new IllegalValueException(option.value(), value);
        }
    }

    private static List<String> values(List<String> arguments, int index) {
        int followingFlag = IntStream.range(index + 1, arguments.size())
                .filter(it -> arguments.get(it).startsWith("-"))
                .findFirst().orElse(arguments.size());

        List<String> values = arguments.subList(index + 1, followingFlag);
        return values;
    }

}
