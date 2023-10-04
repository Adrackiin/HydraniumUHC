package fr.adrackiin.hydranium.uhc.game.state;

public enum GameState {

    WAITING_HOST(1),
    CONFIG(2),
    PREGEN(3),
    WAITING_WHITELIST(4),
    WHITELIST(5),
    CLOSE_WHITELIST(6),
    OPEN(7),
    CLOSE(8),
    START(9),
    TELEPORATTION(10),
    INVULNERABILITY(11),
    MINING(12),
    PVP(13),
    BORDER(14),
    MEETUP(15),
    VICTORY(16);

    private final int id;

    GameState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
