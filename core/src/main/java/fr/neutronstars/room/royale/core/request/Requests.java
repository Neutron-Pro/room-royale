package fr.neutronstars.room.royale.core.request;

import fr.neutronstars.room.royale.core.RoomRoyale;
import fr.neutronstars.room.royale.core.request.handler.RequestHandler;
import fr.neutronstars.room.royale.core.request.message.Request;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Requests {
    private final Map<Class<? extends Request>, RequestHandler<?>> handlerMap = new HashMap<>();
    private final Queue<Request> requests = new ConcurrentLinkedDeque<>();
    private final Logger logger;

    public Requests(Logger logger) {
        this.logger = logger;
    }

    public <T extends Request> Requests register(Class<T> clazz, RequestHandler<T> handler) {
        this.handlerMap.put(clazz, handler);
        return this;
    }

    public void add(Request request) {
        this.requests.add(request);
    }

    public void handle(RoomRoyale roomRoyale) {
        Request request;

        int count = 0;

        while ((request = this.requests.poll()) != null) {
            final RequestHandler handler = this.handlerMap.get(request.getClass());
            if (handler != null) {
                try {
                    handler.handle(roomRoyale, request);
                    count++;
                } catch (Throwable throwable) {
                    this.logger.error(throwable.getMessage(), throwable);
                }
            }
        }
        if (count > 0) {
            this.logger.trace("{} handler(s) called !", count);
        }
    }
}
