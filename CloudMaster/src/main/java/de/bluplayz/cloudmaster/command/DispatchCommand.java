package de.bluplayz.cloudmaster.command;

import de.bluplayz.CloudMaster;
import de.bluplayz.cloudlib.command.Command;
import de.bluplayz.cloudlib.packet.CommandSendPacket;
import de.bluplayz.cloudmaster.locale.LocaleAPI;
import de.bluplayz.cloudmaster.server.BungeeCordProxy;
import de.bluplayz.cloudmaster.server.SpigotServer;

import java.util.Arrays;
import java.util.UUID;

public class DispatchCommand extends Command {

    public DispatchCommand() {
        super( "Dispatch" );
    }

    @Override
    public void execute( String label, String[] args ) {
        // dispatch <servername> <commandline>
        if ( args.length < 2 ) {
            LocaleAPI.log( "command_dispatch_usage" );
            return;
        }

        String server = args[0];
        String commandline = Arrays.toString( args ).substring( server.length() + 3, Arrays.toString( args ).length() - 1 ).replaceAll( ", ", " " );

        SpigotServer spigotServer = this.getSpigotServer( server );
        BungeeCordProxy bungeeCordProxy = this.getBungeeCordProxy( server );

        if ( spigotServer != null ) {
            CommandSendPacket commandSendPacket = new CommandSendPacket( spigotServer.getName(), commandline );
            spigotServer.getCloudWrapper().sendPacket( commandSendPacket );

            LocaleAPI.log( "command_dispatch_success", spigotServer.getName(), commandline );
            return;
        }

        if ( bungeeCordProxy != null ) {
            CommandSendPacket commandSendPacket = new CommandSendPacket( bungeeCordProxy.getName(), commandline );
            bungeeCordProxy.getCloudWrapper().sendPacket( commandSendPacket );

            LocaleAPI.log( "command_dispatch_success", bungeeCordProxy.getName(), commandline );
            return;
        }

        LocaleAPI.log( "command_dispatch_server_not_exist", server );
    }

    private SpigotServer getSpigotServer( String server ) {
        if ( server.split( "-" ).length == 2 ) {
            // Check for name
            SpigotServer spigotServer = CloudMaster.getInstance().getServerManager().getServerByName( server );
            if ( spigotServer != null ) {
                return spigotServer;
            }
        } else {
            // Check for UUID
            UUID uuid;
            try {
                uuid = UUID.fromString( server );

                SpigotServer spigotServer = CloudMaster.getInstance().getServerManager().getServerByUUID( uuid );
                if ( spigotServer != null ) {
                    return spigotServer;
                }
            } catch ( IllegalArgumentException ignored ) {
            }
        }

        return null;
    }

    private BungeeCordProxy getBungeeCordProxy( String server ) {
        if ( server.split( "-" ).length == 2 ) {
            // Check for name
            BungeeCordProxy bungeeCordProxy = CloudMaster.getInstance().getServerManager().getProxyByName( server );
            if ( bungeeCordProxy != null ) {
                return bungeeCordProxy;
            }
        } else {
            // Check for UUID
            UUID uuid;
            try {
                uuid = UUID.fromString( server );

                BungeeCordProxy bungeeCordProxy = CloudMaster.getInstance().getServerManager().getProxyByUUID( uuid );
                if ( bungeeCordProxy != null ) {
                    return bungeeCordProxy;
                }
            } catch ( IllegalArgumentException ignored ) {
            }
        }

        return null;
    }
}
