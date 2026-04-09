package TOPSECRET.dto;

public record US014PublicationTypeDTO(String id, String mediaFormat) {

    public US014PublicationTypeDTO{

        if(id == null){
            throw new IllegalArgumentException("Id cannot be null");
        }
        if(mediaFormat == null){
            throw new IllegalArgumentException("Media Format cannot be null");
        }
    }


}
