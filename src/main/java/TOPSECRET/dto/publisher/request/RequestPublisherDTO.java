package TOPSECRET.dto.publisher.request;

import jakarta.validation.constraints.NotBlank;

public record RequestPublisherDTO(

        @NotBlank(message = "Publisher name must not be blank")
        String name,

        @NotBlank(message = "VAT number must not be blank")
        String vatNumber,

        @NotBlank(message = "Phone number must not be blank")
        String phoneNumber,

        @NotBlank(message = "Extension must not be blank")
        String extension

) {}