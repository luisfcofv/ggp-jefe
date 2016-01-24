package org.ggp.base.server.event;

import org.ggp.base.util.observer.Event;

import java.io.Serializable;

@SuppressWarnings("serial")
public final class ServerAbortedMatchEvent extends Event implements Serializable
{
    public ServerAbortedMatchEvent()
    {

    }
}
