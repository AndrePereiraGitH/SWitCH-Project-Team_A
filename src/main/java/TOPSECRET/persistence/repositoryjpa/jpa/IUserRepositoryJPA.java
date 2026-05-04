package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.datamodel.UserDM;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepositoryJPA extends JpaRepository<UserDM, UserId> {
    Optional<UserDM> findByUserName(String userName);
}
