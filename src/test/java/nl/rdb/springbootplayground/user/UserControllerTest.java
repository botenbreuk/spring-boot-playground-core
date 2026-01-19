package nl.rdb.springbootplayground.user;

import nl.rdb.springbootplayground.test.AbstractWebIntegrationTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserControllerTest extends AbstractWebIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void before() {
        userRepository.deleteAll();
    }

    @Nested
    class FindAll {

        @Test
        void findAllGebruikers() {
            userFixtures.sjonnyb();

            client.get()
                    .uri("/gebruikers")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.length()").isEqualTo(1);
        }

        @Test
        void findAllGebruikers_filterByEmail() {
            User piet = userFixtures.piet();
            userFixtures.sjonnyb();

            client.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/gebruikers")
                            .queryParam("email", "piet")
                            .build()
                    )
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.length()").isEqualTo(1)
                    .jsonPath("$[0].email").isEqualTo(piet.getEmail());
        }
    }
}
