package fr.adrackiin.hydranium.uhc.game.scenarios.werewolves.roles;

public enum Roles {

    WOLF("Loup-Garou",false, null),
    WHITE_WOLF("Loup-Garou Blanc", true, null),
    FATHER_WOLF("Infect père des loups", true, null),
    WILD_CHILDREN("Enfant Sauvage", true, null),
    ASSASSIN("Assassin", true, null),
    VILLAGER("Simple Villageois", false, null),
    CHAMAN("Chaman", true, null),
    LITTLE_GIRL("Petite Fille",true, null),
    WITCH("Sorcière", true, null),
    OLD("Ancien", true, null),
    SHOWER("Montreur d'Ours", true, null),
    CUPID("Cupidon", true, null),
    SISTERS("Soeur", false, null),
    SHOWY("Voyante", true, null),
    ANGEL("Ange", true, null),
    SAVING("Salvateur", true, null),
    FOX("Renard", true, null);

    private final String name;
    private final boolean unique;
    private Role classRole;

    Roles(String name, boolean unique, Role classRole){
        this.name = name;
        this.unique = unique;
        this.classRole = classRole;
    }

    public String getName() {
        return name;
    }

    public boolean isUnique() {
        return unique;
    }

    public Role getClassRole() {
        return classRole;
    }

    public void setClassRole(Role classRole) {
        this.classRole = classRole;
    }
}
