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

/**
 * ---------- Stop Internal Action ----------
 * This class represents the internal action that handles the logic of stopping the police agent in the city when
 * they arrive at the jail and all the criminals have been arrested.
 */

public class Stop extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        // Check the number of arguments
        if (args.length != 2) {
            throw new JasonException("This internal action requires two argument.");
        }

        // Extract the police agent ID
        int policeId = (int)((NumberTerm) args[0]).solve();
        String name = ((Atom)args[1]).getFunctor();

        CityEnvironment env = CityEnvironment.getInstance();
        CityModel model = CityModel.getCityModel();

        // Get the number of arrested criminals
        int numArrestedCriminals = model.getArrestedCriminals();
        System.out.println("****** Number of arrested criminals: " + numArrestedCriminals);

        // Check if all criminals have been arrested
        if (numArrestedCriminals != 2) {

        }
        else {
            // If all criminals have been arrested, destory all agents
            AgentPercept.destroyAllAgents(env);
        }
        return true;
    }
}
