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

import java.text.*;
import java.util.Date;


public class TimeConverter
{
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "dd-MM-yyyy HH:mm:ss");

    private final long t0;


    public TimeConverter(String timeZero)
    {
        try {
            t0 = DATE_FORMAT.parse("2016-10-05 00:00:00.000").getTime();
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    public double sarlToMatsim(String date)
    {
        try {
            long t = DATE_FORMAT.parse(date).getTime();
            return ((double) (t - t0)) / 1000;
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }


    public String matsimToSarl(double time)
    {
        Date date = new Date(t0 + (long) (time * 1000));
        return DATE_FORMAT.format(date);
    }
}
