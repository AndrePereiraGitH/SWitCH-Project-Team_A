package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.PublicationTypeRestController;
import TOPSECRET.domain.ValueObjects.UserId;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Provides HATEOAS links related to PublicationType resources.
 */
@Component
public class PublicationTypeLinkProvider {

    /**
     * Returns the available links for PublicationType operations.
     *
     * @param userId user performing the operation
     * @return list of HATEOAS links
     */
    public List<Link> getLinksForPublicationType(UserId userId) {

        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(PublicationTypeRestController.class)
                .createPublicationType(null, userId))
                .withRel("create-publication-type"));

        return links;
    }
}