package TOPSECRET.dto;

/**
 * Utility class responsible for mapping raw collection and user data
 * into {@link US020CollectionOfPublicationsDTO} instances.
 * <p>
 * This mapper applies basic data normalization rules such as trimming
 * leading and trailing whitespace from input values.
 * </p>
 *
 * <p>
 * All methods are static and the class is not meant to be instantiated.
 * </p>
 */
public class US020CollectionOfPublicationsDTOMapper {

    private US020CollectionOfPublicationsDTOMapper() {
        // Prevents instantiation
    }

    /**
     * Converts a collection name and an owner username into a DTO.
     * <p>
     * Both input parameters are validated to ensure they are not null.
     * Leading and trailing whitespace is removed from both values.
     * </p>
     *
     * @param collectionName the name of the collection to be mapped
     * @param ownerName the username of the collection owner
     * @return a new {@link US020CollectionOfPublicationsDTO} containing the normalized data
     * @throws IllegalArgumentException if either {@code collection} or {@code ownerName} is null
     */
    public static US020CollectionOfPublicationsDTO toDTO(String collectionName, String ownerName) {

        if (collectionName == null || collectionName.isBlank()) {
            throw new IllegalArgumentException("Collection name cannot be null");
        }

        if (ownerName == null || ownerName.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null");
        }

        return new US020CollectionOfPublicationsDTO(collectionName.trim(), ownerName.trim());
    }
}
