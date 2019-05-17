package uk.co.xsc.player;

public enum Character {

    ARRAN("Arran", 201, 2, 3, 4, 2,
            4, 7, 2, 7, 6, 1),
    BEN("Ben", 202, 3, 6, 6, 8,
            5, 8, 3, 7, 6, 7),
    BENEDICT("Benedict", 203, 6, 7, 2, 6,
            5, 1, 10, 1, 3, 7),
    JAMES("James", 204, 9, 7, 6, 5,
            6, 7, 5, 5, 7, 7),
    JASPER("Jasper", 205, 7, 7, 5, 5,
            5, 7, 5, 7, 9, 4),
    MILES("Miles", 206, 5, 8, 5, 6,
            6, 6, 6, 7, 8, 9),
    ;

    public final String name;
    public final int renderId;
    public final int scoreSound;
    public final int scoreLighting;
    public final int scoreRigging;
    public final int scoreStageManagement;
    public final int scoreProduction;
    public final int scoreLeadership;
    public final int scoreEvil;
    public final int scoreKind;
    public final int scoreHonest;
    public final int scoreMusicality;

    Character(
            String name,
            int renderId,
            int scoreSound,
            int scoreLighting,
            int scoreRigging,
            int scoreStageManagement,
            int scoreProduction,
            int scoreLeadership,
            int scoreEvil,
            int scoreKind,
            int scoreHonest,
            int scoreMusicality) {

        this.name = name;

        this.renderId = renderId;
        this.scoreSound = scoreSound;
        this.scoreLighting = scoreLighting;
        this.scoreRigging = scoreRigging;
        this.scoreStageManagement = scoreStageManagement;
        this.scoreProduction = scoreProduction;
        this.scoreLeadership = scoreLeadership;
        this.scoreEvil = scoreEvil;
        this.scoreKind = scoreKind;
        this.scoreHonest = scoreHonest;
        this.scoreMusicality = scoreMusicality;

    }

}
