package TOPSECRET.persistence.mem;

import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.IAppraisalEntityRepository;
import TOPSECRET.domain.ValueObjects.AppraisalEntityId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * In-memory implementation of {@link IAppraisalEntityRepository}.
 *
 * <p>
 * Stores {@link AppraisalEntity} instances in memory.
 * Used mainly for testing or non-persistent scenarios.
 * </p>
 */
public class AppraisalEntityRepositoryMem implements IAppraisalEntityRepository {

    private final List<AppraisalEntity> appraisalEntities;

    /**
     * Creates a new repository instance.
     */
    public AppraisalEntityRepositoryMem () {
        this.appraisalEntities = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AppraisalEntity save(AppraisalEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }
        appraisalEntities.removeIf(e -> e.id().equals(entity.id()));

        appraisalEntities.add(entity);

        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<AppraisalEntity> findById(AppraisalEntityId id) {

        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        return appraisalEntities.stream()
                .filter(e -> e.id().equals(id))
                .findFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AppraisalEntity> findAll() {
        return List.copyOf(appraisalEntities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(AppraisalEntityId id) {

        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        return appraisalEntities.stream()
                .anyMatch(e -> e.id().equals(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteById(AppraisalEntityId id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        Optional<AppraisalEntity> existing = findById(id);

        if (existing.isEmpty()) {
            return false;
        }

        appraisalEntities.remove(existing.get());
        return true;
    }
}