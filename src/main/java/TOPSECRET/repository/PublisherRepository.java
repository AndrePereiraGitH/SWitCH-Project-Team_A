package TOPSECRET.repository;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Publisher;

import java.util.ArrayList;
import java.util.List;

public class  PublisherRepository {

    private List<Publisher> publishers;

    public PublisherRepository() {
        publishers = new ArrayList<>();
    }

    public Publisher add(String name, String vatNumber, Contact contact) {

        Publisher publisher = new Publisher( name,  vatNumber,  contact);

        if (exists(publisher)) {
            return null;
        }

        publishers.add(publisher);
        return publisher;
    }

    private boolean exists(Publisher publisher) {
        for (Publisher p : publishers) {
            if (p.equals(publisher)) {
                return true;
            }
        }
        return false;
    }
}
