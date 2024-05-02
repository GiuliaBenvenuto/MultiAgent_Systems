/** ---------- CRIMINAL AGENT in project criminals_and_agents ----------
 *  This is the agent that represents the criminal in the city and it is probably the simplest agent in the project.
 *  In the city there are two criminals:
 *  - criminal0 at position (10,12) but reached at (11,12) by the police agent
 *  - criminal1 at position (26,21) but reached at (27,21) by the police agent
 *
 *  Criminals are static, they do not move in the grid. 
 *  When they are caught by the police agent, they are arrested and escorted to the jail. 
 *  After being arrested they are removed from their position in the grid and not visible anymore in the original position 
 *  but we can see that the police agent icon change meaning that the he is carrying the criminal to the jail 
 *  and then when they arrive to jail, the jail icon changes to show that the criminal is inside it.
 */


/* ----- Initial beliefs and rules ------ */
// Start the agent lifecycle
!start.


/* ----- Plans ----- */
// Plan triggered when the agent is created
+!start : true <- .print("I'm a criminal.").

// Plan triggered when the agent's position is updated
+at(Xc,Yc) : myId(ID) <- 
    // ID = 0 --> Name: criminal1
    // ID = 1 --> Name: criminal2
    .print("-----> I'm the criminal: ", (ID + 1), " and I'm hiding at: (", Xc, ",", Yc, ").").

// Belief about the agent's ID
// +myId(ID) : true <- .print("My ID is: ", ID).


// Belief added when the criminal is found by the police agent
+foundYouAt(X,Y)[source(AgentId)] : true <-
    .print("I'm a criminal and I was found by agent: ", AgentId, " at: ", X, ",", Y, ".");
    ?at(Xc,Yc);
    // Add the belief that the criminal was arrested at the position where he was found
    +arrestedAt(X,Y);
    .send(AgentId, tell, arrestedCriminal(Xc,Yc)).


// Belief added when the criminal is arrested by the police agent
+arrestedAt(X,Y) : myId(ID) & at(Xc,Yc) <-
    .print("Police agent is picking me up at: ", X, ",", Y, " and taking me to jail.");
    // Execute Arrested internal action to remove the criminal from the grid
    path.Arrested(ID, Xc, Yc).


// Belief that triggers the agent destruction
+destroyAllAgents(NAME) : true <-
    .print("Agent with name: ", NAME, " has been destroyed.");
    .kill_agent(NAME).