package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.dto.DirectSaleDTO;
import TOPSECRET.repository.AuthorRepository;
import TOPSECRET.repository.DirectSaleRepository;
import TOPSECRET.dto.DirectSaleMapper;
import java.util.List;

public class ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController {

    private final AuthorRepository authorRepository;
    private final DirectSaleRepository directSaleRepository;
    private final DirectSaleMapper mapper;

    public ToGetTheListOfItemsOnDirectSaleOfAGivenAuthorController(
            AuthorRepository authorRepository,
            DirectSaleRepository directSaleRepository,
            DirectSaleMapper mapper) {

        this.authorRepository = authorRepository;
        this.directSaleRepository = directSaleRepository;
        this.mapper = mapper;
    }

    public List<Author> getAllAuthors() {
        return authorRepository.getAllAuthors();
    }

    public List<DirectSaleDTO> getListOfItemsOnDirectSale(Author author) {
        List<DirectSale> directSales =
                directSaleRepository.getListOfItemsOnDirectSale(author);

        return mapper.toDTO(directSales);
    }
}