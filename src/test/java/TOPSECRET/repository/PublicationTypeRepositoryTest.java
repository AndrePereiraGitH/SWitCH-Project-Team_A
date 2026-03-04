package TOPSECRET.repository;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.FactoryPublicationType;
import TOPSECRET.domain.PublicationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicationTypeRepositoryTest {

    FactoryPublicationType factory;
    PublicationTypeRepository repo;

    @BeforeEach
    void setup()
    {
        factory = mock(FactoryPublicationType.class);
        repo = new PublicationTypeRepository(factory);
    }

    @Test
    void createPublicationTypeDelegatesToFactory()
    {
        MediaFormat mediaFormat = MediaFormat.BOOK;
        TypeOfBinding typeOfBinding = TypeOfBinding.HARDBACK;
        String details = "detail test";

        PublicationType expected = mock(PublicationType.class);

        when(factory.create(mediaFormat, typeOfBinding, details)).thenReturn(expected);

        PublicationType actual = repo.create(mediaFormat, typeOfBinding, details);

        assertEquals(expected, actual);
    }

    @Test
    void invalidPublicationShouldThrowWhenCreating()
    {
        MediaFormat mediaFormat = MediaFormat.BOOK;
        TypeOfBinding typeOfBinding = TypeOfBinding.HARDBACK;
        String details = null;

        when(factory.create(mediaFormat, typeOfBinding, details)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> repo.create(mediaFormat, typeOfBinding, details));
        verify(factory, times(1)).create(mediaFormat, typeOfBinding, details);
        verifyNoMoreInteractions(factory);
    }

}