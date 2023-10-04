package fr.adrackiin.hydranium.uhc.events;

import fr.adrackiin.hydranium.uhc.game.core.players.UHCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class UHCPlayerDeathEvent extends Event implements Cancellable{

    private static final HandlerList handlers = new HandlerList();
    private final UHCPlayer death;
    private final UHCPlayer killer;
    private final EntityType killerPVE;
    private final boolean offline;
    private final EntityDamageEvent.DamageCause damageCause;
    private final UHCPlayer.DeathCause deathCause;
    private final ItemStack[] loots;
    private final ItemStack[] armorLoots;
    private final String deathMessage;
    private final boolean cancellable;
    private boolean cancelled;
    private boolean keepInventory;

    public UHCPlayerDeathEvent(UHCPlayer death, UHCPlayer killer, EntityType killerPVE, boolean offline, EntityDamageEvent.DamageCause damageCause, UHCPlayer.DeathCause deathCause , boolean keepInventory, ItemStack[] loots, ItemStack[] armorLoots, boolean cancellable){
        this.death = death;
        this.killer = killer;
        this.killerPVE = killerPVE;
        this.offline = offline;
        this.damageCause = damageCause;
        this.deathCause = deathCause;
        this.keepInventory = keepInventory;
        this.loots = loots;
        this.armorLoots = armorLoots;
        this.cancellable = cancellable;
        this.deathMessage = setDeathMessage();
        effect();
    }

    public UHCPlayer getDeath() {
        return death;
    }

    public UHCPlayer getKiller() {
        return killer;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isOffline() {
        return offline;
    }

    public EntityDamageEvent.DamageCause getDamageCause() {
        return damageCause;
    }

    public UHCPlayer.DeathCause getDeathCause() {
        return deathCause;
    }

    public boolean isKeepInventory() {
        return keepInventory;
    }

    public void setKeepInventory(boolean keepInventory) {
        this.keepInventory = keepInventory;
    }

    public ItemStack[] getLoots() {
        return loots;
    }

    public ItemStack[] getArmorLoots() {
        return armorLoots;
    }

    public boolean isCancellable() {
        return cancellable;
    }

    public EntityType getKillerPVE() {
        return killerPVE;
    }

    public String getDeathMessage() {
        return deathMessage;
    }

    private void effect(){
        Location locDeath = this.getDeath().getAPPlayer().getLocation();
        if(locDeath == null){
            locDeath = this.getDeath().getOfflineLocation();
        }
        int x = locDeath.getBlockX();
        int z = locDeath.getBlockZ();
        for(Player p : Bukkit.getOnlinePlayers()){
            int x2 = x - p.getLocation().getBlockX();
            int z2 = z - p.getLocation().getBlockZ();
            if (Math.sqrt(x2 * x2 + z2 * z2) < 100.0D) {
                p.playSound(p.getLocation(), Sound.EXPLODE, 10.0F, 0.6F);
            }
        }
        locDeath.getWorld().strikeLightningEffect(locDeath);
    }

    private String setDeathMessage(){
        switch(this.getDamageCause()) {
            case VOID:
                return this.getDeath().getAPPlayer().getName() + " est mystérieusement tombé dans le vide...";
            case FIRE:
                return this.getDeath().getAPPlayer().getName() + " a succombé aux flammes" + (this.getKiller() == null ? "" : ", après avoir été attaqué par " + this.getKiller().getAPPlayer().getName());
            case FALL:
                return this.getDeath().getAPPlayer().getName() + " n'a pas su amortir sa chute" + (this.getKiller() == null ? "" : ", après avoir été poussé par " + this.getKiller().getAPPlayer().getName());
            case LAVA:
                return this.getDeath().getAPPlayer().getName() + " n'a pas supporté la chaleur de la lave" + (this.getKiller() == null ? "" : " dans laquelle " + this.getKiller().getAPPlayer().getName() + " l'a envoyé");
            case MAGIC:
                return this.getDeath().getAPPlayer().getName() + " a été tué par un sorcier ou une sorcière" + (this.getKiller() == null ? "" : ", qui n'est autre que " + this.getKiller().getAPPlayer().getName());
            case POISON:
                return this.getDeath().getAPPlayer().getName() + " s'est fait empoisonné" + (this.getKiller() == null ? "" : " par " + this.getKiller().getAPPlayer().getName());
            case THORNS:
                return this.getDeath().getAPPlayer().getName() + " s'est fait piqué" + (this.getKiller() == null ? "" : " en attaquant " + this.getKiller().getAPPlayer().getName());
            case WITHER:
                return this.getDeath().getAPPlayer().getName() + " a été anéanti par une créature maléfique" + (this.getKiller() == null ? "" : " ainsi que " + this.getKiller().getAPPlayer().getName());
            case CONTACT:
                return this.getDeath().getAPPlayer().getName() + " s'est attaché à un mauvais bloc" + (this.getKiller() == null ? "" : ", après que " + this.getKiller().getAPPlayer().getName() + " lui ait présenté");
            case MELTING:
                return this.getDeath().getAPPlayer().getName() + " est mort fondu" + (this.getKiller() == null ? "" : ", après avoir été réchauffé par " + this.getKiller().getAPPlayer().getName());
            case SUICIDE:
                return this.getDeath().getAPPlayer().getName() + " s'est suicidé :'(";
            case DROWNING:
                return this.getDeath().getAPPlayer().getName() + " n'a pas résisté à l'appel de la mer" + (this.getKiller() == null ? "" : ", à cause de " + this.getKiller().getAPPlayer().getName());
            case FIRE_TICK:
                return this.getDeath().getAPPlayer().getName() + " n'a pas trouvé d'eau" + (this.getKiller() == null ? "" : ", après avoir été enflammé par " + this.getKiller().getAPPlayer().getName());
            case LIGHTNING:
                return this.getDeath().getAPPlayer().getName() + " a été tué par Odin en personne" + (this.getKiller() == null ? "" : ", puisque " + this.getKiller().getAPPlayer().getName() + " l'a invoqué");
            case PROJECTILE:
                return this.getDeath().getAPPlayer().getName() + " s'est fait tirer dessus" + (this.getKiller() == null ? (this.getKillerPVE() == null ? "" : " par un(e) " + this.getKillerPVE().getName()) : " par " + this.getKiller().getAPPlayer().getName());
            case STARVATION:
                return this.getDeath().getAPPlayer().getName() + " n'a pas trouvé de quoi manger" + (this.getKiller() == null ? "" : ", après avoir été laissé pour mort par " + this.getKiller().getAPPlayer().getName());
            case SUFFOCATION:
                return this.getDeath().getAPPlayer().getName() + " a été enfermé dans un bloc" + (this.getKiller() == null ? "" : " par " + this.getKiller().getAPPlayer().getName());
            case ENTITY_ATTACK:
                return this.getDeath().getAPPlayer().getName() + " a été tué" + (this.getKiller() == null ? (this.getKillerPVE() == null ? "" : " par un(e) " + this.getKillerPVE().getName()) : " par " + this.getKiller().getAPPlayer().getName());
            case FALLING_BLOCK:
                return this.getDeath().getAPPlayer().getName() + " a reçu un coup fatal sur la tête" + (this.getKiller() == null ? "" : ", qui a été envoyé par " + this.getKiller().getAPPlayer().getName());
            case BLOCK_EXPLOSION:
            case ENTITY_EXPLOSION:
                return this.getDeath().getAPPlayer().getName() + " s'est fait exploser" + (this.getKiller() == null ? "" : " par " + this.getKiller().getAPPlayer().getName());
            case CUSTOM:
                if(this.getDeathCause() != null) {
                    switch(this.getDeathCause()){
                        case OFFLINE:
                            return this.getDeath().getAPPlayer().getName() + " est mort de déconnexion" + (this.getKiller() == null ? "" : ", à cause de " + this.getKiller().getAPPlayer().getName());
                        case DARKNESS:
                            return this.getDeath().getAPPlayer().getName() + " est passé du côté obscur" + (this.getKiller() == null ? "" : ", à cause de " + this.getKiller().getAPPlayer().getName());
                    }
                }
        }
        return "Nom: " + this.getDeath().getAPPlayer().getName() + ". Taille: 1.80m. Mort: Inconnue";
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
