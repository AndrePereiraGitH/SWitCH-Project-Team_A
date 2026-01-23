package TOPSECRET.controller;

import TOPSECRET.domain.Author;
import TOPSECRET.dto.AuthorDTO_US025;
import TOPSECRET.dto.AuthorMapper_US025;
import TOPSECRET.dto.DirectSaleDTO_US025;
import TOPSECRET.dto.DirectSaleMapper_US025;
import TOPSECRET.repository.AuthorRepository;
import TOPSECRET.repository.DirectSaleRepository;
import java.util.List;

public class ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController {

    private final AuthorRepository authorRepository;
    private final AuthorMapper_US025 amapper;
    private final DirectSaleRepository directSaleRepository;
    private final DirectSaleMapper_US025 dsmapper;

    public ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
            AuthorRepository authorRepository,
            AuthorMapper_US025 amapper,
            DirectSaleRepository directSaleRepository,
            DirectSaleMapper_US025 dsmapper) {
        this.authorRepository = authorRepository;
        this.directSaleRepository = directSaleRepository;
        this.amapper = amapper;
        this.dsmapper = dsmapper;
    }

    public List<AuthorDTO_US025> getAllAuthorNames() {
        return amapper.toDTO(authorRepository.getAllAuthors());
    }

    public List<DirectSaleDTO_US025> getListOfItemsOnDirectSale(Author author) {
        return dsmapper.toDTO(directSaleRepository.getListOfItemsOnDirectSale(author));
    }
}