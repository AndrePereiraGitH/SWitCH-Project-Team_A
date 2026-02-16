package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Publication;


import java.util.ArrayList;
import java.util.List;

public class PublicationDetailsMapper {

    public static List<PublicationDetailsDTO> toDTOs(List<Publication> publications){
        List<PublicationDetailsDTO> listOfPublicationsDTO = new ArrayList<>();

        for(Publication pub: publications){
            PublicationDetailsDTO pubDTO = toDTO(pub);
            listOfPublicationsDTO.add(pubDTO);
        }

        return listOfPublicationsDTO;

    }

    public static PublicationDetailsDTO toDTO(Publication publication){
        String titleDTO = publication.getBibliographicalData().getTitle();
        List<Author> listOfAuthors = publication.getBibliographicalData().getAuthors();
        List<String> authorsDTO = new ArrayList<>();

        for(Author author: listOfAuthors ){
            authorsDTO.add(author.getProfile().getName());
        }

        String typeDTO = String.valueOf(publication.getPublicationType().getTypeOfBinding());

        String codeDTO = String.valueOf(publication.getCode());


        PublicationDetailsDTO pubDTO = new PublicationDetailsDTO(titleDTO,authorsDTO, codeDTO, typeDTO);

        return pubDTO;
    }


}


