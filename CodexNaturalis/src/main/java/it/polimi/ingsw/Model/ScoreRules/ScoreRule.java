package it.polimi.ingsw.Model.ScoreRules;

import it.polimi.ingsw.Model.PlayerGround;

public interface ScoreRule {
    int calculatePoints(PlayerGround gameBoard);
}