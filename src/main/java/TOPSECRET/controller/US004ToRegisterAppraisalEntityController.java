package TOPSECRET.controller;

import TOPSECRET.domain.AppraisalEntity;
import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.dto.GenreDetailsDTO_US004;
import TOPSECRET.dto.PublicationTypeDTO_US004;
import TOPSECRET.repository.AppraisalEntityRepository;
import TOPSECRET.repository.GenreRepositoryMem;
import TOPSECRET.repository.IPublicationTypeRepository;

import java.util.List;

/**
 * US004
 *
 * Controller responsible for handling the registration of an Appraisal Entity.
 *
 * This controller coordinates the interaction between the UI layer and the
 * domain/repository layer. It performs the following responsibilities:
 *
 * - Retrieves available Genres and PublicationTypes from their respective repositories.
 * - Converts domain objects to DTOs to be safely presented to the UI.
 * - Receives selected DTOs from the UI and converts them back into domain objects.
 * - Validates that at least one Genre and one PublicationType are selected.
 * - Delegates the creation of the AppraisalEntity to the repository.
 *
 * This class contains repositories as attributes because it needs to access
 * and persist domain data.
 *
 * It also contains mapper objects (GenreDTO and PublicationTypeDTO)
 * because the controller is responsible for converting between DTOs and
 * domain objects, maintaining a clear separation between presentation and domain layers.
 */

public class US004ToRegisterAppraisalEntityController {

    private GenreRepositoryMem _genreRepository;
    private IPublicationTypeRepository _publicationTypeRepository;
    private AppraisalEntityRepository _appraisalEntityRepository;

    /**
     * Constructor.
     *
     * @param genreRepository Repository of Genre.
     * @param publicationTypeRepository Repository of PublicationType.
     * @param appraisalEntityRepository Repository of AppraisalEntity.
     */
    public US004ToRegisterAppraisalEntityController(
            GenreRepositoryMem genreRepository,
            IPublicationTypeRepository publicationTypeRepository,
            AppraisalEntityRepository appraisalEntityRepository) {

        if (genreRepository == null) {
            throw new IllegalArgumentException("GenreRepository cannot be null.");
        }

        if (publicationTypeRepository == null) {
            throw new IllegalArgumentException("PublicationTypeRepositoryMem cannot be null.");
        }

        if (appraisalEntityRepository == null) {
            throw new IllegalArgumentException("AppraisalEntityRepository cannot be null.");
        }

        this._genreRepository = genreRepository;
        this._publicationTypeRepository = publicationTypeRepository;
        this._appraisalEntityRepository = appraisalEntityRepository;
    }

    /**
     * Retrieves all available genres from the repository and converts them
     * into DTO objects suitable for presentation in the UI layer.
     * The method performs the following steps:
     *   <li>Requests the list of {@link Genre} objects from the {@link GenreRepositoryMem}.</li>
     *   <li>Creates a new list to avoid exposing the original domain collection.</li>
     *   <li>Converts each {@link Genre} domain object into a {@link GenreDetailsDTO_US004}.</li>
     *   <li>Only the genre title is included in the DTO to prevent exposing
     *       internal domain details.</li>
     * This ensures a clear separation between the domain layer and the
     * presentation layer by exposing only the necessary information.
     * @return a list of {@link GenreDetailsDTO_US004} containing the titles
     * of all available genres.
     */
    public List<GenreDetailsDTO_US004> getAllGenres() {

        List<Genre> genres = _genreRepository.getAllGenres();

        return GenreDetailsDTO_US004.genreToDTO(genres);
    }

    /**
     * Retrieves all available publication types from the repository
     * and converts them into DTO objects for presentation in the UI layer.
     *
     * <p>
     * The method performs the following steps:
     * </p>
     * <ol>
     *     <li>Requests the list of {@link PublicationType} domain objects from
     *         the {@link IPublicationTypeRepository}.</li>
     *     <li>Creates a new list to avoid exposing the original domain collection.</li>
     *     <li>Converts each {@link PublicationType} domain object into a
     *         {@link PublicationTypeDTO_US004}.</li>
     *     <li>Returns the resulting list of DTO objects containing only the
     *         attributes required by the UI.</li>
     * </ol>
     *
     * <p>
     * This ensures that internal domain objects are not directly exposed to the
     * presentation layer and maintains a clear separation between the domain
     * model and the UI representation.
     * </p>
     *
     * @return list of {@link PublicationTypeDTO_US004} representing
     *         all available publication types.
     */
    public List<PublicationTypeDTO_US004> getAllPublicationTypes() {

        List<PublicationType> publicationTypes =
                _publicationTypeRepository.getAllPublicationTypes();

        List<PublicationTypeDTO_US004> result =
                PublicationTypeDTO_US004.publicationTypesToDTO(publicationTypes);

        return result;
    }

    /**
     * Registers a new AppraisalEntity.
     * Validates that at least one Genre and one PublicationType are selected.
     *
     * @param name Name of the Appraisal Entity.
     * @param contact Contact information.
     * @param selectedGenresDTO Selected Genre DTOs.
     * @param selectedPublicationTypesDTO Selected PublicationType DTOs.
     * @return The created AppraisalEntity.
     */
    public AppraisalEntity registerAppraisalEntity(
            String name,
            Contact contact,
            List<GenreDetailsDTO_US004> selectedGenresDTO,
            List<PublicationTypeDTO_US004> selectedPublicationTypesDTO) {

        if (selectedGenresDTO == null || selectedGenresDTO.isEmpty()) {
            throw new IllegalArgumentException("At least one Genre must be selected.");
        }

        if (selectedPublicationTypesDTO == null || selectedPublicationTypesDTO.isEmpty()) {
            throw new IllegalArgumentException("At least one PublicationType must be selected.");
        }


        return _appraisalEntityRepository.createAndAddAppraisalEntity(
                name,
                contact,
                selectedGenresDTO,
                selectedPublicationTypesDTO
        );
    }

}
