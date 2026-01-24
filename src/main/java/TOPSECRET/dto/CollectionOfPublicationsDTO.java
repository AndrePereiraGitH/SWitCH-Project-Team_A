package TOPSECRET.dto;

public class CollectionOfPublicationsDTO {

    private final String listName;
    private final String ownerUsername;

    public CollectionOfPublicationsDTO(String listName, String ownerUsername) {
        if (listName == null || listName.isBlank()) {
            throw new IllegalArgumentException("List name can't be null/blank.");
        }
        if (ownerUsername == null || ownerUsername.isBlank()) {
            throw new IllegalArgumentException("Owner username can't be null/blank.");
        }
        this.listName = listName.trim();
        this.ownerUsername = ownerUsername.trim();
    }

    public String getListName() {
        return listName;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

}
