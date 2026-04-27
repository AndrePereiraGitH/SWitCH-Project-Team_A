package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.persistence.repositoryjpa.datamodel.AuthorDM;
import org.springframework.data.repository.CrudRepository;

public interface IAuthorRepositoryJPA extends CrudRepository<AuthorDM, AuthorId> {



}
