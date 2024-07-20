package tech.jotave.demoparkapi.web.controller;

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

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

    // entity encapsula a resposta
    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserCreateDto userCreateDto) {
        User userResponse = userService.save(UserMapper.toUser(userCreateDto));
        return ResponseEntity.status(HttpStatus.CREATED).bo dy(UserMapper.toDto(userResponse));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll() {
        List<User> usersResponse = userService.getAll();
        return ResponseEntity.ok(UserMapper.toListDto(usersResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
        User userResponse = userService.getById(id);
        UserResponseDto userResponseDto = UserMapper.toDto(userResponse);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

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
