package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType.PublicationTypeFactory;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.dto.US014PublicationTypeDTO;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ToGetPublicationTypeListControllerTest {

    @Test
    void findAllWhenRepositoryIsEmptyReturnsEmptyList() {

        // Arrange
        IPublicationTypeRepository repository = mock(IPublicationTypeRepository.class);

        when(repository.findAll()).thenReturn(List.of());

        ToGetPublicationTypeListController controller =
                new ToGetPublicationTypeListController(repository);

        // Act
        List<US014PublicationTypeDTO> result = controller.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

    }

    @Test
    void findAllPublicationTypesOnePublicationTypeReturnsOneDTO() {

        // Arrange
        IPublicationTypeRepository repository = mock(IPublicationTypeRepository.class);

        PublicationType typeDouble = mock(PublicationType.class);
        PublicationTypeId idDouble = mock(PublicationTypeId.class);

        when(repository.findAll()).thenReturn(List.of(typeDouble));
        when(typeDouble.id()).thenReturn(idDouble);
        when(idDouble.getId()).thenReturn("PT1");
        when(typeDouble.getMediaFormat()).thenReturn("Book");

        ToGetPublicationTypeListController controller =
                new ToGetPublicationTypeListController(repository);

        // Act
        List<US014PublicationTypeDTO> result =
                controller.findAll();

        // Assert
        assertEquals(1, result.size());

        US014PublicationTypeDTO dto = result.get(0);
        assertEquals("PT1", dto.id());
        assertEquals("Book", dto.mediaFormat());

    }

    @Test
    void findAllPublicationTypesMultipleReturnsListDTOs() {

        // Arrange
        IPublicationTypeRepository repository = mock(IPublicationTypeRepository.class);

        PublicationType t1Double = mock(PublicationType.class);
        PublicationType t2Double = mock(PublicationType.class);

        PublicationTypeId idDouble = mock(PublicationTypeId.class);
        PublicationTypeId idDouble2 = mock(PublicationTypeId.class);

        when(repository.findAll()).thenReturn(List.of(t1Double, t2Double));
        when(t1Double.id()).thenReturn(idDouble);
        when(t2Double.id()).thenReturn(idDouble2);
        when(idDouble.getId()).thenReturn("PT1");
        when(idDouble2.getId()).thenReturn("PT2");
        when(t1Double.getMediaFormat()).thenReturn("Book");
        when(t2Double.getMediaFormat()).thenReturn("Magazine");

        ToGetPublicationTypeListController controller =
                new ToGetPublicationTypeListController(repository);

        // Act
        List<US014PublicationTypeDTO> result =
                controller.findAll();

        // Assert
        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(d -> d.mediaFormat().equals("Book")));
        assertTrue(result.stream().anyMatch(d -> d.mediaFormat().equals("Magazine")));
    }


}
