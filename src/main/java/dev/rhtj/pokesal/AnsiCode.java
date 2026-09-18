package dev.rhtj.pokesal;

public enum AnsiCode {
    RESET("\u001B[0m"),

    BOLD("\u001B[1m"),
    DIM("\u001B[2m"),
    ITALIC("\u001B[3m"),
    UNDERLINE("\u001B[4m"),
    BLINK("\u001B[5m"),
    REVERSE("\u001B[7m"),
    HIDDEN("\u001B[8m"),
    STRIKETHROUGH("\u001B[9m"),

    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[38;5;214m"),
    ORANGE("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m"),

    DARK_GRAY("\u001B[90m"),
    BRIGHT_RED("\u001B[91m"),
    BRIGHT_GREEN("\u001B[92m"),
    BRIGHT_YELLOW("\u001B[93m"),
    BRIGHT_BLUE("\u001B[94m"),
    BRIGHT_PURPLE("\u001B[95m"),
    BRIGHT_CYAN("\u001B[96m"),
    LIGHT_GRAY("\u001B[37m"),
    BRIGHT_WHITE("\u001B[97m"),

    BG_BLACK("\u001B[40m"),
    BG_RED("\u001B[41m"),
    BG_GREEN("\u001B[42m"),
    BG_YELLOW("\u001B[43m"),
    BG_BLUE("\u001B[44m"),
    BG_PURPLE("\u001B[45m"),
    BG_CYAN("\u001B[46m"),
    BG_WHITE("\u001B[47m");

    private final String code;

    AnsiCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }

    public static String apply(String text, AnsiCode... styles) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(RESET);
        for (AnsiCode ansiCode : styles) {
            stringBuilder.append(ansiCode);
        }
        stringBuilder.append(text).append(RESET);
        return stringBuilder.toString();
    }

}
