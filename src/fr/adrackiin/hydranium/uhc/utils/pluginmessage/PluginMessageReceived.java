package fr.adrackiin.hydranium.uhc.utils.pluginmessage;

import fr.adrackiin.hydranium.uhc.UHCCore;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PluginMessageReceived implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        PluginMessageManager.receivePluginMessage(UHCCore.getPlugin(), channel, player, bytes);

    }
}
