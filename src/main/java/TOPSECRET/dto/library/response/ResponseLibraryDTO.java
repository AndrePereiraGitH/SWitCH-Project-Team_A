package TOPSECRET.dto.library.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

/**
 * Data Transfer Object representing a Library response.
 *
 * <p>This DTO is used to expose library information to clients
 * without leaking internal domain model details.</p>
 *
 * <p>The class extends {@link RepresentationModel} to support
 * HATEOAS integration when needed.</p>
 *
 * <p>This class is a pure data container and does not contain
 * business logic.</p>
 */

@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
@AllArgsConstructor

public class ResponseLibraryDTO extends RepresentationModel<ResponseLibraryDTO> {
    private final String libraryId;
    private final String libraryName;
}
