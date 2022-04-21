package com.samyyc.invader.feature;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

public interface Feature {
    void hook(EventNode<Event> node);
}
