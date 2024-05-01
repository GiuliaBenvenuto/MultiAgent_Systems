package path;

import city.CityEnvironment;
import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.Location;
import jason.JasonException;
import city.*;
import java.util.*;

/** ---------- Escorting Internal Action ----------
 * This class represents the internal action that handles the logic of a police agent escorting a criminal towards the jail.
 * This action is simply used to set the escorting flag to true or false in the CityModel.
 */

public class Escorting extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        // Check the number of arguments
        if (args.length != 2) {
            throw new JasonException("The 'escorting' internal action requires two arguments.");
        }

        // Ensure the arguments are integers (agent IDs)
        if (!(args[0] instanceof NumberTerm) || !(args[1] instanceof Atom)) {
            throw new JasonException("The 'escorting' internal action requires an integer and a boolean (as an atom) argument.");
        }

        // Extract the police agent ID and the criminal agent ID
        int policeId = (int)((NumberTerm) args[0]).solve();
        boolean isEscorting = ((Atom)args[1]).getFunctor().equals("true");

        // Access the CityEnvironment to call methods related to escorting
        CityEnvironment env = CityEnvironment.getInstance();
        if (isEscorting) {
            // Start escorting
            env.getCityModel().startEscorting(policeId);
        } else {
            // Stop escorting
            env.getCityModel().stopEscorting(policeId);
        }

        return true;
    }
}
