package TOPSECRET.dto.publisher;

import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.publisher.request.RequestPublisherDTO;
import TOPSECRET.dto.publisher.response.ResponsePublisherDTO;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublisherMapper}.
 */
class PublisherMapperTest {

    @Test
    void shouldThrowExceptionWhenInstantiatingUtilityClass() throws Exception {

        // ARRANGE
        Constructor<PublisherMapper> constructor =
                PublisherMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // ACT
        InvocationTargetException ex =
                assertThrows(InvocationTargetException.class, constructor::newInstance);

        // ASSERT
        assertInstanceOf(UnsupportedOperationException.class, ex.getCause());
        assertEquals("Utility class", ex.getCause().getMessage());
    }

    @Test
    void shouldConvertPublisherToDTO() {

        // ARRANGE
        Publisher publisher = mock(Publisher.class);
        PublisherID publisherID = mock(PublisherID.class);

        when(publisher.getName()).thenReturn("Test Publisher");
        when(publisher.id()).thenReturn(publisherID);
        when(publisherID.toString()).thenReturn("PUB-1");

        // ACT
        ResponsePublisherDTO result = PublisherMapper.toDTO(publisher);

        // ASSERT
        assertEquals("Test Publisher", result.getName());
        assertEquals("PUB-1", result.getPublisherId());
    }

    @Test
    void shouldThrowExceptionWhenPublisherIsNull() {

        // ACT + ASSERT
        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () -> PublisherMapper.toDTO(null)
                );

