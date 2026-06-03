package TOPSECRET.domain.ValueObjects;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Represents the identification code of a publication.
 * Supports three formats: ISBN-10 (books, 10 chars), ISBN-13 (books, 13 digits), ISSN (magazines, 8 chars).
 * Each instance holds exactly one type — the other two are always null.
 * Use the static factory methods to create instances: fromIsbn10, fromIsbn13, fromIssn.
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Code {

    // only one of these will have a value — the other two will be null
    private String isbn10;
    private String isbn13;
    private String issn;

    /**
     * Private constructor — only the factory methods can call this, after validation.
     */
    public Code(String isbn10, String isbn13, String issn) {
        this.isbn10 = isbn10;
        this.isbn13 = isbn13;
        this.issn = issn;
    }

    /**
     * Creates a Code from an ISBN-10 string (10 characters; last can be a digit or 'X').
     * Hyphens are removed before validation, so "0-306-40615-2" is also accepted.
     *
     * @param isbn10 the ISBN-10 value; must not be null or blank
     * @throws IllegalArgumentException if the format is invalid
     */
    public static Code fromIsbn10(String isbn10) {
        if (isbn10 == null || isbn10.isBlank()) {
            throw new IllegalArgumentException("ISBN10 cannot be null or blank");
        }

        // remove hyphens so "0-306-40615-2" is treated the same as "0306406152"
        String clean = removeHyphens(isbn10);

        if (clean.length() != 10) {
            throw new IllegalArgumentException("ISBN10 must have 10 characters");
        }

        // first 9 must be digits
        for (int i = 0; i < 9; i++) {
            if (!Character.isDigit(clean.charAt(i))) {
                throw new IllegalArgumentException("Invalid ISBN10 format");
            }
        }

        // last character can be a digit or 'X' (standard check digit)
        char last = clean.charAt(9);
        if (!Character.isDigit(last) && last != 'X') {
            throw new IllegalArgumentException("Invalid ISBN10 format");
        }

        return new Code(isbn10, null, null);
    }

    /**
     * Creates a Code from an ISBN-13 string (13 digits, no letters allowed).
     * Hyphens are removed before validation.
     *
     * @param isbn13 the ISBN-13 value; must not be null or blank
     * @throws IllegalArgumentException if the format is invalid
     */
    public static Code fromIsbn13(String isbn13) {
        if (isbn13 == null || isbn13.isBlank()) {
            throw new IllegalArgumentException("ISBN13 cannot be null or blank");
        }

        String clean = removeHyphens(isbn13);

        if (clean.length() != 13) {
            throw new IllegalArgumentException("ISBN13 must have 13 digits");
        }

        // all 13 characters must be digits — no 'X' allowed in ISBN-13
        for (int i = 0; i < clean.length(); i++) {
            if (!Character.isDigit(clean.charAt(i))) {
                throw new IllegalArgumentException("Invalid ISBN13 format");
            }
        }

        return new Code(null, isbn13, null);
    }

    /**
     * Creates a Code from an ISSN string (8 characters; last can be a digit or 'X').
     * Hyphens are removed before validation, so "0317-8471" is also accepted.
     *
     * @param issn the ISSN value; must not be null or blank
     * @throws IllegalArgumentException if the format is invalid
     */
    public static Code fromIssn(String issn) {
        if (issn == null || issn.isBlank()) {
            throw new IllegalArgumentException("ISSN cannot be null or blank");
        }

        String clean = removeHyphens(issn);

        if (clean.length() != 8) {
            throw new IllegalArgumentException("ISSN must have 8 characters");
        }

        // first 7 must be digits
        for (int i = 0; i < 7; i++) {
            if (!Character.isDigit(clean.charAt(i))) {
                throw new IllegalArgumentException("Invalid ISSN format");
            }
        }

        // last character can be a digit or 'X'
        char last = clean.charAt(7);
        if (!Character.isDigit(last) && last != 'X') {
            throw new IllegalArgumentException("Invalid ISSN format");
        }

        return new Code(null, null, issn);
    }

    /**
     * Removes hyphens and converts to uppercase before validation.
     * The original value is always stored — this is only used internally during checks.
     */
    private static String removeHyphens(String value) {
        return value.replace("-", "").toUpperCase();
    }

    /** Returns the ISBN-10 value, or null if this is not an ISBN-10. */
    public String getIsbn10() {
        return isbn10;
    }

    /** Returns the ISBN-13 value, or null if this is not an ISBN-13. */
    public String getIsbn13() {
        return isbn13;
    }

    /** Returns the ISSN value, or null if this is not an ISSN. */
    public String getIssn() {
        return issn;
    }

    /**
     * Returns whichever code value is stored.
     * Only one field is non-null at a time, so this checks each one in order.
     */
    @Override
    public String toString() {
        if (isbn13 != null && !isbn13.isBlank()) return isbn13;
        if (isbn10 != null && !isbn10.isBlank()) return isbn10;
        if (issn   != null && !issn.isBlank())   return issn;
        return "";
    }

    /**
     * Two codes are equal if all three fields match.
     * Since only one field is ever set, this means same type and same value.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Code code = (Code) o;
        return Objects.equals(isbn10, code.isbn10)
                && Objects.equals(isbn13, code.isbn13)
                && Objects.equals(issn, code.issn);
    }

    /** hashCode is consistent with equals — same fields, same hash. */
    @Override
    public int hashCode() {
        return Objects.hash(isbn10, isbn13, issn);
    }

    public static Code empty() {
        return new Code("", "", "");
    }
}