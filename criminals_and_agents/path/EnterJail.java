package path;

import city.CityEnvironment;
import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;
import jason.JasonException;
import city.*;
import java.util.*;
import helper.AgentPercept;


public class EnterJail extends DefaultInternalAction {

    private static final long serialVersionUID = 1L;

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        // Check the number of arguments
        if (args.length != 3) {
            throw new JasonException("This internal action requires two arguments.");
        }

        // Extract the police agent ID and the criminal agent ID
        int policeId = (int)((NumberTerm) args[0]).solve();
        int Xj = (int)((NumberTerm) args[1]).solve();
        int Yj = (int)((NumberTerm) args[2]).solve();

        System.out.println("--------> Police id: " + policeId + " is entering jail at (" + Xj + ", " + Yj + ")");

        // string agentName
        String agentName = "police" + (policeId + 1);
        System.out.println("--------> Agent name: " + agentName);

        // Access the CityEnvironment to call methods related to escorting
        CityEnvironment env = CityEnvironment.getInstance();
        CityModel model = CityModel.getCityModel();



        //model.removePoliceAgent(policeId, Xj, Yj);

        // Remove the police agent
        // get the actual position of the police agent
        Location currentPoliceLoc = model.getAgPos(policeId);
        //if (currentPoliceLoc.x == Xj && currentPoliceLoc.y == Yj ) {
        if (currentPoliceLoc.x == Xj && currentPoliceLoc.y == Yj || currentPoliceLoc.x == Xj+1 && currentPoliceLoc.y == Yj || currentPoliceLoc.x == Xj-1 && currentPoliceLoc.y == Yj || currentPoliceLoc.x == Xj && currentPoliceLoc.y == Yj+1 || currentPoliceLoc.x == Xj && currentPoliceLoc.y == Yj-1 || currentPoliceLoc.x == Xj+1 && currentPoliceLoc.y == Yj+1 || currentPoliceLoc.x == Xj-1 && currentPoliceLoc.y == Yj-1 || currentPoliceLoc.x == Xj+1 && currentPoliceLoc.y == Yj-1 || currentPoliceLoc.x == Xj-1 && currentPoliceLoc.y == Yj+1) {
        //if (currentPoliceLoc.x == Xj && currentPoliceLoc.y == Yj ) {
            // Only execute removePoliceAgent if the police agent is at the specified location
            // model.removePoliceAgent(policeId, Xj, Yj);
            model.removePoliceAgent(policeId, currentPoliceLoc.x, currentPoliceLoc.y);
            System.out.println("--------> Removing police agent at: (" + Xj + ", " + Yj + ")");
        } else {
            // nothing to do
        }

        // Returns true because the action has been executed successfully
        return true;
    }
}
