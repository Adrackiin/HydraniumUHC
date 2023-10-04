package fr.adrackiin.hydranium.uhc.game.settings;

import fr.adrackiin.hydranium.uhc.UHCCore;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class PotionsDisabler extends Configurable implements Listener {

    private final HashMap<PotionRecipe, PotionCraft> craft = new HashMap<>();
    private final ArrayList<PotionType> disabledPotion = new ArrayList<>();
    private final ArrayList<PotionType> disabledSplash = new ArrayList<>();
    private final ArrayList<PotionType> disabledLevel = new ArrayList<>();
    private final ArrayList<PotionType> disabledDuration = new ArrayList<>();

    public PotionsDisabler(){
        activate();
        PotionRecipe temp = new PotionRecipe(Material.SUGAR);
        craft.put(temp, new PotionCraft(temp, PotionType.SPEED));
        temp = new PotionRecipe(Material.BLAZE_POWDER);
        craft.put(temp, new PotionCraft(temp, PotionType.STRENGTH));
        temp = new PotionRecipe(Material.MAGMA_CREAM);
        craft.put(temp, new PotionCraft(temp, PotionType.FIRE_RESISTANCE));
        temp = new PotionRecipe(Material.SPECKLED_MELON);
        craft.put(temp, new PotionCraft(temp, PotionType.INSTANT_HEAL));
        temp = new PotionRecipe(Material.GHAST_TEAR);
        craft.put(temp, new PotionCraft(temp, PotionType.REGEN));
        temp = new PotionRecipe(Material.FERMENTED_SPIDER_EYE, PotionType.NIGHT_VISION);
        craft.put(temp, new PotionCraft(temp, PotionType.INVISIBILITY));
        temp = new PotionRecipe(Material.SPIDER_EYE);
        craft.put(temp, new PotionCraft(temp, PotionType.POISON));
        temp = new PotionRecipe(Material.FERMENTED_SPIDER_EYE, PotionType.INSTANT_HEAL);
        craft.put(temp, new PotionCraft(temp, PotionType.INSTANT_DAMAGE));
        temp = new PotionRecipe(Material.FERMENTED_SPIDER_EYE, PotionType.POISON);
        craft.put(temp, new PotionCraft(temp, PotionType.INSTANT_DAMAGE));
        temp = new PotionRecipe(Material.FERMENTED_SPIDER_EYE);
        craft.put(temp, new PotionCraft(temp, PotionType.WEAKNESS));
        temp = new PotionRecipe(Material.FERMENTED_SPIDER_EYE, PotionType.SPEED);
        craft.put(temp, new PotionCraft(temp, PotionType.SLOWNESS));
        temp = new PotionRecipe(Material.FERMENTED_SPIDER_EYE, PotionType.JUMP);
        craft.put(temp, new PotionCraft(temp, PotionType.SLOWNESS));
        ArrayList<Material> allIngredients = new ArrayList<>();
        allIngredients.addAll(Arrays.asList(Material.SUGAR, Material.BLAZE_POWDER, Material.MAGMA_CREAM, Material.SPECKLED_MELON, Material.GHAST_TEAR, Material.FERMENTED_SPIDER_EYE, Material.SPIDER_EYE, Material.POTION));
    }

    @Override
    public void activate() {
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @Override
    public void deactivate() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPotion(BrewEvent e){
            PotionType[] potionTypes = new PotionType[3];
            boolean[] returns = new boolean[]{false, false, false};
            for(byte i = 0; i < 3; i ++){
                if(e.getContents().getContents()[i] == null){
                    potionTypes[i] = null;
                } else {
                    if(Potion.fromItemStack(e.getContents().getContents()[i]).getType() == null){
                        potionTypes[i] = PotionType.WATER;
                    } else {
                        potionTypes[i] = Potion.fromItemStack(e.getContents().getContents()[i]).getType();
                    }
                }
                switch(e.getContents().getContents()[3].getType()){
                    case REDSTONE:
                        if(disabledDuration.contains(potionTypes[i])){
                            e.setCancelled(true);
                            return;
                        }
                        break;
                    case GLOWSTONE_DUST:
                        if(disabledLevel.contains(potionTypes[i])){
                            e.setCancelled(true);
                            return;
                        }
                        break;
                    case SULPHUR:
                        if(disabledSplash.contains(potionTypes[i])){
                            e.setCancelled(true);
                            return;
                        }
                        break;
                    default:
                        break;
                }
                if(craft.get(new PotionRecipe(e.getContents().getContents()[3].getType(), potionTypes[i])) == null){
                    returns[i] = true;
                }
            }
            if(returns[0] && returns[1] && returns[2]){
                return;
            }
            if((!returns[0] && disabledPotion.contains(craft.get(new PotionRecipe(e.getContents().getContents()[3].getType(), potionTypes[0])).getResult())) ||
                    (!returns[1] && disabledPotion.contains(craft.get(new PotionRecipe(e.getContents().getContents()[3].getType(), potionTypes[1])).getResult())) ||
                    (!returns[2] && disabledPotion.contains(craft.get(new PotionRecipe(e.getContents().getContents()[3].getType(), potionTypes[2])).getResult()))){
                e.setCancelled(true);
            }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onConsumePotion(PlayerItemConsumeEvent e){
        if(e.getItem().getType() == Material.POTION){
            if(disabledPotion.contains(Potion.fromItemStack(e.getItem()).getType())){
                UHCCore.getPlugin().logServer("Potion obtenu: " + Potion.fromItemStack(e.getItem()).getType() + " " + e.getPlayer() + " " + e.getItem());
                e.setCancelled(true);
            }
            if(Potion.fromItemStack(e.getItem()).hasExtendedDuration() && disabledDuration.contains(Potion.fromItemStack(e.getItem()).getType())){
                UHCCore.getPlugin().logServer("Potion obtenu (Rallongée): " + Potion.fromItemStack(e.getItem()).getType() + " " + e.getPlayer() + " " + e.getItem());
                e.setCancelled(true);
            }
            if(Potion.fromItemStack(e.getItem()).getLevel() > 1 && disabledLevel.contains(Potion.fromItemStack(e.getItem()).getType())){
                UHCCore.getPlugin().logServer("Potion obtenu (Level): " + Potion.fromItemStack(e.getItem()).getType() + " " + e.getPlayer() + " " + e.getItem());
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSplash(PotionSplashEvent e){
        if(Potion.fromItemStack(e.getPotion().getItem()).isSplash() && disabledSplash.contains(Potion.fromItemStack(e.getPotion().getItem()).getType())){
            UHCCore.getPlugin().logServer("Potion obtenu (Splash): " + Potion.fromItemStack(e.getPotion().getItem()).getType());
            e.setCancelled(true);
        }
    }

    public boolean isPotion(PotionType type){
        return !disabledPotion.contains(type);
    }

    public boolean isLevel(PotionType type){
        return !disabledLevel.contains(type);
    }

    public boolean isDuration(PotionType type){
        return !disabledDuration.contains(type);
    }

    public boolean isSplash(PotionType type){
        return !disabledSplash.contains(type);
    }

    public void setPotion(PotionType type, boolean set){
        if(set){
            if(!disabledPotion.contains(type)){
                return;
            }
            disabledPotion.remove(type);
        } else {
            if(disabledPotion.contains(type)){
                return;
            }
            disabledPotion.add(type);
        }
        checkActive();
    }

    public void setLevel(PotionType type, boolean set){
        if(set){
            if(!disabledLevel.contains(type)){
                return;
            }
            disabledLevel.remove(type);
        } else {
            if(disabledLevel.contains(type)){
                return;
            }
            disabledLevel.add(type);
        }
        checkActive();
    }

    public void setDuration(PotionType type, boolean set){
        if(set){
            if(!disabledDuration.contains(type)){
                return;
            }
            disabledDuration.remove(type);
        } else {
            if(disabledDuration.contains(type)){
                return;
            }
            disabledDuration.add(type);
        }
        checkActive();
    }

    public void setSplash(PotionType type, boolean set){
        if(set){
            if(!disabledSplash.contains(type)){
                return;
            }
            disabledSplash.remove(type);
        } else {
            if(disabledSplash.contains(type)){
                return;
            }
            disabledSplash.add(type);
        }
        checkActive();
    }

    private void checkActive() {
        if(!disabledPotion.isEmpty() || !disabledDuration.isEmpty() || !disabledLevel.isEmpty() || !disabledSplash.isEmpty()){
            activate();
        }
        if(disabledPotion.isEmpty() && disabledDuration.isEmpty() && disabledLevel.isEmpty() && disabledSplash.isEmpty()){
            deactivate();
        }
    }

    public class PotionRecipe {

        private final Material ingredient;
        private final PotionType potion;

        public PotionRecipe(Material ingredient, PotionType... potion) {
            this.ingredient = ingredient;
            if(potion.length == 0){
                this.potion = PotionType.WATER;
            } else {
                this.potion = potion[0];
            }
        }

        public Material getIngredient() {
            return ingredient;
        }

        public PotionType getPotion() {
            return potion;
        }

        @Override
        public int hashCode() {
            return Objects.hash(ingredient, potion);
        }

        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            PotionRecipe that = (PotionRecipe) o;
            return ingredient == that.ingredient && potion == that.potion;
        }
    }

    public class PotionCraft {

        private final PotionRecipe recipe;
        private final PotionType result;

        public PotionCraft(PotionRecipe recipe, PotionType result) {
            this.recipe = recipe;
            this.result = result;
        }

        public PotionRecipe getRecipe() {
            return recipe;
        }

        public PotionType getResult() {
            return result;
        }
    }

}
