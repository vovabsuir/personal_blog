package org.example.personalblog.controller;

import lombok.RequiredArgsConstructor;
import org.example.personalblog.dto.SubscriptionRequest;
import org.example.personalblog.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<Void> subscribe(@RequestBody SubscriptionRequest subscriptionRequest) {
        subscriptionService.subscribe(subscriptionRequest);

        return ResponseEntity.accepted().build();
    }

    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmEmail(@RequestParam("token") String token) {
        subscriptionService.confirmEmail(token);

        return ResponseEntity.noContent().build();
    }
}
