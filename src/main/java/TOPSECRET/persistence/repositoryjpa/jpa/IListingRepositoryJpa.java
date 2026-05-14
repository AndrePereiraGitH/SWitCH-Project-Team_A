package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.persistence.repositoryjpa.datamodel.ListingDM;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Spring Data JPA repository for {@link ListingDM} persistence.
 *
 * <p>Extends {@link JpaRepository} to inherit standard CRUD and pagination
 * operations keyed by {@link ListingId}. Custom query methods are declared
 * here and resolved automatically by Spring Data at runtime.</p>
 */
public interface IListingRepositoryJpa extends JpaRepository<ListingDM, ListingId> {

    /**
     * Retrieves all {@link ListingDM} instances whose items collection contains
     * at least one of the given {@link PublicationSampleId}s.
     *
     * @param itemsId the list of publication sample identifiers to match against;
     *                must not be {@code null}
     * @return a list of matching {@link ListingDM}s; never {@code null}, may be empty
     */
    List<ListingDM> findAllByItemsId(List<PublicationSampleId> itemsId);

}
