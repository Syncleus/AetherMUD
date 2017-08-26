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
package com.syncleus.aethermud.command.commands;


import com.syncleus.aethermud.core.GameManager;
import com.syncleus.aethermud.server.communication.Color;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import java.util.Arrays;
import java.util.List;

public class ColorsCommand extends Command {

    final static List<String> validTriggers = Arrays.asList("colors");
    final static String description = "Display available color examples.";
    final static String correctUsage = "colors";

    public ColorsCommand(GameManager gameManager) {
        super(gameManager, validTriggers, description, correctUsage);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        execCommand(ctx, e, () -> {
            write("BLACK: " + Color.BLACK + "This is an example of the color." + Color.RESET + "\r\n");
            write("BLUE: " + Color.BLUE + "This is an example of the color." + Color.RESET + "\r\n");
            write("CYAN: " + Color.CYAN + "This is an example of the color." + Color.RESET + "\r\n");
            write("GREEN: " + Color.GREEN + "This is an example of the color." + Color.RESET + "\r\n");
            write("MAGENTA: " + Color.MAGENTA + "This is an example of the color." + Color.RESET + "\r\n");
            write("RED: " + Color.RED + "This is an example of the color." + Color.RESET + "\r\n");
            write("WHITE: " + Color.WHITE + "This is an example of the color." + Color.RESET + "\r\n");
            write("YELLOW: " + Color.YELLOW + "This is an example of the color." + Color.RESET + "\r\n");
            write("\r\n\r\nBOLD COLORS\r\n");
            write("BLACK: " + Color.BOLD_ON + Color.BLACK + "This is an example of the color." + Color.RESET + "\r\n");
            write("BLUE: " + Color.BOLD_ON + Color.BLUE + "This is an example of the color." + Color.RESET + "\r\n");
            write("CYAN: " + Color.BOLD_ON + Color.CYAN + "This is an example of the color." + Color.RESET + "\r\n");
            write("GREEN: " + Color.BOLD_ON + Color.GREEN + "This is an example of the color." + Color.RESET + "\r\n");
            write("MAGENTA: " + Color.BOLD_ON + Color.MAGENTA + "This is an example of the color." + Color.RESET + "\r\n");
            write("RED: " + Color.BOLD_ON + Color.RED + "This is an example of the color." + Color.RESET + "\r\n");
            write("WHITE: " + Color.BOLD_ON + Color.WHITE + "This is an example of the color." + Color.RESET + "\r\n");
            write("YELLOW: " + Color.BOLD_ON + Color.YELLOW + "This is an example of the color." + Color.RESET + "\r\n");
        });
    }
}
