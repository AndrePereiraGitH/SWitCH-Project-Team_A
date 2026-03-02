package TOPSECRET.dto;

import TOPSECRET.domain.CollectionOfPublications;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO used exclusively in US018 to represent a user's collection.
 */
public class CollectionOfPublicationsDTO_US018 {

    private final String name;
    private final String genreName;
    private final String ownerName;

    public CollectionOfPublicationsDTO_US018(String name, String genreName, String ownerName) {
        this.name = name;
        this.genreName = genreName;
        this.ownerName = ownerName;
    }

    /**
     * Converts domain collections into DTOs for US018.
     * @param collections list of domain collections
     * @return list of CollectionOfPublicationsDTO_US018
     */
    public static List<CollectionOfPublicationsDTO_US018> collectionOfPublicationToDTO (
            List<CollectionOfPublications> collections) {

        List<CollectionOfPublicationsDTO_US018> result = new ArrayList<>();

        for (CollectionOfPublications cp : collections) {

            result.add(
                    new CollectionOfPublicationsDTO_US018(
                            cp.getCollectionName(),
                            cp.getCollectionGenre().getTitle(),
                            cp.getOwner().getUserName()
                    )
            );
        }

        return result;
    }
    public String getName() {
        return name;
    }
    public String getGenreName() {
        return genreName;
    }
    public String getOwnerName() {
        return ownerName;
    }
}
