package TOPSECRET.services;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.PublicationSampleFactory;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Application service responsible for orchestrating the creation and persistence
 * of {@link PublicationSample} aggregates.
 *
 * <p>Acts as the intermediary between the controller layer and the domain/persistence layers,
 * delegating creation to {@link PublicationSampleFactory} and persistence to
 * {@link IPublicationSampleRepository}.</p>
 *
 * <p>This service does not enforce domain invariants directly — those are the
 * responsibility of the factory and the domain object. It is responsible for
 * null-guarding its own inputs and coordinating the creation and save flow.</p>
 */
@Service
public class PublicationSampleService {

    private final IPublicationSampleRepository publicationSampleRepository;
    private final PublicationSampleFactory factory;

    public PublicationSampleService(IPublicationSampleRepository publicationSampleRepository, PublicationSampleFactory factory) {
        this.publicationSampleRepository = publicationSampleRepository;
        this.factory = factory;
    }

    /**
     * Creates a new {@link PublicationSample} and persists it to the repository.
     *
     * <p>Delegates creation to {@link PublicationSampleFactory#create} and
     * persistence to {@link IPublicationSampleRepository#save}.
     * All parameters are mandatory — passing {@code null} for any argument
     * will throw a {@link NullPointerException}.</p>
     *
     * @param bibliographicalDataId the bibliographical data identifier; must not be {@code null}
     * @param userId                the identifier of the owning user; must not be {@code null}
     * @param publicationStatus     the initial publication status; must not be {@code null}
     * @param editionId             the edition identifier; must not be {@code null}
     * @param condition             the physical condition of the sample; must not be {@code null}
     * @return the saved {@link PublicationSample} with its persisted identity
     * @throws NullPointerException      if any parameter is {@code null}
     * @throws IllegalArgumentException  if a publication with the same identity already exists
     */
    public PublicationSample createAndSave(BibliographicalDataID bibliographicalDataId, UserId userId,
                                           PublicationStatus publicationStatus, EditionID editionId, Condition condition){

        Objects.requireNonNull(bibliographicalDataId, "BibliographicalDataID cannot be null");
        Objects.requireNonNull(userId, "UserId cannot be null");
        Objects.requireNonNull(publicationStatus, "PublicationStatus cannot be null");
        Objects.requireNonNull(editionId, "EditionID cannot be null");
        Objects.requireNonNull(condition, "Condition cannot be null");

        PublicationSample pbSampleCreated = factory.create(
                bibliographicalDataId,
                userId,
                publicationStatus,
                editionId,
                condition);

        PublicationSample pbSampleSaved = publicationSampleRepository.save(pbSampleCreated);

        return pbSampleSaved;
    }

}
