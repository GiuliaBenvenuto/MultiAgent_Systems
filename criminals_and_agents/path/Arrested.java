package path;

import city.CityEnvironment;
import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.Location;
import jason.JasonException;
import city.*;
import java.util.*;



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
        int y = (int)((NumberTerm) args[2]).solve();

        System.out.println("INTERNAL: " + criminalId + " at (" + x + ", " + y + ")");

        // criminal at 10,12 -> ID = 0 -> In the CityModel ID = 3
        // criminal at 15,25 -> ID = 1 -> In the CityModel ID = 4

        // Access the CityEnvironment to call methods related to criminal arrest
        CityEnvironment env = CityEnvironment.getInstance();

        // Arrest the criminal and remove their icon from the grid
        // Here, we assume the removal is based on the ID and the coordinates provided
        env.getCityModel().arrestCriminal(criminalId, x, y);

        // Returns true because the action was executed successfully
        return true;
    }
}