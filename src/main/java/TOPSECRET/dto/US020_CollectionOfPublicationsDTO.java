package TOPSECRET.dto;

/**
 * Data Transfer Object (DTO) representing a public collection of publications.
 * <p>
 * This DTO is used to expose only the necessary information
 * about a collection to external layers (e.g., UI or service layer),
 * without revealing internal domain details.
 * <p>
 * It contains:
 * <ul>
 *     <li>The name of the collection</li>
 *     <li>The username of the collection owner</li>
 * </ul>
 *
 * This class is immutable.
 */

public class US020_CollectionOfPublicationsDTO {

    private final String _collectionName;
    private final String _ownerName;

    /**
     * Constructs a {@code CollectionOfPublicationsDTO}.
     *
     * @param collectionName       the name of the publication list (must not be null or blank)
     * @param ownerName  the username of the collection owner (must not be null or blank)
     * @throws IllegalArgumentException if {@code listName} or {@code ownerUsername}
     *                                  is null or blank
     */
    public US020_CollectionOfPublicationsDTO(String collectionName, String ownerName)
    {
        if (collectionName == null || collectionName.isBlank()) {
            throw new IllegalArgumentException("List name can't be null/blank.");
        }
        if (ownerName == null || ownerName.isBlank()) {
            throw new IllegalArgumentException("Owner username can't be null/blank.");
        }
        this._collectionName = collectionName.trim();
        this._ownerName = ownerName.trim();
    }

    public String getListName() { return _collectionName; }

    public String getOwnerUsername() { return _ownerName; }

}
