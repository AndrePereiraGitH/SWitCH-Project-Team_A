package TOPSECRET.dto;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Publication;

import java.util.ArrayList;
import java.util.List;

public class PublicationMapper_US023 {

    public static PublicationDTO_US023 toDTO(Publication publication){
        String titleDTO = publication.getBibliographicalData().getTitle();
        List<Author> listOfAuthors = publication.getBibliographicalData().getAuthors();
        List<String> authorsDTO = new ArrayList<>();
        for(Author author: listOfAuthors ){
            authorsDTO.add(author.getProfile().getName());
        }

        String type = String.valueOf(publication.getPublicationType().getMediaFormat());

        String code = String.valueOf(publication.getCode());


        PublicationDTO_US023 pubDTO = new PublicationDTO_US023(titleDTO,authorsDTO,code,type);

        return pubDTO;
    }

    public static List<PublicationDTO_US023> getListOfPublicationsDTO(List<Publication> publications){
        List<PublicationDTO_US023> listOfPublicationsDTO = new ArrayList<>();
        for(Publication pub: publications){
            listOfPublicationsDTO.add(toDTO(pub));
        }

        return listOfPublicationsDTO;

    }
}


