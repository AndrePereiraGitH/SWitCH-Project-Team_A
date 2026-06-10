package TOPSECRET.dto.bibliographicalData.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

/**
 * Response DTO representing bibliographical data information.
 * <p>
 * Extends {@link RepresentationModel} to support HATEOAS links.
 */
@EqualsAndHashCode(callSuper = false)
@ToString
@Getter
@AllArgsConstructor
public class ResponseBibliographicalDataDTO extends RepresentationModel<ResponseBibliographicalDataDTO> {

    private final String bdId;
    private final String pubTitle;
    private final List<String> authorName;
}


