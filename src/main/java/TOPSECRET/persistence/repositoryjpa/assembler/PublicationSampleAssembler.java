package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationSample.PublicationSampleFactory;
import TOPSECRET.persistence.repositoryjpa.datamodel.PublicationSampleDM;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Assembler responsible for converting between {@link PublicationSample} domain objects
 * and {@link PublicationSampleDM} persistence entities.
 *
 * <p>This class handles two conversion directions:</p>
 * <ul>
 *   <li><b>toDM</b> — domain object → data model, used during the save flow.</li>
 *   <li><b>toDomain</b> — data model → domain object, used during the reconstruction flow.</li>
 * </ul>
 *
 * <p>This assembler is the only component permitted to call the reconstruction
 * overload of {@link PublicationSampleFactory#create}, which accepts a pre-existing
 * {@code PublicationSampleId}. All other callers must use the creation overload.</p>
 */
@Service
public class PublicationSampleAssembler {

    private final PublicationSampleFactory _factory;

    /**
     * Constructs a {@code PublicationSampleAssembler} with the required factory dependency.
     *
     * @param factory the factory used to reconstruct {@link PublicationSample} domain objects;
     *                must not be {@code null}
     */
    public PublicationSampleAssembler(PublicationSampleFactory factory) {
        _factory = Objects.requireNonNull(factory, "PublicationSampleFactory cannot be null.");
    }

    /**
     * Converts a {@link PublicationSample} domain object into a {@link PublicationSampleDM}
     * persistence entity.
     *
     * <p>Used during the save flow, before persisting to the database.
     * All fields are extracted from the domain object via its getters and
     * passed directly to the {@link PublicationSampleDM} constructor.</p>
     *
     * @param pbSample the domain object to convert; must not be {@code null}
     * @return a fully populated {@link PublicationSampleDM} ready for persistence
     */
    public PublicationSampleDM toDM(PublicationSample pbSample){

        Objects.requireNonNull(pbSample, "PublicationSample cannot be null.");

        PublicationSampleDM pbSampleDM = new PublicationSampleDM(pbSample.id(),pbSample.getBibliographicalDataID(),
                pbSample.getOwnerID(),pbSample.getEditionID(),pbSample.getLibraryID(),pbSample.getCollecOfPubID(),
                pbSample.getPublicationStatus(),pbSample.getCondition());


        return pbSampleDM;
    }

    /**
     * Reconstructs a {@link PublicationSample} domain object from a {@link PublicationSampleDM}
     * persistence entity.
     *
     * <p>Used during the reconstruction flow, after loading from the database.
     * Delegates to the reconstruction overload of {@link PublicationSampleFactory#create},
     * supplying the persisted {@code PublicationSampleId} to preserve the original identity.
     * No new identity is generated during this process.</p>
     *
     * <p>Domain invariants are still enforced — the factory delegates to the
     * {@link PublicationSample} constructor, which validates all arguments.</p>
     *
     * @param pbSampleDM the persistence entity to convert; must not be {@code null}
     * @return a fully reconstructed {@link PublicationSample} with its original identity restored
     */
    public PublicationSample toDomain(PublicationSampleDM pbSampleDM){

        Objects.requireNonNull(pbSampleDM, "PublicationSampleDM cannot be null.");

        PublicationSample pbSample = _factory.create(pbSampleDM.getPubSampleId(),pbSampleDM.getBdId(),
                pbSampleDM.getOwnerId(),pbSampleDM.getPublicationStatus(),pbSampleDM.getEditionId(),pbSampleDM.getCondition());

        return pbSample;
    }

    /**
     * Converts a list of {@link PublicationSampleDM} persistence entities
     * into a list of {@link PublicationSample} domain objects.
     *
     * <p>
     * Each {@link PublicationSampleDM} is individually converted using
     * {@link #toDomain(PublicationSampleDM)}.
     * </p>
     *
     * @param dmList list of PublicationSampleDM objects to convert
     * @return list of corresponding PublicationSample domain objects
     * @throws NullPointerException if the provided list is null
     */
    public List<PublicationSample> toDomainList (List<PublicationSampleDM> dmList) {

        Objects.requireNonNull(dmList,
                "PublicationSampleDM list must not be null");

        List<PublicationSample> result = new ArrayList<>();

        for (PublicationSampleDM dm : dmList) {
            result.add(toDomain(dm));
        }

        return result;
    }

}
