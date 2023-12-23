package ru.belosludtsev.kursach.emailclient.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.belosludtsev.kursach.emailclient.entities.EmailAccount;

import java.util.Optional;

@Repository
public interface EmailAccountRepositories extends JpaRepository<EmailAccount, Integer> {
    Optional<EmailAccount> findByEmail(String email);
}
