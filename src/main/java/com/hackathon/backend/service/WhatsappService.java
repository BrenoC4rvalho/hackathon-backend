package com.hackathon.backend.service;

import com.hackathon.backend.config.TwilioProperties;
import com.hackathon.backend.dto.ReceivedWhatsappMessage;
import com.hackathon.backend.dto.SendWhatsappResponse;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class WhatsappService {

    private final TwilioProperties properties;

    public WhatsappService(TwilioProperties properties) {
        this.properties = properties;
    }

    public SendWhatsappResponse sendMessage(String to, String text) {
        String formattedTo = formatWhatsappNumber(to);

        Message message = Message.creator(
                new PhoneNumber(formattedTo),
                new PhoneNumber(properties.whatsappFrom()),
                text
        ).create();

        return new SendWhatsappResponse(
                message.getSid(),
                message.getStatus().toString(),
                formattedTo
        );
    }

    public void receiveMessage(ReceivedWhatsappMessage message) {
        System.out.println("Mensagem recebida:");
        System.out.println("De: " + message.from());
        System.out.println("Para: " + message.to());
        System.out.println("Texto: " + message.body());
        System.out.println("SID: " + message.messageSid());
    }

    private String formatWhatsappNumber(String number) {
        if (number.startsWith("whatsapp:")) {
            return number;
        }

        return "whatsapp:" + number;
    }

}
