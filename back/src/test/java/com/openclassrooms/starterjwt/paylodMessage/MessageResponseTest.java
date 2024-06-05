package com.openclassrooms.starterjwt.paylodMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import org.junit.jupiter.api.Test;

public class MessageResponseTest {

    @Test
    void testGetMessage() {
        String expectedMessage = "Test message";
        MessageResponse messageResponse = new MessageResponse(expectedMessage);

        // Vérifier que le message récupéré est celui attendu
        String actualMessage = messageResponse.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testSetMessage() {
        // Créer une instance de MessageResponse avec un message initial
        MessageResponse messageResponse = new MessageResponse("Initial message");

        // Définir un nouveau message
        String newMessage = "New message";
        messageResponse.setMessage(newMessage);

        // Vérifier que le message récupéré est le nouveau message défini
        String actualMessage = messageResponse.getMessage();
        assertEquals(newMessage, actualMessage);
    }
}
