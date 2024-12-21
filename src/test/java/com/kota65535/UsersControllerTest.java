package com.kota65535;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.kota65535.controller.User;
import com.kota65535.controller.Users;
import com.kota65535.repository.one.Db1UserRepository;
import com.kota65535.repository.two.Db2UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class UsersControllerTest {

    @LocalServerPort
    int port;
    RestTemplate client;

    @MockitoSpyBean
    Db1UserRepository repository1;

    @MockitoSpyBean
    Db2UserRepository repository2;

    @BeforeEach
    void beforeEach() {
        client = new RestTemplateBuilder()
                .rootUri("http://localhost:%d".formatted(port))
                .build();
    }

    @Test
    void testGetUsers() {
        Users users = client.getForObject("/users", Users.class);
        assertNotNull(users);
        assertThat(users.getUsers()).hasSize(4);

        // "foo", "bar" is from db1, "hoge", "piyo" from db2
        assertThat(users.getUsers())
                .extracting(User::getName)
                .containsExactlyInAnyOrder("foo", "bar", "hoge", "piyo");

        verify(repository1).findAll();
        verify(repository2).findAll();
    }
}
