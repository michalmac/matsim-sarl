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
import org.matsim.api.core.v01.network.Link;

import com.google.gson.*;

import json.JsonConstants;


public class MatsimToSarlWriter
{
    private final TimeConverter timeConverter;

    final Queue<MultiTripRequest> submitProposals = new LinkedList<>();
    final Queue<MultiTripRequest> acceptedCommitProposals = new LinkedList<>();
    final Queue<MultiTripRequest> rejectedCommitProposals = new LinkedList<>();


    MatsimToSarlWriter(Scenario scenario)
    {
        this.timeConverter = SarlScenarioUtils.getSarlData(scenario).getTimeConverter();
    }


    //TYPE SUBMIT PROPOSAL:
    //contains an array of all the proposals that are generated for a specific timestamp.
    //TYPE COMMIT PROPOSAL ACCEPTED:
    //contains an array of all the proposals for which the commits are accepted for a specific timestamp.
    //TYPE COMMIT PROPOSAL REJECTED:
    //contains an array of all the proposals for which the commits are rejected for a specific timestamp.
    String write()
    {
        JsonObject message = new JsonObject();
        addSubmitProposals(message);
        addAcceptedCommitProposals(message);
        addRejectedCommitProposals(message);
        return message.toString();
    }


    private void addSubmitProposals(JsonObject output)
    {
        if (submitProposals.isEmpty()) {
            return;
        }

        JsonArray submitProposalArray = new JsonArray();
        while (!submitProposals.isEmpty()) {
            MultiTripRequest multiTripRequest = submitProposals.poll();
            JsonObject submitProposal = new JsonObject();

            JsonObject proposalsForRequest = createBaseJsonObject(multiTripRequest);
            proposalsForRequest.addProperty(JsonConstants.ORIGINAL_REQUESTER,
                    multiTripRequest.getRequesterId());
            proposalsForRequest.add(JsonConstants.ALTERNATIVES_FOR_REQUEST,
                    createSingleAlternativeForRequest(multiTripRequest));

            submitProposal.add(JsonConstants.PROPOSALS_FOR_REQUEST, proposalsForRequest);
            submitProposalArray.add(submitProposal);
        }
        output.add(JsonConstants.TYPE_SUBMIT_PROPOSAL, submitProposalArray);
    }


    private JsonArray createSingleAlternativeForRequest(MultiTripRequest multiTripRequest)
    {
        JsonArray alternativeForRequestArray = new JsonArray();

        JsonObject alternativeForRequest = new JsonObject();
        alternativeForRequest.addProperty(JsonConstants.ALTERNATIVE_ID, 1);
        JsonArray proposalArray = new JsonArray();
        for (RequestedTrip reqTrip : multiTripRequest) {
            JsonObject proposal = new JsonObject();
            addTime(proposal, JsonConstants.TIME_WINDOW_DEPARTURE_START, reqTrip.getDepartureT0());
            addTime(proposal, JsonConstants.TIME_WINDOW_DEPARTURE_END, reqTrip.getDepartureT1());
            addTime(proposal, JsonConstants.TIME_WINDOW_ARRIVAL_START, reqTrip.getArrivalT0());
            addTime(proposal, JsonConstants.TIME_WINDOW_ARRIVAL_END, reqTrip.getArrivalT1());

            addLocation(proposal, JsonConstants.ORIGIN, reqTrip.getFromLink());
            addLocation(proposal, JsonConstants.DESTINATION, reqTrip.getFromLink());

            JsonArray customersArray = new JsonArray();
            for (Customer c : reqTrip.getCustomers()) {
                JsonObject customer = new JsonObject();
                addId(customer, JsonConstants.CUSTOMER_ID, c);
                customersArray.add(customer);
            }
            proposal.add(JsonConstants.CUSTOMERS, customersArray);

            proposalArray.add(proposal);
        }
        alternativeForRequest.add(JsonConstants.PROPOSALS, proposalArray);

        alternativeForRequestArray.add(alternativeForRequest);
        return alternativeForRequestArray;
    }


    private JsonObject createBaseJsonObject(MultiTripRequest multiTripRequest)
    {
        JsonObject basicJsonObject = new JsonObject();
        addId(basicJsonObject, JsonConstants.REQUEST_ID, multiTripRequest);
        addId(basicJsonObject, JsonConstants.RECEIVER, multiTripRequest.getCompany());
        basicJsonObject.addProperty(JsonConstants.STATE_MACHINE_ID,
                multiTripRequest.getStateMachineId());
        return basicJsonObject;
    }


    private void addAcceptedCommitProposals(JsonObject output)
    {
        if (acceptedCommitProposals.isEmpty()) {
            return;
        }

        JsonArray commitProposalAcceptedArray = createCommitProposalAnswerArrayImpl(
                acceptedCommitProposals);
        output.add(JsonConstants.TYPE_COMMIT_PROPOSAL_ACCEPTED, commitProposalAcceptedArray);
    }


    private void addRejectedCommitProposals(JsonObject output)
    {
        if (rejectedCommitProposals.isEmpty()) {
            return;
        }

        JsonArray commitProposalRejectedArray = createCommitProposalAnswerArrayImpl(
                rejectedCommitProposals);
        output.add(JsonConstants.TYPE_COMMIT_PROPOSAL_REJECTED, commitProposalRejectedArray);
    }


    private JsonArray createCommitProposalAnswerArrayImpl(
            Queue<MultiTripRequest> commitProposalsToAnswer)
    {
        JsonArray commitProposalAnswerArray = new JsonArray();
        while (!commitProposalsToAnswer.isEmpty()) {
            MultiTripRequest multiTripRequest = commitProposalsToAnswer.poll();
            JsonObject commitProposalAnswer = createBaseJsonObject(multiTripRequest);
            commitProposalAnswer.addProperty(JsonConstants.ALTERNATIVE_ID, 1);
            commitProposalAnswerArray.add(commitProposalAnswer);
        }
        return commitProposalAnswerArray;
    }


    private void addId(JsonObject jsonObject, String memberName, Identifiable<?> identifiable)
    {
        jsonObject.addProperty(memberName, identifiable.getId().toString());
    }


    private void addTime(JsonObject jsonObject, String memberName, double time)
    {
        jsonObject.addProperty(memberName, timeConverter.matsimToSarl(time));
    }


    private void addLocation(JsonObject jsonObject, String memeberName, Link link)
    {
        JsonObject location = new JsonObject();
        addId(location, JsonConstants.LINK_ID, link);
        location.addProperty(JsonConstants.POSITION_ON_LINK, 0);
        jsonObject.add(memeberName, location);
    }
}
