package TOPSECRET.services;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.PublicationSampleFactory;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.EditionId;
import TOPSECRET.domain.ValueObjects.UserId;
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

        this.publicationSampleRepository =
                Objects.requireNonNull(publicationSampleRepository, "Repository cannot be null");

        this.factory =
                Objects.requireNonNull(factory, "Factory cannot be null");
    }

    /**
     * Creates a new {@link PublicationSample} and persists it.
     *
     * <p>Delegates object creation to {@link PublicationSampleFactory} and
     * persistence to {@link IPublicationSampleRepository}.</p>
     *
     * <p>No duplicate validation is performed, as multiple publication samples
     * with identical attributes are allowed. Identity uniqueness is ensured
     * by the {@link PublicationSample} itself.</p>
     *
     * @param bibliographicalDataId the bibliographical data identifier; must not be {@code null}
     * @param userId                the identifier of the owning user; must not be {@code null}
     * @param publicationStatus     the initial publication status; must not be {@code null}
     * @param editionId             the edition identifier; must not be {@code null}
     * @param condition             the physical condition of the sample; must not be {@code null}
     * @return the persisted {@link PublicationSample}
     * @throws NullPointerException if any parameter is {@code null}
     */
    public PublicationSample createAndSave(
            BibliographicalDataId bibliographicalDataId,
            UserId userId,
            PublicationStatus publicationStatus,
            EditionId editionId,
            Condition condition) {

        PublicationSample created = factory.create(
                bibliographicalDataId, userId, publicationStatus, editionId, condition);

        return publicationSampleRepository.save(created);
    }
}