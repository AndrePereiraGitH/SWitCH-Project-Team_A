package TOPSECRET.domain.PublicationType;

import TOPSECRET.domain.ValueObjects.PublicationTypeId;

import java.util.List;
import java.util.Optional;

/**
 * Repository contract for {@link PublicationType} persistence operations.
 *
 * <p>
 * This interface defines methods for storing and retrieving
 * PublicationType aggregates. It does not contain business logic,
 * which should be handled in the service layer.
 * </p>
 */
public interface IPublicationTypeRepository {

    /**
     * Persists a publication type.
     *
     * @param pubType the publication type to persist; must not be null
     * @return the persisted {@link PublicationType}
     * @throws IllegalArgumentException if {@code pubType} is null
     */
    PublicationType save(PublicationType pubType);

    /**
     * Retrieves all publication types.
     *
     * @return a list of {@link PublicationType}
     */
    List<PublicationType> findAll();

    /**
     * Finds a publication type by its unique identifier.
     *
     * @param id the publication type identifier; must not be null
     * @return an {@link Optional} containing the publication type if found,
     * or empty if no match exists
     * @throws IllegalArgumentException if {@code id} is null
     */
    Optional<PublicationType> findById(PublicationTypeId id);

    /**
     * Finds a publication type by its media format.
     *
     * @param mediaFormat the media format to search for; must not be null or blank
     * @return an {@link Optional} containing the publication type if found,
     * or empty if no match exists
     * @throws IllegalArgumentException if {@code mediaFormat} is null or blank
     */
    Optional<PublicationType> findByMediaFormat(String mediaFormat);

}