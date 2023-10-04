package fr.adrackiin.hydranium.uhc.commands;

import fr.adrackiin.hydranium.api.APICore;
import fr.adrackiin.hydranium.api.commands.manager.APCommand;
import fr.adrackiin.hydranium.api.commands.manager.APCommandListener;
import fr.adrackiin.hydranium.api.core.APPlayer;
import fr.adrackiin.hydranium.api.utils.APText;
import fr.adrackiin.hydranium.uhc.game.Game;
import fr.adrackiin.hydranium.uhc.game.border.UHCBorder;
import fr.adrackiin.hydranium.uhc.game.scenarios.managers.Scenario;
import fr.adrackiin.hydranium.uhc.game.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionType;

import java.util.UUID;

public class RulesCmd implements APCommandListener {

    private final APCommand command;
    private final Settings settings = Game.getGame().getSettings();

    private final APText generalRules = new APText(" §c» §cRègles générales");
    private final APText config = new APText(" §e» §6Configuration");
    private final APText time = new APText(" §e» §6Temps");
    private final APText border = new APText(" §e» §6Bordure");
    private final APText rate = new APText(" §e» §6Taux");
    private final APText mobs = new APText(" §e» §6Mobs");
    private final APText setting = new APText(" §e» §6Paramètres");
    private final APText drops = new APText(" §e» §6Drops au kill");
    private final APText nether = new APText(" §e» §6Nether");
    private final APText potions = new APText(" §e» §6Potions");
    private final APText rules = new APText(" §e» §6Règles");

    public RulesCmd(){
        this.command = new APCommand(
                "rules",
                new String[]{"regles", "rule", "regle", "game", "scen", "scenario"},
                "Afficher les règles de l'UHC",
                new String[]{},
                "hydranium.uhc.command.rules"
        );
        APICore.getPlugin().getCommandManager().addCommand(this);
        updateBorder();
        updateConfig();
        updateDrops();
        updateGeneralRules();
        updateMobs();
        updateNether();
        updateOther();
        updatePotions();
        updateRate();
        updateRules();
        updateTime();
    }



    public void updateGeneralRules(){
        generalRules.showText("§8§m----------§6 Règles générales §8§m----------\n" +
                " §7» Les insultes, spam... sont passibles d'un §cmute§7\n" +
                " §7» Toute forme de §cCheat §7(et autre avantages) est §cinterdite§7\n" +
                " §7» Le ToggleSneak est toléré §chors des inventaires§7\n" +
                " §7» Le PvPLog/Suicide/Brûler son stuff est §cinterdit§7\n" +
                " §7» Nous nous réservons le droit de sanctionner sans préavis \n" +
                "   §7si ces règles ou celles de l'UHC ne sont pas respectées !");
    }
    
    public void updateConfig(){
        config.showText("§8§m----------§6 Configuration §8§m----------\n" +
                " §e» §7Teams: §6" + (Game.getGame().isTeam() ? "To" + settings.getTeam() : "FFA") + "\n" +
                " §e» §7Slots: §6" + settings.getMaxPlayers());
    }
    
    public void updateTime(){
        time.showText("§8§m----------§6 Temps §8§m----------\n" +
                " §e» §7Final Heal: §6" + settings.getFinalHeal() + "§7 minutes\n" +
                " §e» §7PvP: §6" + settings.getPvp() + "§7 minutes\n" +
                " §e» §7Bordure: §6" + settings.getBorder() + "§7 minutes\n" +
                " §e» §7Temps de réduction: §6" + settings.getMeetup() + "§7 minutes");
    }
    
    public void updateBorder(){
        border.showText("§8§m----------§6 Bordure §8§m----------\n" +
                " §e» §7Type: §6" + (settings.getBorderType() == UHCBorder.Type.BORDERMOVE ? "En mouvement" : "Téléportation\n" +
                " §e» §7Taille: §6±" + settings.getSize() + " §7blocs\n" +
                " §e» §7Taille Finale: §6±" + settings.getFinalSize() + " §7blocs"));
    }
    
