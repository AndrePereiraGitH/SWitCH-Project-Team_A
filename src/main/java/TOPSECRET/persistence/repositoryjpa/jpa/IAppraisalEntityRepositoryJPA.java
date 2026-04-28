package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.AppraisalEntityId;
import TOPSECRET.persistence.repositoryjpa.datamodel.AppraisalEntityDM;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IAppraisalEntityRepositoryJPA extends JpaRepository<AppraisalEntityDM, AppraisalEntityId> {

}
