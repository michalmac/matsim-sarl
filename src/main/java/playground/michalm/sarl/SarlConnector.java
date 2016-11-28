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

import org.matsim.core.controler.events.StartupEvent;
import org.matsim.core.controler.listener.StartupListener;
import org.matsim.core.mobsim.framework.events.*;
import org.matsim.core.mobsim.framework.listeners.*;


public class SarlConnector
    implements StartupListener, MobsimInitializedListener, MobsimBeforeSimStepListener,
    MobsimAfterSimStepListener
{
    public static final int SYNC_PERIOD = 60; //1 minute


    @Override
    public void notifyStartup(StartupEvent event)
    {
        // TODO setup all data and the connection with SARL

    }


    @Override
    public void notifyMobsimInitialized(MobsimInitializedEvent e)
    {
        // TODO update/reset necessary data and synchronise with SARL

    }


    @Override
    public void notifyMobsimBeforeSimStep(MobsimBeforeSimStepEvent e)
    {
        if (e.getSimulationTime() % SYNC_PERIOD == 0) {
            // TODO First listen to the incoming JSON input, process it
            processInput();
        }
        
        //trigger SARL events planned for this time step 
    }


    @Override
    public void notifyMobsimAfterSimStep(MobsimAfterSimStepEvent e)
    {
        //collect matsim events (if not collected directly during simStep)
        
        if ( (e.getSimulationTime() - 1) % SYNC_PERIOD == 0) {
            // TODO Generate output for SARL and send it as JSON objects
            provideOutput();
        }
    }


    //TYPE SUBMIT REQUEST:
    //contains an array of all the requests that are generated for a specific timestamp.
    //TYPE COMMIT PROPOSAL:
    //contains an array of all the proposals that wants to be committed for a specific timestamp.
    //TYPE DENY PROPOSAL:
    //contains an array of all the proposals that wants to be denied for a specific timestamp.
    private void processInput()
    {
        //TODO
        //read input
        //schedule SARL events
    }


    //TYPE SUBMIT PROPOSAL:
    //contains an array of all the proposals that are generated for a specific timestamp.
    //TYPE COMMIT PROPOSAL ACCEPTED:
    //contains an array of all the proposals for which the commits are accepted for a specific timestamp.
    //TYPE COMMIT PROPOSAL REJECTED:
    //contains an array of all the proposals for which the commits are rejected for a specific timestamp.
    private void provideOutput()
    {
        //TODO
        //answer requests
        //include matsim events (pickup, dropoff, etc.) collected over the last period
        //clear the collection
    }
}
