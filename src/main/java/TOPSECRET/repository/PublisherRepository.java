package TOPSECRET.repository;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Publisher;

import java.util.ArrayList;
import  java.util.List;

public class   PublisherRepository {

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

    public List<Publisher> getAllPublishers() {
        return List.copyOf(publishers); // safe read-only copy
    }

    public Publisher findByVatNumber(String vatNumber) {
        for (Publisher p : publishers) {
            if (p.getVatNumber().equals(vatNumber)) {
                return p;
            }
        }
        return null; // or throw, depending on your style
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
