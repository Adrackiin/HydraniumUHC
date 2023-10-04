package fr.adrackiin.hydranium.uhc.utils.pluginmessage;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.management.data.Data;
import fr.adrackiin.hydranium.uhc.UHCCore;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PluginMessageManager {

    private static final String BUNGEECORD_CHANNEL = "BungeeCord";

    public static void registerChannels(UHCCore uhcCore){
        uhcCore.getServer().getMessenger().registerIncomingPluginChannel(uhcCore, BUNGEECORD_CHANNEL, new PluginMessageReceived());
        uhcCore.getServer().getMessenger().registerOutgoingPluginChannel(uhcCore, BUNGEECORD_CHANNEL);
    }

    public static void connect(UUID uuid){
        APPlayer player = APICore.getPlugin().getAPPlayer(uuid);
        APICore.getPlugin().getDataManager().set(uuid, new Data("stats", player.getStats().serialize()), new Data("settings", player.getSettings().serialize()));

        final ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Connect");
        out.writeUTF("lobby");

        player.getPlayer().sendPluginMessage(UHCCore.getPlugin(), BUNGEECORD_CHANNEL, out.toByteArray());
    }

    public static void receivePluginMessage(UHCCore uhcCore, String channel, Player player, byte[] bytes){
        if(!channel.equals(PluginMessageManager.BUNGEECORD_CHANNEL)){
            return;
        }

        final ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        final String subChannel = in.readUTF();
    
        if(subChannel.equals("Connect")){
        } else {
            uhcCore.logServer("Sub channel inexistant: " + subChannel);
        }
    }
}
