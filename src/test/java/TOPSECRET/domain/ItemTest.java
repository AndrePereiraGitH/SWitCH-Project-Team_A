package TOPSECRET.domain;

import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    // -------------------------
    // Helpers
    // -------------------------

    private Condition validCondition() {
        return Condition.LIKE_NEW;
    }

    private double validWeight() {
        return 300.2;
    }

    private Dimensions validDimensions() {
        return new Dimensions(10.0, 0.2, 5.2);
    }

    private Publication validPublication() {
        Profile profile = new Profile("Tove Ditlevsen", new Country("DENMARK"), LocalDate.of(1917, 12, 14));
        Author author = new Author(profile, "valid bio");
        Address address = new Address("R Maria", "12", "156-5", new Country("PORTUGAL"));
        Email email = new Email("maria1", "gmail.com");
        Contact contact = new Contact("915945695", "+351", email, address);
        Publisher publisher = new Publisher("Some Publisher", "PT123456789", contact);

        PublicationType publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback");

        BibliographicalData bd = new BibliographicalData(
                "Childhood, Youth, Dependency: The Copenhagen Trilogy",
                List.of(author),
                Locale.ENGLISH,
//                publisher,
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Fiction")),
//                200,
                "Some text"
        );

        User user = new User("Maria", contact, profile, UserRole.NORMAL_USER);
        Code code = Code.fromIsbn13("9781234567890");

        // Edition
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.MAGAZINE, TypeOfBinding.CASE_BINDING, "Some details");
        Edition edition= new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

//        return new Publication(bd, publicationType, user, PublicationStatus.FOR_SALE, "1st edition", code);
        return new Publication(bd, publicationType, user, PublicationStatus.FOR_SALE, edition, code);
    }

    private Item validItem() {
        return new Item(validCondition(), validWeight(), validDimensions(), validPublication());
    }

    // helper: makes it easy to override one field and keep the rest valid
    private Item itemWith(Condition condition, double weight, Dimensions dimensions, Publication publication) {
        return new Item(                                                // Ternary operator (short if/else):
                condition != null ? condition : validCondition(),       // If 'condition' is not null, use it;
                weight,                                                 // Else, use valid condition().
                dimensions != null ? dimensions : validDimensions(),    // This allows tests to change only one field while keeping the others valid.
                publication != null ? publication : validPublication()
        );
    }

    // -------------------------
    // Helper to ensure Publication inequality
    // -------------------------

    private Publication publicationWithDifferentIdentity() {
        Profile profile = new Profile("Tove Ditlevsen", new Country("DENMARK"), LocalDate.of(1917, 12, 14));
        Author author = new Author(profile, "valid bio");
        Address address = new Address("R Maria", "12", "156-5", new Country("PORTUGAL"));
        Email email = new Email("maria1", "gmail.com");
        Contact contact = new Contact("915945695", "+351", email, address);
        Publisher publisher = new Publisher("Some Publisher", "PT123456789", contact);

        PublicationType publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback");

        BibliographicalData bd = new BibliographicalData(
                "Childhood, Youth, Dependency: The Copenhagen Trilogy",
                List.of(author),
                Locale.ENGLISH,
//                publisher,
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Fiction")),
//                200,
                "Some text"
        );

        User user = new User("Maria", contact, profile, UserRole.NORMAL_USER);

        // NOTE: Different ISBN + different edition to guarantee inequality.
        Code differentCode = Code.fromIsbn13("9781234567891");

        // Edition
        Code code = Code.fromIsbn13 ("9783161484123");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
        Edition edition= new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

//        return new Publication(bd, publicationType, user, PublicationStatus.FOR_SALE, "2nd edition", differentCode);
        return new Publication(bd, publicationType, user, PublicationStatus.FOR_SALE, edition, differentCode);
    }

    // -------------------------
    // Happy path
    // -------------------------

    @Test
    void shouldCreateItem_withValidData() {
        Item item = validItem();

        assertNotNull(item);
        assertEquals(Condition.LIKE_NEW, item.getCondition());
        assertEquals(300.2, item.getWeight(), 0.0001);    // For doubles, we use a "delta" (tolerance), because doubles can have small precision issues
        assertNotNull(item.getDimension());
        assertNotNull(item.getPublication());
    }

    // -------------------------
    // Invalid arguments
    // -------------------------

    @Test
    void constructor_shouldThrow_whenConditionIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                // This "() -> ..." is a lambda: it means "run this code later"
                // We pass the constructor call inside the lambda because assertThrows needs to execute it itself.
                new Item(null, validWeight(), validDimensions(), validPublication())
        );
        assertEquals("Condition cannot be null", ex.getMessage());
    }

    @Test
    void constructor_shouldThrow_whenWeightIsZero() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Item(validCondition(), 0.0, validDimensions(), validPublication())
        );
        assertEquals("Weight must be greater than 0.", ex.getMessage());
    }

    @Test
    void constructor_shouldThrow_whenWeightIsNegative() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Item(validCondition(), -1.0, validDimensions(), validPublication())
        );
        assertEquals("Weight must be greater than 0.", ex.getMessage());
    }

    @Test
    void constructor_shouldThrow_whenDimensionsIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Item(validCondition(), validWeight(), null, validPublication())
        );
        assertEquals("Dimensions cannot be null", ex.getMessage());
    }

    @Test
    void constructor_shouldThrow_whenPublicationIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Item(validCondition(), validWeight(), validDimensions(), null)
        );
        assertEquals("Publication cannot be null", ex.getMessage());
    }

    // -------------------------
    // equals / hashCode coverage
    // -------------------------

    @Test
    void equals_shouldReturnFalse_whenOtherIsNull() {
        Item a = validItem();
        assertNotEquals(null, a);
    }

    @Test
    void equals_shouldReturnFalse_whenOtherIsDifferentClass() {
        Item a = validItem();
        assertNotEquals("not an Item", a); //executes the (getClass() != o.getClass()) branch
    }

    @Test
    void equals_shouldReturnTrue_whenSameValues() {
        Item a = validItem();
        Item b = itemWith(a.getCondition(), a.getWeight(), a.getDimension(), a.getPublication());

        assertEquals(a, b);
        assertEquals(b, a); // symmetry
    }

    @Test
    void equals_shouldReturnFalse_whenWeightDiffers() {
        Item a = validItem();
        Item b = itemWith(validCondition(), validWeight() + 1.0, validDimensions(), validPublication());

        assertNotEquals(a, b);
    }

    @Test
    void equals_shouldReturnFalse_whenConditionDiffers() {
        Item a = validItem();
        Condition different = (validCondition() == Condition.LIKE_NEW) ? Condition.GOOD : Condition.LIKE_NEW;
        Item b = itemWith(different, validWeight(), validDimensions(), validPublication());

        assertNotEquals(a, b);
    }

    @Test
    void equals_shouldReturnFalse_whenDimensionsDiffers() {
        Item a = validItem();
        Dimensions differentDimensions = new Dimensions(99.0, 0.2, 5.2);
        Item b = itemWith(validCondition(), validWeight(), differentDimensions, validPublication());

        assertNotEquals(a, b);
    }

    @Test
    void equals_shouldReturnFalse_whenPublicationDiffers() {
        Item a = validItem();
        Publication differentPublication = publicationWithDifferentIdentity();
        Item b = itemWith(validCondition(), validWeight(), validDimensions(), differentPublication);

        assertNotEquals(a, b);
    }

    @Test
    void hashCode_shouldBeEqual_whenItemsAreEqual() {
        Item a = validItem();

        Item b = itemWith(a.getCondition(), a.getWeight(), a.getDimension(), a.getPublication());

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
