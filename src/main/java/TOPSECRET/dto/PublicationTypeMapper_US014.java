package TOPSECRET.dto;

import TOPSECRET.domain.PublicationType;

import java.util.ArrayList;
import java.util.List;

public class PublicationTypeMapper_US014 {

    // Cria um DTO de um único Objeto
    public PublicationTypeDTO_US014 toDTO(PublicationType publicationType) {

        return new PublicationTypeDTO_US014(publicationType);
    }

    // Cria um DTO de uma lista
    public List<PublicationTypeDTO_US014> toDTO(List<PublicationType> publicationTypes) {
        // Cria uma nova lista Vazia

        List<PublicationTypeDTO_US014> listOfPublicationTypeDTO_US014 = new ArrayList<>();

        // percorre todos os objetos da class
        // pT é uma variável temporária que aponta para class PublicationType

        for (PublicationType pT : publicationTypes) {
            // result.add vai adicionar o DTO à lista, assim só contém DTO
            // cria um novo DTO, passa a domain class e o construtor DTO extrai os dados

            listOfPublicationTypeDTO_US014.add(toDTO(pT));
        }

        return listOfPublicationTypeDTO_US014;
    }

}
