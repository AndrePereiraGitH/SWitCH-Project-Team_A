package TOPSECRET.repository;

import TOPSECRET.domain.BibliographicalData;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for storing and providing access to {@link BibliographicalData}.
 *
 * <p>
 * This repository maintains an internal list of bibliographical records and offers
 * read‑only access to the stored publications. The internal collection is defensively
 * copied to ensure that external modifications do not affect the repository state.
 * </p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *     <li>Store an initial collection of {@code BibliographicalData} objects</li>
 *     <li>Provide read‑only access to all stored publications</li>
 * </ul>
 *
 * <h2>Immutability</h2>
 * <p>
 * The repository does not expose its internal list directly. Methods returning collections
 * always provide unmodifiable copies to preserve encapsulation and prevent unintended
 * external changes.
 * </p>
 */

public class BibliographicalDataRepository {

    private final List<BibliographicalData> _bdList;

    // Constructor -----------------------------------------------------------------------------------------------------

    /**
     * Creates a new repository with an initial collection of bibliographical records.
     *
     * <p>
     * The provided list is defensively copied to guarantee that subsequent modifications
     * to the original list do not affect the repository.
     * </p>
     *
     * @param bdList the initial list of {@link BibliographicalData} to store;
     *               must not be {@code null}
     * @throws NullPointerException if {@code bdList} is {@code null}
     */

    public BibliographicalDataRepository(List<BibliographicalData> bdList) {
        _bdList = new ArrayList<>(bdList);          // throws automatically NullPointerException if bdList = null
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Returns an unmodifiable view of all stored bibliographical publications.
     *
     * <p>
     * The returned list is a read‑only copy, ensuring that callers cannot modify
     * the internal repository state.
     * </p>
     *
     * @return an unmodifiable list containing all {@link BibliographicalData} records
     */

    // Get all publications --------------------------------------------------------------------------------------------
    public List<BibliographicalData> getAllPublicationsBD() {
        return List.copyOf(_bdList);
    }
}


