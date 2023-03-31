package com.alisoft.hatim.config.websocket;

import com.alisoft.hatim.config.security.jwt.JwtTokenProvider;
import com.alisoft.hatim.exception.NotFoundException;
import com.alisoft.hatim.service.UserService;
import com.alisoft.hatim.service.WsService;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Objects;
import java.util.UUID;

@Component
@Slf4j
@Data
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final WsService wsService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("Web socket disconnected : " + event);
    }

    @SneakyThrows
    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event){
        String simpDestination = Objects.requireNonNull(event.getMessage().getHeaders().get("simpDestination")).toString();
        if (simpDestination.startsWith("/topic/") && simpDestination.endsWith("/list_of_juz")) {
            simpDestination = simpDestination.replace("/topic/" , "");
            simpDestination = simpDestination.replace("/list_of_juz", "");
            try {
                wsService.getJuzByHatim(UUID.fromString(simpDestination));
            } catch (NotFoundException e) {
                throw new NotFoundException(e.getMessage());
            }
        }
        if (simpDestination.startsWith("/topic/") && simpDestination.endsWith("/list_of_page")) {
            simpDestination = simpDestination.replace("/topic/" , "");
            simpDestination = simpDestination.replace("/list_of_page", "");
            try {
                wsService.getPagesByJuz(UUID.fromString(simpDestination));
            } catch (NotFoundException e) {
                throw new NotFoundException(e.getMessage());
            }
        }
        if (simpDestination.startsWith("/topic/") && simpDestination.endsWith("/user_pages")) {
            simpDestination = simpDestination.replace("/topic/" , "");
            simpDestination = simpDestination.replace("/user_pages", "");
            wsService.getUserPages(simpDestination);
        }
    }

}