    public void updateRate(){
        rate.showText("§8§m----------§6 Taux §8§m----------\n" +
                " §e» §7Pommes: " + (settings.isAllTrees() ? "§bTous les arbres" : "§bChênes seulement") + "\n" +
                " §e» §7Pommes: §6" + settings.getAppleRate() + "§7%\n" +
                " §e» §7Silex: §6" + settings.getFlintRate() + "§7%");
    }
    
    public void updateMobs(){
        mobs.showText("§8§m----------§6 Mobs §8§m----------\n" +
                " §e» §7Chevaux: " + (settings.isMobForbid(EntityType.HORSE) ? "§cDésactivés" : "§aActivés") + "\n" +
                "  §e» §7Selles: " + (settings.isHorseSaddle() ? "§aActivés" : "§cDésactivés") + "\n" +
                "  §e» §7Armures: " + (settings.isHorseArmor() ? "§aActivés" : "§cDésactivés") + "\n" +
                "  §e» §7Heal: " + (settings.isHorseHeal() ? "§aActivé" : "§cDésactivé"));
    }
    
    public void updateOther(){
        setting.showText("§8§m----------§6 Paramètres §8§m----------\n" +
                " §e» §7Cisailles: §6" + (settings.isShears() ? "§aactivés" : "§cdésactivés") + "\n" +
                " §e» §7Dégâts Enderperle: §6" + (settings.isEnderPearlDamage() ? "§aactivés" : "§cdésactivés") + "\n" +
                (Game.getGame().isTeam() ? " §e» §7FriendlyFire: §6" + (settings.isFriendlyFire() ? "§aactivé" : "§cdésactivé") + "\n" : "") +
                " §e» §7Cycle Jour/Nuit: §6" + (settings.isDayNightCycle() ? "§aactivé" : "§cdésactivé") + "\n" +
                " §e» §7Pommes de Notch: §6" + (settings.isNotchApple() ? "§aactivés" : "§cdésactivés") + "\n" +
                " §e» §7Absorption: §6" + (settings.isAbsorption() ? "§aactivé" : "§cdésactivé") + "\n" +
                " §e» §7Craft Ghead: §6" + (settings.isCraftGoldenHead() ? "§aactivé" : "§cdésactivé"));
    }

    public void updateDrops(){
        drops.showText("§8§m----------§6 Drops au Kill §8§m----------\n" +
                " §e» §7Gapple: §6" + (settings.isGapple() ? "§aactivé" : "§cdésactivé") + "\n" +
                " §e» §7Ghead: §6" + (settings.isGhead() ? "§aactivé" : "§cdésactivé") + "\n" +
                " §e» §7Head: §6" + (settings.isDropHead() ? "§b" + (settings.howDropHead() == Settings.DropHead.FENCE ? "Sur poteau" : "Par terre") : "§cdésactivé"));
    }

    public void updateNether(){
        nether.showText("§8§m----------§6 Nether §8§m----------\n" +
                " §e» §7Nether: §6" + (settings.isNether() ? "§aactivé" : "§cdésactivé") + "\n" +
                " §e» §7Seau de lave: §6" + (settings.isLavaBucketNether() ? "§aactivé" : "§cdésactivé") + "\n" +
                " §e» §7Briquet: §6" + (settings.isFlintSteelNether() ? "§aactivé" : "§cdésactivé"));
    }

