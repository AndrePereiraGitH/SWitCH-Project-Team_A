//package TOPSECRET.repository;
//
//import TOPSECRET.domain.Enum.MediaFormat;
//import TOPSECRET.domain.Enum.TypeOfBinding;
//import TOPSECRET.domain.PublicationType.PublicationTypeFactory;
//import TOPSECRET.domain.PublicationType.PublicationType;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Repository class responsible for managing {@link PublicationType} instances.
// * <p>
// * This repository provides functionality to:
// * <ul>
// *     <li>Create new {@link PublicationType} objects using a factory</li>
// *     <li>Validate publication types before insertion</li>
// *     <li>Store unique publication types in memory</li>
// *     <li>Retrieve an immutable list of all stored publication types</li>
// * </ul>
// *
// * The repository prevents null and duplicate {@link PublicationType} objects
// * from being added to the internal collection.
// */
//
//public class PublicationTypeRepository {
//
//    /**
//     * Internal list that stores all created publication types.
//     */
//    private final List<PublicationType> _publicationTypes = new ArrayList<>();
//    PublicationTypeFactory _factory;
//
//    /**
//     * Constructs an empty {@code PublicationTypeRepository}.
//     */
//    public PublicationTypeRepository(PublicationTypeFactory factory) {
//        this._factory = factory;
//    }
//
//    /**
//     * Creates a new {@link PublicationType} using {@link PublicationTypeFactory}.
//     *
//     * @param mediaFormat   the media format of the publication (e.g., book, magazine)
//     * @param typeOfBinding the type of binding (e.g., hardback, paperback)
//     * @param details       additional details describing the publication type
//     * @return a newly created {@link PublicationType} instance
//     */
//    public PublicationType create(MediaFormat mediaFormat, TypeOfBinding typeOfBinding, String details)
//    {
//        PublicationType p1 = _factory.create(mediaFormat, typeOfBinding, details);
//        return p1;
//    }
//
//    /**
//     * Validates a {@link PublicationType} instance.
//     * <p>
//     * A publication type is considered valid if:
//     * <ul>
//     *     <li>It is not {@code null}</li>
//     *     <li>It does not already exist in the repository</li>
//     * </ul>
//     *
//     * @param pubType the publication type to validate
//     * @return {@code true} if the publication type is valid and can be added;
//     *         {@code false} otherwise
//     */
//    private boolean validatePublicationType(PublicationType pubType)
//    {
//        if (pubType == null) {
//            return false;
//        }
//        return !_publicationTypes.contains(pubType); // Returns true if the publication type does not already exist in the list and false if it does
//    }
//
//
//    /**
//     * Adds a {@link PublicationType} to the repository if it passes validation.
//     *
//     * @param pubType the publication type to add
//     * @return the added {@link PublicationType}
//     * @throws IllegalArgumentException if the publication type is {@code null}
//     *                                  or already exists in the repository
//     */
//    public PublicationType addToList(PublicationType pubType)
//    {
//        if (!validatePublicationType(pubType)) { // if validation returns false
//            throw new IllegalArgumentException("Publication type already exists or is null");
//        }
//
//        _publicationTypes.add(pubType); // if it returns true, adds publication type to the list
//        return pubType;
//    }
//
//
//    /**
//     * Returns an immutable copy of all stored {@link PublicationType} instances.
//     *
//     * @return an unmodifiable list containing all publication types
//     */
//    public List<PublicationType> getAllPublicationTypes() { return List.copyOf(_publicationTypes); }
//
//    // ------------------------ US004 starts ------------------------
//
//    /**
//     * US004
//     * Finds an existing PublicationType by its attributes.
//     *
//     * @param mediaFormat   Media format of the publication.
//     * @param typeOfBinding Type of binding.
//     * @param details       Additional details describing the publication type.
//     * @return Existing PublicationType domain object.
//     * @throws IllegalArgumentException if attributes are invalid or not found.
//     */
//    public PublicationType findByAttributes(MediaFormat mediaFormat,
//                                                   TypeOfBinding typeOfBinding,
//                                                   String details) {
//
//        // Validate input parameters
//        if (mediaFormat == null) {
//            throw new IllegalArgumentException("MediaFormat cannot be null.");
//        }
//
//        if (typeOfBinding == null) {
//            throw new IllegalArgumentException("TypeOfBinding cannot be null.");
//        }
//
//        if (details == null || details.isBlank()) {
//            throw new IllegalArgumentException("Details cannot be null or blank.");
//        }
//
//
//        for (PublicationType pt : _publicationTypes) {
//            if (pt.getMediaFormat() == mediaFormat &&
//                    pt.getTypeOfBinding() == typeOfBinding &&
//                    pt.getDetails().equalsIgnoreCase(details)) {
//                return pt;
//            }
//        }
//
//        throw new IllegalArgumentException("PublicationType not found.");
//    }
//
//    // ------------------------ ends ------------------------
//
//}
