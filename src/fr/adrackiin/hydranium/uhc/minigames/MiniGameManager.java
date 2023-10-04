package fr.adrackiin.hydranium.uhc.minigames;

import fr.adrackiin.hydranium.api.events.PostInitEvent;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.minigames.shifumi.Shifumi;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class MiniGameManager implements Listener {

    private Shifumi shifumi;

    public MiniGameManager(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPostinit(PostInitEvent e){
        this.shifumi = new Shifumi();
    }

    public Shifumi getShifumi(){
        return shifumi;
    }
}
