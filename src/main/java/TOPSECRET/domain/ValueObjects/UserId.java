package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class UserId implements DomainID {
    /**
     * Value Object representing a unique identifier for a User.
     * <p>
     * A {@code UserId} is immutable and is considered equal to another
     * {@code UserId} if both share the same underlying identifier string.
     * </p>
     */

    private String userId;
    /**
     * Constructs a new {@code UserId} with the given identifier.
     *
     * @param id the string to be used as the user identifier;
     *           must not be {@code null} or blank
     * @throws IllegalArgumentException if {@code id} is {@code null} or blank
     */
    public UserId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("UserId can't be null or empty.");
        }
        this.userId = id;
    }

    /**
     * Indicates whether some other object is equal to this {@code UserId}.
     * <p>
     * Two {@code UserId} instances are considered equal if and only if
     * their underlying identifier strings are equal.
     * </p>
     *
     * @param object the object to compare with
     * @return {@code true} if the given object is a {@code UserId}
     *         with the same identifier; {@code false} otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (object instanceof UserId) {

            UserId oUserId = (UserId) object;

            if (this.userId.equals(oUserId.userId))
                return true;
        }
        return false;

    }

    /**
     * Returns the string identifier of this {@code UserId}.
     *
     * @return the user identifier string
     */
    public String getUserId() {
        return userId;
    }

}
