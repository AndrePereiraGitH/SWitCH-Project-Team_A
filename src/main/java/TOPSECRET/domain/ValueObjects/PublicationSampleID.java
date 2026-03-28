package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;

import java.util.Objects;

public class PublicationSampleID implements DomainID {

    private String pubSampleID;

    public PublicationSampleID(String pubSampleID) {
        if(pubSampleID == null || pubSampleID.isBlank()){
            throw new IllegalArgumentException("ID value should not be null or blank");
        }
        this.pubSampleID = pubSampleID;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PublicationSampleID that = (PublicationSampleID) o;
        return Objects.equals(pubSampleID, that.pubSampleID);
    }

}
