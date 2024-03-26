package it.polimi.ingsw.Model;

    public class Corner{
        private String pos;//forse mettere corner
        private Resource cornerRes;
        private boolean isAvailable;
        public Corner(String pos, Resource res, boolean availabilty){
            this.pos=pos;
            this.cornerRes=res;
            this.isAvailable=availabilty;
        }
        public String getPos(){
            return pos;
        }
        public Resource getCornerRes() {
            return cornerRes;
        }
        public boolean getAvailability(){
            return isAvailable;
        }
        public void setAvailable(boolean availability){
        this.isAvailable=availability;
        }


    }
