package playground.michalm.sarl;

import java.io.IOException;

import org.matsim.api.core.v01.Scenario;

import server.Server;


public class MatsimServerLauncher
{
    public static void main(String[] args)
    {
        int portToListen = Integer.parseInt(args[0]);
        int portToSend = Integer.parseInt(args[1]);

        Scenario scenario = SarlScenarioUtils.createTestScenario();

        try {
            Thread t = new Server(portToListen, new DummySarlConnector(portToSend, scenario));
            t.start();
            Thread.sleep(2000);
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
