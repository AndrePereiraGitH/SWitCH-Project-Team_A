package TOPSECRET.dto.appraisalEntity.reponse;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;


@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@ToString
@Getter
public class ResponseAppraisalEntityDTO extends RepresentationModel<ResponseAppraisalEntityDTO> {

    @NotBlank(message = "Id is required")
    String id;

    @NotBlank(message = "Name is required")
    String name;

    @NotEmpty(message = "At least one publication type must be provided")
    List<String> publicationTypeIds;

    @NotEmpty(message = "At least one genre must be provided")
    List<String> genreIds;
}
