package fr.adrackiin.hydranium.uhc.utils.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Players {

    @SuppressWarnings("deprecation")
    public static OfflinePlayer getOfflinePlayer(String name){
        return Bukkit.getOfflinePlayer(name);
    }

}
