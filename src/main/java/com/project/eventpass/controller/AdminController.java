package com.project.eventpass.controller;

import com.project.eventpass.entity.EntryLog;
import com.project.eventpass.entity.Event;
import com.project.eventpass.repository.EntryLogRepository;
import com.project.eventpass.repository.EventRepository;
import com.project.eventpass.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private EventRepository eventRepository;
    private EntryLogRepository entryLogRepository;
    private EventService eventService;

    @Autowired
    public AdminController(EventRepository eventRepository, EntryLogRepository entryLogRepository, EventService eventService) {
        this.eventRepository = eventRepository;
        this.entryLogRepository = entryLogRepository;
        this.eventService = eventService;
    }

    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.createEvent(event));
    }

    @GetMapping("/events")
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @PutMapping("/events/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        try {
            Event updatedEvent = eventService.updateEvent(id, eventDetails);
            return ResponseEntity.ok(updatedEvent);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.ok("Event deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/logs")
    public List<EntryLog> getLogs() {
        return entryLogRepository.findAll();
    }
}
