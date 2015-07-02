package com.comandante.creeper.managers;

import net.kencochrane.raven.Dsn;
import net.kencochrane.raven.Raven;
import net.kencochrane.raven.RavenFactory;
import net.kencochrane.raven.event.Event;
import net.kencochrane.raven.event.EventBuilder;
import net.kencochrane.raven.event.interfaces.ExceptionInterface;

public class SentryManager {

    private static Raven raven = RavenFactory.ravenInstance(new Dsn("https://f6888b8b68384ae8a6e5809f8fb08a6d:e2e3dbaaaa8d40639c87d7b16a1539f9@app.getsentry.com/47336"));

    public static void logSentry(Class c, Exception e, String msg) {
        EventBuilder eventBuilder = new EventBuilder()
                .setMessage(msg)
                .setLevel(Event.Level.ERROR)
                .setLogger(c.getName())
                .addSentryInterface(new ExceptionInterface(e));

        raven.runBuilderHelpers(eventBuilder);
        raven.sendEvent(eventBuilder.build());
    }

    public static void logSentry(Class c, Throwable e, String msg) {
        EventBuilder eventBuilder = new EventBuilder()
                .setMessage(msg)
                .setLevel(Event.Level.ERROR)
                .setLogger(c.getName())
                .addSentryInterface(new ExceptionInterface(e));

        raven.runBuilderHelpers(eventBuilder);
        raven.sendEvent(eventBuilder.build());
    }
}
