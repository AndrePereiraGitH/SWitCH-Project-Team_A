package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.assembler.UserAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.UserDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IUserRepositoryJPA;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JPA-backed implementation of {@link IUserRepository}.
 *
 * <p>
 * Delegates persistence operations to {@link IUserRepositoryJPA} and uses
 * {@link UserAssembler} to convert between the domain model ({@link User})
 * and the persistence data model ({@link UserDM}).
 * </p>
 */
@Repository
public class UserRepositoryJPAImpl implements IUserRepository {

    private final IUserRepositoryJPA userRepositoryJpa;
    private final UserAssembler assembler;

    public UserRepositoryJPAImpl(IUserRepositoryJPA userRepositoryJpa, UserAssembler assembler) {
        this.userRepositoryJpa = Objects.requireNonNull(userRepositoryJpa, "userRepositoryJpa must not be null");
        this.assembler = Objects.requireNonNull(assembler, "assembler must not be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public User save(User user) {
        Objects.requireNonNull(user, "User cannot be null");

        UserDM userDM = assembler.toDM(user);
        UserDM savedUserDM = userRepositoryJpa.save(userDM);

        return assembler.toDomain(savedUserDM);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }

        return userRepositoryJpa.findByUserName(username).isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findByID(UserId userID) {
        Objects.requireNonNull(userID, "UserId cannot be null");

        return userRepositoryJpa.findById(userID)
                .map(assembler::toDomain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<UserId> findIdByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }

        return userRepositoryJpa.findByUserName(username)
                .map(UserDM::getId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> findAll() {
        return userRepositoryJpa.findAll()
                .stream()
                .map(assembler::toDomain)
                .collect(Collectors.toList());
    }
}