package tdd.leesper.jargs.exceptions;

public class InsufficientArgumentException extends RuntimeException {
    private final String option;

    public InsufficientArgumentException(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }
}
