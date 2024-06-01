package it.polimi.ingsw.Model;

import java.io.Serializable;

/**
 * The class Corner is related to the class Card, as every Card object owns
 * two arrays of four corners each: front and back corners.
 * Each corner has a position (top left/right, bottom left/right) and a Resource.
 * When a Card is placed on the PlayerGround, each of its corners may be available
 * or not.
 * The methods of this class include the setting of the availability status.
 */

    public class Corner implements Serializable {
    private static final long serialVersionUID = 11L;
        private String pos;
        private Resource cornerRes;
        private boolean isAvailable;

        /**
         * Class constructor.
         *
         * @param pos is the position of the corner between four possibilities:
         *            top left/right, bottom left/right
         * @param res indicates the resource displayed on the corner.
         *            May be nothing.
         * @param availability indicates whether the corner is available or not.ù
         *                     Possible values: true or false.
         */
        public Corner(String pos, Resource res, boolean availability){
            this.pos=pos;
            this.cornerRes=res;
            this.isAvailable=availability;
        }

        /**
         * The method gets the position of a corner as a string.
         *
         * @return corner position.
         */
        public String getPos(){
            return pos;
        }

        /**
         * The method gets the Resource of a corner.
         *
         * @return corner resource from class Resource. May be null.
         */
        public Resource getCornerRes() {
            return cornerRes;
        }

        /**
         * The method gets the status of a corner's availability.
         *
         * @return true if the corner is available, false otherwise.
         */
        public boolean getAvailability(){
            return isAvailable;
        }

        /**
         * The method sets the availability or non-availability of a corner.
         *
         * @param availability is the availability value (true or false) to be set.
         */
        public void setAvailable(boolean availability){
        this.isAvailable=availability;
        }

    /**
     * The method set the resource of a corner
     *
     * @param resource the resource of the corner
     */
    public void setResource(Resource resource) {this.cornerRes = resource;}
    }
