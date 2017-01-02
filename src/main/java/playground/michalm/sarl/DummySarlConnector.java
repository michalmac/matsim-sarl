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

import org.matsim.api.core.v01.Scenario;

import client.Client;
import listener.IMessageReceivedEventListener;


public class DummySarlConnector
    implements IMessageReceivedEventListener
{
    private final int portToSend;
    private final Scenario scenario;
    private final SarlData sarlData;


    public DummySarlConnector(int portToSend, Scenario scenario)
    {
        this.portToSend = portToSend;
        this.scenario = scenario;
        this.sarlData = SarlScenarioUtils.getSarlData(scenario);
    }


    @Override
    public void messageReceived(String inMessage)
    {
        System.out.println("Server received \n" + inMessage + "\n\n");
        String outMessage = processMessage(inMessage);
        System.out.println("Server sent\n" + outMessage + "\n-----------------------------\n");
        Client.send("localhost", portToSend, outMessage);
    }


    public String processMessage(String inMessage)
    {
        SarlToMatsimReader reader = new SarlToMatsimReader(scenario);
        reader.read(inMessage);
        MatsimToSarlWriter writer = new MatsimToSarlWriter(scenario);

        //dummy logic
        for (MultiTripRequest mtr : reader.submittedRequests) {
            sarlData.addMultiTripRequest(mtr);
        }

        //no optimisation, we accept all
        writer.submitProposals.addAll(reader.submittedRequests);
        writer.acceptedCommitProposals.addAll(reader.committedProposals);

        return writer.write();
    }
}
