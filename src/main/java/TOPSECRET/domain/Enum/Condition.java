package TOPSECRET.domain.Enum;

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

}
