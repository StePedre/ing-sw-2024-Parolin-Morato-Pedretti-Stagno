package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.ScoreRules.ScoreRule;

public class ObjectiveCard {

        private final int id;
        private final ScoreRule rule;

        public ObjectiveCard(int id, ScoreRule rule){
            this.id = id;
            this.rule = rule;
        }

        public int getId(){
            return id;
        }

        public ScoreRule getRule(){
            return rule;
        }

    }
