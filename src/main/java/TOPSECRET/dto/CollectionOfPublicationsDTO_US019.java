package TOPSECRET.dto;

import TOPSECRET.domain.CollectionOfPublications;
import TOPSECRET.domain.User;

public class CollectionOfPublicationsDTO_US019 {

    private String _name;
    private String _genreName;
    private User ownerName;

    public CollectionOfPublicationsDTO_US019(String name, String genreName, User ownerName) {
        this._name = name;
        this._genreName = genreName;
        this.ownerName = ownerName;
    }

    public static CollectionOfPublicationsDTO_US019 toDTO(CollectionOfPublications collection) {

       CollectionOfPublicationsDTO_US019 collection1DTO = new CollectionOfPublicationsDTO_US019(
               collection.getCollectionName(),
               collection.getCollectionGenre().getGenreType(),
               collection.getOwner()
       );

        return collection1DTO;
    }

    public String get_name() {
        return _name;
    }

    public String get_genreName() {
        return _genreName;
    }

    public User getOwnerName() {
        return ownerName;
    }
}
