package TOPSECRET.controller;

import TOPSECRET.domain.AppraisalEntity;
import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.dto.GenreDetailsDTO_US004;
import TOPSECRET.dto.PublicationTypeDTO_US004;
import TOPSECRET.repository.AppraisalEntityRepository;
import TOPSECRET.repository.GenreRepository;
import TOPSECRET.repository.PublicationTypeRepository;

import java.util.ArrayList;
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

    private GenreRepository _genreRepository;
    private PublicationTypeRepository _publicationTypeRepository;
    private AppraisalEntityRepository _appraisalEntityRepository;

    /**
     * Constructor.
     *
     * @param genreRepository Repository of Genre.
     * @param publicationTypeRepository Repository of PublicationType.
     * @param appraisalEntityRepository Repository of AppraisalEntity.
     */
    public US004ToRegisterAppraisalEntityController(
            GenreRepository genreRepository,
            PublicationTypeRepository publicationTypeRepository,
            AppraisalEntityRepository appraisalEntityRepository) {

        if (genreRepository == null) {
            throw new IllegalArgumentException("GenreRepository cannot be null.");
        }

        if (publicationTypeRepository == null) {
            throw new IllegalArgumentException("PublicationTypeRepository cannot be null.");
        }

        if (appraisalEntityRepository == null) {
            throw new IllegalArgumentException("AppraisalEntityRepository cannot be null.");
        }

        this._genreRepository = genreRepository;
        this._publicationTypeRepository = publicationTypeRepository;
        this._appraisalEntityRepository = appraisalEntityRepository;
    }

    /**
     * Retrieves all available Genres and converts them to DTO objects.
     * @return List of GenreDetailsDTO_US004 for UI display.
     */
    public List<GenreDetailsDTO_US004> getAllGenres() {

        List<Genre> genres = _genreRepository.getAllGenres();
        List<GenreDetailsDTO_US004> result = new ArrayList<>();

        for (Genre genre : genres) {
            result.add(new GenreDetailsDTO_US004(genre.getGenreType()));
        }
        return result;
    }

    /**
     * Retrieves all available PublicationTypes and converts them to DTO objects.
     * @return List of PublicationTypeDTO_US004 for UI display.
     */
    public List<PublicationTypeDTO_US004> getAllPublicationTypes() {

        List<PublicationType> types =
                _publicationTypeRepository.getAllPublicationTypes();

        List<PublicationTypeDTO_US004> result = new ArrayList<>();

        for (PublicationType type : types) {
            result.add(
                    new PublicationTypeDTO_US004(
                            type.getMediaFormat(),
                            type.getTypeOfBinding(),
                            type.getDetails()
                    )
            );
        }
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
