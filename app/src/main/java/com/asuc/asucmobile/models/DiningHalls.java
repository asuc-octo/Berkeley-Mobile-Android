package com.asuc.asucmobile.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DiningHalls {

    @SerializedName("dining_halls")
    public List<DiningHall> data;

    @SerializedName("dining_hall")
    public DiningHall datum;

    public static class DiningHall implements Cardable {

        private String id;
        private String name;
        private ArrayList<FoodItem> breakfastMenu;
        private ArrayList<FoodItem> lunchMenu;
        private ArrayList<FoodItem> dinnerMenu;
        private ArrayList<FoodItem> lateNightMenu;
        private Date breakfastOpen;
        private Date breakfastClose;
        private Date lunchOpen;
        private Date lunchClose;
        private Date dinnerOpen;
        private Date dinnerClose;
        private Date lateNightOpen;
        private Date lateNightClose;
        private String imageLink;

        public DiningHall(String id, String name, ArrayList<FoodItem> breakfastMenu,
                          ArrayList<FoodItem> lunchMenu, ArrayList<FoodItem> dinnerMenu,
                          ArrayList<FoodItem> lateNightMenu, Date breakfastOpen, Date breakfastClose,
                          Date lunchOpen, Date lunchClose, Date dinnerOpen, Date dinnerClose,
                          Date lateNightOpen, Date lateNightClose, String imageLink) {
            this.id = id;
            this.name = name;
            this.breakfastMenu = breakfastMenu;
            this.lunchMenu = lunchMenu;
            this.dinnerMenu = dinnerMenu;
            this.lateNightMenu = lateNightMenu;
            this.breakfastOpen = breakfastOpen;
            this.breakfastClose = breakfastClose;
            this.lunchOpen = lunchOpen;
            this.lunchClose = lunchClose;
            this.dinnerOpen = dinnerOpen;
            this.dinnerClose = dinnerClose;
            this.lateNightOpen = lateNightOpen;
            this.lateNightClose = lateNightClose;
            this.imageLink = imageLink;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public ArrayList<FoodItem> getBreakfastMenu() {
            return breakfastMenu;
        }

        public ArrayList<FoodItem> getLunchMenu() {
            return lunchMenu;
        }

        public ArrayList<FoodItem> getDinnerMenu() {
            return dinnerMenu;
        }

        public ArrayList<FoodItem> getLateNightMenu() {
            return lateNightMenu;
        }

        public String getImageLink() {
            return imageLink;
        }

        public Date getBreakfastOpening() {
            return breakfastOpen;
        }

        public Date getBreakfastClosing() {
            return breakfastClose;
        }

        public Date getLunchOpening() {
            return lunchOpen;
        }

        public Date getLunchClosing() {
            return lunchClose;
        }

        public Date getDinnerOpening() {
            return dinnerOpen;
        }

        public Date getDinnerClosing() {
            return dinnerClose;
        }

        public Date getLateNightOpening() {
            return lateNightOpen;
        }

        public Date getLateNightClosing() {
            return lateNightClose;
        }

        /**
         * isBreakfastOpen() returns whether or not breakfast is open. The same goes for the other
         * three open methods.
         *
         * @return Boolean indicating if a certain dining hall menu is open or not.
         */
        public boolean isBreakfastOpen() {
            if (breakfastOpen == null || breakfastClose == null) {
                return false;
            }

            Date currentTime = new Date();
            return currentTime.after(breakfastOpen) && currentTime.before(breakfastClose);
        }

        public boolean isLunchOpen() {
            if (lunchOpen == null || lunchClose == null) {
                return false;
            }

            Date currentTime = new Date();
            return currentTime.after(lunchOpen) && currentTime.before(lunchClose);
        }

        public boolean isDinnerOpen() {
            if (dinnerOpen == null || dinnerClose == null) {
                return false;
            }

            Date currentTime = new Date();
            return currentTime.after(dinnerOpen) && currentTime.before(dinnerClose);
        }

        public boolean isLateNightOpen() {
            if (lateNightOpen == null || lateNightClose == null) {
                return false;
            }

            Date currentTime = new Date();
            return currentTime.after(lateNightOpen) && currentTime.before(lateNightClose);
        }

        public boolean lateNightToday() {
            return lateNightOpen != null && lateNightClose != null;
        }

        public boolean isOpen() {
            return isBreakfastOpen() | isLunchOpen() | isDinnerOpen() | isLateNightOpen();
        }

        @Override
        public String toString() {
            return "DiningHall{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", breakfastMenu=" + breakfastMenu +
                    ", lunchMenu=" + lunchMenu +
                    ", dinnerMenu=" + dinnerMenu +
                    ", lateNightMenu=" + lateNightMenu +
                    ", breakfastOpen=" + breakfastOpen +
                    ", breakfastClose=" + breakfastClose +
                    ", lunchOpen=" + lunchOpen +
                    ", lunchClose=" + lunchClose +
                    ", dinnerOpen=" + dinnerOpen +
                    ", dinnerClose=" + dinnerClose +
                    ", lateNightOpen=" + lateNightOpen +
                    ", lateNightClose=" + lateNightClose +
                    ", imageLink='" + imageLink + '\'' +
                    '}';
        }

        @Override
        public String getTimes() {
            // TODO: what the fuck is this shit why they different?
            String menuOpen = null;
            if (this.isBreakfastOpen()) {
                menuOpen = "Breakfast:" + HOURS_FORMAT.format(this.getBreakfastOpening()) + "- "  +
                        HOURS_FORMAT.format(this.getBreakfastClosing()) ;
            } else if (this.isLunchOpen()) {
                menuOpen = "Lunch:" + this.getLunchOpening().toString() + "- " +
                            this.getLunchClosing().toString();
            } else if (this.isDinnerOpen()) {
                menuOpen = "Dinner: " + this.getDinnerOpening().toString() + "- " +
                            this.getDinnerClosing().toString();
            } else if (this.isLateNightOpen()) {
                menuOpen = "Late Night: " + this.getLateNightOpening().toString() + "- " +
                            this.getLateNightClosing().toString();
            }
            return menuOpen;
        }

    }
}