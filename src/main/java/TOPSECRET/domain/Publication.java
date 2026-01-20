package TOPSECRET.domain;

import TOPSECRET.domain.Enum.PublicationStatus;

public class Publication {

    private BibliographicalData _bibliographicalData;

    private PublicationType _publicationType;

    private User _userName;

    private PublicationStatus _publicationStatus;

    private String _edition;

    private Code _code;


    public Publication(BibliographicalData bibliographicalData, String publicationType, User userName, PublicationStatus publicationStatus,
                       String edition, Code code) {

        if (bibliographicalData == null) {
            throw new IllegalArgumentException("BibliographicalData must not be left blank");
        }
        if (publicationType == null) {
            throw new IllegalArgumentException("PublicationType must not be left blank");
        }
        if (userName == null) {
            throw new IllegalArgumentException("UserName must not be left blank");
        }
        if (publicationStatus == null) {
            throw new IllegalArgumentException("PublicationStatus must not be left blank");
        }

        //We are allowing for Edition and Code to potentially be left blank

    }

    public String getEdition() {
        return _edition;
    }

}