package TOPSECRET.dto.publisher;

import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.PublisherID;
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

            Constructor<PublisherMapper> constructor =
                    PublisherMapper.class.getDeclaredConstructor();

            constructor.setAccessible(true);

            InvocationTargetException ex =
                    assertThrows(InvocationTargetException.class, constructor::newInstance);

            assertInstanceOf(UnsupportedOperationException.class, ex.getCause());
            assertEquals("Utility class", ex.getCause().getMessage());
        }

        @Test
        void shouldConvertPublisherToDTO() {

            Publisher publisher = mock(Publisher.class);

            when(publisher.getName()).thenReturn("Test Publisher");
            when(publisher.id()).thenReturn(mock(TOPSECRET.domain.ValueObjects.PublisherID.class));

            ResponsePublisherDTO result = PublisherMapper.toDTO(publisher);

            assertEquals("Test Publisher", result.getName());
            assertNotNull(result.getPublisherId());
        }

        @Test
        void shouldThrowExceptionWhenPublisherIsNull() {

            NullPointerException exception =
                    assertThrows(NullPointerException.class,
                            () -> PublisherMapper.toDTO(null));

            assertEquals("Publisher cannot be null", exception.getMessage());
        }

        @Test
        void shouldConvertListOfPublishersToDTOList() {

            Publisher p1 = mock(Publisher.class);
            Publisher p2 = mock(Publisher.class);

            when(p1.getName()).thenReturn("Publisher One");
            when(p2.getName()).thenReturn("Publisher Two");

            when(p1.id()).thenReturn(mock(TOPSECRET.domain.ValueObjects.PublisherID.class));
            when(p2.id()).thenReturn(mock(TOPSECRET.domain.ValueObjects.PublisherID.class));

            List<Publisher> publishers = List.of(p1, p2);

            List<ResponsePublisherDTO> result = PublisherMapper.toDTOList(publishers);

            assertEquals(2, result.size());
            assertEquals("Publisher One", result.get(0).getName());
            assertEquals("Publisher Two", result.get(1).getName());
        }

        @Test
        void shouldThrowExceptionWhenListOfPublishersIsNull() {

            NullPointerException exception =
                    assertThrows(NullPointerException.class,
                            () -> PublisherMapper.toDTOList(null));

            assertEquals("Publishers list cannot be null", exception.getMessage());
        }

    @Test
    void shouldThrowExceptionWhenListContainsNullPublisher() {

        Publisher validPublisher = mock(Publisher.class);
        PublisherID id = mock(PublisherID.class);

        when(validPublisher.getName()).thenReturn("Test");
        when(id.toString()).thenReturn("PUB-1");
        when(validPublisher.id()).thenReturn(id);

        List<Publisher> publishers = Arrays.asList(
                validPublisher,
                null
        );

        NullPointerException exception =
                assertThrows(NullPointerException.class,
                        () -> PublisherMapper.toDTOList(publishers));

        assertEquals("Publisher cannot be null", exception.getMessage());
    }

        @Test
        void shouldReturnImmutableList() {

            Publisher publisher = mock(Publisher.class);

            when(publisher.getName()).thenReturn("Publisher");
            when(publisher.id()).thenReturn(mock(TOPSECRET.domain.ValueObjects.PublisherID.class));

            List<ResponsePublisherDTO> result =
                    PublisherMapper.toDTOList(List.of(publisher));

            assertThrows(
                    UnsupportedOperationException.class,
                    () -> result.add(mock(ResponsePublisherDTO.class))
            );
        }


    @Test
    void shouldConvertRequestDTOToContactSuccessfullyInfoPhone() {
        // ARRANGE
        RequestPublisherDTO dto = new RequestPublisherDTO(
                "123456789",
                "123456789",
                "911111111",
                "1",
                "john",
                "gmail.com",
                "Main Street",
                "10",
                "2388776",
                "1000-001"
        );

        // ACT
        Contact contact = PublisherMapper.toContact(dto);

        // ASSERT
        assertEquals("911111111", contact.getPhoneNum());
        assertEquals("1", contact.getExtension());
    }

    @Test
    void shouldConvertRequestDTOToContactSuccessfullyInfoEmail() {
        // ARRANGE
        RequestPublisherDTO dto = new RequestPublisherDTO(
                "123456789",
                "123456789",
                "911111111",
                "1",
                "john",
                "gmail.com",
                "Main Street",
                "10",
                "2388776",
                "1000-001"
        );

        // ACT
        Contact contact = PublisherMapper.toContact(dto);

        // ASSERT
        assertEquals("john", contact.getEmail().getUserName());
        assertEquals("gmail.com", contact.getEmail().getDomain());
    } @Test
    void shouldConvertRequestDTOToContactSuccessfullyInfoAddress() {
        // ARRANGE
        RequestPublisherDTO dto = new RequestPublisherDTO(
                "Anabela",
                "123456789",
                "123456789",
                "911111111",
                "john",
                "gmail.com",
                "Main Street",
                "2388776",
                "1000-001",
                "1"
        );

        // ACT
        Contact contact = PublisherMapper.toContact(dto);

        // ASSERT
        assertEquals("Main Street", contact.getAddress().getStreet());
        assertEquals("2388776", contact.getAddress().getNumber());
        assertEquals("1000-001", contact.getAddress().getZipCode());
        assertEquals("1", contact.getAddress().getCityID().getCityID());

    }

    @Test
    void shouldThrowNullPointerException_whenDTOIsNull() {
        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> PublisherMapper.toContact(null));
    }
    }