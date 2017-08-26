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
package com.syncleus.aethermud.command.commands;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class UseItemOnTest {

    @Test
    public void testUseItemOnParseWithNoTargetSpecified() {
        String itemTrigger = "a spell book wow.";
        String fullCommand = "use " + itemTrigger;
        List<String> originalMessageParts = Command.getOriginalMessageParts(fullCommand);
        UseCommand.UseItemOn useItemOn = new UseCommand.UseItemOn(originalMessageParts);
        Assert.assertTrue(itemTrigger.equals(useItemOn.getItem()));
    }

    @Test
    public void testUseItemOnParseWithNoTargetSpecifiedButOnIs() {
        String itemTrigger = "a spell book wow.";
        String fullCommand = "use " + itemTrigger + " on   ";
        List<String> originalMessageParts = Command.getOriginalMessageParts(fullCommand);
        UseCommand.UseItemOn useItemOn = new UseCommand.UseItemOn(originalMessageParts);
        Assert.assertTrue(itemTrigger.equals(useItemOn.getItem()));
    }

    @Test
    public void testUseItemOnParseWithTargetSpecifiedHasOnInName() {
        String itemTrigger = "a spell book wow.";
        String itemTarget = "BONONONIGBOGCHRNGVONJAONL";
        String fullCommand = "use " + itemTrigger + " on " + itemTarget;
        List<String> originalMessageParts = Command.getOriginalMessageParts(fullCommand);
        UseCommand.UseItemOn useItemOn = new UseCommand.UseItemOn(originalMessageParts);
        Assert.assertTrue(itemTrigger.equals(useItemOn.getItem()));
        Assert.assertTrue(itemTarget.equals(useItemOn.getTarget().get()));
    }

    @Test
    public void testUseItemOnParseWithTargetSpecifiedRandomCasing() {
        String itemTrigger = "a spell book wow.";
        String itemTarget = "big bad wolf";
        String fullCommand = "use " + itemTrigger + " oN " + itemTarget;
        List<String> originalMessageParts = Command.getOriginalMessageParts(fullCommand);
        UseCommand.UseItemOn useItemOn = new UseCommand.UseItemOn(originalMessageParts);
        Assert.assertTrue(itemTrigger.equals(useItemOn.getItem()));
        Assert.assertTrue(itemTarget.equals(useItemOn.getTarget().get()));
    }

}
