package path;

import city.CityEnvironment;
import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.Location;
import jason.JasonException;
import city.*;
import java.util.*;


/** ---------- Arrested Internal Action ----------
 * This class represents the internal action to arrest a criminal in the city.
 * The action requires three arguments: the criminal agent ID and the coordinates (X, Y) of the criminal.
 */

public class Arrested extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        // Check the number of arguments
        if (args.length != 3) {
            throw new JasonException("The 'arrested' internal action requires exactly three arguments.");
        }

        // Ensure the arguments are the correct types
        if (!(args[0] instanceof NumberTerm) || !(args[1] instanceof NumberTerm) || !(args[2] instanceof NumberTerm)) {
            throw new JasonException("The 'arrested' internal action requires three integer arguments: agent ID and coordinates (X, Y).");
        }

        // Extract the criminal agent ID and coordinates
        int criminalId = (int)((NumberTerm) args[0]).solve();
        int x = (int)((NumberTerm) args[1]).solve();
        int y = (int)((NumberTerm) args[2]).solve();;

        // criminal at 10,12 -> ID = 0 -> In the CityModel ID = 3
        // criminal at 15,25 -> ID = 1 -> In the CityModel ID = 4

        CityEnvironment env = CityEnvironment.getInstance();
        // Arrest the criminal and remove their icon from the grid
        env.getCityModel().arrestCriminal(criminalId, x, y);

        return true;
    }
}