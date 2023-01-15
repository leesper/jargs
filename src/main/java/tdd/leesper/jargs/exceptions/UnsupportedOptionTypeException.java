package tdd.leesper.jargs.exceptions;

public class UnsupportedOptionTypeException extends RuntimeException {
    private final String option;
    private final Class<?> parameterType;

    public UnsupportedOptionTypeException(String option, Class<?> parameterType) {
        this.option = option;
        this.parameterType = parameterType;
    }

    public String getOption() {
        return option;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }
}
