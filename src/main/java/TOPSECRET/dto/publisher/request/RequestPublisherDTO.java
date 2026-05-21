package TOPSECRET.dto.publisher.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class RequestPublisherDTO {

    @NonNull
    @NotBlank
    private String name;

    @NonNull
    private String _vatNumber;

    @NonNull
    private String _phoneNum;

    @NonNull
    private String _extension;
}
