package com.asuc.asucmobile.infrastructure.transformers;

import com.asuc.asucmobile.domain.models.Journey;
import com.asuc.asucmobile.domain.models.Stop;
import com.asuc.asucmobile.domain.models.Trip;
import com.asuc.asucmobile.domain.models.TripRespModel;
import com.asuc.asucmobile.infrastructure.models.TripBeforeTransform;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TripListToJourneyTransformer {

    private static final java.text.SimpleDateFormat DATE_FORMAT =
            new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
    private static final java.util.TimeZone PST = java.util.TimeZone.getTimeZone("America/Los_Angeles");

    public Journey tripListToJourney(TripRespModel tripResponse, StopListToLineTransformer sllTransformer) throws ParseException {
        ArrayList<Trip> newTripList = new ArrayList<>();
        for (TripBeforeTransform tripElem : tripResponse.getTripList()) {
            Long tmpTime = DATE_FORMAT.parse(tripElem.getTmpStartTime()).getTime();
            Date startTime = new Date(tmpTime + PST.getOffset(tmpTime));
            tmpTime = DATE_FORMAT.parse(tripElem.getTmpEndTime()).getTime();
            Date endTime = new Date(tmpTime + PST.getOffset(tmpTime));
            Stop startStop = sllTransformer.getStop(tripElem.getStartingStop().getStartId(), tripElem.getStartingStop().getStartName());
            Stop endStop = sllTransformer.getStop(tripElem.getDestinationStop().getEndId(), tripElem.getDestinationStop().getEndName());
            ArrayList<Stop> lineStops = sllTransformer.getLine(tripElem.getLineId(), tripElem.getLineName()).getStops();
            ArrayList<Stop> stops = new ArrayList<>();
            boolean isPastEnd = false;
            int index = lineStops.indexOf(startStop);
            while (!isPastEnd) {
                Stop stop = lineStops.get(index);
                stops.add(stop);
                if (stop.equals(endStop)) {
                    isPastEnd = true;
                } else if (stops.size() > lineStops.size()) {
                    // We have a problem!
                }
                index = (index + 1) % lineStops.size();
            }
            newTripList.add(new Trip(startTime, endTime, stops, tripElem.getLineName()));
        }
        return new Journey(newTripList);
    }
}
