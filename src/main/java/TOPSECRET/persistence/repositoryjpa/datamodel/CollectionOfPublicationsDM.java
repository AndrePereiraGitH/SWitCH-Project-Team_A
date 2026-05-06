package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * JPA data model for {@code CollectionOfPublications}.
 *
 * <p>This class represents the persistence-layer mapping of a collection of
 * publications and is used to store and retrieve collection data from the
 * database.</p>
 *
 * <p>It acts as a persistence representation separate from the domain model,
 * following the anti-corruption layer approach. Conversion between this class
 * and the domain entity is handled by the corresponding assembler.</p>
 *
 * <p>The class is annotated as a JPA {@link Entity} and uses
 * {@link CollectionOfPublicationsId} as its embedded primary key.</p>
 *
 * <p>A protected no-args constructor is provided for JPA use only.</p>
 */
@Entity
@Table(name = "collectionsOfPublications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CollectionOfPublicationsDM {

    /**
     * Unique identifier of the collection.
     */
    @EmbeddedId
    @AttributeOverride(name = "id", column = @Column(name = "collection_id"))
    private CollectionOfPublicationsId id;

    /**
     * Name of the collection.
     */
    @AttributeOverride(name = "collectionName", column = @Column(name = "collection_name",
            nullable = false))
    private String collectionName;

    /**
     * Genre associated with the collection.
     */
    @Embedded
    @AttributeOverride(name = "genreID", column = @Column(name = "collection_genre_id"))
    private GenreId genreId;

    /**
     * Identifier of the user who owns the collection.
     */
    @Embedded
    @AttributeOverride(name = "userId", column = @Column(name = "owner_id"))
    private UserId userId;

    /**
     * Indicates whether the collection is private.
     *
     * <p>{@code true} means the collection is private and visible only
     * to the owner; {@code false} means it is public.</p>
     */
    @AttributeOverride(name = "isPrivate", column = @Column(name = "is_private"))
    private boolean isPrivate;

    /**
     * Creates a new persistence model for a collection of publications.
     *
     * @param collectionOfPub_id unique identifier of the collection;
     *                           must not be null
     * @param collectionName     name of the collection; must not be null
     * @param genreID            genre associated with the collection;
     *                           must not be null
     * @param userId             identifier of the owner; must not be null
     * @param isPrivate          privacy flag indicating whether the collection
     *                           is private or public
     * @throws NullPointerException if any mandatory argument is null
     */
    public CollectionOfPublicationsDM(CollectionOfPublicationsId collectionOfPub_id,
                                      String collectionName, GenreId genreID,
                                      UserId userId, boolean isPrivate) {

        this.id = Objects.requireNonNull(collectionOfPub_id,
                "collectionOfPublicationsId cannot be null");

        this.collectionName = Objects.requireNonNull(collectionName,
                "collectionName cannot be null");

        this.genreId = Objects.requireNonNull(genreID,
                "genreID cannot be null");

        this.userId = Objects.requireNonNull(userId,
                "userId cannot be null");

        this.isPrivate = isPrivate;
    }
}