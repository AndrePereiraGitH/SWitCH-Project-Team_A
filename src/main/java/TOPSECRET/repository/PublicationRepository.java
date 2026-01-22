package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.PublicationStatus;


import java.util.ArrayList;
import java.util.List;

public class PublicationRepository {

    private List<Publication> allPublicationsInSystem = new ArrayList<>();

    public PublicationRepository(List<Publication> publicationList) {
        allPublicationsInSystem = new ArrayList<>(publicationList);
    }

    public PublicationRepository(){
        this.allPublicationsInSystem = new ArrayList<>(); //no-argument constructor so that it can compile (this way it's not restrictive)
    }

    public List<Publication> getAllPublications() { //getter to obtain list of all publications in the system
        return List.copyOf(allPublicationsInSystem);
    }


    public boolean createAndAdd(BibliographicalData bibliographicalData, PublicationType publicationType, User username,
                                PublicationStatus publicationStatus, String edition, Code code){

        Publication publication = new Publication(bibliographicalData,publicationType,username,publicationStatus,edition,code);

        if(isExistent(publication)){
            return false;
        }
        allPublicationsInSystem.add(publication);
        return true;
    }


    public boolean isExistent(Publication pub){
        for(Publication publication: allPublicationsInSystem){
            if(pub.equals(publication)){
                return true;
            }
        }
        return false;
    }

    public List<Publication> getListOfDifferentPublications(List<Publication> listOfPublicationsInLibrary){
        List<Publication> listOfDifferentPublications = new ArrayList<>();

        for (Publication publication : allPublicationsInSystem) {
            //If the user library does not contain the publication
            if (!listOfPublicationsInLibrary.contains(publication)){
                listOfDifferentPublications.add(publication);
            }
        }
        return List.copyOf(listOfDifferentPublications);

    }
    //Method for testing purposes only (AddPublicationToLibraryControllerTest)
    public void addPublication(Publication p) {
        if (!allPublicationsInSystem.contains(p)) {
            allPublicationsInSystem.add(p);
        }
    }
}
