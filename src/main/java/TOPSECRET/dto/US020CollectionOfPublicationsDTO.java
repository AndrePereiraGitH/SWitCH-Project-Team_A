package TOPSECRET.dto;

/**
 * Data Transfer Object representing a public collection of publications.
 * <p>
 * This DTO contains only the data required for the US020 use case:
 * the collection name and the corresponding owner's username.
 * </p>
 *
 * <p>
 * It is immutable and implemented as a Java record.
 * </p>
 *
 * @param collectionName the name of the collection (trimmed, never null)
 * @param userName the username of the collection owner (trimmed, never null)
 */
public record US020CollectionOfPublicationsDTO(
        String collectionName,
        String userName
) {
}
