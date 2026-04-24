package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType.PublicationTypeFactory;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.dto.US014PublicationTypeDTO;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToGetPublicationTypeListControllerTest {

    @Test
    void getAllPublicationTypesWhenRepositoryIsEmptyReturnsEmptyList() {

        // Arrange
        IPublicationTypeRepository repository = mock(IPublicationTypeRepository.class);

        when(repository.getAllPublicationTypes()).thenReturn(List.of());

        ToGetPublicationTypeListController controller =
                new ToGetPublicationTypeListController(repository);

        // Act
        List<US014PublicationTypeDTO> result = controller.getAllPublicationTypes();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

    }

    @Test
    void getAllPublicationTypesOnePublicationTypeReturnsOneDTO() {

        // Arrange
        IPublicationTypeRepository repository = mock(IPublicationTypeRepository.class);

        PublicationTypeFactory factory = new PublicationTypeFactory();

        PublicationType type =
                factory.create(
                        "Book");

        when(repository.getAllPublicationTypes())
                .thenReturn(List.of(type));

        ToGetPublicationTypeListController controller =
                new ToGetPublicationTypeListController(repository);

        // Act
        List<US014PublicationTypeDTO> result =
                controller.getAllPublicationTypes();

        // Assert
        assertEquals(1, result.size());

        US014PublicationTypeDTO dto = result.get(0);

        assertEquals("Book", dto.mediaFormat());

    }

    @Test
    void getAllPublicationTypesMultiplePublicationTypesReturnsListDTOs() {

        // Arrange
        IPublicationTypeRepository repository = mock(IPublicationTypeRepository.class);

        PublicationTypeFactory factory = new PublicationTypeFactory();

        PublicationType t1 =
                factory.create(
                        "Book");

        PublicationType t2 =
                factory.create(
                        "Magazine");

        when(repository.getAllPublicationTypes())
                .thenReturn(List.of(t1, t2));

        ToGetPublicationTypeListController controller =
                new ToGetPublicationTypeListController(repository);

        // Act
        List<US014PublicationTypeDTO> result =
                controller.getAllPublicationTypes();

        // Assert
        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(d -> d.mediaFormat().equals("Book")));
        assertTrue(result.stream().anyMatch(d -> d.mediaFormat().equals("Magazine")));
    }


}
