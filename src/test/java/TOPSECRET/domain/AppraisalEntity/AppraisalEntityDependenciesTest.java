//package TOPSECRET.domain;
//
//import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
//import TOPSECRET.domain.Enum.MediaFormat;
//import TOPSECRET.domain.Enum.TypeOfBinding;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.junit.jupiter.params.provider.Arguments;
//
//import java.util.List;
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Unit tests for AppraisalEntity.
// * Validates constructor rules, equals/hashCode,
// * getters and full information.
// */
//class AppraisalEntityDependenciesTest {
//
//    // =========================
//    // HELPERS
//    // =========================
//
//    private Contact createValidContact() {
//        return new Contact("912345678", "351", null, null);
//    }
//
//    // Creates a valid PublicationType for testing
//    private PublicationType createValidPublicationType(String details) {
//        return new PublicationType(
//                MediaFormat.BOOK,
//                TypeOfBinding.HARDBACK,
//                "Standard publication"
//        );
//    }
//
//    // Creates a valid Genre for testing
//    private Genre createValidGenre() {
//        return new Genre("Fiction");
//    }
//
//    private Genre createValidGenre(String title) {
//        return new Genre(title);
//    }
//
//    // Static helper used by MethodSource
//    private static PublicationType createValidPublicationTypeStatic() {
//        return new PublicationType(
//                MediaFormat.BOOK,
//                TypeOfBinding.HARDBACK,
//                "Standard publication"
//        );
//    }
//    private List<PublicationType> createPublicationTypeList (String... details) {
//        return java.util.Arrays.stream(details)
//                .map(this::createValidPublicationType)
//                .toList();
//    }
//
//    private List<Genre> createGenreList(String... titles) {
//        return java.util.Arrays.stream(titles)
//                .map(this::createValidGenre)
//                .toList();
//    }
//
//    // =========================
//    // PARAMETER PROVIDERS
//    // =========================
//
//    static Stream<String> invalidNamesProvider() {
//        return Stream.of(null, "", " ", "   ", "\t", "\n");
//    }
//
//    static Stream<Arguments> nullParameterProvider() {
//        return Stream.of(
//                Arguments.of(null,
//                        List.of(new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Standard")),
//                        List.of(new Genre("Fiction"))),
//
//                Arguments.of(new Contact("999", "351", null, null),
//                        null,
//                        List.of(new Genre("Fiction"))),
//
//                Arguments.of(new Contact("999", "351", null, null),
//                        List.of(new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Standard")),
//                        null)
//        );
//    }
//
//    // =========================
//    // VALID CONSTRUCTION
//    // =========================
//
//    @Test
//    void shouldCreateAppraisalEntityWhenAllDataIsValid() {
//
//        // ARRANGE
//        String expectedName = "Entity One";
//        Contact expectedContact = createValidContact();
//
//        List<PublicationType> publicationTypes =
//                createPublicationTypeList("Standard");
//
//        List<Genre> genres =
//                createGenreList("Fiction");
//
//        // ACT
//        AppraisalEntity entity =
//                new AppraisalEntity(
//                        expectedName,
//                        expectedContact,
//                        publicationTypes,
//                        genres
//                );
//
//        // ASSERT
//        assertEquals(expectedName, entity.getName());
//        assertEquals(expectedContact, entity.getContact());
//        assertEquals(1, entity.getPublicationTypes().size());
//        assertEquals(1, entity.getGenres().size());
//    }
//
//    // =========================
//    // INVALID NAME
//    // =========================
//
//    @ParameterizedTest
//    @MethodSource("invalidNamesProvider")
//    void shouldThrowExceptionWhenInvalidNameIsProvided(String invalidName) {
//
//        // ARRANGE
//        Contact contact = createValidContact();
//
//        // ACT + ASSERT
//        assertThrows(IllegalArgumentException.class, () ->
//                new AppraisalEntity(
//                        invalidName,
//                        contact,
//                        createPublicationTypeList("Standard"),
//                        createGenreList("Fiction")
//                )
//        );
//    }
//
//    // =========================
//    // NULL PARAMETERS
//    // =========================
//
//    @ParameterizedTest
//    @MethodSource("nullParameterProvider")
//    void shouldThrowExceptionWhenMandatoryParameterIsNull(
//            Contact contact,
//            List<PublicationType> publicationTypes,
//            List<Genre> genres) {
//
//        // ARRANGE
//        String validName = "Valid Name";
//
//        // ACT + ASSERT
//        assertThrows(IllegalArgumentException.class, () ->
//                new AppraisalEntity(
//                        validName,
//                        contact,
//                        publicationTypes,
//                        genres
//                )
//        );
//    }
//
//    // =========================
//    // EQUALS
//    // =========================
//
//    @Test
//    void shouldReturnTrueWhenNameAndContactAreEqual() {
//
//        // ARRANGE
//        Contact contact = createValidContact();
//
//        AppraisalEntity entity1 =
//                new AppraisalEntity("Entity",
//                        contact,
//                        createPublicationTypeList("Standard"),
//                        createGenreList("Fiction"));
//
//        AppraisalEntity entity2 =
//                new AppraisalEntity("entity",
//                        contact,
//                        createPublicationTypeList("Collector"),
//                        createGenreList("Drama"));
//
//        // ACT
//        boolean result = entity1.equals(entity2);
//
//        // ASSERT
//        assertEquals(true, result);
//    }
//
//    @Test
//    void shouldReturnFalseWhenNameIsDifferent() {
//
//        // ARRANGE
//        Contact contact = createValidContact();
//
//        AppraisalEntity entity1 =
//                new AppraisalEntity("Entity A",
//                        contact,
//                        createPublicationTypeList("Standard"),
//                        createGenreList("Fiction"));
//
//        AppraisalEntity entity2 =
//                new AppraisalEntity("Entity B",
//                        contact,
//                        createPublicationTypeList("Standard"),
//                        createGenreList("Fiction"));
//
//        // ACT
//        boolean result = entity1.equals(entity2);
//
//        // ASSERT
//        assertEquals(false, result);
//    }
//
//    @Test
//    void shouldReturnFalseWhenContactIsDifferent() {
//
//        // ARRANGE
//        AppraisalEntity entity1 =
//                new AppraisalEntity("Entity",
//                        new Contact("111", "351", null, null),
//                        createPublicationTypeList("Standard"),
//                        createGenreList("Fiction"));
//
//        AppraisalEntity entity2 =
//                new AppraisalEntity("Entity",
//                        new Contact("222", "351", null, null),
//                        createPublicationTypeList("Standard"),
//                        createGenreList("Fiction"));
//
//        // ACT
//        boolean result = entity1.equals(entity2);
//
//        // ASSERT
//        assertEquals(false, result);
//    }
//
//    @Test
//    void equalsShouldReturnTrueWhenSameInstance() {
//
//        // ARRANGE
//        AppraisalEntity entity =
//                new AppraisalEntity("Entity",
//                        createValidContact(),
//                        createPublicationTypeList("Standard"),
//                        createGenreList("Fiction"));
//
//        // ASSERT
//        assertTrue(entity.equals(entity));
//    }
//
//    @Test
//    void equalsShouldReturnFalseWhenComparedWithNull() {
//
//        // ARRANGE
//        AppraisalEntity entity =
//                new AppraisalEntity("Entity",
//                        createValidContact(),
//                        createPublicationTypeList("Standard"),
//                        createGenreList("Fiction"));
//
//        // ASSERT
//        assertFalse(entity.equals(null));
//    }
//
//    @Test
//    void equalsShouldReturnFalseWhenComparedWithDifferentClass() {
//
//        // ARRANGE
//        AppraisalEntity entity =
//                new AppraisalEntity("Entity",
//                        createValidContact(),
//                        createPublicationTypeList("Standard"),
//                        createGenreList("Fiction"));
//
//        // ASSERT
//        assertFalse(entity.equals("NotAnEntity"));
//    }
//
//    // =========================
//    // HASHCODE
//    // =========================
//
//    @Test
//    void hashCodeShouldBeEqualWhenObjectsAreEqual() {
//
//        // ARRANGE
//        Contact contact = createValidContact();
//
//        AppraisalEntity entity1 =
//                new AppraisalEntity("Entity",
//                        contact,
//                        createPublicationTypeList("Standard"),
//                        createGenreList("Fiction"));
//
//        AppraisalEntity entity2 =
//                new AppraisalEntity("ENTITY",
//                        contact,
//                        createPublicationTypeList("Collector"),
//                        createGenreList("Drama"));
//
//        // ACT
//        int hash1 = entity1.hashCode();
//        int hash2 = entity2.hashCode();
//
//        // ASSERT
//        assertEquals(hash1, hash2);
//    }
//
//    @Test
//    void hashCodeShouldBeDifferentWhenObjectsAreDifferent() {
//
//        // ARRANGE
//        AppraisalEntity entity1 =
//                new AppraisalEntity("Entity A",
//                        createValidContact(),
//                        createPublicationTypeList("Standard"),
//                        createGenreList("Fiction"));
//
//        AppraisalEntity entity2 =
//                new AppraisalEntity("Entity B",
//                        createValidContact(),
//                        createPublicationTypeList("Standard"),
//                        createGenreList("Fiction"));
//
//        // ASSERT
//        assertNotEquals(entity1.hashCode(), entity2.hashCode());
//    }
//
//    @Test
//    void hashCodeShouldBeConsistent() {
//
//        // ARRANGE
//        AppraisalEntity entity =
//                new AppraisalEntity("Entity",
//                        createValidContact(),
//                        createPublicationTypeList("Standard"),
//                        createGenreList("Fiction"));
//
//        int first = entity.hashCode();
//        int second = entity.hashCode();
//
//        // ASSERT
//        assertEquals(first, second);
//    }
//
//    // =========================
//    // FULL INFO
//    // =========================
//
//    @Test
//    void shouldReturnFullInformationString() {
//
//        // ARRANGE
//        String name = "Entity One";
//        Contact contact = createValidContact();
//
//        AppraisalEntity entity =
//                new AppraisalEntity(name,
//                        contact,
//                        createPublicationTypeList("Standard"),
//                        createGenreList("Fiction"));
//
//        // ACT
//        String result = entity.AppraisalEntityFullInformation();
//
//        // ASSERT
//        assertEquals(true, result.contains(name));
//        assertEquals(true, result.contains(contact.toString()));
//    }
//}