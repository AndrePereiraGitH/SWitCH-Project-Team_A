package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.persistence.repositoryjpa.datamodel.CountryDM;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA interface for persisting {@link CountryDM}.
 */
public interface ICountryRepositoryJPA extends JpaRepository<CountryDM, CountryId> {
}