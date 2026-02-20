package TOPSECRET.domain;

import java.util.Objects;

/**
 * Represents a publication code.
 * A Code can be:
 *  - ISBN-10 (books)
 *  - ISBN-13 (books)
 *  - ISSN (magazines)
 *
 * Only one type of code exists per instance.
 */

public class Code {

    // Attributes

    private final String isbn10;
    private final String isbn13;
    private final String issn;

    /**
     * Private constructor.
     * Objects must be created using the factory methods.
     */
    public Code(String isbn10, String isbn13, String issn) {
        this.isbn10 = isbn10;
        this.isbn13 = isbn13;
        this.issn = issn;
    }

    /**
     * Creates a Code with an ISBN-10.
     */
    public static Code fromIsbn10(String isbn10) {
        if (isbn10 == null || isbn10.isBlank()) {
            throw new IllegalArgumentException("ISBN10 cannot be null or blank");
        }

        String clean = removeHyphens(isbn10);

        // ISBN-10 must have exactly 10 characters
        if (clean.length() != 10) {
            throw new IllegalArgumentException("ISBN10 must have 10 characters");
        }

        // First 9 characters must be digits
        for (int i = 0; i < 9; i++) {
            if (!Character.isDigit(clean.charAt(i))) {
                throw new IllegalArgumentException("Invalid ISBN10 format");
            }
        }

        // Last character can be a digit or 'X'
        char last = clean.charAt(9);
        if (!Character.isDigit(last) && last != 'X') {
            throw new IllegalArgumentException("Invalid ISBN10 format");
        }

        return new Code(isbn10, null, null);
    }

    /**
     * Creates a Code with an ISBN-13.
     */
    public static Code fromIsbn13(String isbn13) {
        if (isbn13 == null || isbn13.isBlank()) {
            throw new IllegalArgumentException("ISBN13 cannot be null or blank");
        }

        String clean = removeHyphens(isbn13);

        // ISBN-13 must have exactly 13 digits
        if (clean.length() != 13) {
            throw new IllegalArgumentException("ISBN13 must have 13 digits");
        }

        for (int i = 0; i < clean.length(); i++) {
            if (!Character.isDigit(clean.charAt(i))) {
                throw new IllegalArgumentException("Invalid ISBN13 format");
            }
        }

        return new Code(null, isbn13, null);
    }

    /**
     * Creates a Code with an ISSN.
     */
    public static Code fromIssn(String issn) {
        if (issn == null || issn.isBlank()) {
            throw new IllegalArgumentException("ISSN cannot be null or blank");
        }

        String clean = removeHyphens(issn);

        // ISSN must have exactly 8 characters
        if (clean.length() != 8) {
            throw new IllegalArgumentException("ISSN must have 8 characters");
        }

        // First 7 characters must be digits
        for (int i = 0; i < 7; i++) {
            if (!Character.isDigit(clean.charAt(i))) {
                throw new IllegalArgumentException("Invalid ISSN format");
            }
        }

        // Last character can be a digit or 'X'
        char last = clean.charAt(7);
        if (!Character.isDigit(last) && last != 'X') {
            throw new IllegalArgumentException("Invalid ISSN format");
        }

        return new Code(null, null, issn);
    }

    /**
     * Removes hyphens from a code.
     * Used only for validation.
     */
    private static String removeHyphens(String value) {
        return value.replace("-", "").toUpperCase();
    }

    public String getIsbn10() {
        return isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public String getIssn() {
        return issn;
    }

    /**
     * Returns the stored code as a string.
     */
    @Override
    public String toString() {
        if (this.isbn10 != null) {
            return this.isbn10;
        }
        if (this.isbn13 != null) {
            return this.isbn13;
        }
        if (this.issn != null) {
            return this.issn;
        }
        return "No Code";
    }

    /**
     * Two Code objects are equal if all their fields are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Code code = (Code) o;
        return Objects.equals(isbn10, code.isbn10)
                && Objects.equals(isbn13, code.isbn13)
                && Objects.equals(issn, code.issn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn10, isbn13, issn);
    }
}
