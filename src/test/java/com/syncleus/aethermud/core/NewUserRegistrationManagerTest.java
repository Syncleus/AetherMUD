/**
 * Copyright 2017 - 2018 Syncleus, Inc.
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
package com.syncleus.aethermud.core;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class NewUserRegistrationManagerTest {
    @Test
    public void isValidUsername() throws Exception {
        String username = StringUtils.repeat("*", 10);
        Assert.assertTrue(NewUserRegistrationManager.isValidUsername(username));

        String longUsername = StringUtils.repeat("W", NewUserRegistrationManager.MAX_USERNAME_LENGTH);
        Assert.assertTrue(NewUserRegistrationManager.isValidUsername(longUsername));

        String tooLongUsername = StringUtils.repeat("W", NewUserRegistrationManager.MAX_USERNAME_LENGTH + 1);
        Assert.assertFalse(NewUserRegistrationManager.isValidUsername(tooLongUsername));

        String tooShortUsername = StringUtils.repeat("W", NewUserRegistrationManager.MIN_USERNAME_LENGTH - 1);
        Assert.assertFalse(NewUserRegistrationManager.isValidUsername(tooShortUsername));

        String shortUsername = StringUtils.repeat("W", NewUserRegistrationManager.MIN_USERNAME_LENGTH);
        Assert.assertTrue(NewUserRegistrationManager.isValidUsername(shortUsername));
    }

}
