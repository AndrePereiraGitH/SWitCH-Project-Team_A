package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.repository.PublicationRepository;

public class US010ToRegisterNewPublicationCtrl {

    private final PublicationRepository _publicationRepository;

    public US010ToRegisterNewPublicationCtrl(PublicationRepository publicationRepository) {

        if (publicationRepository == null) {
            throw new IllegalArgumentException("PublicationRepository cannot be left blank");

        }

        this._publicationRepository = publicationRepository;
    }

    //User is part of Publication's attributes and any User can register a Publication (i.e., no need to be Admin)

    public boolean createAndAddPublication(
            BibliographicalData bibliographicalData1, PublicationType publicationType1,
            User user1, PublicationStatus publicationStatus1, String _edition, Code _code
    ) {

        return _publicationRepository.createAndAdd(bibliographicalData1, publicationType1,
                user1, publicationStatus1, _edition, _code);

    }

    //We are using boolean here, even though other repos use the class itself (like City and Author)

}
