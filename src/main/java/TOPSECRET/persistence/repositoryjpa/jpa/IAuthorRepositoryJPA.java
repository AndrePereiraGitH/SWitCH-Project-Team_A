package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.persistence.repositoryjpa.datamodel.AuthorDM;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuthorRepositoryJPA extends JpaRepository<AuthorDM, AuthorId> {



}
