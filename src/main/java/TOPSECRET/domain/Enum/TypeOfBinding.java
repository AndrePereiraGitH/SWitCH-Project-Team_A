package TOPSECRET.domain.Enum;

public enum TypeOfBinding {
    HARDBACK("Hardback"),          //Enum constants with associated data
    PAPERBACK("Paperback"),
    SADDLE_STICHING("Saddle Stitching"),
    PERFECT_BINDING("Perfect Binding"),
    WIRE_O_BINDING("Wire-O Binding"),
    CASE_BINDING("Case Binding"),
    OTHER("Other");

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
