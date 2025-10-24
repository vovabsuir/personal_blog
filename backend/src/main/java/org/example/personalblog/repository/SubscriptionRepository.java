package org.example.personalblog.repository;

import org.example.personalblog.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    Optional<Subscription> findByEmail(String email);

    Page<Subscription> findByIsConfirmedTrue(Pageable pageable);
}
