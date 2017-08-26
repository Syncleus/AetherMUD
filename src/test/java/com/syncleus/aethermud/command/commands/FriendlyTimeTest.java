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

import com.syncleus.aethermud.common.FriendlyTime;
import org.junit.Test;


public class FriendlyTimeTest {

    @Test
    public void testFriendlyParsing() throws Exception {

        FriendlyTime friendlyTime = new FriendlyTime(400);

        System.out.println("Friendly Long: " + friendlyTime.getFriendlyFormatted());

        System.out.println("Friendly Short: " + friendlyTime.getFriendlyFormattedShort());
    }

}
