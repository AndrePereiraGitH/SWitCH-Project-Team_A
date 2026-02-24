package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.repository.PublicationSampleRepository;

public class US010ToRegisterNewPublicationCtrl {

    private final PublicationSampleRepository _publicationRepository;

    public US010ToRegisterNewPublicationCtrl(PublicationSampleRepository publicationRepository) {

        if (publicationRepository == null) {
            throw new IllegalArgumentException("PublicationRepository cannot be left blank");

        }

        this._publicationRepository = publicationRepository;
    }

    //User is part of Publication's attributes and any User can register a Publication (i.e., no need to be Admin)

    public boolean createAndAddPublication(
            BibliographicalData bibliographicalData1,
            User user1, PublicationStatus publicationStatus1, Edition _edition, Condition condition
    ) {

        return _publicationRepository.createAndAdd(bibliographicalData1,
                user1, publicationStatus1, _edition, condition);

    }

    //We are using boolean here, even though other repos use the class itself (like City and Author)

}
