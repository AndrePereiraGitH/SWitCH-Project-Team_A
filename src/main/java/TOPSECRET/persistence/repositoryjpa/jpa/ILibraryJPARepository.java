package TOPSECRET.persistence.repositoryjpa.jpa;


import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.datamodel.LibraryDM;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ILibraryJPARepository extends JpaRepository<LibraryDM, LibraryId> {
    Optional<LibraryDM> findByUserId(UserId userId);

}

