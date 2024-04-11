package path;

import city.CityEnvironment;
import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.Location;
import jason.JasonException;
import city.*;
import java.util.*;



public class Escape extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        // Check argument length
        if (args.length == 4) { // Expecting 4 arguments: criminalId, startX, startY, and the variable to unify with the path
            int criminalId = (int)((NumberTerm) args[0]).solve();  // Get criminal agent ID
            int startX = (int)((NumberTerm) args[1]).solve();      // Get start X coordinate
            int startY = (int)((NumberTerm) args[2]).solve();      // Get start Y coordinate

            System.out.println("Finding escape path for criminal " + criminalId + " from (" + startX + ", " + startY + ")");

            // Get the city environment
            CityEnvironment env = CityEnvironment.getInstance();

            if (env instanceof CityEnvironment) {
                CityModel cityModel = env.getCityModel();
                AStar aStar = new AStar(cityModel);

                // Generate random escape coordinates
                Random rand = new Random();
                int endX = rand.nextInt(11) - 5 + startX;
                int endY = rand.nextInt(11) - 5 + startY;

                List<Location> path = aStar.findPath(criminalId, new Location(startX, startY), new Location(endX, endY));

                if (path == null || path.isEmpty()) {
                    System.out.println("No escape path found.");
                    return un.unifies(ASSyntax.createList(), args[3]); // Unify with an empty list if no path is found
                } else {
                    System.out.println("Escape path found: " + path);
                    env.processCriminalEscape(criminalId, path);
                }

                // Convert and return the path
                ListTerm pathList = new ListTermImpl();
                for (Location loc : path) {
                    pathList.add(ASSyntax.createStructure("location", new NumberTermImpl(loc.x), new NumberTermImpl(loc.y)));
                }
                return un.unifies(pathList, args[3]);
            } else {
                throw new JasonException("The environment is not an instance of CityEnvironment.");
            }
        } else {
            throw new JasonException("Incorrect number of arguments. Expected 4, received " + args.length);
        }
    }
}

/*
public class Escape extends DefaultInternalAction {

    //private static final long serialVersionUID = 1L;

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        // Check argument length
        if (args.length == 4) {
            int criminalId = (int)((NumberTerm) args[0]).solve();     // Get criminal agent ID
            int startX = (int)((NumberTerm) args[1]).solve();       // Get start X coordinate
            int startY = (int)((NumberTerm) args[2]).solve();       // Get start Y coordinate
            //int endX = (int)((NumberTerm) args[3]).solve();         // Get end X coordinate
            //int endY = (int)((NumberTerm) args[4]).solve();         // Get end Y coordinate

            System.out.println("Finding path for criminal " + criminalId + " from (" + startX + ", " + startY + ")");

            // Get the city environment
            CityEnvironment env = CityEnvironment.getInstance();

            if (env instanceof CityEnvironment) {
                // Get the city model
                CityModel cityModel = env.getCityModel();

                // Create an instance of AStar to perform pathfinding
                AStar aStar = new AStar(cityModel);

                // Random position at +5 -5 from startX and startY
                Random rand = new Random();
                int endX = rand.nextInt(11) - 5 + startX;
                int endY = rand.nextInt(11) - 5 + startY;

                // Execute the pathfinding
                List<Location> path = aStar.findPath(criminalId, new Location(startX, startY), new Location(endX, endY));
                // Process the path to move criminal agents icons
                if (path == null || path.isEmpty()) {
                    System.out.println("No path found for criminal " + criminalId + " from (" + startX + ", " + startY + ") to (" + endX + ", " + endY + ")");
                    return un.unifies(ASSyntax.createList(), args[3]); // Return an empty list if no path is found
                } else {
                    // If a path is found, process it
                    System.out.println("Path found for criminal " + criminalId + ": " + path.toString());
                    CityEnvironment.getInstance().processCriminalEscape(criminalId, path);
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
                return un.unifies(pathList, args[3]);
            } else {
                throw new JasonException("The environment is not an instance of CityEnvironment.");
            }
        } else {
            throw new JasonException("Incorrect number of arguments. Expected 5, received " + args.length);
        }
    }
}

 */