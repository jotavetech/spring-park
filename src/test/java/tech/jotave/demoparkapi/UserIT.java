package tech.jotave.demoparkapi;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import tech.jotave.demoparkapi.web.dto.UserCreateDto;
import tech.jotave.demoparkapi.web.dto.UserPasswordDto;
import tech.jotave.demoparkapi.web.dto.UserResponseDto;
import tech.jotave.demoparkapi.web.exceptions.ErrorMessage;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserIT {
    @Autowired
    WebTestClient webTestClient;

    @Test
    public void createUserWithValidUsernameAndPasswordShouldReturn201() {
        String username = "joaovitin@email.com";
        String password = "123456";

        UserResponseDto response = webTestClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto(username, password))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getId()).isNotNull();
        Assertions.assertThat(response.getUsername()).isEqualTo(username);
        Assertions.assertThat(response.getRole()).isEqualTo("CLIENT");
    }

    @Test
    public void createUserWithInvalidUsernameAndPasswordShouldReturn422() {
        ErrorMessage responseBody = webTestClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("tody", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = webTestClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("todyemail", "123456"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUserWithRepeatedUsernameShouldReturn409() {
        String username = "ana@email.com";
        String password = "123456";

        ErrorMessage response = webTestClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto(username, password))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(409);
    }

    @Test
    public void findUserWithValidIdShouldShouldReturnUserWithStatus200() {
        UserResponseDto response = webTestClient
                .get()
                .uri("/api/v1/users/100")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getId()).isEqualTo(100);
        Assertions.assertThat(response.getUsername()).isEqualTo("ana@email.com");
        Assertions.assertThat(response.getRole()).isEqualTo("ADMIN");
    }

    @Test
    public void findUserWithANotValidIdShouldShouldReturnStatus404() {
        ErrorMessage response = webTestClient
                .get()
                .uri("/api/v1/users/134")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    public void editPasswordWithValidDataShouldReturn204() {
        String oldPassword = "123456";
        String newPassword = "123457";
        String confirmPassword = "123457";

        webTestClient
                .patch()
                .uri("/api/v1/users/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto(oldPassword, newPassword, confirmPassword))
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void editPasswordWithNotValidIdShouldReturnStatus404() {
        String oldPassword = "123456";
        String newPassword = "123457";
        String confirmPassword = "123457";

        ErrorMessage response = webTestClient
                .patch()
                .uri("/api/v1/users/130")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto(oldPassword, newPassword, confirmPassword))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    public void editPasswordWithEmptyDataShouldReturnStatus400() {
        String oldPassword = "";
        String newPassword = "";
        String confirmPassword = "";

        ErrorMessage response = webTestClient
                .patch()
                .uri("/api/v1/users/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto(oldPassword, newPassword, confirmPassword))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void editPasswordWithPasswordLessThan6ReturnStatus400() {
        String oldPassword = "12345";
        String newPassword = "12345";
        String confirmPassword = "12345";

        ErrorMessage response = webTestClient
                .patch()
                .uri("/api/v1/users/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto(oldPassword, newPassword, confirmPassword))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void editPasswordWithDifferentPasswordsPReturnStatus400() {
        String oldPassword = "123456";
        String newPassword = "123455";
        String confirmPassword = "123458";

        ErrorMessage response = webTestClient
                .patch()
                .uri("/api/v1/users/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto(oldPassword, newPassword, confirmPassword))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(400);

        oldPassword = "129999";
        newPassword = "123457";
        confirmPassword = "123457";

        response = webTestClient
                .patch()
                .uri("/api/v1/users/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserPasswordDto(oldPassword, newPassword, confirmPassword))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(400);
    }

    @Test
    public void listUsersWithoutParamsShouldReturn200() {
        List<UserResponseDto> response = webTestClient
                .get()
                .uri("api/v1/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response).size().isEqualTo(3);
    }
}

