package TOPSECRET.dto.appraisalEntity.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

/**
 * Response DTO used to represent an AppraisalEntity in API responses.
 *
 * <p>This DTO is returned after successful creation or retrieval
 * of an AppraisalEntity and includes identifiers for associated
 * genres and publication types.</p>
 *
 * <p>Extends {@link RepresentationModel} to support HATEOAS links.</p>
 */
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@ToString
@Getter
public class ResponseAppraisalEntityDTO extends RepresentationModel<ResponseAppraisalEntityDTO> {

    String id;
    String name;
    List<String> publicationTypeIds;
    List<String> genreIds;
}
