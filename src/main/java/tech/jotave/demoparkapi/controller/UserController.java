package tech.jotave.demoparkapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tech.jotave.demoparkapi.entity.User;

import tech.jotave.demoparkapi.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

    // entity encapsula a resposta
    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        User userResponse = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        User userResponse = userService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

}
