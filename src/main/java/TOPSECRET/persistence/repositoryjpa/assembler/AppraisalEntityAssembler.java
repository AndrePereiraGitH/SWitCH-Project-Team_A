package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntityFactory;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.persistence.repositoryjpa.datamodel.AppraisalEntityDM;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Assembler responsible for converting between
 * AppraisalEntity (domain) and AppraisalEntityDM (data model).
 *
 * <p>
 * This class isolates persistence concerns from the domain layer,
 * ensuring that transformations are handled in a single place.
 * </p>
 */
@Service
public class AppraisalEntityAssembler {

    private final AppraisalEntityFactory factory;

    /**
     * Constructs the assembler with the required factory.
     *
     * @param factory the factory used to rehydrate domain objects
     */
    public AppraisalEntityAssembler(AppraisalEntityFactory factory) {

        if (factory == null) {
            throw new IllegalArgumentException("Factory cannot be null");
        }

        this.factory = factory;
    }

    /**
     * Converts a Data Model into a Domain object.
     *
     * @param dm the data model instance
     * @return a reconstructed AppraisalEntity domain object
     * @throws IllegalArgumentException if dm is null
     */
    public AppraisalEntity toDomain(AppraisalEntityDM dm) {

        if (dm == null) {
            throw new IllegalArgumentException("Appraisal Entity cannot be null");
        }

        if (dm.getPublicationTypeIds() == null || dm.getGenreIds() == null) {
            throw new IllegalArgumentException("Lists cannot be null");
        }

        List<PublicationTypeId> publicationTypes =
                new ArrayList<>(dm.getPublicationTypeIds());

        List<GenreId> genres =
                new ArrayList<>(dm.getGenreIds());

        return factory.create(
                dm.getId(),
                dm.getName(),
                dm.getContact(),
                publicationTypes,
                genres
        );
    }

    /**
     * Converts a Domain object into a Data Model.
     *
     * @param domain the domain object
     * @return a data model ready for persistence
     * @throws IllegalArgumentException if domain is null
     */
    public AppraisalEntityDM toDM(AppraisalEntity domain) {

        if (domain == null) {
            throw new IllegalArgumentException("AppraisalEntity cannot be null");
        }

        List<PublicationTypeId> publicationTypes =
                domain.getPublicationTypeIds() != null
                        ? new ArrayList<>(domain.getPublicationTypeIds())
                        : new ArrayList<>();

        List<GenreId> genres =
                domain.getGenreIds() != null
                        ? new ArrayList<>(domain.getGenreIds())
                        : new ArrayList<>();

        return new AppraisalEntityDM(
                domain.id(),
                domain.getName(),
                domain.getContact(),
                publicationTypes,
                genres
        );
    }

}