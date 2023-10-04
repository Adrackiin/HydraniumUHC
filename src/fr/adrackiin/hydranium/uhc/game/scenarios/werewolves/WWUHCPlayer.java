package fr.adrackiin.hydranium.uhc.game.scenarios.werewolves;

import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles.Roles;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;

public class WWUHCPlayer {

    private final UUID uuid;
    private Roles role;
    private boolean isLover = false;
    private boolean isInfected = false;
    private boolean hasVoted = false;
    private int vote = 0;
    private boolean alive;
    private UUID killer = null;
    private final UHCPlayer player;

    public WWUHCPlayer(UUID uuid) {
        this.uuid = uuid;
        this.player = Game.getGame().getUHCPlayer(uuid);
    }

    public void resetVote() {
        this.vote = 0;
        hasVoted = false;
    }

    public void addVote(int vote) {
        this.vote += vote;
    }

    public void removePotionEffect(PotionEffectType effect) {
        player.getAPPlayer().removePotionEffect(effect);
    }

    public void addPotionEffect(PotionEffectType effect, int i, int i1, boolean b) {
        player.getAPPlayer().addPotionEffect(effect, i, i1, b);
    }

    public void sendMessage(String s) {
        player.getAPPlayer().sendMessage(s);
    }

    public void give(List<ItemStack> itemStacks) {
        player.getAPPlayer().give(itemStacks);
    }

    public UUID getUuid(){
        return uuid;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        if(getRole() != null){
            getRole().getClassRole().getMembers().remove(player.getAPPlayer().getUUID());
        }
        this.role = role;
        this.role.getClassRole().getMembers().add(uuid);
    }

    public boolean isLover() {
        return isLover;
    }

    public void setLover(boolean lover) {
        isLover = lover;
    }

    public boolean isInfected() {
        return isInfected;
    }

    public void setInfected(boolean infected) {
        isInfected = infected;
    }

    public int getVote() {
        return vote;
    }

    public boolean isHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public UUID getKiller() {
        return killer;
    }

    public void setKiller(UUID killer) {
        this.killer = killer;
    }

    public UHCPlayer getUHCPlayer(){
        return player;
    }

    public String getName() {
        return player.getAPPlayer().getName();
    }

    public Player getPlayer() {
        return player.getAPPlayer().getPlayer();
    }

    public void setMaxHealth(double maxHealth) {
        player.getAPPlayer().setMaxHealth(maxHealth);
    }
}
