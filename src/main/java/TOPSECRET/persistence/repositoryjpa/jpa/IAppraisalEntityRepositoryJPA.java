package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.AppraisalEntityId;
import TOPSECRET.persistence.repositoryjpa.datamodel.AppraisalEntityDM;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA repository interface for managing {@link AppraisalEntityDM} persistence.
 *
 * <p>
 * Extends {@link JpaRepository} to provide standard CRUD operations and
 * query capabilities for {@link AppraisalEntityDM} entities.
 * </p>
 *
 * <p>
 * This repository acts as the data access layer for appraisal entities,
 * allowing interaction with the underlying database without requiring
 * explicit implementation of common persistence operations.
 * </p>
 *
 * <p>
 * The repository uses {@link AppraisalEntityId} as the identifier type
 * for entity lookup and management.
 * </p>
 *
 * <p>
 * Additional query methods can be defined here following Spring Data JPA
 * naming conventions, if needed.
 * </p>
 */
public interface IAppraisalEntityRepositoryJPA extends JpaRepository<AppraisalEntityDM, AppraisalEntityId> {

}
