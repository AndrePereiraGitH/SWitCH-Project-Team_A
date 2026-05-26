package TOPSECRET.dto.appraisalEntity;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Email;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.dto.appraisalEntity.request.RequestAppraisalEntityDTO;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppraisalEntityMapperTest {

    @Test
    void ToEmailShouldCreateEmailUsernameWhenEmailIsValid() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of()
                );

        // ACT
        Email result = AppraisalEntityMapper.toEmail(dto);

        // ASSERT
        assertEquals("john", result.getUserName());
    }

    @Test
    void ToEmailShouldCreateEmailDomainWhenEmailIsValid() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of()
                );

        // ACT
        Email result = AppraisalEntityMapper.toEmail(dto);

        // ASSERT
        assertEquals("test.com", result.getDomain());
    }

    @Test
    void ToContactShouldCreatePhoneNumber() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of()
                );

        // ACT
        Contact result = AppraisalEntityMapper.toContact(dto);

        // ASSERT
        assertEquals("912345678", result.getPhoneNum());
    }

    @Test
    void ToContactShouldCreateExtension() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of()
                );

        // ACT
        Contact result = AppraisalEntityMapper.toContact(dto);

        // ASSERT
        assertEquals("123", result.getExtension());
    }

    @Test
    void ToContactShouldCreateEmail() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of()
                );

        // ACT
        Contact result = AppraisalEntityMapper.toContact(dto);

        // ASSERT
        assertNotNull(result.getEmail());
    }

    @Test
    void ToContactShouldCreateAddress() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of()
                );

        // ACT
        Contact result = AppraisalEntityMapper.toContact(dto);

        // ASSERT
        assertNotNull(result.getAddress());
    }

    @Test
    void shouldThrowExceptionWhenUtilityClassConstructorIsCalled()
            throws Exception {

        // ARRANGE
        Constructor<AppraisalEntityMapper> constructor =
                AppraisalEntityMapper.class
                        .getDeclaredConstructor();

        constructor.setAccessible(true);

        // ACT + ASSERT
        InvocationTargetException result =
                assertThrows(
                        InvocationTargetException.class,
                        constructor::newInstance
                );

        assertInstanceOf(
                UnsupportedOperationException.class,
                result.getCause()
        );
    }

    @Test
    void ToContactShouldThrowExceptionWhenDtoIsNull() {

        // ARRANGE
        RequestAppraisalEntityDTO dto = null;

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> AppraisalEntityMapper.toContact(dto)
        );
    }

    @Test
    void ToEmailShouldThrowExceptionWhenDtoIsNull() {

        // ARRANGE
        RequestAppraisalEntityDTO dto = null;

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> AppraisalEntityMapper.toEmail(dto)
        );
    }

    @Test
    void ToAddressShouldThrowExceptionWhenDtoIsNull() {

        // ARRANGE
        RequestAppraisalEntityDTO dto = null;

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> AppraisalEntityMapper.toAddress(dto)
        );
    }

    @Test
    void ToPubTypeIdsShouldConvertListSizeCorrectly() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of("A", "B"),
                        List.of()
                );

        // ACT
        List<PublicationTypeId> result = AppraisalEntityMapper.toPubTypeIds(dto);

        // ASSERT
        assertEquals(2, result.size());
    }

    @Test
    void ToPubTypeIdsShouldThrowExceptionWhenDtoIsNull() {

        // ARRANGE
        RequestAppraisalEntityDTO dto = null;

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> AppraisalEntityMapper.toPubTypeIds(dto)
        );
    }

    @Test
    void ToGenreIdsShouldConvertListSizeCorrectly() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "john@test.com",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of("Drama", "Action")
                );

        // ACT
        List<GenreId> result = AppraisalEntityMapper.toGenreIds(dto);

        // ASSERT
        assertEquals(2, result.size());
    }

    @Test
    void ToGenreIdsShouldThrowExceptionWhenDtoIsNull() {

        // ARRANGE
        RequestAppraisalEntityDTO dto = null;

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> AppraisalEntityMapper.toGenreIds(dto)
        );
    }
}