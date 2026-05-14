package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.datamodel.CollectionOfPublicationsDM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CollectionOfPublicationsAssembler}.
 *
 * <p>Covers both conversion directions:</p>
 * <ul>
 *     <li><b>toDM</b> — verifies that a {@link CollectionOfPublications}
 *     domain object is correctly mapped to a
 *     {@link CollectionOfPublicationsDM} persistence entity.</li>
 *
 *     <li><b>toDomain</b> — verifies that a
 *     {@link CollectionOfPublicationsDM} persistence entity is correctly
 *     reconstructed into a {@link CollectionOfPublications} domain object.</li>
 * </ul>
 *
 * <p>Dependencies are isolated using Mockito and the factory is mocked
 * to avoid coupling assembler tests to domain construction logic.</p>
 */
class CollectionOfPublicationsAssemblerTest {

    private CollectionOfPublicationsFactory factoryDouble;
    private CollectionOfPublicationsAssembler assembler;

    private CollectionOfPublicationsId collectionIdDouble;
    private GenreId genreIdDouble;
    private UserId userIdDouble;

    @BeforeEach
    void setup() {
        factoryDouble = mock(CollectionOfPublicationsFactory.class);
        assembler = new CollectionOfPublicationsAssembler(factoryDouble);

        collectionIdDouble = mock(CollectionOfPublicationsId.class);
        genreIdDouble = mock(GenreId.class);
        userIdDouble = mock(UserId.class);
    }

    @Test
    void toDMShouldReturnAValidDataModel() {
        // Arrange
        CollectionOfPublications collectionDouble =
                mock(CollectionOfPublications.class);

        String collectionName = "Fantasy Collection";
        boolean isPrivate = false;

        when(collectionDouble.id()).thenReturn(collectionIdDouble);
        when(collectionDouble.getCollectionName()).thenReturn(collectionName);
        when(collectionDouble.getCollectionGenre()).thenReturn(genreIdDouble);
        when(collectionDouble.getOwnerId()).thenReturn(userIdDouble);
        when(collectionDouble.getIsPrivate()).thenReturn(isPrivate);

        // Act
        CollectionOfPublicationsDM result =
                assembler.toDM(collectionDouble);

        // Assert
        assertNotNull(result);
    }

    @Test
    void toDMShouldThrowIfCollectionNull() {
        assertThrows(IllegalArgumentException.class,
                () -> assembler.toDM(null));
    }

    @Test
    void toDomainShouldReturnAValidDomainObject() {
        // Arrange
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);
        String collectionName = "Fantasy Collection";
        boolean isPrivate = false;

        CollectionOfPublicationsDM collectionDM =
                new CollectionOfPublicationsDM(
                        collectionIdDouble,
                        collectionName,
                        genreIdDouble,
                        userIdDouble,
                        isPrivate,
                        publicUntil
                );

        CollectionOfPublications collectionDouble =
                mock(CollectionOfPublications.class);

        when(factoryDouble.create(
                collectionIdDouble,
                collectionName,
                genreIdDouble,
                userIdDouble,
                isPrivate,
                publicUntil
        )).thenReturn(collectionDouble);

        // Act
        CollectionOfPublications result =
                assembler.toDomain(collectionDM);

        // Assert
        assertNotNull(result);
        assertSame(collectionDouble, result);
    }

    @Test
    void toDomainShouldThrowIfCollectionDMIsNull() {
        assertThrows(NullPointerException.class,
                () -> assembler.toDomain(null));
    }

    // =========================================================================
    // toDomainList()
    // =========================================================================

    @Test
    void toDomainListShouldReturnAValidList() {

        // Arrange
        String collectionName = "Fantasy Collection";
        boolean isPrivate = false;
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);

        CollectionOfPublicationsDM collectionDM =
                new CollectionOfPublicationsDM(
                        collectionIdDouble,
                        collectionName,
                        genreIdDouble,
                        userIdDouble,
                        isPrivate,
                        publicUntil
                );

        List<CollectionOfPublicationsDM> dmList =
                List.of(collectionDM);

        CollectionOfPublications collectionDouble =
                mock(CollectionOfPublications.class);

        when(factoryDouble.create(
                collectionIdDouble,
                collectionName,
                genreIdDouble,
                userIdDouble,
                isPrivate,
                publicUntil
        )).thenReturn(collectionDouble);

        // Act
        List<CollectionOfPublications> result =
                assembler.toDomainList(dmList);

        // Assert
        assertNotNull(result);
    }

    @Test
    void toDomainListShouldReturnCorrectSize() {

        // Arrange
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);
        String collectionName = "Fantasy Collection";
        boolean isPrivate = false;

        CollectionOfPublicationsDM collectionDM =
                new CollectionOfPublicationsDM(
                        collectionIdDouble,
                        collectionName,
                        genreIdDouble,
                        userIdDouble,
                        isPrivate,
                        publicUntil
                );

        List<CollectionOfPublicationsDM> dmList =
                List.of(collectionDM);

        CollectionOfPublications collectionDouble =
                mock(CollectionOfPublications.class);

        when(factoryDouble.create(
                collectionIdDouble,
                collectionName,
                genreIdDouble,
                userIdDouble,
                isPrivate,
                publicUntil
        )).thenReturn(collectionDouble);

        // Act
        List<CollectionOfPublications> result =
                assembler.toDomainList(dmList);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void toDomainListShouldReturnEmptyListWhenInputIsEmpty() {

        // Arrange
        List<CollectionOfPublicationsDM> dmList =
                List.of();

        // Act
        List<CollectionOfPublications> result =
                assembler.toDomainList(dmList);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void toDomainListShouldThrowIfDMListIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> assembler.toDomainList(null));
    }

}