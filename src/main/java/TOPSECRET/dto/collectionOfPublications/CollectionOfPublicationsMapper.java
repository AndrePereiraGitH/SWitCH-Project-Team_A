package TOPSECRET.dto.collectionOfPublications;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.collectionOfPublications.response.PublicCollectionOfPublicationsDTO;
import TOPSECRET.dto.collectionOfPublications.response.ResponseCollectionOfPublicationsDTO;

import java.util.List;
import java.util.Objects;

/**
 * Utility class for mapping {@link CollectionOfPublications} domain objects
 * to their corresponding Data Transfer Objects.
 *
 * <p>This class is not instantiable.</p>
 */
public final class CollectionOfPublicationsMapper {

    private CollectionOfPublicationsMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Maps a single {@link CollectionOfPublications} domain object to a
     * {@link ResponseCollectionOfPublicationsDTO}.
     *
     * @param collectionOfPublications the domain object to map; must not be {@code null}
     * @return a {@link ResponseCollectionOfPublicationsDTO} representing the given collection
     * @throws NullPointerException if {@code collectionOfPublications} is {@code null}
     */
    public static ResponseCollectionOfPublicationsDTO toDTO(CollectionOfPublications collectionOfPublications) {
        Objects.requireNonNull(collectionOfPublications, "collectionOfPublications cannot be null");

        return new ResponseCollectionOfPublicationsDTO(
                collectionOfPublications.getCollectionName(),
                collectionOfPublications.id().toString()
        );
    }

    /**
     * Maps a list of {@link CollectionOfPublications} domain objects to a list of
     * {@link ResponseCollectionOfPublicationsDTO}.
     *
     * <p>Any {@code null} elements within the list are silently ignored and excluded
     * from the returned list.</p>
     *
     * @param collectionsOfPublications the list of domain objects to map; must not be {@code null}
     * @return an unmodifiable list of {@link ResponseCollectionOfPublicationsDTO}
     * @throws NullPointerException if {@code collectionsOfPublications} is {@code null}
     */
    public static List<ResponseCollectionOfPublicationsDTO>
    toDTOList(List<CollectionOfPublications> collectionsOfPublications) {

        Objects.requireNonNull(collectionsOfPublications, "collectionOfPublications list cannot be null");

        return collectionsOfPublications.stream()
                .filter(Objects::nonNull)
                .map(CollectionOfPublicationsMapper::toDTO)
                .toList();
    }

    /**
     * Maps a {@link CollectionOfPublications} domain object to a
     * {@link PublicCollectionOfPublicationsDTO}.
     *
     * @param collection the collection to map; must not be {@code null}
     * @return a DTO representing the publicly visible information of the collection
     * @throws NullPointerException if {@code collection} is {@code null}
     */
    public static PublicCollectionOfPublicationsDTO toPublicDTO(
            CollectionOfPublications collection,
            String ownerName) {

        Objects.requireNonNull(collection);

        return new PublicCollectionOfPublicationsDTO(
                collection.id().toString(),
                collection.getCollectionName(),
                ownerName,
                collection.getPublicUntil()
        );
    }

    /**
     * Converts a {@link String} identifier into an {@link GenreId} Value Object.
     *
     * @param genreId the string representation of the genre identifier; must not be {@code null}
     * @return the corresponding {@link GenreId}
     */
    public static GenreId toGenreId(String genreId) {

        return new GenreId(genreId);
    }

    /**
     * Converts a {@link String} identifier into an {@link UserId} Value Object.
     *
     * @param userId the string representation of the user identifier; must not be {@code null}
     * @return the corresponding {@link UserId}
     */
    public static UserId toUserId(String userId) {

        return new UserId(userId);
    }

    /**
     * Converts a {@link String} identifier into an {@link CollectionOfPublicationsId} Value Object.
     *
     * @param collectionOfPublicationsId the string representation of the collection identifier; must not be {@code null}
     * @return the corresponding {@link CollectionOfPublicationsId}
     */
    public static CollectionOfPublicationsId toCollectionId(String collectionOfPublicationsId) {

        return new CollectionOfPublicationsId(collectionOfPublicationsId);
    }
}