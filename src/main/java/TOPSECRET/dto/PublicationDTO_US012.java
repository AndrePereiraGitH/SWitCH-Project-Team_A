package TOPSECRET.dto;

import java.util.List;

public class PublicationDTO_US012 {
    public String pubTitle;
    public List<String> authorName;
    public String code;
    public String publicationType;

    public PublicationDTO_US012(String pubTitle, List<String> authorName, String code, String publicationType) {
        this.pubTitle = pubTitle;
        this.authorName = authorName;
        this.code = code;
        this.publicationType = publicationType;
    }


}
