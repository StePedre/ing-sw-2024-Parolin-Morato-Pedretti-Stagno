ScoreRule.javapackage it.polimi.ingsw.Model.ScoreRules;

import it.polimi.ingsw.Model.PlayerGround;

public class NoRule implements ScoreRule {
    @Override
    public int calculatePoints(PlayerGround gameBoard) {
        return 0;
    }
}