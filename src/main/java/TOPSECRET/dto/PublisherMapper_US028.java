package TOPSECRET.dto;

import TOPSECRET.domain.Publisher;

import java.util.ArrayList;
import java.util.List;

public class PublisherMapper_US028 {
    // Create a list of PublisherDTO from a List of domain object
    public List<PublisherDTO_US028> toDTO(List<Publisher> publishers) {
        // Creates an empty DTO List
        List<PublisherDTO_US028> result = new ArrayList<>();
        // Iterates over all Publisher domain
        for (Publisher p : publishers) {
            // adds new PublisherDTO to the DTO List
            result.add(new PublisherDTO_US028(p));
        }
        // gives final DTO List
        return result;
    }
}
