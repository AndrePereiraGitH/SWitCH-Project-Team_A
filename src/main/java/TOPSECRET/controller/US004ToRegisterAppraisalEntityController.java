package TOPSECRET.controller;

import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.IAppraisalEntityRepository;
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

import java.util.List;


/**
 * Controller responsible for US004 - Register Appraisal Entity.
 *
 * Coordinates interaction between UI, domain repositories and mappers.
 */
public class US004ToRegisterAppraisalEntityController {

    private IGenreRepository _genreRepository;
    private IPublicationTypeRepository _publicationTypeRepository;
    private IAppraisalEntityRepository _appraisalEntityRepository;

    /**
     * Constructor.
     *
     * @param genreRepository Genre repository
     * @param publicationTypeRepository PublicationType repository
     * @param appraisalEntityRepository AppraisalEntity repository
     */
    public US004ToRegisterAppraisalEntityController(
            IGenreRepository genreRepository,
            IPublicationTypeRepository publicationTypeRepository,
            IAppraisalEntityRepository appraisalEntityRepository) {

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
     * Retrieves all Genres and converts them into DTOs.
     *
     * @return list of Genre DTOs
     */
    public List<US004GenreDTO> getAllGenres() {

        List<Genre> genres = _genreRepository.getAllGenres();

        return US004GenreMapper.toDTOs(genres);
    }

    /**
     * Retrieves all PublicationTypes and converts them into DTOs.
     *
     * @return list of PublicationType DTOs
     */
    public List<US004PublicationTypeDTO> getAllPublicationTypes() {

        List<PublicationType> publicationTypes =
                _publicationTypeRepository.getAllPublicationTypes();

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

        if (genresId == null || genresId.isEmpty()) {
            throw new IllegalArgumentException("At least one Genre must be selected.");
        }

        if (publicationTypesId == null || publicationTypesId.isEmpty()) {
            throw new IllegalArgumentException("At least one PublicationType must be selected.");
        }

        return _appraisalEntityRepository.createAndAdd(
                name,
                contact,
                genresId,
                publicationTypesId
        );
    }
}