        assertEquals("Publisher cannot be null", exception.getMessage());
    }

    @Test
    void shouldConvertListOfPublishersToDTOList() {

        // ARRANGE
        Publisher p1 = mock(Publisher.class);
        Publisher p2 = mock(Publisher.class);

        PublisherID id1 = mock(PublisherID.class);
        PublisherID id2 = mock(PublisherID.class);

        when(p1.getName()).thenReturn("Publisher One");
        when(p2.getName()).thenReturn("Publisher Two");

        when(p1.id()).thenReturn(id1);
        when(p2.id()).thenReturn(id2);

        when(id1.toString()).thenReturn("PUB-1");
        when(id2.toString()).thenReturn("PUB-2");

        List<Publisher> publishers = List.of(p1, p2);

        // ACT
        List<ResponsePublisherDTO> result =
                PublisherMapper.toDTOList(publishers);

        // ASSERT
        assertEquals(2, result.size());
        assertEquals("Publisher One", result.get(0).getName());
        assertEquals("Publisher Two", result.get(1).getName());
    }

    @Test
    void shouldThrowExceptionWhenListOfPublishersIsNull() {

        // ACT + ASSERT
        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () -> PublisherMapper.toDTOList(null)
                );

        assertEquals("Publishers list cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenListContainsNullPublisher() {

        // ARRANGE
        Publisher validPublisher = mock(Publisher.class);
        PublisherID id = mock(PublisherID.class);

        when(validPublisher.getName()).thenReturn("Test");
        when(validPublisher.id()).thenReturn(id);
        when(id.toString()).thenReturn("PUB-1");

        List<Publisher> publishers = Arrays.asList(validPublisher, null);

        // ACT + ASSERT
        NullPointerException exception =
                assertThrows(
                        NullPointerException.class,
                        () -> PublisherMapper.toDTOList(publishers)
                );

        assertEquals("Publisher cannot be null", exception.getMessage());
    }

    @Test
    void shouldReturnImmutableList() {

        // ARRANGE
        Publisher publisher = mock(Publisher.class);
        PublisherID id = mock(PublisherID.class);

        when(publisher.getName()).thenReturn("Publisher");
        when(publisher.id()).thenReturn(id);
        when(id.toString()).thenReturn("PUB-1");

        // ACT
        List<ResponsePublisherDTO> result =
                PublisherMapper.toDTOList(List.of(publisher));

        // ASSERT
        assertThrows(
                UnsupportedOperationException.class,
                () -> result.add(mock(ResponsePublisherDTO.class))
        );
    }

    @Test
    void shouldThrowNullPointerExceptionWhenDTOIsNullInToContact() {

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> PublisherMapper.toContact(null)
        );
    }

    @Test
    void shouldConvertDTOToContact() {

        // ARRANGE
        RequestPublisherDTO pubDTO = mock(RequestPublisherDTO.class);

        when(pubDTO.street()).thenReturn("Main St");
        when(pubDTO.number()).thenReturn("42");
        when(pubDTO.zipCode()).thenReturn("12345");
        when(pubDTO.cityId()).thenReturn("CITY-1");
        when(pubDTO.phoneNumber()).thenReturn("912345678");
        when(pubDTO.extension()).thenReturn("101");
        when(pubDTO.email()).thenReturn("test@example.com");

        // ACT
        Contact result = PublisherMapper.toContact(pubDTO);

        // ASSERT
        assertNotNull(result);
    }

    @Test
    void shouldThrowWhenCityIdIsNull() {

        // ARRANGE
        RequestPublisherDTO pubDTO = mock(RequestPublisherDTO.class);

        when(pubDTO.cityId()).thenReturn(null);

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> PublisherMapper.toContact(pubDTO)
        );
    }

    @Test
    void shouldThrowWhenEmailIsNull() {

        // ARRANGE
        RequestPublisherDTO pubDTO = mock(RequestPublisherDTO.class);

        when(pubDTO.street()).thenReturn("Main St");
        when(pubDTO.number()).thenReturn("42");
        when(pubDTO.zipCode()).thenReturn("12345");
        when(pubDTO.cityId()).thenReturn("CITY-1");
        when(pubDTO.phoneNumber()).thenReturn("912345678");
        when(pubDTO.extension()).thenReturn("101");
        when(pubDTO.email()).thenReturn(null);

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> PublisherMapper.toContact(pubDTO)
        );
    }

    @Test
    void shouldThrowWhenEmailHasNoAtSign() {

        // ARRANGE
        RequestPublisherDTO pubDTO = mock(RequestPublisherDTO.class);

        when(pubDTO.street()).thenReturn("Main St");
        when(pubDTO.number()).thenReturn("42");
        when(pubDTO.zipCode()).thenReturn("12345");
        when(pubDTO.cityId()).thenReturn("CITY-1");
        when(pubDTO.phoneNumber()).thenReturn("912345678");
        when(pubDTO.extension()).thenReturn("101");
        when(pubDTO.email()).thenReturn("invalidemail");

        // ACT + ASSERT
        assertThrows(
                ArrayIndexOutOfBoundsException.class,
                () -> PublisherMapper.toContact(pubDTO)
        );
    }

    @Test
    void toPublisherId_shouldReturnPublisherID_whenValidString() {

        // ARRANGE
        String raw = "pub-123";

        // ACT
        PublisherID result = PublisherMapper.toPublisherId(raw);

        // ASSERT
        assertNotNull(result);
        assertEquals(raw, result.getPublisherId());
    }

    @Test
    void toPublisherId_shouldThrow_whenNull() {

        assertThrows(Exception.class, () -> PublisherMapper.toPublisherId(null));
    }

    @Test
    void toPublisherId_shouldThrow_whenBlank() {

        assertThrows(Exception.class, () -> PublisherMapper.toPublisherId("  "));
    }

    @Test
    void toUserId_shouldReturnUserId_whenValidString() {

        // ARRANGE
        String raw = "user-456";

        // ACT
        UserId result = PublisherMapper.toUserId(raw);

        // ASSERT
        assertNotNull(result);
        assertEquals(raw, result.getUserId());
    }

    @Test
    void toUserId_shouldThrow_whenNull() {

        assertThrows(Exception.class, () -> PublisherMapper.toUserId(null));
    }

    @Test
    void toUserId_shouldThrow_whenBlank() {

        assertThrows(Exception.class, () -> PublisherMapper.toUserId("  "));
    }
}