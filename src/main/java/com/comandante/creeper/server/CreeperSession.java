package com.comandante.creeper.server;

import com.comandante.creeper.CreeperEntry;
import com.comandante.creeper.command.Command;
import com.comandante.creeper.merchant.Merchant;
import com.google.common.base.Optional;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class CreeperSession {

    private java.util.Optional<String> username = java.util.Optional.empty();
    private Optional<String> password = Optional.absent();
    private boolean isAuthed = false;
    private AtomicBoolean isAbleToDoAbility = new AtomicBoolean(false);
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

    State state;

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

    public void setIsAbleToDoAbility(boolean b) {
        this.isAbleToDoAbility.set(b);
    }

    public boolean IsAbleToDoAbility() {
        return this.isAbleToDoAbility.get();
    }

    public java.util.Optional<CreeperEntry<UUID, Command>> getGrabMultiLineInput() {
        return grabMultiLineInput;
    }

    public void setGrabMultiLineInput(java.util.Optional<CreeperEntry<UUID, Command>> grabMultiLineInput) {
        this.grabMultiLineInput = grabMultiLineInput;
    }

    public AtomicBoolean getIsAbleToDoAbility() {
        return isAbleToDoAbility;
    }

    public void setIsAbleToDoAbility(AtomicBoolean isAbleToDoAbility) {
        this.isAbleToDoAbility = isAbleToDoAbility;
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

