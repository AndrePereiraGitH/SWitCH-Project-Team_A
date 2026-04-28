package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EditionDMTest {

    private Code codeDouble = mock(Code.class);
    private PhysicalAttributes physicalAttributesDouble = mock(PhysicalAttributes.class);
    private EditionID editionIDDouble = mock(EditionID.class);
    private PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);
    private PublisherID publisherIDDouble = mock(PublisherID.class);
    private TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
    private Locale languageDouble = mock(Locale.class);
    private LocalDate dateDouble = mock(LocalDate.class);



    private EditionDM createValidDM() {
        return new EditionDM(
                editionIDDouble,
                "1st",
                codeDouble,
                physicalAttributesDouble,
                publicationTypeIdDouble,
                publisherIDDouble,
                "Book",
                languageDouble,
                dateDouble,
                typeOfBindingDouble
        );
    }

    // ─────────────────────────────────────────────
    // Constructor validation
    // ─────────────────────────────────────────────

    @Test
    void shouldThrow_whenEditionIDIsNull() {

        //Act & Assert
        assertThrows(NullPointerException.class, () ->
                new EditionDM(
                        null, "1st", codeDouble,
                        physicalAttributesDouble, publicationTypeIdDouble, publisherIDDouble,
                        "Book", languageDouble, dateDouble, typeOfBindingDouble
                ));
    }

    @Test
    void shouldThrow_whenEditionNumberIsNull() {

        //Act & Assert
        assertThrows(NullPointerException.class, () ->
                new EditionDM(
                        editionIDDouble, null, codeDouble,
                        physicalAttributesDouble, publicationTypeIdDouble, publisherIDDouble,
                        "Book", languageDouble, dateDouble, typeOfBindingDouble
                ));
    }

    @Test
    void shouldThrow_whenEditionNumberIsBlank() {

        //Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new EditionDM(
                        editionIDDouble, "   ", codeDouble,
                        physicalAttributesDouble, publicationTypeIdDouble, publisherIDDouble,
                        "Book", languageDouble, dateDouble, typeOfBindingDouble
                ));
    }

    @Test
    void shouldThrow_whenPhysicalAttributesIsNull() {

        //Act & Assert
        assertThrows(NullPointerException.class, () ->
                new EditionDM(
                        editionIDDouble, "1st", codeDouble,
                        null, publicationTypeIdDouble, publisherIDDouble,
                        "Book", languageDouble, dateDouble, typeOfBindingDouble
                ));
    }

    @Test
    void shouldThrow_whenPublicationTypeIdIsNull() {
        //Act & Assert
        assertThrows(NullPointerException.class, () ->
                new EditionDM(
                        editionIDDouble, "1st", codeDouble,
                        physicalAttributesDouble, null, publisherIDDouble,
                        "Book", languageDouble, dateDouble, typeOfBindingDouble
                ));
    }

    @Test
    void shouldThrow_whenPublisherIdIsNull() {

        //Act & Assert
        assertThrows(NullPointerException.class, () ->
                new EditionDM(
                        editionIDDouble, "1st", codeDouble,
                        physicalAttributesDouble, publicationTypeIdDouble, null,
                        "Book", languageDouble, dateDouble, typeOfBindingDouble
                ));
    }

    @Test
    void shouldThrow_whenMediaFormatIsNull() {

        //Act & Assert
        assertThrows(NullPointerException.class, () ->
                new EditionDM(
                        editionIDDouble, "1st", codeDouble,
                        physicalAttributesDouble, publicationTypeIdDouble, publisherIDDouble,
                        null, languageDouble, dateDouble, typeOfBindingDouble
                ));
    }

    @Test
    void shouldThrow_whenLanguageIsNull() {

        //Act & Assert
        assertThrows(NullPointerException.class, () ->
                new EditionDM(
                        editionIDDouble, "1st", codeDouble,
                        physicalAttributesDouble, publicationTypeIdDouble, publisherIDDouble,
                        "Book", null, dateDouble, typeOfBindingDouble
                ));
    }

    @Test
    void shouldThrow_whenEditionDateIsNull() {

        //Act & Assert
        assertThrows(NullPointerException.class, () ->
                new EditionDM(
                        editionIDDouble, "1st", codeDouble, physicalAttributesDouble, publicationTypeIdDouble, publisherIDDouble,
                        "Book", languageDouble, null, typeOfBindingDouble
                ));
    }

    @Test
    void shouldThrow_whenTypeOfBindingIsNull() {


        //Act & Assert
        assertThrows(NullPointerException.class, () ->
                new EditionDM(
                        editionIDDouble, "1st", codeDouble,
                        physicalAttributesDouble, publicationTypeIdDouble, publisherIDDouble,
                        "Book", languageDouble, dateDouble, null
                ));
    }

    // ─────────────────────────────────────────────
    // Getters
    // ─────────────────────────────────────────────

    @Test
    void shouldReturnAllFieldsCorrectly() {

        // Act
        EditionDM dm = createValidDM();

        // Assert
        assertEquals(editionIDDouble, dm.getEditionID());
        assertEquals("1st", dm.getEditionNumber());
        assertEquals(codeDouble, dm.getCode());
        assertEquals(physicalAttributesDouble, dm.getPhysicalAttributes());
        assertEquals(publicationTypeIdDouble, dm.getPublicationTypeId());
        assertEquals(publisherIDDouble, dm.getPublisherId());
        assertEquals("Book", dm.getMediaFormat());
        assertEquals(languageDouble, dm.getEditionLanguage());
        assertEquals(dateDouble, dm.getEditionDate());
        assertEquals(typeOfBindingDouble, dm.getTypeOfBinding());
    }

    // ─────────────────────────────────────────────
    // Default constructor
    // ─────────────────────────────────────────────

    @Test
    void defaultConstructor_shouldCreateObject() {
        //Act
        EditionDM dm = new EditionDM();

        //Assert
        assertNotNull(dm);
    }
}