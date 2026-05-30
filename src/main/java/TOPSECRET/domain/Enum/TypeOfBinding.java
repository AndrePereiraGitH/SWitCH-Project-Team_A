package TOPSECRET.domain.Enum;

import java.util.Arrays;

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

    public static TypeOfBinding fromString(String label) {
        return Arrays.stream(TypeOfBinding.values())
                .filter(typeOfBinding -> typeOfBinding.toString().equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown TypeOfBinding"));
    }

        // Overriding toString()
    @Override
    public String toString() {
        return label;}
}
