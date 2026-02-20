package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;


import java.util.ArrayList;
import java.util.List;

public class PublicationSampleRepository {

    private List<PublicationSample> allPublicationsInSystem = new ArrayList<>();

    public PublicationSampleRepository(List<PublicationSample> publicationList) {
        allPublicationsInSystem = new ArrayList<>(publicationList);
    }

    public PublicationSampleRepository(){
        this.allPublicationsInSystem = new ArrayList<>(); //no-argument constructor so that it can compile (this way it's not restrictive)
    }

    public List<PublicationSample> getAllPublications() { //getter to obtain list of all publications in the system
        return List.copyOf(allPublicationsInSystem);
    }


//    public boolean createAndAdd(BibliographicalData bibliographicalData, PublicationType publicationType, User username,
//                                PublicationStatus publicationStatus, String edition, Code code){
    public boolean createAndAdd(BibliographicalData bibliographicalData, PublicationType publicationType, User username,
                            PublicationStatus publicationStatus, Edition edition, Condition condition){

        PublicationSample publication = new PublicationSample(bibliographicalData, username, publicationStatus,edition, condition);

        if(isExistent(publication)){
            return false;
        }
        allPublicationsInSystem.add(publication);
        return true;
    }


    public boolean isExistent(PublicationSample pub){
        for(PublicationSample publication: allPublicationsInSystem){
            if(pub.equals(publication)){
                return true;
            }
        }
        return false;
    }

    public List<PublicationSample> getListOfDifferentPublications(List<Publication> listOfPublicationsInLibrary){
        List<PublicationSample> listOfDifferentPublications = new ArrayList<>();

        for (PublicationSample publication : allPublicationsInSystem) {
            //If the user library does not contain the publication
            if (!listOfPublicationsInLibrary.contains(publication)){
                listOfDifferentPublications.add(publication);
            }
        }
        return List.copyOf(listOfDifferentPublications);

    }
    //Method for testing purposes only (AddPublicationToLibraryControllerTest)
    public void addPublication(PublicationSample p) {
        if (!allPublicationsInSystem.contains(p)) {
            allPublicationsInSystem.add(p);
        }
    }
}
