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
    private GenreDetailsDTO_US004 _genreDTO;
    private PublicationTypeDTO_US004 _publicationTypeDTO;

    public US004ToRegisterAppraisalEntityController(
            GenreRepository genreRepository,
            PublicationTypeRepository publicationTypeRepository,
            AppraisalEntityRepository appraisalEntityRepository,
            GenreDetailsDTO_US004 genreDTO,
            PublicationTypeDTO_US004 publicationTypeDTO) {

        this._genreRepository = genreRepository;
        this._publicationTypeRepository = publicationTypeRepository;
        this._appraisalEntityRepository = appraisalEntityRepository;
        this._genreDTO = genreDTO;
        this._publicationTypeDTO = publicationTypeDTO;
    }

    /**
     * Retrieves all available genres and converts them to DTO objects.
     * @return List of GenreDetailsDTO to be displayed to the Administrator.
     */
    public List<GenreDetailsDTO_US004> getAllGenres() {
        List<Genre> genres = _genreRepository.getAllGenres();
        return _genreDTO.genreListToDTO(genres);
    }

    /**
     * Retrieves all available publication types and converts them to DTO objects.
     *
     * @return List of PubTypeDTO to be displayed to the Administrator.
     */
    public List<PublicationTypeDTO_US004> getAllPublicationTypes() {
        List<PublicationType> types = _publicationTypeRepository.getAllPublicationTypes();
        return _publicationTypeDTO.publicationTypeListToDTO(types);
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

        List<Genre> genres = new ArrayList<>();
        for (GenreDetailsDTO_US004 dto : selectedGenresDTO) {
            genres.add(dto.dtoToGenre());
        }

        List<PublicationType> publicationTypes = new ArrayList<>();
        for (PublicationTypeDTO_US004 dto : selectedPublicationTypesDTO) {
            publicationTypes.add(dto.dtoToPublicationType());
        }

        return _appraisalEntityRepository.createAndAddAppraisalEntity(
                name, contact, publicationTypes, genres);
    }

}
