/*package TOPSECRET.controller;

import TOPSECRET.dto.DirectSaleDTO;
import TOPSECRET.dto.DirectSaleMapper;
import TOPSECRET.repository.AuthorRepository;

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

    public List<String> getAllAuthors() {
        return authorRepository.getAllAuthors();
    }

    public List<DirectSaleDTO> getListOfItemsOnDirectSale(String author) {
        var list = directSaleRepository.getListOfItemsOnDirectSale(author);
        return mapper.toDTO(list);
    }
}*/