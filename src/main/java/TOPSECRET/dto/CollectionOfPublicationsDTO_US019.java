package TOPSECRET.dto;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;


/**
 * Data Transfer Object for US019 - Publicly Share a Collection of Publications.
 * <p>
 * Carries the display data of a {@link CollectionOfPublications}
 * across application layers, containing only primitive data to avoid exposing domain objects.
 * </p>
 */
public class CollectionOfPublicationsDTO_US019 {

    private String _name;
    private String _genreName;
    private String _ownerName;


    /**
     * Creates a new DTO with the given collection data.
     *
     * @param name      the name of the collection.
     * @param genreName the genre of the collection.
     * @param ownerName the username of the collection's owner.
     */
    public CollectionOfPublicationsDTO_US019(String name, String genreName, String ownerName) {
        this._name = name;
        this._genreName = genreName;
        this._ownerName = ownerName;
    }

    /**
     * Converts a {@link CollectionOfPublications} domain object into a DTO.
     *
//     * @param collection the domain object to convert.
     * @return a new {@link CollectionOfPublicationsDTO_US019} with the collection's data.
     */
//    public static CollectionOfPublicationsDTO_US019 toDTO(CollectionOfPublications collection) {
//
//       CollectionOfPublicationsDTO_US019 collection1DTO = new CollectionOfPublicationsDTO_US019(
//               collection.getCollectionName(),
//               collection.getCollectionGenre().getGenreType(),
//               collection.getOwner().getUserName()
//       );
//
//        return collection1DTO;
//    }

    public String get_name() {
        return _name;
    }

    public String get_genreName() {
        return _genreName;
    }

    public String getOwnerName() {
        return _ownerName;
    }
}
