package path;

import city.CityEnvironment;
import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.Location;
import jason.JasonException;
import city.*;
import java.util.*;



public class FindPath extends DefaultInternalAction {

    //private static final long serialVersionUID = 1L;

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        // Check argument length
        if (args.length == 6) {
            int policeId = (int)((NumberTerm) args[0]).solve();     // Get police agent ID
            int startX = (int)((NumberTerm) args[1]).solve();       // Get start X coordinate
            int startY = (int)((NumberTerm) args[2]).solve();       // Get start Y coordinate
            int endX = (int)((NumberTerm) args[3]).solve();         // Get end X coordinate
            int endY = (int)((NumberTerm) args[4]).solve();         // Get end Y coordinate

            //System.out.println("Finding path for police " + policeId + " from (" + startX + ", " + startY + ") to (" + endX + ", " + endY + ")");

            // Get the city environment
            CityEnvironment env = CityEnvironment.getInstance();

            if (env instanceof CityEnvironment) {
                // Get the city model
                CityModel cityModel = env.getCityModel();

                // Create an instance of AStar to perform pathfinding
                AStar aStar = new AStar(cityModel);

                // Execute the pathfinding
                List<Location> path = aStar.findPath(policeId, new Location(startX, startY), new Location(endX, endY));
                // Process the path to move police agents icons
                // Add the case where path is <no value>
                if (path == null || path.isEmpty() || path.size() == 0) {
                    // return un.unifies(ASSyntax.createList(), args[5]); // Return an empty list if no path is found
                    // If a path can't be found to the left of the agent find the path that arrives to the right of the agent
                    System.out.println("//////EMPTY PATH, GOING TO endX+2");
                    path = aStar.findPath(policeId, new Location(startX, startY), new Location(endX+2, endY));
                    CityEnvironment.getInstance().processPath(policeId, path);
                } else {
                    // If a path is found, process it
                    //System.out.println("Path found for police " + policeId + ": " + path.toString());
                    CityEnvironment.getInstance().processPath(policeId, path);
                }

                // Convert the path (a List of Locations) into a Jason ListTerm
                ListTerm pathList = new ListTermImpl();
                for (Location loc : path) {
                    Term[] locationTerms = new Term[2];
                    locationTerms[0] = new NumberTermImpl(loc.x);
                    locationTerms[1] = new NumberTermImpl(loc.y);
                    pathList.add(ASSyntax.createStructure("location", locationTerms));
                }

                // Return the result
                return un.unifies(pathList, args[5]);
            } else {
                throw new JasonException("The environment is not an instance of CityEnvironment.");
            }
        } else {
            throw new JasonException("Incorrect number of arguments. Expected 5, received " + args.length);
        }
    }
}
