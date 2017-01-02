/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2016 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package playground.michalm.sarl;

import java.util.*;

import org.matsim.api.core.v01.*;


public class MultiTripRequest
    implements Identifiable<MultiTripRequest>, Iterable<RequestedTrip>
{
    private final Id<MultiTripRequest> id;
    private final Company company;

    private final String requesterId;
    private final String stateMachineId;

    private final List<RequestedTrip> requestedTrips = new ArrayList<>();


    public MultiTripRequest(Id<MultiTripRequest> id, Company company, String requester,
            String stateMachineId)
    {
        this.id = id;
        this.company = company;
        this.requesterId = requester;
        this.stateMachineId = stateMachineId;
    }


    @Override
    public Iterator<RequestedTrip> iterator()
    {
        return requestedTrips.iterator();
    }


    @Override
    public Id<MultiTripRequest> getId()
    {
        return id;
    }


    public Company getCompany()
    {
        return company;
    }


    public String getRequesterId()
    {
        return requesterId;
    }


    public String getStateMachineId()
    {
        return stateMachineId;
    }


    public void addRequestedTrip(RequestedTrip requestedTrip)
    {
        requestedTrips.add(requestedTrip);
    }


    public int getTripCount()
    {
        return requestedTrips.size();
    }
}
