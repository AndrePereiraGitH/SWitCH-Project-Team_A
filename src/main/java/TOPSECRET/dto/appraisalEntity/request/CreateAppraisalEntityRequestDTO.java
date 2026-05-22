package TOPSECRET.dto.appraisalEntity.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Data Transfer Object used to create a new AppraisalEntity.
 *
 * <p>This class contains all input data required by the API to register
 * a new appraisal, including contact information, address details,
 * and associated publication and genre identifiers.</p>
 *
 * <p>It is used exclusively as an input model and should not contain
 * business logic.</p>
 */

@EqualsAndHashCode
@AllArgsConstructor
@ToString
@Getter
public class CreateAppraisalEntityRequestDTO {

    @NotNull
    String name;

    // To construct
    // Contact
    @NotNull
    String phoneNumber;
    @NotNull
    String extension;

    // Email
    @Email
    @NotNull
    String email;

    // Address
    @NotNull
    String street;
    @NotNull
    String number;
    @NotNull
    String zipCode;
    @NotNull
    String cityId;

    @NotNull
    List<String> publicationTypeIds;
    @NotNull
    List<String> genreIds;

}
