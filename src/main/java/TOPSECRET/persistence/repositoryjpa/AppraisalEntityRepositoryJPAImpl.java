package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.IAppraisalEntityRepository;
import TOPSECRET.domain.ValueObjects.AppraisalEntityId;
import TOPSECRET.persistence.repositoryjpa.assembler.AppraisalEntityAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.AppraisalEntityDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IAppraisalEntityRepositoryJPA;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Repository implementation using JPA.
 *
 * Handles conversion between domain and data model using the assembler.
 */
@Repository
public class AppraisalEntityRepositoryJPAImpl implements IAppraisalEntityRepository {

    private final IAppraisalEntityRepositoryJPA apEnRepositoryJPA;
    private final AppraisalEntityAssembler assembler;

    public AppraisalEntityRepositoryJPAImpl(IAppraisalEntityRepositoryJPA jpaRepository,
                                            AppraisalEntityAssembler assembler
    ) {
        if (jpaRepository == null) {
            throw new IllegalArgumentException("JPA repository cannot be null");
        }
        if (assembler == null) {
            throw new IllegalArgumentException("Assembler cannot be null");
        }

        this.apEnRepositoryJPA = jpaRepository;
        this.assembler = assembler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AppraisalEntity save(AppraisalEntity entity) {

        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        AppraisalEntityDM dm = assembler.toDataModel(entity);
        AppraisalEntityDM saved = apEnRepositoryJPA.save(dm);

        return assembler.toDomain(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<AppraisalEntity> findById(AppraisalEntityId id) {

        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        return apEnRepositoryJPA.findById(id)
                .map(assembler::toDomain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AppraisalEntity> findAll() {
        return List.copyOf(apEnRepositoryJPA.findAll()
                                            .stream()
                                            .map(assembler::toDomain)
                                            .toList()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(AppraisalEntityId id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return apEnRepositoryJPA.existsById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteById(AppraisalEntityId id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (!apEnRepositoryJPA.existsById(id)) {
            return false;
        }
        apEnRepositoryJPA.deleteById(id);
        return true;
    }
}