    public void updatePotions(){
        potions.showText("§8§m----------§6 Potions §8§m----------\n" +
                " §e» " + (settings.isPotion(PotionType.SPEED) ? "§7Vitesse (§aactivé§7)" +
                "  §f»  " + (settings.isPotionLevel(PotionType.SPEED) ? "§a" : "§c") + "Level II §f- §6" + (settings.isPotionDuration(PotionType.SPEED) ? "§a" : "§c") + "Rallongée §f- §6" + (settings.isPotionSplash(PotionType.SPEED) ? "§a" : "§c") + "Splash" : "§7Vitesse (§cdésactivé§7)") + "\n" +
                " §e» " + (settings.isPotion(PotionType.STRENGTH) ? "§7Force (§aactivé§7)" +
                "  §f»  " + (settings.isPotionLevel(PotionType.STRENGTH) ? "§a" : "§c") + "Level II §f- §6" + (settings.isPotionDuration(PotionType.STRENGTH) ? "§a" : "§c") + "Rallongée §f- §6" + (settings.isPotionSplash(PotionType.STRENGTH) ? "§a" : "§c") + "Splash" : "§7Force (§cdésactivé§7)") + "\n" +
                " §e» " + (settings.isPotion(PotionType.INSTANT_HEAL) ? "§7Soin (§aactivé§7)" +
                "  §f»  " + (settings.isPotionLevel(PotionType.INSTANT_HEAL) ? "§a" : "§c") + "Level II §f- §6" + (settings.isPotionSplash(PotionType.INSTANT_HEAL) ? "§a" : "§c") + "Splash" : "§7Soin (§cdésactivé§7)") + "\n" +
                " §e» " + (settings.isPotion(PotionType.REGEN) ? "§7Régénération (§aactivé§7)" +
                "  §f»  " + (settings.isPotionLevel(PotionType.REGEN) ? "§a" : "§c") + "Level II §f- §6" + (settings.isPotionDuration(PotionType.REGEN) ? "§a" : "§c") + "Rallongée §f- §6" + (settings.isPotionSplash(PotionType.REGEN) ? "§a" : "§c") + "Splash" : "§7Régénération (§cdésactivé§7)") + "\n" +
                " §e» " + (settings.isPotion(PotionType.FIRE_RESISTANCE) ? "§7Résistance au feu (§aactivé§7)" +
                "  §f»  " + (settings.isPotionDuration(PotionType.FIRE_RESISTANCE) ? "§a" : "§c") + "Rallongée §f- §6" + (settings.isPotionSplash(PotionType.FIRE_RESISTANCE) ? "§a" : "§c") + "Splash" : "§7Résistance au feu (§cdésactivé§7)") + "\n" +
                " §e» " + (settings.isPotion(PotionType.INVISIBILITY) ? "§7Invisibilité (§aactivé§7)" +
                "  §f»  " + (settings.isPotionDuration(PotionType.INVISIBILITY) ? "§a" : "§c") + "Rallongée §f- §6" + (settings.isPotionSplash(PotionType.INVISIBILITY) ? "§a" : "§c") + "Splash" : "§7Invisibilité (§cdésactivé§7)") + "\n" +
                " §e» " + (settings.isPotion(PotionType.INSTANT_DAMAGE) ? "§7Dégâts (§aactivé§7)" +
                "  §f»  " + (settings.isPotionLevel(PotionType.INSTANT_DAMAGE) ? "§a" : "§c") + "Level II §f- §6" + (settings.isPotionSplash(PotionType.INSTANT_DAMAGE) ? "§a" : "§c") + "Splash" : "§7Dégâts (§cdésactivé§7)") + "\n" +
                " §e» " + (settings.isPotion(PotionType.POISON) ? "§7Poison (§aactivé§7)" +
                "  §f»  " + (settings.isPotionLevel(PotionType.POISON) ? "§a" : "§c") + "Level II §f- §6" + (settings.isPotionDuration(PotionType.POISON) ? "§a" : "§c") + "Rallongée §f- §6" + (settings.isPotionSplash(PotionType.POISON) ? "§a" : "§c") + "Splash" : "§7Poison (§cdésactivé§7)") + "\n" +
                " §e» " + (settings.isPotion(PotionType.SLOWNESS) ? "§7Lenteur (§aactivé§7)" +
                "  §f»  " + (settings.isPotionLevel(PotionType.SLOWNESS) ? "§a" : "§c") + "Level II §f- §6" + (settings.isPotionDuration(PotionType.SLOWNESS) ? "§a" : "§c") + "Rallongée §f- §6" + (settings.isPotionSplash(PotionType.SLOWNESS) ? "§a" : "§c") + "Splash" : "§7Lenteur (§cdésactivé§7)") + "\n" +
                " §e» " + (settings.isPotion(PotionType.WEAKNESS) ? "§7Faiblesse (§aactivé§7)" +
                "  §f»  " + (settings.isPotionLevel(PotionType.WEAKNESS) ? "§a" : "§c") + "Level II §f- §6" + (settings.isPotionDuration(PotionType.WEAKNESS) ? "§a" : "§c") + "Rallongée §f- §6" + (settings.isPotionSplash(PotionType.WEAKNESS) ? "§a" : "§c") + "Splash" : "§7Faiblesse (§cdésactivé§7)"));
    }

