package TOPSECRET.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class US016PublicationSampleDTOTest {


    @Test
    @DisplayName("Should create DTO successfully when all fields are valid")
    void shouldCreateDTOWhenAllFieldsAreValid() {
        US016PublicationSampleDTO dto = new US016PublicationSampleDTO(
                "validPublicationSampleId",
                "Clean Code",
                List.of("Robert C. Martin"),
                Locale.ENGLISH,
                List.of("Programming", "Software Engineering"),
                "validUserId",
                "GOOD"
        );

        assertNotNull(dto);
        assertEquals("validPublicationSampleId", dto.publicationSampleID());
        assertEquals("Clean Code", dto.title());
        assertEquals(List.of("Robert C. Martin"), dto.authorName());
        assertEquals(Locale.ENGLISH, dto.language());
        assertEquals(List.of("Programming", "Software Engineering"), dto.genreType());
        assertEquals("validUserId", dto.ownerName());
        assertEquals("GOOD", dto.condition());
    }

    @Test
    @DisplayName("Should throw exception when publicationSampleID is null")
    void shouldThrowExceptionWhenPublicationSampleIdIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new US016PublicationSampleDTO(
                        null,
                        "Clean Code",
                        List.of("Robert C. Martin"),
                        Locale.ENGLISH,
                        List.of("Programming"),
                        "validUserId",
                        "GOOD"
                )
        );

        assertEquals("All fields must be not-null!", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when title is null")
    void shouldThrowExceptionWhenTitleIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new US016PublicationSampleDTO(
                        "validPublicationSampleId",
                        null,
                        List.of("Robert C. Martin"),
                        Locale.ENGLISH,
                        List.of("Programming"),
                        "validUserId",
                        "GOOD"
                )
        );

        assertEquals("All fields must be not-null!", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when authorName is null")
    void shouldThrowExceptionWhenAuthorNameIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new US016PublicationSampleDTO(
                        "validPublicationSampleId",
                        "Clean Code",
                        null,
                        Locale.ENGLISH,
                        List.of("Programming"),
                        "validUserId",
                        "GOOD"
                )
        );

        assertEquals("All fields must be not-null!", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when language is null")
    void shouldThrowExceptionWhenLanguageIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new US016PublicationSampleDTO(
                        "validPublicationSampleId",
                        "Clean Code",
                        List.of("Robert C. Martin"),
                        null,
                        List.of("Programming"),
                        "validUserId",
                        "GOOD"
                )
        );

        assertEquals("All fields must be not-null!", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when genreType is null")
    void shouldThrowExceptionWhenGenreTypeIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new US016PublicationSampleDTO(
                        "validPublicationSampleId",
                        "Clean Code",
                        List.of("Robert C. Martin"),
                        Locale.ENGLISH,
                        null,
                        "validUserId",
                        "GOOD"
                )
        );

        assertEquals("All fields must be not-null!", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when ownerName is null")
    void shouldThrowExceptionWhenOwnerNameIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new US016PublicationSampleDTO(
                        "validPublicationSampleId",
                        "Clean Code",
                        List.of("Robert C. Martin"),
                        Locale.ENGLISH,
                        List.of("Programming"),
                        null,
                        "GOOD"
                )
        );

        assertEquals("All fields must be not-null!", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when condition is null")
    void shouldThrowExceptionWhenConditionIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new US016PublicationSampleDTO(
                        "validPublicationSampleId",
                        "Clean Code",
                        List.of("Robert C. Martin"),
                        Locale.ENGLISH,
                        List.of("Programming"),
                        "validUserId",
                        null
                )
        );

        assertEquals("All fields must be not-null!", ex.getMessage());
    }

}