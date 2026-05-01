package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.persistence.repositoryjpa.datamodel.PublicationTypeDM;
import org.springframework.data.jpa.repository.JpaRepository;


/**

 * JPA repository interface for PublicationType persistence.
 *
 * <p>
 * Extends JpaRepository to provide basic CRUD operations.
 * </p>
 */
public interface IPublicationTypeRepositoryJPA extends JpaRepository<PublicationTypeDM, PublicationTypeId> {
}
