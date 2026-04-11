package TOPSECRET.controller;

import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.IAppraisalEntityRepository;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.dto.US004GenreDTO;
import TOPSECRET.dto.US004PublicationTypeDTO;
import TOPSECRET.dto.US004GenreMapper;
import TOPSECRET.dto.US004PublicationTypeMapper;

import java.util.ArrayList;
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
    private US004GenreMapper _US004GenreMapper;
    private US004PublicationTypeMapper _US004PublicationTypeMapper;

    /**
     * Constructor.
     *
     * @param genreRepository Genre repository
     * @param publicationTypeRepository PublicationType repository
     * @param appraisalEntityRepository AppraisalEntity repository
     * @param US004GenreMapper Genre mapper
     * @param US004PublicationTypeMapper PublicationType mapper
     */
    public US004ToRegisterAppraisalEntityController(
            IGenreRepository genreRepository,
            IPublicationTypeRepository publicationTypeRepository,
            IAppraisalEntityRepository appraisalEntityRepository,
            US004GenreMapper US004GenreMapper,
            US004PublicationTypeMapper US004PublicationTypeMapper) {

        if (genreRepository == null) {
            throw new IllegalArgumentException("GenreRepository cannot be null.");
        }
        if (publicationTypeRepository == null) {
            throw new IllegalArgumentException("PublicationTypeRepositoryMem cannot be null.");
        }
        if (appraisalEntityRepository == null) {
            throw new IllegalArgumentException("AppraisalEntityRepository cannot be null.");
        }
        if (US004GenreMapper == null) {
            throw new IllegalArgumentException("GenreMapper cannot be null.");
        }
        if (US004PublicationTypeMapper == null) {
            throw new IllegalArgumentException("PublicationTypeMapper cannot be null.");
        }

        this._genreRepository = genreRepository;
        this._publicationTypeRepository = publicationTypeRepository;
        this._appraisalEntityRepository = appraisalEntityRepository;
        this._US004GenreMapper = US004GenreMapper;
        this._US004PublicationTypeMapper = US004PublicationTypeMapper;
    }


    /**
     * Retrieves all Genres and converts them into DTOs.
     *
     * @return list of Genre DTOs
     */
    public List<US004GenreDTO> getAllGenres() {

        List<Genre> genres = _genreRepository.getAllGenres();

        return _US004GenreMapper.toDTO(genres);
    }

    /**
     * Retrieves all PublicationTypes and converts them into DTOs.
     *
     * @return list of PublicationType DTOs
     */
    public List<US004PublicationTypeDTO> getAllPublicationTypes() {

        List<PublicationType> publicationTypes =
                _publicationTypeRepository.getAllPublicationTypes();

        return _US004PublicationTypeMapper.toDTO(publicationTypes);
    }


    /**
     * Registers a new AppraisalEntity.
     *
     * @param name name of the entity
     * @param contact contact info
     * @param selectedGenresDTO selected genres
     * @param selectedPublicationTypesDTO selected publication types
     * @return created AppraisalEntity
     */
    public AppraisalEntity registerAppraisalEntity(
            String name,
            Contact contact,
            List<US004GenreDTO> selectedGenresDTO,
            List<US004PublicationTypeDTO> selectedPublicationTypesDTO) {

        if (selectedGenresDTO == null || selectedGenresDTO.isEmpty()) {
            throw new IllegalArgumentException("At least one Genre must be selected.");
        }

        if (selectedPublicationTypesDTO == null || selectedPublicationTypesDTO.isEmpty()) {
            throw new IllegalArgumentException("At least one PublicationType must be selected.");
        }

        List<GenreID> genresId = new ArrayList<>();

        for (US004GenreDTO dto : selectedGenresDTO) {
            genresId.add(dto.id());
        }

        List<PublicationTypeId> publicationTypesId = new ArrayList<>();

        for (US004PublicationTypeDTO dto : selectedPublicationTypesDTO) {
            publicationTypesId.add(dto.id());
        }

        return _appraisalEntityRepository.createAndAdd(
                name,
                contact,
                genresId,
                publicationTypesId
        );
    }

}
