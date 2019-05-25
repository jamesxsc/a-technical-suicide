package uk.co.xsc.tutorial.points;

import uk.co.xsc.ATechnicalSuicide;
import uk.co.xsc.tutorial.TutorialManager;

public class WillToLiveTutorialPoint extends TutorialManager.TutorialPoint {

    public WillToLiveTutorialPoint() {
        super((2 * ATechnicalSuicide.width / 3) + 130, 10,
                "These bars show your will to live. It is currently at its maximum (30). If it reaches 0, you die.", 51);
    }

}
