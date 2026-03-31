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
 * Tests for AppraisalEntity.
 *
 * Validates constructor rules, attribute initialization,
 * identity, equality, and general behavior.
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
        assertEquals(1, entity.getPublicationTypeId().size());
        assertEquals(1, entity.getGenreId().size());
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

    // SAMEAS TESTS


    @Test
    void sameAs_shouldReturnFalse_whenOnlyOneFieldDifferent_complexCase() {
        // Arrange
        Contact contact = mock(Contact.class);

        PublicationTypeId pub1 = mock(PublicationTypeId.class);
        PublicationTypeId pub2 = mock(PublicationTypeId.class);

        GenreID genre = mock(GenreID.class);

        AppraisalEntity entity1 = new AppraisalEntity(
                "Name",
                contact,
                List.of(pub1, pub2),
                List.of(genre)
        );

        AppraisalEntity entity2 = new AppraisalEntity(
                "Name",
                contact,
                List.of(pub1),
                List.of(genre)
        );

        // Act
        boolean result = entity1.sameAs(entity2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAs_shouldReturnFalse_whenListOrderDifferent() {
        // Arrange
        Contact contact = mock(Contact.class);

        PublicationTypeId pub1 = mock(PublicationTypeId.class);
        PublicationTypeId pub2 = mock(PublicationTypeId.class);

        GenreID genre = mock(GenreID.class);

        AppraisalEntity entity1 = new AppraisalEntity(
                "Name",
                contact,
                List.of(pub1, pub2),
                List.of(genre)
        );

        AppraisalEntity entity2 = new AppraisalEntity(
                "Name",
                contact,
                List.of(pub2, pub1),
                List.of(genre)
        );

        // Act
        boolean result = entity1.sameAs(entity2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAs_shouldReturnFalse_whenIdsDifferent() {
        // Arrange
        AppraisalEntity entity1 = createValidEntity();
        AppraisalEntity entity2 = createValidEntity();

        // Act
        boolean result = entity1.sameAs(entity2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAs_shouldHandleInstanceCorrectly() {
        // Arrange
        AppraisalEntity entity = createValidEntity();

        // Act
        boolean result = entity.sameAs(entity);

        // Assert
        assertTrue(result);
    }


    @Test
    void idShouldReturnSameAsGetId() {
        // Arrange
        AppraisalEntity entity = createValidEntity();

        // Act
        AppraisalEntityId id1 = entity.getId();
        AppraisalEntityId id2 = entity.id();

        // Assert
        assertEquals(id1, id2);
    }

    @Test
    void sameAs_shouldReturnFalse_whenObjectIsNull() {
        // Arrange
        Contact contactDouble = mock(Contact.class);
        PublicationTypeId pubDouble = mock(PublicationTypeId.class);
        GenreID genreDouble = mock(GenreID.class);

        AppraisalEntity entity = new AppraisalEntity("Test Name", contactDouble, List.of(pubDouble), List.of(genreDouble));

        // Act
        boolean result = entity.sameAs(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAs_shouldReturnFalse_whenDifferentType() {
        // Arrange
        Contact contactDouble = mock(Contact.class);
        PublicationTypeId pubDouble = mock(PublicationTypeId.class);
        GenreID genreDouble = mock(GenreID.class);

        AppraisalEntity entity = new AppraisalEntity("Test Name", contactDouble, List.of(pubDouble), List.of(genreDouble));

        Object other = mock(Object.class);

        // Act
        boolean result = entity.sameAs(other);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAs_shouldReturnTrue_whenSameInstance() {
        // Arrange
        Contact contactDouble = mock(Contact.class);
        PublicationTypeId pubDouble = mock(PublicationTypeId.class);
        GenreID genreDouble = mock(GenreID.class);

        AppraisalEntity entity = new AppraisalEntity("Test Name", contactDouble, List.of(pubDouble), List.of(genreDouble));
        // Act
        boolean result = entity.sameAs(entity);

        // Assert
        assertTrue(result);
    }

    @Test
    void sameAs_shouldReturnTrue_whenAllFieldsEqual() {
        // Arrange
        Contact contactDouble = mock(Contact.class);
        PublicationTypeId pubDouble = mock(PublicationTypeId.class);
        GenreID genreDouble = mock(GenreID.class);

        AppraisalEntity entity1 = new AppraisalEntity("Test Name", contactDouble, List.of(pubDouble), List.of(genreDouble));
        AppraisalEntity entity2 = new AppraisalEntity("Test Name", contactDouble, List.of(pubDouble), List.of(genreDouble));

        // Act
        boolean result = entity1.sameAs(entity2);

        // Assert
        assertTrue(result);
    }

    @Test
    void sameAs_shouldReturnFalse_whenNameDifferent() {
        // Arrange
        Contact contactDouble = mock(Contact.class);
        PublicationTypeId pubDouble = mock(PublicationTypeId.class);
        GenreID genreDouble = mock(GenreID.class);

        AppraisalEntity entity1 = new AppraisalEntity("Test Name", contactDouble, List.of(pubDouble), List.of(genreDouble));
        AppraisalEntity entity2 = new AppraisalEntity("Test Name1", contactDouble, List.of(pubDouble), List.of(genreDouble));

        // Act
        boolean result = entity1.sameAs(entity2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAs_shouldReturnFalse_whenContactDifferent() {
        // Arrange
        Contact contactDouble1 = mock(Contact.class);
        Contact contactDouble2 = mock(Contact.class);
        PublicationTypeId pubDouble = mock(PublicationTypeId.class);
        GenreID genreDouble = mock(GenreID.class);

        AppraisalEntity entity1 = new AppraisalEntity("Test Name", contactDouble1, List.of(pubDouble), List.of(genreDouble));
        AppraisalEntity entity2 = new AppraisalEntity("Test Name", contactDouble2, List.of(pubDouble), List.of(genreDouble));
        // Act
        boolean result = entity1.sameAs(entity2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAs_shouldReturnFalse_whenGenreIdDifferent() {
        // Arrange
        Contact contactDouble = mock(Contact.class);
        PublicationTypeId pubDouble = mock(PublicationTypeId.class);
        GenreID genreDouble1 = mock(GenreID.class);
        GenreID genreDouble2 = mock(GenreID.class);

        AppraisalEntity entity1 = new AppraisalEntity("Test Name", contactDouble, List.of(pubDouble), List.of(genreDouble1));
        AppraisalEntity entity2 = new AppraisalEntity("Test Name", contactDouble, List.of(pubDouble), List.of(genreDouble2));
        // Act
        boolean result = entity1.sameAs(entity2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAs_shouldReturnFalse_whenPublicationTypeIdDifferent() {
        // Arrange
        Contact contactDouble = mock(Contact.class);
        PublicationTypeId pubDouble1 = mock(PublicationTypeId.class);
        PublicationTypeId pubDouble2 = mock(PublicationTypeId.class);
        GenreID genreDouble = mock(GenreID.class);

        AppraisalEntity entity1 = new AppraisalEntity("Test Name", contactDouble, List.of(pubDouble1), List.of(genreDouble));
        AppraisalEntity entity2 = new AppraisalEntity("Test Name", contactDouble, List.of(pubDouble2), List.of(genreDouble));
        // Act
        boolean result = entity1.sameAs(entity2);

        // Assert
        assertFalse(result);
    }

    @Test
    void sameAsShouldReturnTrueForEqualBusinessData() {

        // ARRANGE
        Contact contactDouble = mock(Contact.class);
        PublicationTypeId pubDouble = mock(PublicationTypeId.class);
        GenreID genreDouble = mock(GenreID.class);

        AppraisalEntity entityDouble1 = new AppraisalEntity(
                "Name",
                contactDouble,
                List.of(pubDouble),
                List.of(genreDouble)
        );

        AppraisalEntity entityDouble2 = new AppraisalEntity(
                "Name",
                contactDouble,
                List.of(pubDouble),
                List.of(genreDouble)
        );

        // ACT
        boolean result = entityDouble1.sameAs(entityDouble2);

        // ASSERT
        assertTrue(result);
    }

    // GETTERS TESTS
    @Test
    void gettersShouldReturnCorrectValues() {

        // ARRANGE
        AppraisalEntity entity = createValidEntity();

        // ACT

        // ASSERT
        assertNotNull(entity.getId());
        assertNotNull(entity.identity());
        assertNotNull(entity.getName());
        assertNotNull(entity.getContact());
        assertNotNull(entity.getPublicationTypeId());
        assertNotNull(entity.getGenreId());
    }

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
        assertEquals(List.of(pubDouble), entity.getPublicationTypeId());
        assertEquals(List.of(genreDouble), entity.getGenreId());
        assertEquals(entity.getId(), entity.identity());
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
        assertTrue(result.contains("name"));
    }

}
