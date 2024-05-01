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


/** ---------- EnterJail Internal Action ----------
 * This class represents the internal action that handles the logic of a police agent entering the jail in the city.
 * So when the police agent takes a criminal and escorts it to the jail, once arrived at jail, this action is executed.
 */

public class EnterJail extends DefaultInternalAction {

    private static final long serialVersionUID = 1L;

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        // Check the number of arguments
        if (args.length != 3) {
            throw new JasonException("This internal action requires three arguments.");
        }

        // Extract the police agent ID and the coordinates of the jail (Xj, Yj)
        int policeId = (int)((NumberTerm) args[0]).solve();
        int Xj = (int)((NumberTerm) args[1]).solve();
        int Yj = (int)((NumberTerm) args[2]).solve();

        String agentName = "police" + (policeId + 1);
        System.out.println("Police agent: " + agentName + " is entering jail at (" + Xj + ", " + Yj + "), escorting a criminal.");

        // Access the CityEnvironment to call methods related to escorting
        CityEnvironment env = CityEnvironment.getInstance();
        CityModel model = CityModel.getCityModel();

        // Remove the police agent
        Location currentPoliceLoc = model.getAgPos(policeId);
        int xJail = 35;
        int yJail = 35;

        if (Math.abs(currentPoliceLoc.x - xJail) <= 2 && Math.abs(currentPoliceLoc.y - yJail) <= 2) {
            model.removePoliceAgent(policeId, currentPoliceLoc.x, currentPoliceLoc.y);
            System.out.println("Police agent: " + agentName + "removed at: (" + Xj + ", " + Yj + ")");
        } else {
            // nothing to do
        }
        return true;
    }
}
