package TOPSECRET.dto.edition.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class EditionIdResponseDTO extends RepresentationModel<EditionIdResponseDTO> {

    private final String editionId;

}