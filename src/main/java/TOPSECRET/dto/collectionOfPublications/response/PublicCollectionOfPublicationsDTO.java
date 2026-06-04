package TOPSECRET.dto.collectionOfPublications.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

/**
 * Data Transfer Object representing a publicly visible collection of publications.
 *
 * <p>This DTO is intended for collection discovery and browsing use cases,
 * exposing only the information relevant to users viewing public collections.
 *
 * <p>Extends {@link RepresentationModel} to support HATEOAS links.</p>
 *
 * @see RepresentationModel
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class PublicCollectionOfPublicationsDTO extends
        RepresentationModel<PublicCollectionOfPublicationsDTO> {

    /**
     * The unique identifier of the collection.
     */
    private final String collectionId;

    /**
     * The name of the collection.
     */
    private final String collectionName;

    /**
     * The name of the collection owner.
     */
    private final String ownerName;

    /**
     * The date and time until which the collection remains public.
     *
     * <p>A {@code null} value indicates that the collection is permanently public.</p>
     */
    private final LocalDate publicUntil;
}