package fr.adrackiin.hydranium.uhc.game.settings;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.events.PostInitEvent;
import fr.adrackiin.hydranium.uhc.UHCCore;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.border.UHCBorder;
import fr.adrackiin.hydranium.uhc.game.core.hblocks.blockmanagers.HBlock;
import fr.adrackiin.hydranium.uhc.game.uhc.AppleDrop;
import fr.adrackiin.hydranium.uhc.game.uhc.GoldenHead;
import fr.adrackiin.hydranium.uhc.utils.PubSub;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionType;

public class Settings implements Listener{

    private HorseHeal horseHealSetting;
    private HorseItems horseItemsSetting;
    private MobsSpawn mobsSpawnSetting;
    private FireNether fireNetherSetting;
    private LavaNether lavaNetherSetting;
    private PotionsDisabler potionsDisablerSetting;
    private Absorption absorptionSetting;
    private DayNightCycle dayNightCycleSetting;
    private EnderPearlDamage enderPearlDamageSetting;
    private Strength strengthSetting;
    private FriendlyFire friendlyFireSetting;
    private NotchApple notchAppleSetting;
    private GoldenHead craftGoldenHeadSetting;
    private AppleDrop appleDropSetting;
    private byte flintRate = 10;
    private byte team = 2;
    private short maxPlayers = 80;
    private int maxWhitelists = 15;
    private int maxSpec = 5;
    private byte invulnerability = 45;
    private byte finalHeal = 3;
    private short pvp = 20;
    private short border = 40;
    private short meetup = 20;
    private UHCBorder.Type borderType = UHCBorder.Type.BORDER_TP;
    private short size = 800;
    private short finalSize = 30;
    private boolean gapple = true;
    private boolean ghead = false;
    private boolean dropHead = true;
    private DropHead howDropHead = DropHead.FENCE;
    private boolean shears = true;
    private boolean absorption = true;
    private boolean dayNightCycle = true;
    private boolean enderPearlDamage = true;
    private boolean flintSteelNether = true;
    private boolean lavaBucketNether = true;
    private boolean friendlyFire = true;
    private boolean horseHeal = true;
    private boolean horseArmor = true;
    private boolean horseSaddle = true;
    private boolean notchApple = false;
    private boolean nether = true;
    private boolean protectionNetherPortal = true;
    private boolean craftGoldenHead = true;
    private short strength = (short)70;
    private boolean nerfStrength = true;
    private boolean allTrees = true;
    private float appleRate = 0.5F;
    private boolean digdown = false;
    private boolean tower = false;
    private boolean build = true;
    private boolean trap = true;
    private boolean pokeHoling = false;
    private boolean stripMining = false;
    private boolean soundMining = true;
    private boolean rollerCoaster = true;
    private boolean ipvp = false;
    private boolean steal = true;
    private boolean stalk = false;
    private boolean crossteam = false;
    public Settings(){
        UHCCore.getPlugin().getServer().getPluginManager().registerEvents(this, UHCCore.getPlugin());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPostLoad(PostInitEvent e){
        horseHealSetting = new HorseHeal();
        horseItemsSetting = new HorseItems();
        mobsSpawnSetting = new MobsSpawn();
        fireNetherSetting = new FireNether();
        lavaNetherSetting = new LavaNether();
        potionsDisablerSetting = new PotionsDisabler();
        absorptionSetting = new Absorption();
        dayNightCycleSetting = new DayNightCycle();
        enderPearlDamageSetting = new EnderPearlDamage();
        strengthSetting = new Strength();
        friendlyFireSetting = new FriendlyFire();
        notchAppleSetting = new NotchApple();
        craftGoldenHeadSetting = new GoldenHead();
        appleDropSetting = new AppleDrop();
        HandlerList.unregisterAll(this);
    }

    public DropHead howDropHead() {
        return howDropHead;
    }

    public boolean isPotion(PotionType type){
        return potionsDisablerSetting.isPotion(type);
    }

    public boolean isPotionLevel(PotionType type){
        return potionsDisablerSetting.isLevel(type);
    }

    public boolean isPotionDuration(PotionType type){
        return potionsDisablerSetting.isDuration(type);
    }

    public boolean isPotionSplash(PotionType type){
        return potionsDisablerSetting.isSplash(type);
    }

    public void setPotion(PotionType type, boolean potion){
        potionsDisablerSetting.setPotion(type, potion);
        UHCCore.getPlugin().getRulesCmd().updatePotions();
    }

    public void setPotionLevel(PotionType type, boolean potion){
        potionsDisablerSetting.setLevel(type, potion);
        UHCCore.getPlugin().getRulesCmd().updatePotions();
    }

    public void setPotionDuration(PotionType type, boolean potion){
        potionsDisablerSetting.setDuration(type, potion);
        UHCCore.getPlugin().getRulesCmd().updatePotions();
    }

    public void setPotionSplash(PotionType type, boolean potion){
        potionsDisablerSetting.setSplash(type, potion);
        UHCCore.getPlugin().getRulesCmd().updatePotions();
    }

    public boolean isMobForbid(EntityType type) {
        return mobsSpawnSetting.isMobForbid(type);
    }

    public byte getTeam() {
        return team;
    }

    public void setTeam(byte team) {
        this.team = team;
        for(APPlayer pl : APICore.getPlugin().getAPPlayers()){
            if(team > 1){
                pl.addPermission("hydranium.uhc.command.team");
                pl.addPermission("hydranium.uhc.command.coords");
            } else {
                pl.removePermission("hydranium.uhc.command.team");
                pl.removePermission("hydranium.uhc.command.coords");
            }
        }
        UHCCore.getPlugin().getRulesCmd().updateConfig();
        PubSub.teamUpdate();
    }

    public short getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(short maxPlayers) {
        this.maxPlayers = maxPlayers;
        UHCCore.getPlugin().getRulesCmd().updateConfig();
        PubSub.maxPlayersUpdate();
    }

    public int getMaxWhitelists() {
        return maxWhitelists;
    }

    public void setMaxWhitelists(int maxWhitelists) {
        this.maxWhitelists = maxWhitelists;
    }

    public int getMaxSpec() {
        return maxSpec;
    }

    public void setMaxSpec(int maxSpec) {
        this.maxSpec = maxSpec;
    }

    public byte getInvulnerability() {
        return invulnerability;
    }

    public void setInvulnerability(byte invulnerability) {
        this.invulnerability = invulnerability;
    }

    public byte getFinalHeal() {
        return finalHeal;
    }

    public void setFinalHeal(byte finalHeal) {
        this.finalHeal = finalHeal;
        UHCCore.getPlugin().getRulesCmd().updateTime();
    }

    public short getPvp() {
        return pvp;
    }

    public void setPvp(short pvp) {
        this.pvp = pvp;
        Game.getGame().getScoreboardManager().setPvp(pvp * 60);
        UHCCore.getPlugin().getRulesCmd().updateTime();
        PubSub.pvpUpdate();
    }

    public short getBorder() {
        return border;
    }

    public void setBorder(short border) {
        this.border = border;
        Game.getGame().getScoreboardManager().setBorder(border * 60);
        UHCCore.getPlugin().getRulesCmd().updateTime();
        PubSub.borderUpdate();
    }

    public short getMeetup() {
        return meetup;
    }

    public void setMeetup(short meetup) {
        this.meetup = meetup;
        UHCCore.getPlugin().getRulesCmd().updateTime();
    }

    public UHCBorder.Type getBorderType() {
        return borderType;
    }

    public void setBorderType(UHCBorder.Type borderType) {
        this.borderType = borderType;
        UHCCore.getPlugin().getRulesCmd().updateBorder();
    }

    public short getSize() {
        return size;
    }

    public void setSize(short size) {
        this.size = size;
        Game.getGame().getScoreboardManager().setActualSize(size);
        Game.getGame().getWorldUHC().getBorder().setSize();
        Game.getGame().getWorldNether().getBorder().setSize();
        Game.getGame().getWorldEnder().getBorder().setSize();
        UHCCore.getPlugin().getRulesCmd().updateBorder();
    }

    public short getFinalSize() {
        return finalSize;
    }

    public void setFinalSize(short finalSize) {
        this.finalSize = finalSize;
        UHCCore.getPlugin().getRulesCmd().updateBorder();
    }

    public boolean isShears() {
        return shears;
    }

    public void setShears(boolean shears) {
        this.shears = shears;
        UHCCore.getPlugin().getRulesCmd().updateOther();
    }

    public boolean isDigdown() {
        return digdown;
    }

    public void setDigdown(boolean digdown) {
        this.digdown = digdown;
        UHCCore.getPlugin().getRulesCmd().updateRules();
    }

    public boolean isTower() {
        return tower;
    }

    public void setTower(boolean tower) {
        this.tower = tower;
        UHCCore.getPlugin().getRulesCmd().updateRules();
    }

    public boolean isBuild() {
        return build;
    }

    public void setBuild(boolean build) {
        this.build = build;
        UHCCore.getPlugin().getRulesCmd().updateRules();
    }

    public boolean isTrap() {
        return trap;
    }

    public void setTrap(boolean trap) {
        this.trap = trap;
        UHCCore.getPlugin().getRulesCmd().updateRules();
    }

    public boolean isPokeHoling() {
        return pokeHoling;
    }

    public void setPokeHoling(boolean pokeHoling) {
        this.pokeHoling = pokeHoling;
        UHCCore.getPlugin().getRulesCmd().updateRules();
    }

    public boolean isStripMining() {
        return stripMining;
    }

    public void setStripMining(boolean stripMining) {
        this.stripMining = stripMining;
        UHCCore.getPlugin().getRulesCmd().updateRules();
    }

    public boolean isSoundMining() {
        return soundMining;
    }

    public void setSoundMining(boolean soundMining) {
        this.soundMining = soundMining;
        UHCCore.getPlugin().getRulesCmd().updateRules();
    }

    public boolean isRollerCoaster() {
        return rollerCoaster;
    }

    public void setRollerCoaster(boolean rollerCoaster) {
        this.rollerCoaster = rollerCoaster;
        UHCCore.getPlugin().getRulesCmd().updateRules();
    }

    public boolean isIpvp() {
        return ipvp;
    }

    public void setIpvp(boolean ipvp) {
        this.ipvp = ipvp;
        UHCCore.getPlugin().getRulesCmd().updateRules();
    }

    public boolean isSteal() {
        return steal;
    }

    public void setSteal(boolean steal) {
        this.steal = steal;
        UHCCore.getPlugin().getRulesCmd().updateRules();
    }

    public boolean isStalk() {
        return stalk;
    }

    public void setStalk(boolean stalk) {
        this.stalk = stalk;
        UHCCore.getPlugin().getRulesCmd().updateRules();
    }

    public boolean isCrossteam() {
        return crossteam;
    }

    public void setCrossteam(boolean crossteam) {
        this.crossteam = crossteam;
        UHCCore.getPlugin().getRulesCmd().updateRules();
    }

    public boolean isGapple() {
        return gapple;
    }

    public void setGapple(boolean gapple) {
        this.gapple = gapple;
        UHCCore.getPlugin().getRulesCmd().updateDrops();
    }

    public boolean isGhead() {
        return ghead;
    }

    public void setGhead(boolean ghead) {
        this.ghead = ghead;
        UHCCore.getPlugin().getRulesCmd().updateDrops();
    }

    public boolean isDropHead() {
        return this.dropHead;
    }

    public void setDropHead(boolean dropHead) {
        this.dropHead = dropHead;
        UHCCore.getPlugin().getRulesCmd().updateDrops();
    }

    public boolean isNether(){
        return this.nether;
    }

    public void setNether(boolean nether){
        this.nether = nether;
        UHCCore.getPlugin().getRulesCmd().updateNether();
    }

    public boolean isNetherPortalProtection(){
        return this.protectionNetherPortal;
    }

    public void setNetherPortalProtection(boolean protectionNetherPortal){
        this.protectionNetherPortal = protectionNetherPortal;
    }

    public boolean isFlintSteelNether(){
        return this.flintSteelNether;
    }

    public void setFlintSteelNether(boolean flintsteelNether){
        this.flintSteelNether = flintsteelNether;
        if(flintsteelNether){
            fireNetherSetting.deactivate();
        } else {
            fireNetherSetting.activate();
        }
        UHCCore.getPlugin().getRulesCmd().updateNether();
    }

    public boolean isLavaBucketNether(){
        return this.lavaBucketNether;
    }

    public void setLavaBucketNether(boolean lavaBucketNether){
        this.lavaBucketNether = lavaBucketNether;
        if(lavaBucketNether){
            lavaNetherSetting.deactivate();
        } else {
            lavaNetherSetting.activate();
        }
        UHCCore.getPlugin().getRulesCmd().updateNether();
    }

    public boolean isEnderPearlDamage(){
        return this.enderPearlDamage;
    }

    public void setEnderPearlDamage(boolean enderPearlDamage){
        this.enderPearlDamage = enderPearlDamage;
        if(enderPearlDamage){
            enderPearlDamageSetting.deactivate();
        } else {
            enderPearlDamageSetting.activate();
        }
        UHCCore.getPlugin().getRulesCmd().updateOther();
    }

    public boolean isNotchApple(){
        return this.notchApple;
    }

    public void setNotchApple(boolean notchApple){
        this.notchApple = notchApple;
        if(notchApple){
            notchAppleSetting.deactivate();
        } else {
            notchAppleSetting.activate();
        }
        UHCCore.getPlugin().getRulesCmd().updateOther();
    }

    public boolean isAbsorption(){
        return this.absorption;
    }

    public void setAbsorption(boolean absorption){
        this.absorption = absorption;
        if(absorption){
            absorptionSetting.deactivate();
        } else {
            absorptionSetting.activate();
        }
        UHCCore.getPlugin().getRulesCmd().updateOther();
    }

    public boolean isFriendlyFire(){
        return this.friendlyFire;
    }

    public void setFriendlyFire(boolean friendlyFire){
        this.friendlyFire = friendlyFire;
        if(friendlyFire){
            friendlyFireSetting.deactivate();
            Game.getGame().getTeamManager().setFriendlyFire(true);
        } else {
            friendlyFireSetting.activate();
            Game.getGame().getTeamManager().setFriendlyFire(false);
        }
        UHCCore.getPlugin().getRulesCmd().updateOther();
    }

    public boolean isDayNightCycle(){
        return this.dayNightCycle;
    }

    public void setDayNightCycle(boolean dayNightCycle){
        this.dayNightCycle = dayNightCycle;
        UHCCore.getPlugin().getRulesCmd().updateOther();
    }

    public boolean isCraftGoldenHead() {
        return this.craftGoldenHead;
    }

    public void setCraftGoldenHead(boolean craftGoldenHead){
        this.craftGoldenHead = craftGoldenHead;
        if(craftGoldenHead){
            craftGoldenHeadSetting.activate();
        } else {
            craftGoldenHeadSetting.deactivate();
        }
        UHCCore.getPlugin().getRulesCmd().updateOther();
    }

    public boolean isHorseHeal(){
        return this.horseHeal;
    }

    public void setHorseHeal(boolean horseHeal){
        this.horseHeal = horseHeal;
        if(horseHeal){
            horseHealSetting.deactivate();
        } else {
            horseHealSetting.activate();
        }
        UHCCore.getPlugin().getRulesCmd().updateMobs();
    }

    public boolean isHorseArmor(){
        return this.horseArmor;
    }

    public void setHorseArmor(boolean horseArmor){
        this.horseArmor = horseArmor;
        horseItemsSetting.setArmor(horseArmor);
        UHCCore.getPlugin().getRulesCmd().updateMobs();
    }

    public boolean isHorseSaddle(){
        return this.horseSaddle;
    }

    public void setHorseSaddle(boolean horseSaddle){
        this.horseSaddle = horseSaddle;
        horseItemsSetting.setSaddle(horseSaddle);
        UHCCore.getPlugin().getRulesCmd().updateMobs();
    }

    public short getStrength() {
        return this.strength;
    }

    public void setStrength(short strength) {
        this.strength = strength;
    }

    public boolean isAllTrees() {
        return allTrees;
    }

    public void setAllTrees(boolean allTrees) {
        this.allTrees = allTrees;
        appleDropSetting.setAllTrees();
        UHCCore.getPlugin().getRulesCmd().updateRate();
    }

    public float getAppleRate() {
        return appleRate;
    }

    public void setAppleRate(float appleRate) {
        this.appleRate = appleRate;
        appleDropSetting.setAppleRate(appleRate);
        UHCCore.getPlugin().getRulesCmd().updateRate();
    }

    public byte getFlintRate() {
        return flintRate;
    }

    public void setFlintRate(byte flintRate) {
        this.flintRate = flintRate;
        Game.getGame().getUHCManager().getUHCBlock(HBlock.Type.GRAVEL).getDrops().changeAmount(Material.GRAVEL, new short[]{(short) (10000 - flintRate * 100)});
        Game.getGame().getUHCManager().getUHCBlock(HBlock.Type.GRAVEL).getDrops().changeFortune(Material.GRAVEL, new short[][]{{(short)(10000 - (flintRate * 140))}, {(short)(10000 - (flintRate * 250))}, {(short)(0)}});
        UHCCore.getPlugin().getRulesCmd().updateRate();
    }

    public boolean isNerfStrength() {
        return nerfStrength;
    }

    public void setNerfStrength(boolean nerfStrength) {
        this.nerfStrength = nerfStrength;
        if(nerfStrength){
            strengthSetting.activate();
        } else {
            strengthSetting.deactivate();
        }
    }

    public GoldenHead getCraftGoldenHeadSetting(){
        return craftGoldenHeadSetting;
    }

    public void setHowDropHead(DropHead howDropHead) {
        this.howDropHead = howDropHead;
    }

    public void setMob(EntityType type){
        mobsSpawnSetting.setMob(type);
        UHCCore.getPlugin().getRulesCmd().updateMobs();
    }

    public enum DropHead {

        GROUND,
        FENCE

    }

}
