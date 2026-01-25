package TOPSECRET.dto;

import TOPSECRET.domain.Publisher;

public class PublisherDTO_US028 {

    public String name;

    public PublisherDTO_US028(Publisher publisher) {

        this.name = publisher.getName();
    }
}
