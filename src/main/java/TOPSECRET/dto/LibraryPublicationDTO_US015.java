package TOPSECRET.dto;

import java.util.List;

public class LibraryPublicationDTO_US015 {
    public final String pubTitle;
    public final List<String> authorName;
    public final String code;
    public final String publicationType;

    public LibraryPublicationDTO_US015(String pubTitle, List<String> authorName, String code, String publicationType) {
        this.pubTitle = pubTitle;
        this.authorName = authorName;
        this.code = code;
        this.publicationType = publicationType;
    }

    @Override
    public String toString() {
        return "PublicationDTO_US015 {" +
                "pubTitle='" + pubTitle + '\'' +
                ", authorName='" + authorName + '\'' +
                ", code='" + code + '\'' +
                ", PublicationType='" + publicationType + '\'' +
                '}';
    }
}
