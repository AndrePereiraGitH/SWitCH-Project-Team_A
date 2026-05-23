package TOPSECRET.domain.Enum;

import java.util.Arrays;

public enum Condition {
    NEW("New"),
    LIKE_NEW("Like New"),
    VERY_GOOD("Very Good"),
    GOOD("Good"),
    ACCEPTABLE("Acceptable"),
    POOR("Poor");

    private final String _description;

    Condition(String description) {
        this._description = description;
    }

    public String getDescription() {
        return _description;
    }

    public static Condition fromString(String value) {
        return Arrays.stream(Condition.values())
                .filter(c -> c.getDescription().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown condition: " + value));
    }
}
