package TOPSECRET.controller;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.dto.PublicationTypeDTO_US014;
import TOPSECRET.dto.PublicationTypeMapper_US014;
import TOPSECRET.repository.PublicationTypeRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetPublicationTypeListControllerTest {

    @Test
    void getAllPublicationTypesWhenRepositoryIsEmptyReturnsEmptyList() {
        PublicationTypeRepository repository = new PublicationTypeRepository();
        PublicationTypeMapper_US014 mapper = new PublicationTypeMapper_US014();

        GetPublicationTypeListController controller =
                new GetPublicationTypeListController(repository, mapper);

        List<PublicationTypeDTO_US014> result = controller.getAllPublicationTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllPublicationTypesOnePublicationTypeReturnsOneDTO() {
        PublicationTypeRepository repository = new PublicationTypeRepository();
        PublicationTypeMapper_US014 mapper = new PublicationTypeMapper_US014();

        PublicationType type =
                repository.createPublicationType(
                        TypeOfBinding.HARDBACK,
                        "Hardback");

        repository.addToList(type);

        GetPublicationTypeListController controller =
                new GetPublicationTypeListController(repository, mapper);

        List<PublicationTypeDTO_US014> result =
                controller.getAllPublicationTypes();

        assertEquals(1, result.size());

        PublicationTypeDTO_US014 dto = result.get(0);
        assertEquals("HARDBACK", dto._typeOfBinding);
        assertEquals("Hardback", dto._details);
    }

    @Test
    void getAllPublicationTypesTwoPublicationTypesReturnsTwoDTOs() {
        PublicationTypeRepository repository = new PublicationTypeRepository();
        PublicationTypeMapper_US014 mapper = new PublicationTypeMapper_US014();

        PublicationType t1 =
                repository.createPublicationType(
                        TypeOfBinding.HARDBACK,
                        "Hardback");

        PublicationType t2 =
                repository.createPublicationType(
                        TypeOfBinding.PAPERBACK,
                        "Paperback");

        repository.addToList(t1);
        repository.addToList(t2);

        GetPublicationTypeListController controller =
                new GetPublicationTypeListController(repository, mapper);

        List<PublicationTypeDTO_US014> result =
                controller.getAllPublicationTypes();

        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(d -> d._details.equals("Hardback")));
        assertTrue(result.stream().anyMatch(d -> d._details.equals("Paperback")));
    }
}