package path;

import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.Location;
import jason.JasonException;
import city.*;
import java.util.*;



public class FindPath extends DefaultInternalAction {

    private static final long serialVersionUID = 1L;

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        // Check the argument length
        if (args.length == 4) {
            int startX = (int)((NumberTerm) args[0]).solve();
            int startY = (int)((NumberTerm) args[1]).solve();
            int endX = (int)((NumberTerm) args[2]).solve();
            int endY = (int)((NumberTerm) args[3]).solve();

            // Cast the environment to your specific environment class to access the city model
            // get the environment
            // TODO: fix this
            // Environment env = ts.getAgArch().getAgArchClassesChain().getEnvironment();
            CityEnvironment env = CityEnvironment.getInstance();

            if (env instanceof CityEnvironment) { // Ensure this matches the name of your environment class
                // CityModel cityModel = ((CityEnvironment) env).getCityModel(); // Assuming getCityModel() is a method in CityEnvironment
                CityModel cityModel = env.getCityModel();

                // Create an instance of AStar with the city model
                AStar aStar = new AStar(cityModel);

                // Execute the pathfinding
                List<Location> path = aStar.findPath(0, new Location(startX, startY), new Location(endX, endY));

                // Convert the path (a List of Locations) into a Jason ListTerm
                ListTerm pathList = new ListTermImpl();
                for (Location loc : path) {
                    Term[] locationTerms = new Term[2];
                    locationTerms[0] = new NumberTermImpl(loc.x);
                    locationTerms[1] = new NumberTermImpl(loc.y);
                    pathList.add(ASSyntax.createStructure("location", locationTerms));
                }

                // Return the result as the next argument (which should be a var)
                return un.unifies(pathList, args[4]);
            } else {
                throw new JasonException("The environment is not an instance of CityEnvironment.");
            }
        } else {
            throw new JasonException("Incorrect number of arguments. Expected 5, received " + args.length);
        }
    }
}