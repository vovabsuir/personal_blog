package org.example.personalblog.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.personalblog.dto.SubscriptionRequest;
import org.example.personalblog.entity.Subscription;
import org.example.personalblog.exception.EmailNotFoundException;
import org.example.personalblog.exception.TokenExpiredException;
import org.example.personalblog.mapper.SubscriptionMapper;
import org.example.personalblog.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.security.KeyPair;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriptionService {
    private final EmailService emailService;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final KeyPair keyPair;

    @Value("${CONFIRMATION_URL}")
    private String confirmationUrl;

    @Transactional
    public void subscribe(SubscriptionRequest subscriptionRequest) {
        Subscription subscription = subscriptionRepository.findByEmail(subscriptionRequest.getEmail()).orElse(null);
        if (subscription == null) {
            subscription = subscriptionMapper.toEntity(subscriptionRequest);

            subscription.setCreatedAt(LocalDateTime.now());
            subscription.setIsConfirmed(true);

            subscriptionRepository.save(subscription);
        } else if (subscription.getIsConfirmed()) {
            throw new IllegalStateException("Subscription is already confirmed");
        }

        String url = confirmationUrl + generateSubscriptionToken(subscription.getEmail());
        emailService.sendSubscriptionEmail(subscription.getEmail(), "Subscription confirmation", url);
    }

    public void confirmEmail(String token) {
        String email = validateSubscriptionToken(token);

        Subscription subscription = subscriptionRepository.findByEmail(email).orElseThrow(
                () -> new EmailNotFoundException("Email '" + email + "' not found"));
        subscription.setIsConfirmed(true);

        subscriptionRepository.save(subscription);
    }

    @Async("emailExecutor")
    public void sendMessage(String subject, String message) {
        int page = 0;
        Page<Subscription> subscriptions;

        do {
            subscriptions = subscriptionRepository.findByIsConfirmedTrue((PageRequest.of(page, 10)));
            subscriptions.forEach(sub ->
                    emailService.sendMessageEmail(sub.getEmail(), subject, message)
            );
            page++;
        } while (!subscriptions.isLast());
    }

    private String generateSubscriptionToken(String email) {
        return Jwts.builder()
                .subject(email)
                .claim("type", "subscription")
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)))
                .signWith(keyPair.getPrivate(), Jwts.SIG.RS256)
                .compact();
    }

    private String validateSubscriptionToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(keyPair.getPublic())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException ex) {
            throw new TokenExpiredException("Confirmation time has expired, please try again with a new one");
        }
    }
}
