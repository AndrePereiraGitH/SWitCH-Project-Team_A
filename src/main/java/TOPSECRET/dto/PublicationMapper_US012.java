package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Publication;


import java.util.ArrayList;
import java.util.List;

public class PublicationMapper_US012 {

    public static PublicationDTO_US012 toDTO(Publication publication){
        String titleDTO = publication.getBibliographicalData().getTitle();
        List<Author> listOfAuthors = publication.getBibliographicalData().getAuthors();
        List<String> authorsDTO = new ArrayList<>();
        for(Author author: listOfAuthors ){
            authorsDTO.add(author.getProfile().getName());
        }

        String typeDTO = String.valueOf(publication.getPublicationType().getMediaFormat());

        String codeDTO = String.valueOf(publication.getCode());


        PublicationDTO_US012 pubDTO = new PublicationDTO_US012(titleDTO,authorsDTO, codeDTO, typeDTO);

        return pubDTO;
    }

    public static List<PublicationDTO_US012> getListOfPublicationsDTO(List<Publication> publications){
        List<PublicationDTO_US012> listOfPublicationsDTO = new ArrayList<>();
        for(Publication pub: publications){
            PublicationDTO_US012 pubDTO = toDTO(pub);
            listOfPublicationsDTO.add(pubDTO);
        }

        return listOfPublicationsDTO;

    }




}


