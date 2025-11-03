package com.example.msevents.Controller;

import com.example.msevents.DTO.CreateEventDTO;
import com.example.msevents.DTO.EventResponseDTO;
import com.example.msevents.DTO.EventUpdateSpentDTO;
import com.example.msevents.Service.EventServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventServiceImpl eventService;

    // 🔹 Crear nuevo evento
    @PostMapping
    public ResponseEntity<EventResponseDTO> createEvent(@RequestBody CreateEventDTO request) {
        EventResponseDTO response = eventService.createEvent(request);
        return ResponseEntity.ok(response);
    }

    // 🔹 Obtener evento por ID
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable Long id) {
        EventResponseDTO response = eventService.getEventById(id);
        return ResponseEntity.ok(response);
    }

    // 🔹 Actualizar gasto (llamado desde ms-wallet)
    @PutMapping("/{id}/spent")
    public ResponseEntity<EventResponseDTO> updateEventSpent(
            @PathVariable Long id,
            @RequestBody EventUpdateSpentDTO updateDTO) {
        EventResponseDTO response = eventService.updateEventSpent(id, updateDTO);
        return ResponseEntity.ok(response);
    }
}
