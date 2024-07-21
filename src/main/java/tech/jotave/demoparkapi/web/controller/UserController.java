package tech.jotave.demoparkapi.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tech.jotave.demoparkapi.entity.User;

import tech.jotave.demoparkapi.service.UserService;
import tech.jotave.demoparkapi.web.dto.UserCreateDto;
import tech.jotave.demoparkapi.web.dto.UserPasswordDto;
import tech.jotave.demoparkapi.web.dto.UserResponseDto;
import tech.jotave.demoparkapi.web.dto.mapper.UserMapper;
import tech.jotave.demoparkapi.web.exceptions.ErrorMessage;

import java.util.List;

@Tag(name = "Usuários", description = "Operações para gerenciar um usuário.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Cria um novo usuário",
            description = "Recurso para criar um novo usuário",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDto.class)
                            )
                    ),

                    @ApiResponse(
                            responseCode = "409",
                            description = "Email já cadastrado no sistema",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Dados de entrada inválidos",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserCreateDto userCreateDto) {
        User userResponse = userService.save(UserMapper.toUser(userCreateDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(userResponse));
    }

    @Operation(
            summary = "Recupera todos os usuários",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuários recuperados com sucesso",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(
                                            implementation = UserResponseDto.class
                                    )))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll() {
        List<User> usersResponse = userService.getAll();
        return ResponseEntity.ok(UserMapper.toListDto(usersResponse));
    }

    @Operation(
            summary = "Recupera um usuário pelo id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
        User userResponse = userService.getById(id);
        UserResponseDto userResponseDto = UserMapper.toDto(userResponse);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @Operation(
            summary = "Atualiza a senha de um usuário",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Senha atualizada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Void.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Senhas não conferem, verifique se a senha antiga está correta e se nova senha confere com a senha de confirmação.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class)
                            )
                    ),
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(
            @PathVariable Long id,
            @RequestBody UserPasswordDto userPasswordDto
    ) {
        userService.updatePassword(
                id,
                userPasswordDto.getOldPassword(),
                userPasswordDto.getNewPassword(),
                userPasswordDto.getConfirmNewPassword()
        );

        return ResponseEntity.noContent().build();
    }

}
