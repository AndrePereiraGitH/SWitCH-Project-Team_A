package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.persistence.repositoryjpa.assembler.PublicationTypeAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.PublicationTypeDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IPublicationTypeRepositoryJPA;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JPA implementation of {@link IPublicationTypeRepository}.
 *
 * <p>
 * This class bridges the domain layer and the persistence layer,
 * converting between {@link PublicationType} and {@link PublicationTypeDM}.
 * </p>
 */
@Repository
public class PublicationTypeRepositoryJPAImpl implements IPublicationTypeRepository {

    private final IPublicationTypeRepositoryJPA jpaRepository;
    private final PublicationTypeAssembler assembler;

    /**
     * Constructs the repository with required dependencies.
     *
     * @param jpaRepository the Spring Data JPA repository
     * @param assembler the assembler used for conversions
     * @throws IllegalArgumentException if any dependency is null
     */
    public PublicationTypeRepositoryJPAImpl(
            IPublicationTypeRepositoryJPA jpaRepository,
            PublicationTypeAssembler assembler
    ) {

        if (jpaRepository == null || assembler == null) {
            throw new IllegalArgumentException("Dependencies cannot be null");
        }

        this.jpaRepository = jpaRepository;
        this.assembler = assembler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PublicationType save(PublicationType pubType) {

        if (pubType == null) {
            throw new IllegalArgumentException("PublicationType cannot be null");
        }

        PublicationTypeDM dm = assembler.toDataModel(pubType);

        PublicationTypeDM saved = jpaRepository.save(dm);

        return assembler.toDomain(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PublicationType> findAll() {

        return jpaRepository.findAll()
                .stream()
                .map(assembler::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PublicationType> findById(PublicationTypeId id) {

        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        return jpaRepository.findById(id)
                .map(assembler::toDomain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<PublicationType> findByMediaFormat(String mediaFormat) {

        if (mediaFormat == null || mediaFormat.isBlank()) {
            throw new IllegalArgumentException("Media format cannot be null or empty");
        }

        return jpaRepository.findAll()
                .stream()
                .map(assembler::toDomain)
                .filter(pt -> pt.getMediaFormat().equals(mediaFormat))
                .findFirst();
    }
}