package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.CityID;
import org.springframework.data.repository.CrudRepository;
import TOPSECRET.persistence.repositoryjpa.datamodel.CityDM;

/**
 * Spring Data JPA interface for persisting {@link CityDM} instances.
 *
 * <p>Extends {@link CrudRepository} to provide standard CRUD operations.
 * The Spring framework generates the implementation automatically at runtime.</p>
 *
 * <p>This interface operates exclusively with {@link CityDM} and is not
 * exposed to the domain layer.</p>
 */
public interface ICityRepositoryJPA extends CrudRepository<CityDM, CityID> {
}