package TOPSECRET.dto;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;

import java.util.List;

/**
 * Mapper for US019 - converts {@link CollectionOfPublications} domain objects
 * into {@link US019CollectionOfPublicationsDTO} instances.
 *
 * <p>All methods are static — this class has no state and should never be instantiated.</p>
 */
public class US019CollectionOfPublicationsMapper {

    private US019CollectionOfPublicationsMapper() {
    }

    /**
     * Converts a single {@link CollectionOfPublications} into a DTO.
     *
     * @param collection the domain object to convert; must not be null
     * @return a new {@link US019CollectionOfPublicationsDTO}
     * @throws IllegalArgumentException if {@code collection} is null
     */
    public static US019CollectionOfPublicationsDTO toDTO(CollectionOfPublications collection) {

        if (collection == null) {
            throw new IllegalArgumentException("Collection cannot be null");
        }

        return new US019CollectionOfPublicationsDTO(
                collection.id(),
                collection.getCollectionName(),
                collection.getCollectionGenre().getID(),
                collection.getOwnerId().getUsername()
        );
    }

    /**
     * Converts a list of {@link CollectionOfPublications} into a list of DTOs.
     *
     * @param collections the list to convert; must not be null
     * @return a new list of {@link US019CollectionOfPublicationsDTO}
     * @throws IllegalArgumentException if {@code collections} is null
     */
    public static List<US019CollectionOfPublicationsDTO> toDTOs(
            List<CollectionOfPublications> collections) {

        if (collections == null) {
            throw new IllegalArgumentException("Collections list cannot be null");
        }

        return collections.stream()
                .map(US019CollectionOfPublicationsMapper::toDTO)
                .toList();
    }
}