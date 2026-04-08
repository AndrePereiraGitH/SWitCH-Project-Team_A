package TOPSECRET.repository;

import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntityFactory;
import TOPSECRET.domain.AppraisalEntity.IAppraisalEntityRepository;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.dto.US004GenreDTO;
import TOPSECRET.dto.US004PublicationTypeDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * In-memory implementation of {@link IAppraisalEntityRepository}.
 *
 * <p>
 * Stores {@link AppraisalEntity} instances in a local collection.
 * Responsible for converting DTOs into domain objects,
 * delegating creation to the factory, and enforcing business rules.
 * </p>
 *
 * <p>
 * Ensures:
 * <ul>
 *     <li>Referenced genres and publication types exist</li>
 *     <li>No duplicate entities exist with the same name and contact</li>
 * </ul>
 * </p>
 */
public class AppraisalEntityRepositoryMem implements IAppraisalEntityRepository {

    private List<AppraisalEntity> _appraisalEntities;
    private IGenreRepository _genreRepository;
    private IPublicationTypeRepository _publicationTypeRepository;
    private AppraisalEntityFactory _appraisalEntityFactory;


    /**
     * Creates a new repository instance.
     *
     * @param genreRepository repository for retrieving Genre objects
     * @param publicationTypeRepository repository for retrieving PublicationType objects
     * @param appraisalEntityFactory factory used to create AppraisalEntity instances
     * @throws IllegalArgumentException if any dependency is null
     */
    public AppraisalEntityRepositoryMem (
            IGenreRepository genreRepository,
            IPublicationTypeRepository publicationTypeRepository,
            AppraisalEntityFactory appraisalEntityFactory) {

        if (genreRepository == null) {
            throw new IllegalArgumentException("GenreRepository cannot be null.");
        }

        if (publicationTypeRepository == null) {
            throw new IllegalArgumentException("PublicationTypeRepository cannot be null.");
        }

        if (appraisalEntityFactory == null) {
            throw new IllegalArgumentException("AppraisalEntityFactory cannot be null.");
        }

        this._appraisalEntities = new ArrayList<>();
        this._genreRepository = genreRepository;
        this._publicationTypeRepository = publicationTypeRepository;
        this._appraisalEntityFactory = appraisalEntityFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AppraisalEntity createAndAdd(String name,
            Contact contact,
            List<US004GenreDTO> selectedGenresDTO,
            List<US004PublicationTypeDTO> selectedPublicationTypesDTO) {

        if (selectedGenresDTO == null || selectedGenresDTO.isEmpty()) {
            throw new IllegalArgumentException("Genres cannot be null or empty.");
        }

        if (selectedPublicationTypesDTO == null || selectedPublicationTypesDTO.isEmpty()) {
            throw new IllegalArgumentException("PublicationTypes cannot be null or empty.");
        }

        List<GenreID> genres = dtoToGenres(selectedGenresDTO);
        List<PublicationTypeId> publicationTypes = dtoToPublicationTypes(selectedPublicationTypesDTO);

        if (existsByNameAndContact(name, contact)) {
            throw new IllegalArgumentException("AppraisalEntity already exists.");
        }


        AppraisalEntity apEn =
                _appraisalEntityFactory.create(name, contact, publicationTypes, genres);

        _appraisalEntities.add(apEn);

        return apEn;
    }

    /**
     * Checks if an AppraisalEntity already exists with the same name and contact.
     *
     * @param name entity name
     * @param contact contact information
     * @return true if a matching entity exists, false otherwise
     */
    private boolean existsByNameAndContact(String name, Contact contact) {

        for (AppraisalEntity entity : _appraisalEntities) {

            if (entity.getName().equalsIgnoreCase(name)
                    && entity.getContact().equals(contact)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Converts a list of {@link US004GenreDTO} into
     * a list of {@link GenreID} by retrieving existing domain objects.
     *
     * <p>
     * Each DTO is validated against the repository to ensure that
     * the corresponding Genre exists in the system.
     * </p>
     *
     * @param dtos list of Genre DTOs
     * @return list of GenreID
     *
     * @throws IllegalArgumentException if any Genre does not exist
     */
    private List<GenreID> dtoToGenres(List<US004GenreDTO> dtos) {

        List<GenreID> genres = new ArrayList<>();

        for (US004GenreDTO dto : dtos) {

            Genre genre = _genreRepository.findById(dto.getId());

            if (genre == null) {
                throw new IllegalArgumentException("Genre not found: " + dto.getTitle());
            }

            genres.add(genre.id());
        }

        return genres;
    }

    /**
     * Converts a list of {@link US004PublicationTypeDTO} into
     * a list of {@link PublicationTypeId} by retrieving existing domain objects.
     *
     * <p>
     * Each DTO is validated against the repository to ensure that
     * the corresponding PublicationType exists in the system.
     * </p>
     *
     * @param dtos list of PublicationType DTOs
     * @return list of PublicationTypeId
     *
     * @throws IllegalArgumentException if any PublicationType does not exist
     */
    private List<PublicationTypeId> dtoToPublicationTypes(List<US004PublicationTypeDTO> dtos) {

        List<PublicationTypeId> types = new ArrayList<>();

        for (US004PublicationTypeDTO dto : dtos) {

            Optional<PublicationType> publicationType =
                    _publicationTypeRepository.findPublicationTypeById(dto.getId());

            if (publicationType.isEmpty()) {
                throw new IllegalArgumentException("PublicationType not found: " + dto.getMediaFormat());
            }

            types.add(publicationType.get().id());
        }

        return types;
    }

}