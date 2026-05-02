package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.Publisher.PublisherFactory;
import TOPSECRET.persistence.repositoryjpa.datamodel.PublisherDM;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Assembler responsible for converting between {@link Publisher} domain objects
 * and {@link PublisherDM} persistence entities.
 *
 * <p>This class handles two conversion directions:</p>
 * <ul>
 *   <li><b>toDM</b> — domain object → data model, used during the save flow.</li>
 *   <li><b>toDomain</b> — data model → domain object, used during the reconstruction flow.</li>
 * </ul>
 *
 * <p>This assembler is the only component permitted to call the reconstruction
 * overload of {@link PublisherFactory#create}, which accepts a pre-existing
 * {@code PublisherID}. All other callers must use the creation overload.</p>
 */
@Service
public class PublisherAssembler {

    private final PublisherFactory _factory;

    /**
     * Constructs a {@code PublisherAssembler} with the required factory dependency.
     *
     * @param factory the factory used to reconstruct {@link Publisher} domain objects;
     *                must not be {@code null}
     */
    public PublisherAssembler(PublisherFactory factory) {
        _factory = Objects.requireNonNull(factory, "PublisherFactory cannot be null");
    }

    /**
     * Converts a {@link Publisher} domain object into a {@link PublisherDM} persistence entity.
     *
     * <p>Used during the save flow, before persisting to the database.</p>
     *
     * @param publisher the domain object to convert; must not be {@code null}
     * @return a fully populated {@link PublisherDM} ready for persistence
     * @throws NullPointerException if {@code publisher} is {@code null}
     */
    public PublisherDM toDM(Publisher publisher) {
        Objects.requireNonNull(publisher, "Publisher cannot be null");

        return new PublisherDM(
                publisher.id(),
                publisher.getName(),
                publisher.getContact()
        );
    }

    /**
     * Reconstructs a {@link Publisher} domain object from a {@link PublisherDM} persistence entity.
     *
     * <p>Used during the reconstruction flow, after loading from the database.
     * Delegates to the reconstruction overload of {@link PublisherFactory#create},
     * supplying the persisted {@code PublisherID} to preserve the original identity.</p>
     *
     * @param dm the persistence entity to convert; must not be {@code null}
     * @return a fully reconstructed {@link Publisher} with its original identity restored
     * @throws NullPointerException if {@code dm} is {@code null}
     */
    public Publisher toDomain(PublisherDM dm) {
        Objects.requireNonNull(dm, "PublisherDM cannot be null");

        return _factory.create(
                dm.getId(),
                dm.getName(),
                dm.getId().getId(),
                dm.getContact()
        );
    }
}