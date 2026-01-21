package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.PublicationStatus;


import java.util.ArrayList;
import java.util.List;

public class PublicationRepository {

    private List<Publication> _publicationList;


    public PublicationRepository(List<Publication> publicationList) {
        _publicationList = new ArrayList<>(publicationList);
    }


    public boolean createAndAdd(BibliographicalData bibliographicalData, PublicationType publicationType, User username,
                                PublicationStatus publicationStatus, String edition, Code code){

        Publication publication = new Publication(bibliographicalData,publicationType,username,publicationStatus,edition,code);

        if(isExistent(publication)){
            return false;
        }
        _publicationList.add(publication);
        return true;
    }


    public boolean isExistent(Publication pub){
        for(Publication publication: _publicationList){
            if(pub.equals(publication)){
                return true;
            }
        }
        return false;
    }

}
