package TOPSECRET.controller;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.User;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;

import javax.management.InstanceAlreadyExistsException;

public class US010ToRegisterNewPublicationCtrl {

    private final IPublicationSampleRepository _publicationRepository;

    public US010ToRegisterNewPublicationCtrl(IPublicationSampleRepository publicationRepository) {

        if (publicationRepository == null) {
            throw new IllegalArgumentException("PublicationRepository cannot be left blank");

        }

        this._publicationRepository = publicationRepository;
    }

    //User is part of Publication's attributes and any User can register a Publication (i.e., no need to be Admin)

//    public PublicationSample createAndAddPublication(
//            BibliographicalData bibliographicalData1,
//            User user1, PublicationStatus publicationStatus1, Edition _edition, Condition condition
//
//    ) throws InstanceAlreadyExistsException {
//
//        return _publicationRepository.createAndAdd(bibliographicalData1,
//                user1, publicationStatus1, _edition, condition);
//
//    }

}
