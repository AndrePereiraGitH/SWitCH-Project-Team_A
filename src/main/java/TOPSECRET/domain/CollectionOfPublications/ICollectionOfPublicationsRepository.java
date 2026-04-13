package TOPSECRET.domain.CollectionOfPublications;

import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;

import java.util.List;

public interface ICollectionOfPublicationsRepository {

    /**
     * Creates a new private collection for a userId, but only if no duplicate exists — US017.
     *
     * <p>US017 rule: a userId cannot have two collections with the same name and the same genreId.
     * This method checks for that duplicate before creating anything.
     *
     * <p>The name comparison uses {@code equalsIgnoreCase} because the name is typed by the userId —
     * "Favorites" and "favorites" should be treated as the same name.
     *
     * <p>Note: we do NOT use {@code CollectionOfPublications.equals()} for the duplicate check
     * because that method also compares {@code isPrivate} and the publications list.
     * Here we only care about owner + name + genreId — that is what defines a duplicate for US017.
     *
     * <p>If no duplicate is found, the factory creates the collection, and it is added to the list.
     * The new collection is always private — that is a US017 rule enforced by the domain constructor.
     *
     * @param userId  the userId who wants to create the collection
     * @param name    the name chosen by the userId
     * @param genreId the genreId chosen by the userId
     * @return the newly created private collection
     * @throws IllegalArgumentException if any parameter is null, or if a duplicate already exists
     */

    CollectionOfPublications createAndAdd(
            UserId userId,
            String name,
            GenreID genreId) throws IllegalArgumentException;

    /**
     * Returns all collections that belong to a given userId — US018 / US019.
     *
     * @param userId the owner of the collections
     * @return list of collections owned by the userId
     * @throws IllegalArgumentException if userId is null
     */

    List<CollectionOfPublications> getUserCollections(UserId userId) throws IllegalArgumentException;
}