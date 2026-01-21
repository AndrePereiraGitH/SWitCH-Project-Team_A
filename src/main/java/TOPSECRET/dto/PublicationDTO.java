package TOPSECRET.dto;

import java.util.List;

public class PublicationDTO {
    public String pubTitle;
    public List<String> authorName;
    public String code;
    public String publicationType;

    public PublicationDTO(String pubTitle, List<String> authorName, String code, String publicationType) {
        this.pubTitle = pubTitle;
        this.authorName = authorName;
        this.code = code;
        this.publicationType = publicationType;
    }

    @Override
    public String toString() {
        return "PublicationDTO{" +
                "pubTitle='" + pubTitle + '\'' +
                ", authorName='" + authorName + '\'' +
                ", code='" + code + '\'' +
                ", PublicationType='" + publicationType + '\'' +
                '}';
    }

}
