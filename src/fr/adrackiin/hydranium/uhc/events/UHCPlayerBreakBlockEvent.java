package fr.adrackiin.hydranium.uhc.events;

import fr.adrackiin.hydranium.uhc.game.core.hblocks.blockmanagers.Drops;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UHCPlayerBreakBlockEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final UHCPlayer player;
    private final Block block;
    private final boolean breakByPlayer;
    private int exp;
    private Drops drops;

    public UHCPlayerBreakBlockEvent(UHCPlayer player, Block block, boolean breakByPlayer, Drops drops, int exp) {
        this.player = player;
        this.block = block;
        this.breakByPlayer = breakByPlayer;
        this.drops = drops;
        this.exp = exp;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public UHCPlayer getPlayer() {
        return player;
    }

    public Block getBlock() {
        return block;
    }

    public Drops getDrops() {
        return drops;
    }

    public void setDrops(Drops drops) {
        this.drops = drops;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public boolean isBreakByPlayer() {
        return breakByPlayer;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
