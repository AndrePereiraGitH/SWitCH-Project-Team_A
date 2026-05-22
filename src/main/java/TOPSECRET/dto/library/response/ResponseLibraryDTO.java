package TOPSECRET.dto.library.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
@AllArgsConstructor
public class ResponseLibraryDTO extends RepresentationModel<ResponseLibraryDTO> {
    private final String libraryName;
}
