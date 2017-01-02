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
import org.matsim.api.core.v01.network.*;

import com.google.gson.*;

import json.JsonConstants;


public class SarlToMatsimReader
{
    private final Network network;
    private final SarlData sarlData;

    final Queue<MultiTripRequest> submittedRequests = new LinkedList<>();
    final Queue<MultiTripRequest> committedProposals = new LinkedList<>();
    final Queue<MultiTripRequest> cancelledProposals = new LinkedList<>();


    SarlToMatsimReader(Scenario scenario)
    {
        this.network = scenario.getNetwork();
        this.sarlData = SarlScenarioUtils.getSarlData(scenario);

    }


    //TYPE SUBMIT REQUEST:
    //contains an array of all the requests that are generated for a specific timestamp.
    //TYPE COMMIT PROPOSAL:
    //contains an array of all the proposals that wants to be committed for a specific timestamp.
    //TYPE DENY PROPOSAL:
    //contains an array of all the proposals that wants to be denied for a specific timestamp.
    void read(String message)
    {
        JsonParser parser = new JsonParser();
        JsonObject jsonMessage = parser.parse(message).getAsJsonObject();
        parseSubmitRequests(jsonMessage);
        parseCommitProposal(jsonMessage);
        parseCancelProposal(jsonMessage);
    }


    private void parseSubmitRequests(JsonObject message)
    {
        JsonArray submitRequestArray = message.getAsJsonArray(JsonConstants.TYPE_SUBMIT_REQUEST);

        if (submitRequestArray != null) {
            for (JsonElement sr : submitRequestArray) {
                JsonObject atomicRequest = sr.getAsJsonObject()
                        .getAsJsonObject(JsonConstants.ATOMIC_REQUESTS);

                Id<MultiTripRequest> reqId = getId(atomicRequest, JsonConstants.REQUEST_ID,
                        MultiTripRequest.class);
                Id<Company> companyId = getId(atomicRequest, JsonConstants.RECEIVER, Company.class);

                String stateMachineId = getString(atomicRequest, JsonConstants.STATE_MACHINE_ID);
                String requesterId = getString(atomicRequest, JsonConstants.ORIGINAL_REQUESTER);

                MultiTripRequest multiTripRequest = new MultiTripRequest(reqId,
                        sarlData.getCompany(companyId), requesterId, stateMachineId);

                JsonArray requestsArray = atomicRequest.getAsJsonArray(JsonConstants.REQUESTS);
                for (JsonElement r : requestsArray) {
                    JsonObject request = r.getAsJsonObject();

                    double departureT0 = getTime(request,
                            JsonConstants.TIME_WINDOW_DEPARTURE_START);
                    double departureT1 = getTime(request, JsonConstants.TIME_WINDOW_DEPARTURE_END);
                    double arrivalT0 = getTime(request, JsonConstants.TIME_WINDOW_ARRIVAL_START);
                    double arrivalT1 = getTime(request, JsonConstants.TIME_WINDOW_ARRIVAL_END);

                    Link fromLink = getLink(request.getAsJsonObject(JsonConstants.ORIGIN));
                    Link toLink = getLink(request.getAsJsonObject(JsonConstants.DESTINATION));

                    RequestedTrip reqTrip = new RequestedTrip(multiTripRequest, fromLink, toLink,
                            departureT0, departureT1, arrivalT0, arrivalT1);

                    JsonArray customersArray = request.getAsJsonArray(JsonConstants.CUSTOMERS);
                    for (JsonElement c : customersArray) {
                        Id<Customer> customerId = getId(c.getAsJsonObject(),
                                JsonConstants.CUSTOMER_ID, Customer.class);
                        Customer customer = sarlData.getCustomer(customerId);
                        reqTrip.addCustomer(customer);
                    }

                    multiTripRequest.addRequestedTrip(reqTrip);
                }

                submittedRequests.add(multiTripRequest);
            }
        }
    }


    private void parseCommitProposal(JsonObject message)
    {
        parseProposalAnswers(message.getAsJsonArray(JsonConstants.TYPE_COMMIT_PROPOSAL),
                committedProposals);
        //XXX If there were more alternatives, the other ones need to be cancelled
    }


    private void parseCancelProposal(JsonObject message)
    {
        parseProposalAnswers(message.getAsJsonArray(JsonConstants.TYPE_CANCEL_PROPOSAL),
                cancelledProposals);
    }


    private void parseProposalAnswers(JsonArray proposalAnswerArray,
            Queue<MultiTripRequest> answeredRequests)
    {
        if (proposalAnswerArray != null) {
            for (JsonElement pa : proposalAnswerArray) {
                JsonObject proposalAnswer = pa.getAsJsonObject();
                Id<MultiTripRequest> reqId = getId(proposalAnswer, JsonConstants.REQUEST_ID,
                        MultiTripRequest.class);

                //currently no alternatives supported
                if (getInt(proposalAnswer, JsonConstants.ALTERNATIVE_ID) != 1) {
                    throw new RuntimeException();
                }

                MultiTripRequest multiTripRequest = sarlData.getMultiTripRequest(reqId);
                answeredRequests.add(multiTripRequest);
            }
        }
    }


    private double getTime(JsonObject jsonObject, String memberName)
    {
        return sarlData.getTimeConverter().sarlToMatsim(getString(jsonObject, memberName));
    }


    private String getString(JsonObject jsonObject, String memberName)
    {
        return jsonObject.getAsJsonPrimitive(memberName).getAsString();
    }


    private int getInt(JsonObject jsonObject, String memberName)
    {
        return jsonObject.getAsJsonPrimitive(memberName).getAsInt();
    }


    private <T> Id<T> getId(JsonObject jsonObject, String memberName, Class<T> clazz)
    {
        return Id.create(getString(jsonObject, memberName), clazz);
    }


    private Link getLink(JsonObject location)
    {
        Id<Link> linkId = getId(location, JsonConstants.LINK_ID, Link.class);
        return network.getLinks().get(linkId);
    }
}
