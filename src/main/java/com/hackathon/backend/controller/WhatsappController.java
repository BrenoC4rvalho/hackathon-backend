package com.hackathon.backend.controller;

import com.hackathon.backend.dto.ReceivedWhatsappMessage;
import com.hackathon.backend.dto.SendWhatsappRequest;
import com.hackathon.backend.dto.SendWhatsappResponse;
import com.hackathon.backend.service.WhatsappService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsappController {

    private final WhatsappService whatsappService;

    public WhatsappController(WhatsappService whatsappService) {
        this.whatsappService = whatsappService;
    }

    @PostMapping("/send")
    public ResponseEntity<SendWhatsappResponse> sendMessage(
            @Valid @RequestBody SendWhatsappRequest request
    ) {
        SendWhatsappResponse response = whatsappService.sendMessage(
                request.to(),
                request.message()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> receiveMessage(
            @RequestParam(name = "From", required = false) String from,
            @RequestParam(name = "To", required = false) String to,
            @RequestParam(name = "Body", required = false) String body,
            @RequestParam(name = "MessageSid", required = false) String messageSid
    ) {
        whatsappService.receiveMessage(
                new ReceivedWhatsappMessage(from, to, body, messageSid)
        );

        return ResponseEntity.ok().build();
    }

}
