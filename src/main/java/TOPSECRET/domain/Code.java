package TOPSECRET.domain;

import java.util.Objects;

/**
 * Represents the identification code of a publication.
 *
 * <p>A publication can be identified by one of three standard code types:
 * <ul>
 *   <li><b>ISBN-10</b> — older code format for books (10 characters)</li>
 *   <li><b>ISBN-13</b> — current standard code format for books (13 digits)</li>
 *   <li><b>ISSN</b> — code format for magazines and journals (8 characters)</li>
 * </ul>
 *
 * <p>Each instance of this class holds exactly one of these three types —
 * the other two fields are always {@code null}.
 *
 * <p>Objects cannot be created directly with {@code new Code(...)}.
 * Instead, use the static factory methods: {@link #fromIsbn10}, {@link #fromIsbn13}, or {@link #fromIssn}.
 * Each factory method validates the format before creating the object,
 * so a {@code Code} is always valid once it exists.
 */
public class Code {

    // only one of these will have a value — the other two will always be null
    private final String isbn10; // e.g. "0306406152"
    private final String isbn13; // e.g. "9783161484100"
    private final String issn;   // e.g. "03178471"

    /**
     * Private constructor — forces creation through the static factory methods.
     *
     * <p>By keeping this private, we make sure no one can create a {@code Code}
     * without going through validation first. The factory methods call this after
     * they check the format, so any Code object that exists is guaranteed to be valid.
     *
     * @param isbn10 ISBN-10 value, or null
     * @param isbn13 ISBN-13 value, or null
     * @param issn   ISSN value, or null
     */
    public Code(String isbn10, String isbn13, String issn) {
        this.isbn10 = isbn10;
        this.isbn13 = isbn13;
        this.issn = issn;
    }

    /**
     * Creates a {@code Code} from an ISBN-10 string.
     *
     * <p>ISBN-10 is an older format used for books. It has 10 characters:
     * the first 9 must be digits, and the last one can be a digit or the letter {@code X}.
     * Hyphens are removed before validation, so "0-306-40615-2" is treated the same as "0306406152".
     *
     * @param isbn10 the ISBN-10 string to validate and store
     * @return a new {@code Code} with the ISBN-10 set
     * @throws IllegalArgumentException if the value is null, blank, or does not match the ISBN-10 format
     */
    public static Code fromIsbn10(String isbn10) {
        if (isbn10 == null || isbn10.isBlank()) {
            throw new IllegalArgumentException("ISBN10 cannot be null or blank");
        }

        // hyphens are removed before checking length and characters
        // this allows inputs like "0-306-40615-2" to be accepted
        String clean = removeHyphens(isbn10);

        // ISBN-10 must have exactly 10 characters after removing hyphens
        if (clean.length() != 10) {
            throw new IllegalArgumentException("ISBN10 must have 10 characters");
        }

        // the first 9 characters must all be digits
        for (int i = 0; i < 9; i++) {
            if (!Character.isDigit(clean.charAt(i))) {
                throw new IllegalArgumentException("Invalid ISBN10 format");
            }
        }

        // the last character is special — it can be a digit or 'X' (used as check digit)
        char last = clean.charAt(9);
        if (!Character.isDigit(last) && last != 'X') {
            throw new IllegalArgumentException("Invalid ISBN10 format");
        }

        // validation passed — create the object with only isbn10 set
        return new Code(isbn10, null, null);
    }

