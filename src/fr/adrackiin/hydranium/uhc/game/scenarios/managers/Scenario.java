package fr.adrackiin.hydranium.uhc.game.scenarios.managers;

public abstract class Scenario {

    private final String name;
    private boolean active = false;
    private String description;
    private String[] descriptionHost;
    private Object source;

    public Scenario(String name, String description, String[] descriptionHost) {
        this.name = name;
        this.description = description;
        this.descriptionHost = descriptionHost;
    }

    public abstract void setScenario(boolean status);

    public abstract void enable();

    public abstract void disable();

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getDescriptionHost() {
        return descriptionHost;
    }

    public void setDescriptionHost(String[] descriptionHost) {
        this.descriptionHost = descriptionHost;
    }

    public Object getSource(){
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}
