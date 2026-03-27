package com.project.eventpass.controller;

import com.project.eventpass.entity.Pass;
import com.project.eventpass.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/passes")
public class PassController {
    private EventService eventService;

    public PassController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/book")
    public ResponseEntity<?> book(@RequestParam Long userId, @RequestParam Long eventId) {
        try {
            return ResponseEntity.ok(eventService.bookPass(userId, eventId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/my")
    public ResponseEntity<List<Pass>> getMyPasses(@RequestParam Long userId) {
        return ResponseEntity.ok(eventService.getMyPasses(userId));
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validate(@RequestParam String passCode) {
        String result = eventService.validatePass(passCode);
        return ResponseEntity.ok(result);
    }
}
