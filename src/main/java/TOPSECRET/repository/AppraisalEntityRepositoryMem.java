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
import TOPSECRET.dto.GenreDetailsDTO_US004;
import TOPSECRET.dto.PublicationTypeDTO_US004;

import java.util.ArrayList;
import java.util.List;


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
            List<GenreDetailsDTO_US004> selectedGenresDTO,
            List<PublicationTypeDTO_US004> selectedPublicationTypesDTO) {

        if (selectedGenresDTO == null || selectedGenresDTO.isEmpty()) {
            throw new IllegalArgumentException("Genres cannot be null or empty.");
        }

        if (selectedPublicationTypesDTO == null || selectedPublicationTypesDTO.isEmpty()) {
            throw new IllegalArgumentException("PublicationTypes cannot be null or empty.");
        }

        List<GenreID> genres = new ArrayList<>();
        for (GenreDetailsDTO_US004 dto : selectedGenresDTO) {
            Genre genre = _genreRepository.findByTitle(dto.getTitle());
            if (genre == null) {
                throw new IllegalArgumentException("Genre not found.");
            }
            genres.add(genre.id());
        }

        List<PublicationTypeId> publicationTypes = new ArrayList<>();
        for (PublicationTypeDTO_US004 dto : selectedPublicationTypesDTO) {
            PublicationType publicationType =
                    _publicationTypeRepository.findPublicationTypeByAttributes(dto.getMediaFormat());
            if (publicationType == null) {
                throw new IllegalArgumentException("PublicationType not found.");
            }

            publicationTypes.add(publicationType.id());
        }

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

}