    /**
     * Creates a {@code Code} from an ISBN-13 string.
     *
     * <p>ISBN-13 is the current standard for books. It has exactly 13 digits.
     * All characters must be digits — there is no 'X' allowed in ISBN-13.
     * Hyphens are removed before validation.
     *
     * @param isbn13 the ISBN-13 string to validate and store
     * @return a new {@code Code} with the ISBN-13 set
     * @throws IllegalArgumentException if the value is null, blank, or does not match the ISBN-13 format
     */
    public static Code fromIsbn13(String isbn13) {
        if (isbn13 == null || isbn13.isBlank()) {
            throw new IllegalArgumentException("ISBN13 cannot be null or blank");
        }

        // hyphens removed before checking
        String clean = removeHyphens(isbn13);

        // ISBN-13 must have exactly 13 digits
        if (clean.length() != 13) {
            throw new IllegalArgumentException("ISBN13 must have 13 digits");
        }

        // every character must be a digit — no letters or 'X' allowed in ISBN-13
        for (int i = 0; i < clean.length(); i++) {
            if (!Character.isDigit(clean.charAt(i))) {
                throw new IllegalArgumentException("Invalid ISBN13 format");
            }
        }

        // validation passed — create the object with only isbn13 set
        return new Code(null, isbn13, null);
    }

    /**
     * Creates a {@code Code} from an ISSN string.
     *
     * <p>ISSN is the standard code for magazines and journals. It has 8 characters:
     * the first 7 must be digits, and the last one can be a digit or the letter {@code X}.
     * Hyphens are removed before validation, so "0317-8471" is treated the same as "03178471".
     *
     * @param issn the ISSN string to validate and store
     * @return a new {@code Code} with the ISSN set
     * @throws IllegalArgumentException if the value is null, blank, or does not match the ISSN format
     */
    public static Code fromIssn(String issn) {
        if (issn == null || issn.isBlank()) {
            throw new IllegalArgumentException("ISSN cannot be null or blank");
        }

        // hyphens removed before checking
        String clean = removeHyphens(issn);

        // ISSN must have exactly 8 characters after removing hyphens
        if (clean.length() != 8) {
            throw new IllegalArgumentException("ISSN must have 8 characters");
        }

        // the first 7 characters must all be digits
        for (int i = 0; i < 7; i++) {
            if (!Character.isDigit(clean.charAt(i))) {
                throw new IllegalArgumentException("Invalid ISSN format");
            }
        }

        // the last character can be a digit or 'X' (check digit, same rule as ISBN-10)
        char last = clean.charAt(7);
        if (!Character.isDigit(last) && last != 'X') {
            throw new IllegalArgumentException("Invalid ISSN format");
        }

        // validation passed — create the object with only issn set
        return new Code(null, null, issn);
    }

    /**
     * Removes hyphens from a code string and converts it to uppercase.
     *
     * <p>This is a private helper used only inside this class during validation.
     * It is not stored — we always store the original value as the user typed it.
     * Uppercase is needed so that 'x' and 'X' are treated the same way.
     *
     * @param value the raw code string
     * @return the same string without hyphens and in uppercase
     */
    private static String removeHyphens(String value) {
        return value.replace("-", "").toUpperCase();
    }

    // ============ Getters ============

    /** Returns the ISBN-10 value, or {@code null} if this code is not an ISBN-10. */
    public String getIsbn10() {
        return isbn10;
    }

    /** Returns the ISBN-13 value, or {@code null} if this code is not an ISBN-13. */
    public String getIsbn13() {
        return isbn13;
    }

    /** Returns the ISSN value, or {@code null} if this code is not an ISSN. */
    public String getIssn() {
        return issn;
    }

    // ============ Object overrides ============

    /**
     * Returns the code as a plain string, whichever type is stored.
     *
     * <p>Only one field will be non-null, so this checks each one in order
     * and returns the first one that has a value.
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
     * Two {@code Code} objects are equal if all three fields are equal.
     *
     * <p>Since only one field is ever set at a time, this effectively means
     * two codes are equal when they have the same type and the same value.
     * {@code Objects.equals} is used because some fields can be null.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Code code = (Code) o;
        return Objects.equals(isbn10, code.isbn10)
                && Objects.equals(isbn13, code.isbn13)
                && Objects.equals(issn, code.issn);
    }

    /**
     * hashCode is consistent with equals — same fields, same hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(isbn10, isbn13, issn);
    }
}