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

import org.matsim.api.core.v01.network.Link;


public class RequestedTrip
{
    private final Link fromLink;
    private final Link toLink;//toLink may be provided during the pickup

    private final double departureT0;
    private final double departureT1;

    private final double arrivalT0;
    private final double arrivalT1;

    private final MultiTripRequest multiTripRequest;
    private final List<Customer> customers = new ArrayList<>();


    public RequestedTrip(MultiTripRequest multiTripRequest, Link fromLink, Link toLink,
            double departureT0, double departureT1, double arrivalT0, double arrivalT1)
    {
        this.multiTripRequest = multiTripRequest;
        this.fromLink = fromLink;
        this.toLink = toLink;
        this.departureT0 = departureT0;
        this.departureT1 = departureT1;
        this.arrivalT0 = arrivalT0;
        this.arrivalT1 = arrivalT1;
    }


    public MultiTripRequest getMultiTripRequest()
    {
        return multiTripRequest;
    }


    public Iterable<Customer> getCustomers()
    {
        return customers;
    }


    public void addCustomer(Customer customer)
    {
        customers.add(customer);
    }


    public Link getFromLink()
    {
        return fromLink;
    }


    public Link getToLink()
    {
        return toLink;
    }


    public double getDepartureT0()
    {
        return departureT0;
    }


    public double getDepartureT1()
    {
        return departureT1;
    }


    public double getArrivalT0()
    {
        return arrivalT0;
    }


    public double getArrivalT1()
    {
        return arrivalT1;
    }
}
