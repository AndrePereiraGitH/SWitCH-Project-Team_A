package TOPSECRET.dto;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.ValueObjects.UserId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Mapper class responsible for converting {@link CollectionOfPublications}
 * domain objects into {@link US020_CollectionOfPublicationsDTO} objects.
 * <p>
 * This mapper is purely responsible for data transformation and does not perform
 * any data access. All required data (such as the owner's name) must be provided
 * by the caller.
 */
public class US020_CollectionOfPublicationsDTOMapper {


    /**
     * Converts a list of {@link CollectionOfPublications} domain objects into a list of
     * {@link US020_CollectionOfPublicationsDTO} data transfer objects.
     *
     * <p>Each collection is mapped to a DTO containing its name and the corresponding
     * owner's name. The owner's name is retrieved from the provided map using the
     * collection's owner identifier.</p>
     *
     * @param collections the list of {@link CollectionOfPublications} to be converted;
     *                    must not be {@code null}
     * @param ownerNames a map associating {@link UserId} with the corresponding owner's name;
     *                   must not be {@code null} and must contain an entry for every owner
     *                   referenced in {@code collections}
     * @return a list of {@link US020_CollectionOfPublicationsDTO} containing the mapped data
     * @throws IllegalStateException if an owner identifier from {@code collections} is not
     *                               found in the {@code ownerNames} map
     */
    public List<US020_CollectionOfPublicationsDTO> toDTO(
            List<CollectionOfPublications> collections,
            Map<UserId, String> ownerNames) {

        List<US020_CollectionOfPublicationsDTO> result = new ArrayList<>();

        for (CollectionOfPublications c : collections) {
            String ownerName = ownerNames.get(c.getOwnerId());
            if (ownerName == null) {
                throw new IllegalStateException("User not found: " + c.getOwnerId());
            }

            String collectionName = c.getCollectionName();

            result.add(new US020_CollectionOfPublicationsDTO(collectionName, ownerName));
        }

        return result;
    }
}
