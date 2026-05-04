package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.persistence.repositoryjpa.datamodel.CountryDM;
import org.springframework.data.repository.CrudRepository;

/**
 * Spring Data JPA interface for persisting {@link CountryDM}.
 */
public interface ICountryRepositoryJPA extends CrudRepository<CountryDM, CountryId> {
}