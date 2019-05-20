package com.kkbill.mockzhihu.async;

import java.util.List;

public interface EventHandler {
    void doHandler(EventModel eventModel);
    List<EventType> getSupportEventTypes();
}
