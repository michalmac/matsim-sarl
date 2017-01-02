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

import org.matsim.api.core.v01.Id;


public class SarlData
{
    private final TimeConverter timeConverter;
    private final Map<Id<Customer>, Customer> customers = new LinkedHashMap<>();
    private final Map<Id<Company>, Company> companies = new LinkedHashMap<>();
    private final Map<Id<MultiTripRequest>, MultiTripRequest> multiTripRequests = new LinkedHashMap<>();


    public SarlData(TimeConverter timeConverter)
    {
        this.timeConverter = timeConverter;
    }


    public TimeConverter getTimeConverter()
    {
        return timeConverter;
    }


    public Customer getCustomer(Id<Customer> id)
    {
        Customer customer = customers.get(id);

        //XXX temp
        if (customer == null) {
            customer = new Customer(id);
            customers.put(id, customer);
            System.err.println("Customer " + id + " created");
        }

        return customer;
    }


    public Company getCompany(Id<Company> id)
    {
        Company company = companies.get(id);

        //XXX temp
        if (company == null) {
            company = new Company(id);
            companies.put(id, company);
            //Id<Vehicle> vehId = Id.create(id, Vehicle.class);
            //company.getVrpData().addVehicle(new VehicleImpl(vehId, vehStartLink, 1, 0, 24 * 3600));
            System.err.println("Company " + id + " created");
        }

        return company;
    }


    public MultiTripRequest getMultiTripRequest(Id<MultiTripRequest> id)
    {
        return multiTripRequests.get(id);
    }


    public void addMultiTripRequest(MultiTripRequest request)
    {
        multiTripRequests.put(request.getId(), request);
    }
}
