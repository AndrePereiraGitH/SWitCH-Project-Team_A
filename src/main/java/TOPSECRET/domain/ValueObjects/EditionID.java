package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;

import java.util.Objects;

public class EditionID implements DomainID {

    private final String editionID;

    public EditionID(String editionID){
        if(editionID == null || editionID.isBlank()){
            throw new IllegalArgumentException("ID cannot be null or blank");
        }

        this.editionID = editionID;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        EditionID that = (EditionID) o;
        return Objects.equals(editionID, that.editionID);
    }

    public String getID(){return editionID;}


}
