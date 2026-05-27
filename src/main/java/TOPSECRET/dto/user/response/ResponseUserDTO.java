package TOPSECRET.dto.user.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

/**
 * This is what the API sends back to the client after creating a user.
 *
 * <p>It only has Strings so the client never sees internal domain objects.
 * It also has a {@code _links} field (from {@link RepresentationModel})
 * that tells the client what it can do next, for example the link to see
 * the user that was just created.</p>
 *
 * <p>Fields returned:</p>
 * <ul>
 *   <li>{@code id} — the unique identifier generated for the user</li>
 *   <li>{@code username} — the username chosen by the client</li>
 *   <li>{@code name} — the full name of the user</li>
 *   <li>{@code email} — the email address of the user</li>
 *   <li>{@code role} — the role assigned to the user (ADMIN or NORMAL_USER)</li>
 * </ul>
 */

@EqualsAndHashCode(callSuper = false)
@Getter
@AllArgsConstructor
public class ResponseUserDTO extends RepresentationModel<ResponseUserDTO> {

    private final String id;
    private final String username;
    private final String name;
    private final String email;
    private final String role;

}