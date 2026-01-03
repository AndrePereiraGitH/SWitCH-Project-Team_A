package TOPSECRET.domain.Enum;

public enum TypeOfBinding {
    HARDBACK("Hardback"),          //Enum constants with associated data
    PAPERBACK("Paperback");

        // Private field to store the value
    private final String label;


        // Enum constructor
    TypeOfBinding(String label) {
        this.label = label;
    }

        // Overriding toString()
    @Override
    public String toString() {
        return label;}
}
