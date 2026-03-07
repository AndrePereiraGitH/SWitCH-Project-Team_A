package TOPSECRET.domain;

/**
 * Factory responsible for creating {@link CollectionOfPublications} instances.
 * <p>
 * This class centralizes the creation logic of {@code CollectionOfPublications}
 * objects, ensuring that all instances are created in a consistent and
 * controlled manner.
 * <p>
 * The factory delegates validation of mandatory fields to the
 * {@code CollectionOfPublications} constructor.
 */
public class CollectionOfPublicationsFactory {

    /**
     * Creates a new private {@link CollectionOfPublications} instance.
     * <p>
     * Collections are always created as private by default, in accordance
     * with US017. The collection can later be made public via US019.
     *
     * @param name  the name of the collection; must not be {@code null} or blank
     * @param genre the genre associated with the collection; must not be {@code null}
     * @param user the user who owns the collection; must not be {@code null}
     * @return a newly created private {@code CollectionOfPublications}
     * @throws IllegalArgumentException if any of the provided arguments are {@code null}
     *                                  or if {@code name} is blank
     */
    public CollectionOfPublications create(String name, Genre genre, User user) {
        return new CollectionOfPublications(name, genre, user);
    }
}
