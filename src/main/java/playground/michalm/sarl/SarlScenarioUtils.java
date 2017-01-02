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
import org.matsim.api.core.v01.network.*;
import org.matsim.contrib.util.distance.DistanceUtils;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.scenario.ScenarioUtils;


public class SarlScenarioUtils
{
    //XXX temp solution. Use guice...
    public static final String SARL_DATA = "sarl_data";


    @SuppressWarnings({ "deprecation" })
    public static SarlData getSarlData(Scenario scenario)
    {
        return (SarlData)scenario.getScenarioElement(SARL_DATA);
    }


    @SuppressWarnings("deprecation")
    public static Scenario createTestScenario()
    {
        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        Network network = scenario.getNetwork();

        Node nodeA = NetworkUtils.createAndAddNode(network, Id.createNodeId("A"), new Coord(0, 0));
        Node nodeB = NetworkUtils.createAndAddNode(network, Id.createNodeId("B"),
                new Coord(1000, 0));
        Node nodeC = NetworkUtils.createAndAddNode(network, Id.createNodeId("C"),
                new Coord(2000, 0));
        Node nodeD = NetworkUtils.createAndAddNode(network, Id.createNodeId("D"),
                new Coord(3000, 0));

        createAndAddLink(network, "1774", nodeA, nodeB);
        //createAndAddLink(network, "1774_rev", nodeB, nodeA);
        createAndAddLink(network, "-4036", nodeB, nodeC);
        //createAndAddLink(network, "-4036_rev", nodeC, nodeB);
        createAndAddLink(network, "2672", nodeC, nodeD);
        //createAndAddLink(network, "2672_rev", nodeD, nodeC);

        TimeConverter timeConverter = new TimeConverter("2016-10-05 00:00:00.000");

        SarlData sarlData = new SarlData(timeConverter);
        scenario.addScenarioElement(SARL_DATA, sarlData);

        return scenario;
    }


    private static Link createAndAddLink(Network network, String id, Node from, Node to)
    {
        double length = DistanceUtils.calculateDistance(from.getCoord(), to.getCoord());
        return NetworkUtils.createAndAddLink(network, Id.createLinkId(id), from, to, length, 10,
                900, 1);
    }
}
