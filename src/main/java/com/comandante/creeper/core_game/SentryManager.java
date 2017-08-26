/**
 * Copyright 2017 Syncleus, Inc.
 * with portions copyright 2004-2017 Bo Zimmerman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.comandante.creeper.core_game;

public class SentryManager {

   // private static Raven raven = RavenFactory.ravenInstance(new Dsn("https://f6888b8b68384ae8a6e5809f8fb08a6d:e2e3dbaaaa8d40639c87d7b16a1539f9@app.getsentry.com/47336"));

    public static void logSentry(Class c, Exception e, String msg) {
      /*  EventBuilder eventBuilder = new EventBuilder()
                .setMessage(msg)
                .setLevel(Event.Level.ERROR)
                .setLogger(c.getName())
                .addSentryInterface(new ExceptionInterface(e));

        raven.runBuilderHelpers(eventBuilder);
        raven.sendEvent(eventBuilder.build());*/
    }

    public static void logSentry(Class c, Throwable e, String msg) {
        /*
        EventBuilder eventBuilder = new EventBuilder()
                .setMessage(msg)
                .setLevel(Event.Level.ERROR)
                .setLogger(c.getName())
                .addSentryInterface(new ExceptionInterface(e));

        raven.runBuilderHelpers(eventBuilder);
        raven.sendEvent(eventBuilder.build());*/
    }
}
