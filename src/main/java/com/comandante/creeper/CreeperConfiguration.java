package com.comandante.creeper;

import org.apache.commons.configuration.Configuration;

public class CreeperConfiguration {

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

    public CreeperConfiguration(Configuration configuration) {
        this.telnetPort = configuration.getInt(TELNET_PORT, TELNET_PORT_DEFAULT);
        this.databaseFileName = configuration.getString(DATABASE_FILE_NAME, DATABASE_FILE_NAME_DEFAULT);
        this.mudName = configuration.getString(MUD_NAME, MUD_NAME_DEFAULT);
        this.graphiteHostname = configuration.getString(GRAPHITE_HOSTNAME, GRAPHITE_HOSTNAME_DEFAULT);
        this.graphitePort = configuration.getInt(GRAPHITE_PORT, GRAPHITE_PORT_DEFAULT);
        this.isProduction = configuration.getBoolean(IS_PRODUCTION, IS_PRODUCTION_DEFAULT);
    }
}
