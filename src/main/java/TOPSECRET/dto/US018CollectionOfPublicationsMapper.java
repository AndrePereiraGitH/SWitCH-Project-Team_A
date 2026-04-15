package TOPSECRET.dto;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;

import java.util.ArrayList;
import java.util.List;


/**
 * Mapper responsible for converting CollectionOfPublications
 * domain objects into DTOs.
 *
 * <p>
 * This mapper performs only data transformation and does not
 * access any external dependencies.
 * </p>
 */
public class US018CollectionOfPublicationsMapper {

    /**
     * Converts a list of CollectionOfPublications domain objects into DTOs.
     *
     * @param collections list of domain objects
     * @return list of DTOs; returns empty list if input is null
     */
    public static List<US018CollectionOfPublicationsDTO> toDTOs(
            List<CollectionOfPublications> collections) {

        List<US018CollectionOfPublicationsDTO> result = new ArrayList<>();

        if (collections == null) {
            return result;
        }

        for (CollectionOfPublications collection : collections) {
            result.add(toDTO(collection));
        }

        return result;
    }

    /**
     * Converts a single CollectionOfPublications domain object into a DTO.
     *
     * @param collection domain object
     * @return DTO representation
     *
     * @throws IllegalArgumentException if collection is null
     */
    public static US018CollectionOfPublicationsDTO toDTO(
            CollectionOfPublications collection) {

        if (collection == null) {
            throw new IllegalArgumentException("Collection cannot be null");
        }

        return new US018CollectionOfPublicationsDTO(
                collection.id(),
                collection.getCollectionName()
        );
    }
}