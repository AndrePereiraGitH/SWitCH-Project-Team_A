package TOPSECRET.domain.AppraisalEntity;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.AppraisalEntityId;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link AppraisalEntity}.
 *
 * <p>
 * Validates domain behavior including:
 * </p>
 *
 * <ul>
 *     <li>Entity creation and validation rules</li>
 *     <li>Getters and identity handling</li>
 *     <li>Equality and hashCode contract</li>
 *     <li>Business comparison via {@code sameAs}</li>
 *     <li>State updates through {@code update}</li>
 * </ul>
 *
 * <p>
 * Ensures that all domain invariants are enforced and that
 * invalid inputs result in appropriate exceptions.
 * </p>
 *
 * <p>
 * Tests are performed using real domain objects and mocks only
 * for dependent value objects.
 * </p>
 */
class AppraisalEntityTest {

    // HELPER
    private AppraisalEntity createValidEntity() {

        Contact contactDouble = mock(Contact.class);
        PublicationTypeId pubDouble = mock(PublicationTypeId.class);
        GenreID genreDouble = mock(GenreID.class);

        return new AppraisalEntity(
                "Test",
                contactDouble,
                List.of(pubDouble),
                List.of(genreDouble)
        );
    }

    // CONSTRUCTOR TESTS
    @Test
    void shouldCreateValidAppraisalEntity() {

        // ARRANGE
        Contact contactDouble = mock(Contact.class);
        PublicationTypeId pubDouble = mock(PublicationTypeId.class);
        GenreID genreDouble = mock(GenreID.class);

        // ACT
        AppraisalEntity entity = new AppraisalEntity(
                "Test Entity",
                contactDouble,
                List.of(pubDouble),
                List.of(genreDouble)
        );

        // ASSERT
        assertNotNull(entity);
        assertEquals("Test Entity", entity.getName());
        assertEquals(contactDouble, entity.getContact());
        assertEquals(1, entity.getPublicationTypeIds().size());
        assertEquals(1, entity.getGenreIds().size());
    }

    @Test
    void shouldThrowWhenNameIsNull() {

        // ARRANGE
        Contact contactDouble = mock(Contact.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntity(null, contactDouble, List.of(mock(PublicationTypeId.class)), List.of(mock(GenreID.class)))
        );
    }

    @Test
    void shouldThrowWhenNameIsBlank() {

        // ARRANGE
        Contact contactDouble = mock(Contact.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntity("", contactDouble, List.of(mock(PublicationTypeId.class)), List.of(mock(GenreID.class)))
        );
    }

