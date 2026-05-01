package TOPSECRET.domain.AppraisalEntity;

import TOPSECRET.domain.ValueObjects.AppraisalEntityId;

import java.util.List;
import java.util.Optional;

/**
 * Repository abstraction for AppraisalEntity aggregate.
 *
 * <p>
 * Defines the contract for persistence operations,
 * independent of the underlying implementation.
 * </p>
 */
public interface IAppraisalEntityRepository {

    /**
     * Saves or updates an AppraisalEntity.
     *
     * @param entity the entity to persist
     * @return the persisted entity
     * @throws IllegalArgumentException if entity is null
     */
    AppraisalEntity save(AppraisalEntity entity);

    /**
     * Finds an AppraisalEntity by its identifier.
     *
     * @param id the entity identifier
     * @return an Optional containing the entity if found
     * @throws IllegalArgumentException if id is null
     */
    Optional<AppraisalEntity> findById(AppraisalEntityId id);

    /**
     * Retrieves all AppraisalEntities.
     *
     * @return list of all entities
     */
    List<AppraisalEntity> findAll();

    /**
     * Checks if an entity exists by its identifier.
     *
     * @param id the entity identifier
     * @return true if exists, false otherwise
     * @throws IllegalArgumentException if id is null
     */
    boolean existsById(AppraisalEntityId id);
}
