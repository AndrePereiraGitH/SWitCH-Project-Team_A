package TOPSECRET.controller.controllerCLI;

import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.dto.US004GenreDTO;
import TOPSECRET.dto.US004PublicationTypeDTO;
import TOPSECRET.dto.US004GenreMapper;
import TOPSECRET.dto.US004PublicationTypeMapper;
import TOPSECRET.services.AppraisalEntityService;

import java.util.List;


/**
 * Controller responsible for Register Appraisal Entity.
 *
 * Coordinates interaction between UI, domain repositories and mappers.
 */
public class ToRegisterAppraisalEntityController {

    private AppraisalEntityService _appraisalService;
    private IGenreRepository _genreRepository;
    private IPublicationTypeRepository _publicationTypeRepository;

    /**
     * Constructor.
     *
     * @param genreRepository Genre repository
     * @param publicationTypeRepository PublicationType repository
     * @param appraisalService AppraisalEntity service
     */
    public ToRegisterAppraisalEntityController(
            IGenreRepository genreRepository,
            IPublicationTypeRepository publicationTypeRepository,
            AppraisalEntityService appraisalService) {

        if (genreRepository == null) {
            throw new IllegalArgumentException("GenreRepository cannot be null.");
        }
        if (publicationTypeRepository == null) {
            throw new IllegalArgumentException("PublicationTypeRepositoryMem cannot be null.");
        }
        if (appraisalService == null) {
            throw new IllegalArgumentException("AppraisalService cannot be null.");
        }

        this._genreRepository = genreRepository;
        this._publicationTypeRepository = publicationTypeRepository;
        this._appraisalService = appraisalService;
    }


    /**
     * Retrieves all Genres and converts them into DTOs.
     *
     * @return list of Genre DTOs
     */
    public List<US004GenreDTO> getAllGenres() {

        List<Genre> genres = _genreRepository.findAll();

        return US004GenreMapper.toDTOs(genres);
    }

    /**
     * Retrieves all PublicationTypes and converts them into DTOs.
     *
     * @return list of PublicationType DTOs
     */
    public List<US004PublicationTypeDTO> getAllPublicationTypes() {

        List<PublicationType> publicationTypes =
                _publicationTypeRepository.findAll();

        return US004PublicationTypeMapper.toDTOs(publicationTypes);
    }

    /**
     * Registers a new AppraisalEntity.
     *
     * @param name name of the entity
     * @param contact contact info
     * @param genresId selected genres
     * @param publicationTypesId selected publication types
     * @return created AppraisalEntity
     */
    public AppraisalEntity registerAppraisalEntity(
            String name,
            Contact contact,
            List<GenreId> genresId,
            List<PublicationTypeId> publicationTypesId) {

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null.");
        }

        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null.");
        }

        if (genresId == null || genresId.isEmpty()) {
            throw new IllegalArgumentException("At least one Genre must be selected.");
        }

        if (publicationTypesId == null || publicationTypesId.isEmpty()) {
            throw new IllegalArgumentException("At least one PublicationType must be selected.");
        }

        return _appraisalService.createAndSave(
                name,
                contact,
                publicationTypesId,
                genresId
        );
    }
}
