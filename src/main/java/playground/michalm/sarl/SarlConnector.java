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
    public static final int SYNC_PERIOD = 60; //1 minute ?????????????????????????
    private double currentTime;


    @Override
    public void notifyStartup(StartupEvent event)
    {
        // TODO
        // open matsim serversocket for communication with sarl
        // setup all data and the connection with SARL (once per simulation)
    }


    @Override
    public void notifyMobsimInitialized(MobsimInitializedEvent e)
    {
        // TODO
        // update/reset necessary data and synchronise with SARL (once per iteration)

        currentTime = 0;
    }


    @Override
    public void notifyMobsimBeforeSimStep(MobsimBeforeSimStepEvent e)
    {
        currentTime = e.getSimulationTime();
        if (currentTime % SYNC_PERIOD == 0) {
            // TODO First listen to the incoming JSON input, process it
            //processInput(message);
        }

        //trigger SARL events planned for this time step
        //TODO when sarl events are triggered? 
        // 1. all at the beginning/end of a period (end == more realistic option)
        // 2. each at exact time step (if we know when they were actually triggered in sarl) 
    }


    @Override
    public void notifyMobsimAfterSimStep(MobsimAfterSimStepEvent e)
    {
        //TODO
        //collect matsim events (if not collected directly during simStep)
        //include matsim events (pickup, dropoff, etc.) collected over the last period

        if ( (currentTime - 1) % SYNC_PERIOD == 0) {
            // TODO Generate output for SARL and send it as JSON objects
            //String message = provideOutput();
        }
    }
}
