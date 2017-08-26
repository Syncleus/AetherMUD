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
package com.syncleus.aethermud.player;

import com.google.gson.GsonBuilder;
import org.junit.Test;

public class CoolDownTest {


    @Test
    public void testSerialization() throws Exception {


        CoolDown coolDown = new CoolDown(CoolDownType.DETAINMENT);

        String s = new GsonBuilder().create().toJson(coolDown, CoolDown.class);

        System.out.println(s);


    }

}
