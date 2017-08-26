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
package com.syncleus.aethermud.configuration;

import org.apache.commons.configuration.Configuration;

public class AetherMudConfiguration {

    public static final String IS_PRODUCTION = "production";
    public static final boolean IS_PRODUCTION_DEFAULT = false;
    public final boolean isProduction;

    public static final String MUD_NAME = "mud.name";
    public static final String MUD_NAME_DEFAULT = "creeper";
    public final String mudName;

    public static final String TELNET_PORT = "telnet.port";
    public static final int TELNET_PORT_DEFAULT = 8080;
    public final int telnetPort;

    public static final String DATABASE_FILE_NAME = "database.file.name";
    public static final String DATABASE_FILE_NAME_DEFAULT = "creeper.mapdb";
    public final String databaseFileName;

    public static final String GRAPHITE_HOSTNAME = "graphite.hostname";
    public static final String GRAPHITE_HOSTNAME_DEFAULT = "127.0.0.1";
    public final String graphiteHostname;

    public static final String GRAPHITE_PORT = "graphite.port";
    public static final int GRAPHITE_PORT_DEFAULT = 2004;
    public final int graphitePort;

    public static final String IRC_SERVER = "irc.server";
    public static final String IRC_SERVER_DEFAULT = "Chicago.IL.US.Undernet.org";
    public final String ircServer;

    public static final String IRC_USERNAME = "irc.username";
    public static final String IRC_USERNAME_DEFAULT = "creeper";
    public final String ircUsername;

    public static final String IRC_NICKNAME = "irc.nickname";
    public static final String IRC_NICKNAME_DEFAULT = "creeper1";
    public final String ircNickname;

    public static final String IRC_CHANNEL = "irc.channel";
    public static final String IRC_CHANNEL_DEFAULT = "#creeper";
    public final String ircChannel;

    public static final String IRC_BRIDGE_ROOM_ID = "irc.bridge.room.id";
    public static final Integer IRC_BRIDGE_ROOM_ID_DEFAULT = 376;
    public final Integer ircBridgeRoomId;

    public static final String IS_IRC_ENABLED = "irc.enabled";
    public static final boolean IS_IRC_ENABLED_DEFAULT = true;
    public final boolean isIrcEnabled;

    public static final String DEFAULT_MAP_SIZE = "map.size.default";
    public static final int DEFAULT_MAP_SIZE_DEFAULT = 14;
    public final int defaultMapSize;


    public static final String FORAGE_RATELIMIT_PER_SECOND = "forage.ratelimit.per.second";
    public static final double FORAGE_RATELIMIT_PER_SECOND_DEFAULT = 1.5;
    public final double forageRateLimitPerSecond;

    public static final String WEATHER_UNDERGROUND_API_KEY = "weather.underground.api.key";
    public static final String WEATHER_UNDERGROUND_API_KEY_DEFAULT = "62651e7a5762cda8";
    public final String weatherUndergroundApiKey;

    public static final String MAX_GOSSIP_CACHE_SIZE = "max.gossip.cache.size";
    public static final int MAX_GOSSIP_CACHE_SIZE_DEFAULT = 1000;
    public final int maxGossipCacheSize;

    public AetherMudConfiguration(Configuration configuration) {
        this.telnetPort = configuration.getInt(TELNET_PORT, TELNET_PORT_DEFAULT);
        this.databaseFileName = configuration.getString(DATABASE_FILE_NAME, DATABASE_FILE_NAME_DEFAULT);
        this.mudName = configuration.getString(MUD_NAME, MUD_NAME_DEFAULT);
        this.graphiteHostname = configuration.getString(GRAPHITE_HOSTNAME, GRAPHITE_HOSTNAME_DEFAULT);
        this.graphitePort = configuration.getInt(GRAPHITE_PORT, GRAPHITE_PORT_DEFAULT);
        this.isProduction = configuration.getBoolean(IS_PRODUCTION, IS_PRODUCTION_DEFAULT);
        this.ircServer = configuration.getString(IRC_SERVER, IRC_SERVER_DEFAULT);
        this.ircUsername = configuration.getString(IRC_USERNAME, IRC_USERNAME_DEFAULT);
        this.ircNickname = configuration.getString(IRC_NICKNAME, IRC_NICKNAME_DEFAULT);
        this.ircChannel = configuration.getString(IRC_CHANNEL, IRC_CHANNEL_DEFAULT);
        this.isIrcEnabled = configuration.getBoolean(IS_IRC_ENABLED, IS_IRC_ENABLED_DEFAULT);
        this.ircBridgeRoomId = configuration.getInteger(IRC_BRIDGE_ROOM_ID, IRC_BRIDGE_ROOM_ID_DEFAULT);
        this.defaultMapSize = configuration.getInteger(DEFAULT_MAP_SIZE, DEFAULT_MAP_SIZE_DEFAULT);
        this.forageRateLimitPerSecond = configuration.getDouble(FORAGE_RATELIMIT_PER_SECOND, FORAGE_RATELIMIT_PER_SECOND_DEFAULT);
        this.weatherUndergroundApiKey = configuration.getString(WEATHER_UNDERGROUND_API_KEY, WEATHER_UNDERGROUND_API_KEY_DEFAULT);
        this.maxGossipCacheSize = configuration.getInt(MAX_GOSSIP_CACHE_SIZE, MAX_GOSSIP_CACHE_SIZE_DEFAULT);
    }
}
