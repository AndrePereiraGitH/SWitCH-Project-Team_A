package TOPSECRET.repository;

import TOPSECRET.domain.AppraisalEntity;
import TOPSECRET.domain.Contact;
import TOPSECRET.domain.AppraisalEntityFactory;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.dto.GenreDetailsDTO_US004;
import TOPSECRET.dto.PublicationTypeDTO_US004;

import java.util.ArrayList;
import java.util.List;

/**
 * US004
 *
 * Repository responsible for storing AppraisalEntity objects.
 *
 * Responsibilities:
 * - Convert selected Genre and PublicationType DTOs into existing domain objects.
 * - Use FactoryAppraisalEntity to create new AppraisalEntity instances.
 * - Prevent duplicates based on name and contact.
 */
public class AppraisalEntityRepository {

    private List<AppraisalEntity> _appraisalEntities;
    private GenreRepository _genreRepository;
    private PublicationTypeRepository _publicationTypeRepository;
    private AppraisalEntityFactory _appraisalEntityFactory;

    /**
     * Constructor.
     *
     * @param genreRepository Repository containing existing Genre domain objects.
     * @param publicationTypeRepository Repository containing existing PublicationType domain objects.
     */
    public AppraisalEntityRepository(
            GenreRepository genreRepository,
            PublicationTypeRepository publicationTypeRepository,
            AppraisalEntityFactory appraisalEntityFactory) {

        if (genreRepository == null) {
            throw new IllegalArgumentException("GenreRepository cannot be null.");
        }

        if (publicationTypeRepository == null) {
            throw new IllegalArgumentException("PublicationTypeRepository cannot be null.");
        }

        this._appraisalEntities = new ArrayList<>();
        this._genreRepository = genreRepository;
        this._publicationTypeRepository = publicationTypeRepository;
        this._appraisalEntityFactory = appraisalEntityFactory;
    }

    /**
     * US004
     *
     * Creates and stores a new AppraisalEntity.
     *
     * Steps:
     * 1. Converts selected Genre DTOs into existing Genre domain objects.
     * 2. Converts selected PublicationType DTOs into existing PublicationType domain objects.
     * 3. Uses FactoryAppraisalEntity to create the AppraisalEntity instance.
     * 4. Validates that no existing entity shares the same name and contact.
     * 5. Stores the entity in the repository.
     *
     * @param name Name of the AppraisalEntity.
     * @param contact Contact of the AppraisalEntity.
     * @param selectedGenresDTO List of selected Genre DTOs.
     * @param selectedPublicationTypesDTO List of selected PublicationType DTOs.
     * @return The created AppraisalEntity.
     *
     * @throws IllegalArgumentException if entity already exists or if any DTO does not match existing domain objects.
     */
    public AppraisalEntity createAndAddAppraisalEntity(
            String name,
            Contact contact,
            List<GenreDetailsDTO_US004> selectedGenresDTO,
            List<PublicationTypeDTO_US004> selectedPublicationTypesDTO) {

        if (selectedGenresDTO == null || selectedGenresDTO.isEmpty()) {
            throw new IllegalArgumentException("Genres cannot be null or empty.");
        }

        if (selectedPublicationTypesDTO == null || selectedPublicationTypesDTO.isEmpty()) {
            throw new IllegalArgumentException("PublicationTypes cannot be null or empty.");
        }

        List<Genre> genres = new ArrayList<>();
        for (GenreDetailsDTO_US004 dto : selectedGenresDTO) {
            Genre genre = _genreRepository.findByTitle(dto.getTitle());
            genres.add(genre);
        }

        List<PublicationType> publicationTypes = new ArrayList<>();
        for (PublicationTypeDTO_US004 dto : selectedPublicationTypesDTO) {
            PublicationType publicationType =
                    _publicationTypeRepository.findByAttributes(
                            dto.getMediaFormat(),
                            dto.getTypeOfBinding(),
                            dto.getDetails()
                    );
            publicationTypes.add(publicationType);
        }

        if (existsByNameAndContact(name, contact)) {
            throw new IllegalArgumentException("AppraisalEntity already exists.");
        }


        AppraisalEntity apEn =
                _appraisalEntityFactory.create(name, contact, genres, publicationTypes);

        _appraisalEntities.add(apEn);
        return apEn;
    }

    /**
     * US004
     *
     * Verifies if an AppraisalEntity already exists
     * with the same name and contact.
     *
     * @param name Name to verify.
     * @param contact Contact to verify.
     * @return true if entity exists, false otherwise.
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