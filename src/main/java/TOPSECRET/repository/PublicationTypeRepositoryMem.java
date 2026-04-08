package TOPSECRET.repository;

import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.PublicationType.PublicationTypeFactory;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory implementation of {@link IPublicationTypeRepository}.
 *
 * <p>
 * Stores publication types in a local collection.
 * Responsible for delegating creation to the factory
 * and enforcing validation rules such as uniqueness.
 * </p>
 */
public class PublicationTypeRepositoryMem implements IPublicationTypeRepository {

    private final List<PublicationType> _publicationTypes = new ArrayList<>();
    private final PublicationTypeFactory _factory;


    /**
     * Creates a new in-memory repository.
     *
     * @param factory factory used to create {@link PublicationType} instances
     */
    public PublicationTypeRepositoryMem(PublicationTypeFactory factory) {
        this._factory = factory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PublicationType create (String mediaFormat) {

        if (mediaFormat == null || mediaFormat.isBlank()) {
            throw new IllegalArgumentException("Media format can't be null or blank.");
        }

        PublicationType pt = _factory.create(mediaFormat);
        return save(pt);
    }

    /**
     * Saves a PublicationType in the repository.
     *
     * @param pubType publication type to save
     * @return saved PublicationType
     * @throws IllegalArgumentException if publication type is invalid or duplicated
     */
    private PublicationType save(PublicationType pubType) {
        if (!isValid(pubType)) {
            throw new IllegalArgumentException("Publication type already exists or is invalid.");
        }

        _publicationTypes.add(pubType);
        return pubType;
    }


    /**
     * Validates if a PublicationType can be added.
     *
     * @param pubType publication type to validate
     * @return true if valid and not duplicated, false otherwise
     */
    private boolean isValid(PublicationType pubType) {
        if (pubType == null) {
            return false;
        }

        String newFormat = pubType.getMediaFormat().trim();

        return _publicationTypes.stream()
                .noneMatch(p -> p.getMediaFormat().trim()
                        .equalsIgnoreCase(newFormat));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PublicationType addToList(PublicationType pubType) {
        if (!isValid(pubType)) {
            throw new IllegalArgumentException("Publication type already exists or is null");
        }

        _publicationTypes.add(pubType);
        return pubType;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<PublicationType> getAllPublicationTypes() {
        return List.copyOf(_publicationTypes);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PublicationType findPublicationTypeByAttributes(String mediaFormat) {

        if (mediaFormat == null || mediaFormat.isBlank()) {
            throw new IllegalArgumentException("MediaFormat can't be null.");
        }

        return _publicationTypes.stream()
                .filter(pt -> pt.getMediaFormat().trim()
                        .equalsIgnoreCase(mediaFormat.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("PublicationType not found."));
    }

    /**
     * {@inheritDoc}
     */
    public Optional<PublicationType> findPublicationTypeById(PublicationTypeId pubTypeId){

        if(pubTypeId == null){
            throw new NullPointerException("Publication type can not be null");
        }

        for(PublicationType type: _publicationTypes){
            if(type.id().equals(pubTypeId)){
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

}
