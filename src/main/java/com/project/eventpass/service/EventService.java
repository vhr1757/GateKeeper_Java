package com.project.eventpass.service;

import com.project.eventpass.entity.EntryLog;
import com.project.eventpass.entity.Event;
import com.project.eventpass.entity.Pass;
import com.project.eventpass.entity.User;
import com.project.eventpass.repository.EntryLogRepository;
import com.project.eventpass.repository.EventRepository;
import com.project.eventpass.repository.PassRepository;
import com.project.eventpass.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {
    private EventRepository eventRepository;
    private UserRepository userRepository;
    private PassRepository passRepository;
    private EntryLogRepository entryLogRepository;

    @Autowired
    public EventService(EventRepository eventRepository,  UserRepository userRepository, PassRepository passRepository, EntryLogRepository entryLogRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.passRepository = passRepository;
        this.entryLogRepository = entryLogRepository;
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event details) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        existingEvent.setTitle(details.getTitle());
        existingEvent.setDescription(details.getDescription());
        existingEvent.setEventDate(details.getEventDate());
        existingEvent.setLocation(details.getLocation());
        existingEvent.setCapacity(details.getCapacity());

        return eventRepository.save(existingEvent);
    }

    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete: Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }

    public List<Pass> getMyPasses(Long userId) {
        if(!userRepository.existsById(userId)) {
            throw new RuntimeException("Cannot find user with id: " + userId);
        }
        return passRepository.findByUserId(userId);
    }

    public Pass bookPass(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not Found"));

        long bookedCount = passRepository.countByEventId(eventId);
        if(bookedCount > event.getCapacity()){
            throw new RuntimeException("Event is FULL!!!");
        }

        if(passRepository.existsByUserIdAndEventId(userId, eventId)){
            throw new RuntimeException("You already have a pass for this event!!!");
        }

        User user = userRepository.findById(userId).get();

        Pass pass = new Pass();
        pass.setPassCode(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        pass.setUser(user);
        pass.setEvent(event);
        pass.setStatus("ACTIVE");

        return passRepository.save(pass);
    }

    public String validatePass(String passCode){
        Pass pass = passRepository.findByPassCode(passCode).orElse(null);
        EntryLog log = new EntryLog();
        log.setEntryTime(LocalDateTime.now());

        if(pass == null){
            return "Invalid Pass Code!!!";
        }

        log.setPass(pass);

        if("USED".equals(log.getStatus())){
            log.setStatus("FAILED - ALREADY USED");
            entryLogRepository.save(log);
            return "REJECTED: Pass already used!";
        }

        pass.setStatus("USED");
        passRepository.save(pass);

        log.setStatus("SUCCESS");
        entryLogRepository.save(log);

        return "ACCESS GRANTED: Welcome to " + pass.getEvent().getTitle();
    }
}
