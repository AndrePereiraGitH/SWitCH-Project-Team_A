package TOPSECRET.dto;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Profile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicationSampleDTO_US016Test {

    @Test
    void toDtoMapsAllFieldsCorrectly() {

        PublicationSample publicationSampleDouble = mock(PublicationSample.class);
        BibliographicalData bdDouble = mock(BibliographicalData.class);

        Author authorDouble1 = mock(Author.class);
        Author authorDouble2 = mock(Author.class);
        Profile profileDouble1 = mock(Profile.class);
        Profile profileDouble2 = mock(Profile.class);

        Genre genreDouble1 = mock(Genre.class);
        Genre genreDouble2 = mock(Genre.class);

        User owner = mock(User.class);
        Condition conditionDouble = mock(Condition.class);

        when(publicationSampleDouble.getBibliographicalData()).thenReturn(bdDouble);

        when(bdDouble.getTitle()).thenReturn("Lord Of the Rings");
        when(bdDouble.getOriginalLanguage()).thenReturn(Locale.ENGLISH);

        when(bdDouble.getAuthors()).thenReturn(List.of(authorDouble1, authorDouble2));
        when(authorDouble1.getProfile()).thenReturn(profileDouble1);
        when(authorDouble2.getProfile()).thenReturn(profileDouble2);
        when(profileDouble1.getName()).thenReturn("Toy");
        when(profileDouble2.getName()).thenReturn("Quim Barreiros");

        when(bdDouble.getGenres()).thenReturn(List.of(genreDouble1, genreDouble2));
        when(genreDouble1.getGenreType()).thenReturn("Fiction");
        when(genreDouble2.getGenreType()).thenReturn("Adventure");

        when(publicationSampleDouble.getOwner()).thenReturn(owner);
        when(owner.getUserName()).thenReturn("Rodolph");

        when(publicationSampleDouble.getCondition()).thenReturn(conditionDouble);
        when(conditionDouble.getDescription()).thenReturn("LIKE_NEW");

        // act
        PublicationSampleDTO_US016 result = PublicationSampleDTO_US016.toDTO(publicationSampleDouble);

        // assert
        assertEquals("Lord Of the Rings", result.getTitle());
        assertEquals(List.of("Toy", "Quim Barreiros"), result.getAuthorName());
        assertEquals(Locale.ENGLISH, result.getLanguage());
        assertEquals(List.of("Fiction", "Adventure"), result.getGenreType());
        assertEquals("Rodolph", result.getOwnerName());
        assertEquals("LIKE_NEW", result.getCondition());

    }

    @Test
    void hashCode_shouldBeDifferent_whenObjectsAreDifferent() {

        PublicationSampleDTO_US016 dto1 = new PublicationSampleDTO_US016("title", List.of("Quim Barreiros"), Locale.ENGLISH, List.of("Fiction"), "owner", "LIKE_NEW");

        PublicationSampleDTO_US016 dto2 = new PublicationSampleDTO_US016("title2", List.of("Quim Barreiros"), Locale.ENGLISH, List.of("Fiction"), "owner", "LIKE_NEW");

        PublicationSampleDTO_US016 dto3 = new PublicationSampleDTO_US016("title3", List.of("Quim Barreiros"), Locale.ENGLISH, List.of("Fiction"), "owner", "LIKE_NEW");

       // assertNotEquals(dto1.hashCode(), dto2.hashCode());

        assertTrue(dto1.hashCode() != dto2.hashCode() || dto1.hashCode() != dto3.hashCode() || dto2.hashCode() != dto3.hashCode());

    }

        /**
     * Tests for equals and hash code
     */

    @Test
    void equals_shouldReturnTrue_forSameReference() {
        PublicationSampleDTO_US016 dto = createPublicationSampleDTO();

        assertEquals(dto, dto);
    }

    @Test
    void equals_shouldReturnFalse_whenComparedWithNull() {
        PublicationSampleDTO_US016 dto = createPublicationSampleDTO();

        assertNotEquals(dto, null);
    }

    @Test
    void equals_shouldReturnFalse_whenComparedWithDifferentClass() {
        PublicationSampleDTO_US016 dto = createPublicationSampleDTO();

        assertNotEquals(dto, "not a dto");
    }

    @Test
    void equals_shouldReturnTrue_forDifferentInstancesWithSameValues() {
        PublicationSampleDTO_US016 dto1 = createPublicationSampleDTO();
        PublicationSampleDTO_US016 dto2 = createPublicationSampleDTO();

        assertEquals(dto1, dto2);
        assertEquals(dto2, dto1);
    }

    @Test
    void hashCode_shouldBeEqual_whenObjectsAreEqual() {
        PublicationSampleDTO_US016 dto1 = createPublicationSampleDTO();
        PublicationSampleDTO_US016 dto2 = createPublicationSampleDTO();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void hashCode_shouldBeConsistent() {
        PublicationSampleDTO_US016 dto = createPublicationSampleDTO();

        int first = dto.hashCode();
        int second = dto.hashCode();

        assertEquals(first, second);
    }

    @Test
    void equals_shouldReturnFalse_whenTitleIsDifferent() {
        PublicationSampleDTO_US016 dto1 = createPublicationSampleDTO();
        PublicationSampleDTO_US016 dto2 = new PublicationSampleDTO_US016(
                "Different title",
                dto1.getAuthorName(),
                dto1.getLanguage(),
                dto1.getGenreType(),
                dto1.getOwnerName(),
                dto1.getCondition()
        );

        assertNotEquals(dto1, dto2);
    }

    @Test
    void equals_shouldReturnFalse_whenAuthorNameIsDifferent() {
        PublicationSampleDTO_US016 dto1 = createPublicationSampleDTO();
        PublicationSampleDTO_US016 dto2 = new PublicationSampleDTO_US016(
                dto1.getTitle(),
                new ArrayList<>(List.of("another author")),
                dto1.getLanguage(),
                dto1.getGenreType(),
                dto1.getOwnerName(),
                dto1.getCondition()
        );

        assertNotEquals(dto1, dto2);
    }

    @Test
    void equals_shouldReturnFalse_whenLanguageIsDifferent() {
        PublicationSampleDTO_US016 dto1 = createPublicationSampleDTO();
        PublicationSampleDTO_US016 dto2 = new PublicationSampleDTO_US016(
                dto1.getTitle(),
                dto1.getAuthorName(),
                Locale.FRENCH,
                dto1.getGenreType(),
                dto1.getOwnerName(),
                dto1.getCondition()
        );

        assertNotEquals(dto1, dto2);
    }

    private PublicationSampleDTO_US016 createPublicationSampleDTO() {
        return new PublicationSampleDTO_US016(
                "Lord of the Rings",
                new ArrayList<>(List.of("author name")),
                Locale.ENGLISH,
                new ArrayList<>(List.of("Fiction")),
                "seller name",
                "LIKE_NEW"
        );
    }

    @Test
    void equals_shouldReturnFalse_whenGenreTypeIsDifferent() {
        PublicationSampleDTO_US016 dto1 = createPublicationSampleDTO();
        PublicationSampleDTO_US016 dto2 = new PublicationSampleDTO_US016(
                dto1.getTitle(),
                dto1.getAuthorName(),
                dto1.getLanguage(),
                new ArrayList<>(List.of("Non-fiction")),
                dto1.getOwnerName(),
                dto1.getCondition()
        );

        assertNotEquals(dto1, dto2);
    }

    @Test
    void equals_shouldReturnFalse_whenOwnerNameIsDifferent() {
        PublicationSampleDTO_US016 dto1 = createPublicationSampleDTO();
        PublicationSampleDTO_US016 dto2 = new PublicationSampleDTO_US016(
                dto1.getTitle(),
                dto1.getAuthorName(),
                dto1.getLanguage(),
                dto1.getGenreType(),
                "different owner",
                dto1.getCondition()
        );

        assertNotEquals(dto1, dto2);
    }

    @Test
    void equals_shouldReturnFalse_whenConditionIsDifferent() {
        PublicationSampleDTO_US016 dto1 = createPublicationSampleDTO();
        PublicationSampleDTO_US016 dto2 = new PublicationSampleDTO_US016(
                dto1.getTitle(),
                dto1.getAuthorName(),
                dto1.getLanguage(),
                dto1.getGenreType(),
                dto1.getOwnerName(),
                "POOR"
        );

        assertNotEquals(dto1, dto2);
    }




}

