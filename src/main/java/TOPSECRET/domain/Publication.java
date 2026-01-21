package TOPSECRET.domain;

import TOPSECRET.domain.Enum.PublicationStatus;

import java.util.Objects;

public class Publication {

    private BibliographicalData _bibliographicalData;

    private PublicationType _publicationType;

    private User _userName;

    private PublicationStatus _publicationStatus;

    private String _edition;

    private Code _code;


    public Publication(BibliographicalData bibliographicalData, PublicationType publicationType, User userName, PublicationStatus publicationStatus,
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


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Publication that = (Publication) o;
        return Objects.equals(_bibliographicalData, that._bibliographicalData) && Objects.equals(_publicationType, that._publicationType) && Objects.equals(_userName, that._userName) && _publicationStatus == that._publicationStatus && Objects.equals(_edition, that._edition) && Objects.equals(_code, that._code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_bibliographicalData, _publicationType, _userName, _publicationStatus, _edition, _code);
    }
}