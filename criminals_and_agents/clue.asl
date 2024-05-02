/** ---------- CLUE AGENT in project criminals_and_agents ----------
 *  This is a particular type of agent that represents a clue in the simulation.
 *  Each clue agent has the belief about the X or Y coordinate of a criminal, in particular the clue on the right side of the city have 
 *  the X and Y coordinates of the criminal in the right side of the city, and the clue on the left side of the city have the X and Y 
 *  coordinates of the criminal in the same side of the city.
 *  - clue0 has Y = 12 (Y coordinate of the criminal in the left side of the city) 
 *  - clue1 has X = 10 (X coordinate of the criminal in the left side of the city)
 *  - clue2 has Y = 21 (Y coordinate of the criminal in the right side of the city)
 *  - clue3 has X = 26 (X coordinate of the criminal in the right side of the city)
 *
 *  --> Consider that the X coordinates are incremented by 1, so 10 becomes 11 and 26 becomes 27.
 *      This is because it's impossible to reach the exact position of the criminal since it is occupied by the 
 *      criminal itself. Thus, when the police agent receives both X and Y of the criminal it will reach the criminal's 
 *      position at (X+1, Y).
 *
 *  When a clue agent is found by a police agent, it sends the X or Y coordinate of the criminal to the police agent.
 */


/* ----- Initial beliefs and rules ------ */
// Start the agent lifecycle
!start.


/* ----- Plans ----- */
// Plan triggered when the agent is created
+!start : true <- .print("I'm a clue.").

// Belief about the agent's position
// +at(X,Y) : true <- .print("---> Updated position: at(", X, ",", Y, ").").

// Belief about the agent's ID
// +myId(ID) : true <- .print("My ID is: ", ID).


// Belief added when the clue agent is found by a police agent
// Triggered only if the clue agent has the X coordinate of the criminal
+foundYouAt(X,Y)[source(AgentId)] : criminalXcoord(CIDX,CX) <-
    .print("I'm a clue and I was found by agent: ", AgentId, " at: ", X, ",", Y);
    .print("Sending X coordinate: ", CX, " of the criminal with ID: ", CIDX, " to agent: ", AgentId);
    .send(AgentId, tell, foundClueX(CIDX,CX)).


// Belief added when the clue agent is found by a police agent
// Triggered only if the clue agent has the Y coordinate of the criminal
+foundYouAt(X,Y)[source(AgentId)] : criminalYcoord(CIDY,CY) <-
    .print("I'm a clue and I was found by agent: ", AgentId, " at: ", X, ",", Y);
    .print("Sending Y coordinate: ", CY, " of the criminal with ID: ", CIDY, " to agent: ", AgentId);
    .send(AgentId, tell, foundClueY(CIDY,CY)).


// Belief about X coordinate of the criminal
+criminalXcoord(CIDX,CX) : true <-
    // criminal with CIDX = 3 is [criminal0]
    // criminal with CIDX = 4 is [criminal1]
    if (CIDX == 3) {
        .print("I have the X coordinate: ", CX, " of the [criminal 0]");
    } else {
        .print("I have the X coordinate: ", CX, " of the [criminal 1]");
    }.

// Belief about Y coordinate of the criminal
+criminalYcoord(CIDY,CY) : true <-
    // criminal with CIDY = 3 is [criminal0]
    // criminal with CIDY = 4 is [criminal1]
    if (CIDY == 3) {
        .print("I have the Y coordinate: ", CY, " of the [criminal 0]");
    } else {
        .print("I have the Y coordinate: ", CY, " of the [criminal 1]");
    }.


// Belief that triggers the agent destruction
+destroyAllAgents(NAME) : true <-
    .print("Agent with name: ", NAME, " has been destroyed.");
    .kill_agent(NAME).