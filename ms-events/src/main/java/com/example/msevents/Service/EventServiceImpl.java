package com.example.msevents.Service;

import com.example.msevents.DTO.CreateEventDTO;
import com.example.msevents.DTO.EventResponseDTO;
import com.example.msevents.DTO.EventUpdateSpentDTO;
import com.example.msevents.Entity.Event;
import com.example.msevents.Feign.UserFeignClient;
import com.example.msevents.Repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl  {

    private final EventRepository eventRepository;
    private final UserFeignClient userClient; // Feign client para ms-auth/ms-users

    public EventResponseDTO createEvent(CreateEventDTO request) {
        try {
            userClient.getUserById(request.getUserId());
        } catch (Exception e) {
            throw new RuntimeException("Usuario no encontrado en ms-users");
        }

        Event event = Event.builder()
                .userId(request.getUserId())
                .name(request.getName())
                .description(request.getDescription())
                .budget(request.getBudget())
                .spent(BigDecimal.ZERO)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .createdAt(LocalDateTime.now())
                .build();

        event = eventRepository.save(event);
        log.info("Evento creado correctamente: {}", event.getId());

        return EventResponseDTO.builder()
                .id(event.getId())
                .userId(event.getUserId())
                .name(event.getName())
                .description(event.getDescription())
                .budget(event.getBudget())
                .spent(event.getSpent())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .createdAt(event.getCreatedAt())
                .build();
    }
    public EventResponseDTO getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
        return mapToResponse(event);
    }


    public EventResponseDTO updateEventSpent(Long id, EventUpdateSpentDTO updateDTO) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        // Sumar el gasto
        event.setSpent(event.getSpent().add(updateDTO.getAmount()));

        eventRepository.save(event);
        log.info("Gasto actualizado para evento {} (+{})", id, updateDTO.getAmount());

        return EventResponseDTO.builder()
                .id(event.getId())
                .userId(event.getUserId())
                .name(event.getName())
                .description(event.getDescription())
                .budget(event.getBudget())
                .spent(event.getSpent())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .createdAt(event.getCreatedAt())
                .build();
    }
    private EventResponseDTO mapToResponse(Event event) {
        return EventResponseDTO.builder()
                .id(event.getId())
                .userId(event.getUserId())
                .name(event.getName())
                .description(event.getDescription())
                .budget(event.getBudget())
                .spent(event.getSpent())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .createdAt(event.getCreatedAt())
                .build();
    }
}