    public void updateRules(){
        rules.showText("§8§m----------§6 Règles §8§m----------\n" +
                " §e» §7iPvP: §6" + (settings.isIpvp() ? "§aautorisé" : "§cinterdit") + "\n" +
                " §e» §7Steal: §6" + (settings.isSteal() ? "§aautorisé" : "§cinterdit") + "\n" +
                " §e» §7Stalk: §6" + (settings.isStalk() ? "§aautorisé" : "§cinterdit") + "\n" +
                " §e» §7CrossTeam: §6" + (settings.isCrossteam() ? "§aautorisé" : "§cinterdit") + "\n" +
                " §e» §7PokeHoling: §6" + (settings.isPokeHoling() ? "§aautorisé" : "§cinterdit") + "\n" +
                " §e» §7StripMining: §6" + (settings.isStripMining() ? "§aautorisé" : "§cinterdit") + "\n" +
                " §e» §7SoundMining: §6" + (settings.isSoundMining() ? "§aautorisé" : "§cinterdit") + "\n" +
                " §e» §7RollerCoaster: §6" + (settings.isRollerCoaster() ? "§aautorisé" : "§cinterdit") + "\n" +
                " §e» §7DigDown (Meetup): §6" + (settings.isDigdown() ? "§aautorisé" : "§cinterdit") + "\n" +
                " §e» §7Tower: §6" + (settings.isTower() ? "§aautorisé" : "§cinterdit") + "\n" +
                " §e» §7Build: §6" + (settings.isBuild() ? "§aautorisé" : "§cinterdit") + "\n" +
                " §e» §7Trap: §6" + (settings.isTrap() ? "§aautorisé" : "§cinterdit"));
    }

    @Override
    public int getSyntax(String[] args){
        if(args.length == 1){
            return 0;
        }
        return 1;
    }

    @Override
    public String onCommand(String command, APPlayer pl, String[] args, short syntax){
        if(args.length == 1 && pl.hasPermission("hydranium.uhc.command.rules.player")){
            if(args[0].equalsIgnoreCase("all")){
                syntax = 2;
            } else {
                if(Bukkit.getPlayer(args[0]) != null){
                    syntax = 3;
                }
            }
        }
        switch(syntax){
            case 0:
                if(pl.hasPermission("hydranium.api.command.rules.player")){
                    if(args[0].equalsIgnoreCase("all")){
                        return onCommand(command, pl, args, (short)2);
                    }
                    if(Bukkit.getPlayer(args[0]) != null){
                        return onCommand(command, pl, args, (short)3);
                    }
                }
            case 1:
                pl.sendMessage("§8§m---------- §c = UHC = §8§m----------");
                pl.sendMessage(" §7(Survolez le Texte pour les détails)");
                pl.sendMessage(generalRules);
                pl.sendMessage(config);
                pl.sendMessage(time);
                pl.sendMessage(border);
                pl.sendMessage(rate);
                pl.sendMessage(mobs);
                pl.sendMessage(setting);
                pl.sendMessage(drops);
                pl.sendMessage(nether);
                pl.sendMessage(potions);
                pl.sendMessage(rules);
                pl.sendMessage("§8§m---------- §c = Scénarios = §8§m----------");
                if(Game.getGame().getScenarios().isEmpty()){
                    pl.sendMessage(" §8» §cAucun scénario n'est activé");
                    return null;
                }
                for(Scenario scenario : Game.getGame().getScenarios().copy()){
                    pl.sendMessage(" §8» §c" + scenario.getName() + ": §7" + scenario.getDescription());
                }
                break;
            case 2:
                for(UUID uuid : Game.getGame().getAlivePlayers().copy()) {
                    APICore.getPlugin().getAPPlayer(uuid).performCommand("rules");
                }
                break;
            case 3:
                Game.getGame().getUHCPlayer(args[0]).getAPPlayer().performCommand("rules");
                break;
        }
        return null;
    }

    @Override
    public APCommand getCommand(){
        return command;
    }
}
