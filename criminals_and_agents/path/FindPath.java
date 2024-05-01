package path;

import city.CityEnvironment;
import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.Location;
import jason.JasonException;
import city.*;
import java.util.*;

/** ---------- FindPath Internal Action ----------
 * This class represents the internal action that handles the logic of finding a path between two locations in the city.
 * This internal action is used by all the police agents in the grid to find the shortest path from the start point to the end point,
 * it is used in several situations: first of all it is used to explore randomly the city, then it is used to find the shortest path
 * to the clue or criminal positions and finally it is used to find the shortest path to the jail.
 */

public class FindPath extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        // Check argument length
        if (args.length == 6) {
            int policeId = (int)((NumberTerm) args[0]).solve();     // Get police agent ID
            int startX = (int)((NumberTerm) args[1]).solve();       // Get start X coordinate
            int startY = (int)((NumberTerm) args[2]).solve();       // Get start Y coordinate
            int endX = (int)((NumberTerm) args[3]).solve();         // Get end X coordinate
            int endY = (int)((NumberTerm) args[4]).solve();         // Get end Y coordinate

            // Get the city environment
            CityEnvironment env = CityEnvironment.getInstance();
            if (env instanceof CityEnvironment) {
                // Get the city model
                CityModel cityModel = env.getCityModel();
                // Create an instance of AStar to perform pathfinding
                AStar aStar = new AStar(cityModel);

                // Execute the pathfinding from start to end
                List<Location> path = aStar.findPath(policeId, new Location(startX, startY), new Location(endX, endY));

                // Process the path to move police agents icons
                // Add the case where path is <no value>
                if (path == null || path.isEmpty() || path.size() == 0) {
                    // If a path can't be found to the left (endX-1) of the agent find the path that arrives to the right of the agent
                    System.out.println("---> EMPTY PATH, GOING TO endX+2 to the right of the agent");
                    path = aStar.findPath(policeId, new Location(startX, startY), new Location(endX+2, endY));
                    CityEnvironment.getInstance().processPath(policeId, path);
                } else {
                    // If a path is found, process it
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
