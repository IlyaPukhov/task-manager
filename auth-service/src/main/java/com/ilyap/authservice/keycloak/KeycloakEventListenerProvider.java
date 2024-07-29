package com.ilyap.authservice.keycloak;

import com.ilyap.authservice.dto.VerificationEmailMessage;
import com.ilyap.authservice.service.producer.EmailSendingProducer;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakEventListenerProvider implements EventListenerProvider {

    @Value("${keycloak.server-url}")
    private String serverUrl;

    private KeycloakSession session;
    private EmailSendingProducer emailProducer;

    @Autowired
    public KeycloakEventListenerProvider(EmailSendingProducer emailProducer) {
        this.emailProducer = emailProducer;
    }

    public KeycloakEventListenerProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public void onEvent(Event event) {
        if (EventType.SEND_VERIFY_EMAIL.equals(event.getType())) {
            String email = event.getDetails().get("email");
            String token = event.getDetails().get("token");

            RealmModel realm = session.getContext().getRealm();
            String verificationUrl = "%s/auth/realms/%s/login-actions/action-token?key=%s"
                    .formatted(serverUrl, realm.getName(), token);

            emailProducer.sendEmail(new VerificationEmailMessage(email, verificationUrl));
        }
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean includeRepresentation) {
    }

    @Override
    public void close() {
    }
}
