/**
 * Copyright 2017 Syncleus, Inc.
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
package com.syncleus.aethermud.server.communication;

import org.fusesource.jansi.Ansi;

public final class Color {
    public static final String YELLOW = new Ansi().fg(Ansi.Color.YELLOW).toString();
    public static final String DEFAULT = new Ansi().fg(Ansi.Color.DEFAULT).toString();
    public static final String WHITE = new Ansi().fg(Ansi.Color.WHITE).toString();
    public static final String GREEN = new Ansi().fg(Ansi.Color.GREEN).toString();
    public static final String CYAN = new Ansi().fg(Ansi.Color.CYAN).toString();
    public static final String BLACK = new Ansi().fg(Ansi.Color.BLACK).toString();
    public static final String BLUE = new Ansi().fg(Ansi.Color.BLUE).toString();
    public static final String MAGENTA = new Ansi().fg(Ansi.Color.MAGENTA).toString();
    public static final String RED = new Ansi().fg(Ansi.Color.RED).toString();
    public static final String RESET = new Ansi().reset().toString();
    public static final String BOLD_ON = new Ansi().bold().toString();
    public static final String BOLD_OFF = new Ansi().boldOff().toString();
}
