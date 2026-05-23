package TOPSECRET.dto.appraisalEntity;

import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.Email;
import TOPSECRET.dto.appraisalEntity.request.RequestAppraisalEntityDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppraisalEntityMapperTest {

    @Test
    void toContact() {
    }

    @Test
    void toEmail() {
    }

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
    void ToEmailShouldThrowExceptionWhenEmailHasNoSeparator() {

        // ARRANGE
        RequestAppraisalEntityDTO dto =
                new RequestAppraisalEntityDTO(
                        "name",
                        "912345678",
                        "123",
                        "invalidEmail",
                        "street",
                        "10",
                        "1234",
                        "city",
                        List.of(),
                        List.of()
                );

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> AppraisalEntityMapper.toEmail(dto));
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
    void ConstructorShouldThrowException() {

        // ARRANGE

        // ACT + ASSERT
        assertThrows(
                UnsupportedOperationException.class,
                AppraisalEntityMapper::new
        );
    }

}