package tech.jotave.demoparkapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.jotave.demoparkapi.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}

