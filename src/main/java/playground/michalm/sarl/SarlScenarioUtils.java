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
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
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


    public static Scenario createTestScenario()
    {
        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        new MatsimNetworkReader(scenario.getNetwork()).readFile("d:/PP-rad/smartpt/sarlMatsim.xml");

        TimeConverter timeConverter = new TimeConverter("2016-10-05 00:00:00.000");

        SarlData sarlData = new SarlData(timeConverter);
        scenario.addScenarioElement(SARL_DATA, sarlData);

        return scenario;
    }
}