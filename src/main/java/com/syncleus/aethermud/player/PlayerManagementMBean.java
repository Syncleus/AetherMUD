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

import java.util.Map;

public interface PlayerManagementMBean {

    void setMarkForDelete(boolean isMark);

    boolean getMarkForDelete();

    int getGold();

    int getGoldInBankAmount();

    void setGoldInBankAmount(int amt);

    void setGold(int amt);

    int getHealth();

    int getMana();

    void setHealth(int amt);

    void setMana(int amt);

    void sendAdminMessage(String message);

    void setExperience(int amt);

    int getExperience();

    void setRoles(String roles);

    String getRoles();

    Map<String, String> getInventory();

    String getPassword();

    void setPassword(String password);

    Map<String, String> getLockerInventory();

    String createItemInInventory(String internalItemName);

    void setPlayerClass(String playerClassName);

    String getPlayerClass();

    void clearAllCoolDowns();

    void detain();
}
