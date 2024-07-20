package tech.jotave.demoparkapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jotave.demoparkapi.entity.User;
import tech.jotave.demoparkapi.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {
    // final impede que seja sobrescrito.
    private final UserRepository userRepository;

    // spring toma conta de abrir, gerenciar e fechar a transação do método.
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found.")
        );
    }
}
