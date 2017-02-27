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

import org.matsim.api.core.v01.*;
import org.matsim.contrib.dvrp.data.*;


public class Company
    implements Identifiable<Company>
{
    private final Id<Company> id;

    private final Fleet fleet = new FleetImpl();


    public Company(Id<Company> id)
    {
        this.id = id;
    }


    @Override
    public Id<Company> getId()
    {
        return id;
    }


    public Fleet getFleet()
    {
        return fleet;
    }
}
