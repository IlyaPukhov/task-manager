package com.ilyap.userservice.repository;

import com.ilyap.userservice.IntegrationTestBase;
import com.ilyap.userservice.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor
class UserRepositoryIT extends IntegrationTestBase {

    private final UserRepository userRepository;

    @Test
    void findByUsername_userExists_returnsUser() {
        var expectedUser = new User(1L, "norris", "Chuck", "Norris",
                LocalDate.of(1940, 1, 1), "r5Q9v@example.com", null);
        var username = "norris";

        var maybeUser = userRepository.findByUsername(username);

        assertThat(maybeUser).isPresent();
        maybeUser.ifPresent(user -> assertThat(user).isEqualTo(expectedUser));
    }

    @Test
    void findByUsername_userNotExists_returnsEmptyOptional() {
        var username = "gates";

        var maybeUser = userRepository.findByUsername(username);

        assertThat(maybeUser).isNotPresent();
    }

    @Test
    void deleteByUsername_successDeleted() {
        var username = "norris";

        userRepository.deleteByUsername(username);

        assertThat(userRepository.findByUsername(username)).isNotPresent();
    }
}
