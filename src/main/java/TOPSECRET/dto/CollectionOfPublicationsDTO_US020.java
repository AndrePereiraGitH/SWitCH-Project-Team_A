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

public class CollectionOfPublicationsDTO_US020 {

    private final String listName;
    private final String ownerUsername;

    /**
     * Constructs a {@code CollectionOfPublicationsDTO}.
     *
     * @param listName       the name of the publication list (must not be null or blank)
     * @param ownerUsername  the username of the collection owner (must not be null or blank)
     * @throws IllegalArgumentException if {@code listName} or {@code ownerUsername}
     *                                  is null or blank
     */
    public CollectionOfPublicationsDTO_US020(String listName, String ownerUsername)
    {
        if (listName == null || listName.isBlank()) {
            throw new IllegalArgumentException("List name can't be null/blank.");
        }
        if (ownerUsername == null || ownerUsername.isBlank()) {
            throw new IllegalArgumentException("Owner username can't be null/blank.");
        }
        this.listName = listName.trim();
        this.ownerUsername = ownerUsername.trim();
    }

    public String getListName() { return listName; }

    public String getOwnerUsername() { return ownerUsername; }

}
