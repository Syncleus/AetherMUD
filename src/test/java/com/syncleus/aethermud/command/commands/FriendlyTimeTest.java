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
import org.junit.Assert;
import org.junit.Test;


public class FriendlyTimeTest {
    private static final long RAW_TIME = 400;
    private static final String EXPECTED_FRIENDLY_TIME = "6 minutes and 40 seconds";
    private static final String EXPECTED_FRIENDLY_TIME_SHORT = "6m40s";

    @Test
    public void testFriendlyParsing() throws Exception {

        FriendlyTime friendlyTime = new FriendlyTime(RAW_TIME);

        Assert.assertEquals(EXPECTED_FRIENDLY_TIME, friendlyTime.getFriendlyFormatted());

        Assert.assertEquals(EXPECTED_FRIENDLY_TIME_SHORT, friendlyTime.getFriendlyFormattedShort());
    }

}
