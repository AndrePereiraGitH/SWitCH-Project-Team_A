package TOPSECRET.dto;

import java.util.List;

public class PublicationDetailsDTO {

    public String pubTitle;
    public List<String> authorName;
    public String code;
    public String publicationType;

    public PublicationDetailsDTO(String pubTitle, List<String> authorName, String code, String publicationType) {
        this.pubTitle = pubTitle;
        this.authorName = authorName;
        this.code = code;
        this.publicationType = publicationType;
    }
}
