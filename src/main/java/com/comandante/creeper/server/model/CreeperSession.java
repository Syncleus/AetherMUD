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
package com.comandante.creeper.server.model;

import com.comandante.creeper.command.commands.Command;
import com.comandante.creeper.common.CreeperEntry;
import com.comandante.creeper.merchant.Merchant;
import com.google.common.base.Optional;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.util.UUID;

public class CreeperSession {

    private java.util.Optional<String> username = java.util.Optional.empty();
    private Optional<String> password = Optional.absent();
    private boolean isAuthed = false;
    private java.util.Optional<CreeperEntry<UUID, Command>> grabMultiLineInput = java.util.Optional.empty();
    private java.util.Optional<CreeperEntry<Merchant, SimpleChannelUpstreamHandler>> grabMerchant = java.util.Optional.empty();
    private String lastMessage;
    private final Long initialLoginTime;
    private Long lastActivity;

    public CreeperSession() {
        long currentTime = System.currentTimeMillis();
        this.initialLoginTime = currentTime;
        this.lastActivity = currentTime;
    }

    public State state;

    public enum State {
        promptedForPassword,
        promptedForUsername,
        newUserPromptedForUsername,
        newUserPromptedForPassword,
        newUserRegCompleted,
        authed
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public java.util.Optional<String> getUsername() {
        return username;
    }

    public void setUsername(java.util.Optional<String> username) {
        this.username = username;
    }

    public Optional<String> getPassword() {
        return password;
    }

    public void setPassword(Optional<String> password) {
        this.password = password;
    }

    public boolean isAuthed() {
        return isAuthed;
    }

    public void setAuthed(boolean isAuthed) {
        this.isAuthed = isAuthed;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public java.util.Optional<CreeperEntry<UUID, Command>> getGrabMultiLineInput() {
        return grabMultiLineInput;
    }

    public void setGrabMultiLineInput(java.util.Optional<CreeperEntry<UUID, Command>> grabMultiLineInput) {
        this.grabMultiLineInput = grabMultiLineInput;
    }

    public java.util.Optional<CreeperEntry<Merchant, SimpleChannelUpstreamHandler>> getGrabMerchant() {
        return grabMerchant;
    }

    public void setGrabMerchant(java.util.Optional<CreeperEntry<Merchant, SimpleChannelUpstreamHandler>> grabMerchant) {
        this.grabMerchant = grabMerchant;
    }

    public Long getInitialLoginTime() {
        return initialLoginTime;
    }

    public Long getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(Long lastActivity) {
        this.lastActivity = lastActivity;
    }
}

