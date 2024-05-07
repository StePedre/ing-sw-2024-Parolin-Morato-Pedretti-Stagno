package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.ScoreRules.ScoreRule;

import java.io.Serializable;

/**
 * ObjectiveCard class only has two attributes and their get methods:
 * numeric identifier and scoring rule from package ScoreRule.
 * It is not generalized in the abstract class Card because there are many differences
 * both in their usage and in their features.
 */

public class ObjectiveCard implements Serializable {
    private static final long serialVersionUID = 7L;
        private final int id;
        private final ScoreRule rule;

        /**
         * Class constructor.
         *
         * @param id is numeric card identifier.
         * @param rule is the scoring rule applied to the card.
         */
        public ObjectiveCard(int id, ScoreRule rule){
            this.id = id;
            this.rule = rule;
        }

        /**
         * The method gets the numeric value of card identifier.
         *
         * @return id value.
         */
        public int getId(){
            return id;
        }

        /**
         * The method gets the ScoreRule of the card.
         *
         * @return scoring rule from package ScoreRule.
         */
        public ScoreRule getRule(){
            return rule;
        }

    }
