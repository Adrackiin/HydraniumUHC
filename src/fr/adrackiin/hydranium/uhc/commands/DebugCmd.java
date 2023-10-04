package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.events.UHCPlayerBreakBlockEvent;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.hblocks.blockmanagers.HBlock;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class DebugCmd implements APCommandListener, Listener {

    private final APCommand command;
    private Player player;

    public DebugCmd(){
        this.command = new APCommand(
                "debug",
                new String[]{},
                "Réservé pour les tests",
                new String[]{},
                "hydranium.uhc.command.debug"
        ).setInvisible();
        APICore.getPlugin().getCommandManager().addCommand(this);
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
        /*APICore.getPlugin().getPacketManager().addPacketListener(APPacket.Context.WRITE, new APPacketListener() {
            @Override
            public Object onPacket(Player player, Packet packet){
                if(packet instanceof PacketPlayOutWindowData){
                    try {
                        Field f = packet.getClass().getDeclaredField("a");
                        Field f2 = packet.getClass().getDeclaredField("b");
                        Field f3 = packet.getClass().getDeclaredField("c");
                        f.setAccessible(true);
                        f2.setAccessible(true);
                        f3.setAccessible(true);
                        System.out.println(f.get(packet) + " " + f2.get(packet) + " " + f3.get(packet));
                    } catch(NoSuchFieldException | IllegalAccessException e){
                        e.printStackTrace();
                    }
                }
                return packet;
            }
        });*/
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length == 1){
            return 2;
        }
        return 1;
    }

    @Override
    public String onCommand(String command, APPlayer pl, String[] args, short syntax){
        switch(syntax){
            case 1:
                for(Player p : Bukkit.getOnlinePlayers()){
                    for(Player p1 : Bukkit.getOnlinePlayers()){
                        p.hidePlayer(p1);
                    }
                }
                for(Player p : Bukkit.getOnlinePlayers()){
                    for(Player p1 : Bukkit.getOnlinePlayers()){
                        p.showPlayer(p1);
                    }
                }
                /*ItemStack item =  ItemCreator.simpleItem(Material.DIAMOND_SWORD);
                ItemMeta meta = item.getItemMeta();
                meta.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
                item.setItemMeta(meta);
                ((CraftPlayer)Bukkit.getPlayer("DeuxFelinsIci")).getHandle().playerConnection.sendPacket(PacketUtils.packetEquipementChange(Bukkit.getPlayer("Adrackiin"), (short)0, item));
                */
                break;
            case 2:
                HBlock.Type type = HBlock.Type.valueOf(args[0].toUpperCase());
                HBlock hBlock = Game.getGame().getUHCManager().getUHCBlock(type);
                UHCPlayer player = Game.getGame().getUHCPlayer(pl.getUUID());
                for(byte x = 0; x < 10; x ++) {
                    for(byte y = 10; y < 20; y++) {
                        for(byte z = 0; z < 10; z++){
                            pl.getLocation().getWorld().getBlockAt(x, y, z).setType(type.getType());
                            pl.getLocation().getWorld().getBlockAt(x, y, z).setData((byte) type.getDatas()[0]);
                        }
                    }
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> {
                    for(byte x = 0; x < 100; x ++) {
                        for(byte y = 0; y < 100; y++) {
                            for(byte z = 0; z < 100; z++){
                                if(pl.getLocation().getWorld().getBlockAt(x, y, z).getType() == type.getType()){
                                    UHCCore.getPlugin().getServer().getPluginManager().callEvent(new UHCPlayerBreakBlockEvent(player, pl.getLocation().getWorld().getBlockAt(x, y, z), false, hBlock.getDrops(), 0));
                                }
                            }
                        }
                    }
                }, 20L);

                Bukkit.getScheduler().scheduleSyncDelayedTask(UHCCore.getPlugin(), ()-> {
                    for(Entity entity : pl.getLocation().getWorld().getEntities()) {
                        entity.teleport(pl.getLocation());
                    }
                }, 30L);
                break;
        }
        return null;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(player == null){
            player = e.getPlayer();
        }
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