    @Test
    void shouldThrowWhenContactIsNull() {

        // ARRANGE

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntity("Test", null, List.of(mock(PublicationTypeId.class)), List.of(mock(GenreID.class)))
        );
    }

    @Test
    void shouldThrowWhenPublicationTypeListIsNull() {

        // ARRANGE
        Contact contactDouble = mock(Contact.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntity("Test", contactDouble, null, List.of(mock(GenreID.class)))
        );
    }

    @Test
    void shouldThrowWhenPublicationTypeListIsEmpty() {

        // ARRANGE
        Contact contactDouble = mock(Contact.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntity("Test", contactDouble, List.of(), List.of(mock(GenreID.class)))
        );
    }

    @Test
    void shouldThrowWhenGenreListIsNull() {

        // ARRANGE
        Contact contactDouble = mock(Contact.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntity("Test", contactDouble, List.of(mock(PublicationTypeId.class)), null)
        );
    }

    @Test
    void shouldThrowWhenGenreListIsEmpty() {

        // ARRANGE
        Contact contactDouble = mock(Contact.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntity("Test", contactDouble, List.of(mock(PublicationTypeId.class)), List.of())
        );
    }

    @Test
    void constructor_shouldFailWhenIdNull() {

        // ARRANGE
        Contact contactDouble = mock(Contact.class);
        PublicationTypeId pubDouble = mock(PublicationTypeId.class);
        GenreID genreDouble = mock(GenreID.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                new AppraisalEntity(
                        null,
                        "Test",
                        contactDouble,
                        List.of(pubDouble),
                        List.of(genreDouble)
                )
        );
    }

    // EQUALS TESTS
    @Test
    void equalsShouldReturnTrueForSameInstance() {

        // ARRANGE
        AppraisalEntity entity = createValidEntity();

        // ACT
        boolean result = entity.equals(entity);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void equalsShouldReturnFalseForDifferentObject() {

        // ARRANGE
        AppraisalEntity entity = createValidEntity();

        // ACT
        boolean result = entity.equals(new Object());

        // ASSERT
        assertFalse(result);
    }

    @Test
    void equalsShouldReturnFalseForDifferentIds() {

        // ARRANGE
        AppraisalEntity entity1 = createValidEntity();
        AppraisalEntity entity2 = createValidEntity();

        // ACT
        boolean result = entity1.equals(entity2);

        // ASSERT
        assertFalse(result);
    }

    // HASHCODE TEST
    @Test
    void hashCodeShouldBeConsistent() {

        // ARRANGE
        AppraisalEntity entity = createValidEntity();

        // ACT
        int hash1 = entity.hashCode();
        int hash2 = entity.hashCode();

        // ASSERT
        assertEquals(hash1, hash2);
    }

    @Test
    void hashCodeShouldBeDifferentForDifferentIds() {

        // ARRANGE
        AppraisalEntity entity1 = createValidEntity();
        AppraisalEntity entity2 = createValidEntity();

        // ACT
        int hash1 = entity1.hashCode();
        int hash2 = entity2.hashCode();

        // ASSERT
        assertNotEquals(hash1, hash2);
    }

    // SAME AS TESTS
    @Test
    void sameAsShouldHandleInstanceCorrectly() {
        // ARRANGE
        AppraisalEntity entity = createValidEntity();

        // ACT
        boolean result = entity.sameAs(entity);

        // ASSERT
        assertTrue(result);
    }


    @Test
    void idShouldReturnSameAsGetId() {
        // ARRANGE
        AppraisalEntity entity = createValidEntity();

        // ACT
        AppraisalEntityId id1 = entity.id();
        AppraisalEntityId id2 = entity.id();

        // ASSERT
        assertEquals(id1, id2);
    }

    @Test
    void sameAsShouldReturnFalseWhenObjectIsNull() {
        // ARRANGE
        Contact contactDouble = mock(Contact.class);
        PublicationTypeId pubDouble = mock(PublicationTypeId.class);
        GenreID genreDouble = mock(GenreID.class);

        AppraisalEntity entity = new AppraisalEntity("Test Name", contactDouble, List.of(pubDouble), List.of(genreDouble));

        // ACT
        boolean result = entity.sameAs(null);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenDifferentType() {
        // ARRANGE
        Contact contactDouble = mock(Contact.class);
        PublicationTypeId pubDouble = mock(PublicationTypeId.class);
        GenreID genreDouble = mock(GenreID.class);

        AppraisalEntity entity = new AppraisalEntity("Test Name", contactDouble, List.of(pubDouble), List.of(genreDouble));

        Object other = mock(Object.class);

        // ACT
        boolean result = entity.sameAs(other);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnTrueWhenSameInstance() {
        // ARRANGE
        Contact contactDouble = mock(Contact.class);
        PublicationTypeId pubDouble = mock(PublicationTypeId.class);
        GenreID genreDouble = mock(GenreID.class);

        AppraisalEntity entity = new AppraisalEntity("Test Name", contactDouble, List.of(pubDouble), List.of(genreDouble));

        // ACT
        boolean result = entity.sameAs(entity);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void sameAsShouldReturnTrueWhenAllFieldsEqual() {
        // ARRANGE
        Contact contactDouble = mock(Contact.class);
        PublicationTypeId pubDouble = mock(PublicationTypeId.class);
        GenreID genreDouble = mock(GenreID.class);

        AppraisalEntity entity1 = new AppraisalEntity("Test Name", contactDouble, List.of(pubDouble), List.of(genreDouble));
        AppraisalEntity entity2 = new AppraisalEntity("Test Name", contactDouble, List.of(pubDouble), List.of(genreDouble));

        // ACT
        boolean result = entity1.sameAs(entity2);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void sameAs_shouldReturnFalse_whenNameDifferent() {
        // ARRANGE
        Contact contactDouble = mock(Contact.class);
        PublicationTypeId pubDouble = mock(PublicationTypeId.class);
        GenreID genreDouble = mock(GenreID.class);

        AppraisalEntity entity1 = new AppraisalEntity("Test Name", contactDouble, List.of(pubDouble), List.of(genreDouble));
        AppraisalEntity entity2 = new AppraisalEntity("Test Name1", contactDouble, List.of(pubDouble), List.of(genreDouble));

        // ACT
        boolean result = entity1.sameAs(entity2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenContactDifferent() {
        // ARRANGE
        Contact contactDouble1 = mock(Contact.class);
        Contact contactDouble2 = mock(Contact.class);
        PublicationTypeId pubDouble = mock(PublicationTypeId.class);
        GenreID genreDouble = mock(GenreID.class);

        AppraisalEntity entity1 = new AppraisalEntity("Test Name", contactDouble1, List.of(pubDouble), List.of(genreDouble));
        AppraisalEntity entity2 = new AppraisalEntity("Test Name", contactDouble2, List.of(pubDouble), List.of(genreDouble));

        // ACT
        boolean result = entity1.sameAs(entity2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenGenreIdDifferent() {
        // ARRANGE
        Contact contactDouble = mock(Contact.class);
        PublicationTypeId pubDouble = mock(PublicationTypeId.class);
        GenreID genreDouble1 = mock(GenreID.class);
        GenreID genreDouble2 = mock(GenreID.class);

        AppraisalEntity entity1 = new AppraisalEntity("Test Name", contactDouble, List.of(pubDouble), List.of(genreDouble1));
        AppraisalEntity entity2 = new AppraisalEntity("Test Name", contactDouble, List.of(pubDouble), List.of(genreDouble2));

        // ACT
        boolean result = entity1.sameAs(entity2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnFalseWhenPublicationTypeIdDifferent() {
        // ARRANGE
        Contact contactDouble = mock(Contact.class);
        PublicationTypeId pubDouble1 = mock(PublicationTypeId.class);
        PublicationTypeId pubDouble2 = mock(PublicationTypeId.class);
        GenreID genreDouble = mock(GenreID.class);

        AppraisalEntity entity1 = new AppraisalEntity("Test Name", contactDouble, List.of(pubDouble1), List.of(genreDouble));
        AppraisalEntity entity2 = new AppraisalEntity("Test Name", contactDouble, List.of(pubDouble2), List.of(genreDouble));

        // ACT
        boolean result = entity1.sameAs(entity2);

        // ASSERT
        assertFalse(result);
    }

    // GETTERS TESTS
    @Test
    void gettersShouldReturnExpectedValues() {

        // ARRANGE
        Contact contactDouble = mock(Contact.class);
        PublicationTypeId pubDouble = mock(PublicationTypeId.class);
        GenreID genreDouble = mock(GenreID.class);

        AppraisalEntity entity = new AppraisalEntity(
                "TestName",
                contactDouble,
                List.of(pubDouble),
                List.of(genreDouble)
        );

        // ACT

        // ASSERT
        assertEquals("TestName", entity.getName());
        assertEquals(contactDouble, entity.getContact());
        assertEquals(List.of(pubDouble), entity.getPublicationTypeIds());
        assertEquals(List.of(genreDouble), entity.getGenreIds());
        assertEquals(entity.id(), entity.id());
    }


    // TO STRING TEST
    @Test
    void toStringShouldContainImportantFields() {

        // ARRANGE
        AppraisalEntity entity = createValidEntity();

        // ACT
        String result = entity.toString();

        // ASSERT
        assertTrue(result.contains("AppraisalEntity"));
        assertTrue(result.contains(entity.getName()));
    }

    // UPDATE TEST
    @Test
    void updateShouldChangeAllFields() {
        // ARRANGE
        AppraisalEntity entity = createValidEntity();

        Contact newContact = mock(Contact.class);
        PublicationTypeId pub = mock(PublicationTypeId.class);
        GenreID genre = mock(GenreID.class);

        // ACT
        entity.update("NewName", newContact, List.of(pub), List.of(genre));

        // ASSERT
        assertEquals("NewName", entity.getName());
        assertEquals(newContact, entity.getContact());
    }

    @Test
    void getPublicationTypeIds_shouldBeImmutable() {
        AppraisalEntity entity = createValidEntity();

        List<PublicationTypeId> list = entity.getPublicationTypeIds();

        assertThrows(UnsupportedOperationException.class,
                list::clear);
    }

    @Test
    void getGenreIds_shouldBeImmutable() {
        AppraisalEntity entity = createValidEntity();

        List<GenreID> list = entity.getGenreIds();

        assertThrows(UnsupportedOperationException.class,
                list::clear);
    }

    @Test
    void shouldBeEqualWhenSameId() {

        // ARRANGE
        AppraisalEntityId id = mock(AppraisalEntityId.class);

        AppraisalEntity a = new AppraisalEntity(id, "Name", mock(Contact.class),
                List.of(mock(PublicationTypeId.class)),
                List.of(mock(GenreID.class)));

        AppraisalEntity b = new AppraisalEntity(id, "Different", mock(Contact.class),
                List.of(mock(PublicationTypeId.class)),
                List.of(mock(GenreID.class)));

        // ACT
        boolean result = a.equals(b);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void updateShouldThrowWhenInvalidData() {

        // ARRANGE
        AppraisalEntity entity = createValidEntity();

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> entity.update(
                        null,
                        mock(Contact.class),
                        List.of(mock(PublicationTypeId.class)),
                        List.of(mock(GenreID.class))
                ));
    }

    @Test
    void updateShouldReturnSameInstance() {

        // ARRANGE
        AppraisalEntity entity = createValidEntity();

        // ACT
        AppraisalEntity result = entity.update(
                "NewName",
                mock(Contact.class),
                List.of(mock(PublicationTypeId.class)),
                List.of(mock(GenreID.class))
        );

        // ASSERT
        assertSame(entity, result);
    }
}
