package tech.jotave.demoparkapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jotave.demoparkapi.entity.User;
import tech.jotave.demoparkapi.exception.UsernameUniqueViolationException;
import tech.jotave.demoparkapi.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    // final impede que seja sobrescrito.
    private final UserRepository userRepository;

    // spring toma conta de abrir, gerenciar e fechar a transação do método.
    @Transactional
    public User save(User user) {
        try {
            return userRepository.save(user);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new UsernameUniqueViolationException(String.format("Email %s já cadastrado", user.getUsername()));
        }
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found.")
        );
    }

    @Transactional
    public User updatePassword(
            Long id,
            String oldPassword,
            String newPassword,
            String confirmNewPassword
    ) {
        if (!newPassword.equals(confirmNewPassword)) {
            throw new RuntimeException("The confirm password must be the same");
        }

        User user = getById(id);

        if (!user.getPassword().equals(oldPassword)) {
            throw new RuntimeException("Old password is wrong");
        }

        user.setPassword(newPassword);
        return user;
    }
}
