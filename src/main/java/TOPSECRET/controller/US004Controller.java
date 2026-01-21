package TOPSECRET.controller;

import TOPSECRET.domain.AppraisalEntity;
import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.repository.AppraisalEntityRepository;

// The controller acts as the entry point to the system.
// It receives data from the user interface and delegates the creation and registration of AppraisalEntity objects to the repository.
// It does not contain business rules or persistence logic.

public class US004Controller {

    private AppraisalEntityRepository _appraisalEntityRepository;

    //  Constructor not needed
      public US004Controller () {
          this._appraisalEntityRepository = new AppraisalEntityRepository();
      }

    // Registers a new AppraisalEntity in the system
    // This method acts as the entry point between the UI and the repository
    public AppraisalEntity registerAppraisalEntity (String name, Contact contact, PublicationType publicationType, Genre genre) {

        return _appraisalEntityRepository.createAndAddToAppraisalEntityList(name, contact, publicationType, genre);
    }

}
