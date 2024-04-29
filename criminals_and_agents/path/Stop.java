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


public class Stop extends DefaultInternalAction {

    private static final long serialVersionUID = 1L;

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        // Check the number of arguments
        if (args.length != 2) {
            throw new JasonException("This internal action requires one argument.");
        }

        // Extract the police agent ID and the criminal agent ID
        int policeId = (int)((NumberTerm) args[0]).solve();
        String name = ((Atom)args[1]).getFunctor();

        CityEnvironment env = CityEnvironment.getInstance();
        CityModel model = CityModel.getCityModel();

        int numArrestedCriminals = model.getArrestedCriminals();
        System.out.println("****** Number of arrested criminals: " + numArrestedCriminals);

        if (numArrestedCriminals != 2) {
            // All criminals have been arrested
            // Stop all the agents calling the agent percept method


        } else {
            AgentPercept.destroyAllAgents(env);
        }


        // string agentName
        String agentName = "police" + (policeId + 1);
        System.out.println("--------> Agent name: " + agentName);

        // Access the CityEnvironment to call methods related to escorting




        return true;
    }
}
