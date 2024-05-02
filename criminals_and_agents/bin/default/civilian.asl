/** ---------- CIVILIAN AGENT in project criminals_and_agents ----------
 *  This is a particular type of agent that represents a civilian in the simulation.
 *  Each civilian agent has the belief about its closest clue location:
 *  - civilian0 has the belief of the clue at (5,5)
 *  - civilian1 has the belief of the clue at (5,35)
 *  - civilian2 has the belief of the clue at (35,5)
 *  - civilian3 has the belief of the clue at (35,30)
 *
 *  --> Consider that the X coordinates are always incremented by 1, so 5 becomes 6, 35 becomes 36.
 *      This is needed since it's impossible to reach the exact position of the clue since it's occupied by itself. 
 *      Then when a clue is found the positon that will be reached by the police agent is (X+1, Y).
 *
 * When a civilian agent is found by a police agent, it sends the clue location to the police agent.
 * 
 * Every time a police agent finds a criminal, the civilian agent is notified and sends a message to the police agent,
 * thanking him for making the city safer.
 */


/* ----- Initial beliefs and rules ------ */
// Start the agent lifecycle
!start.


/* ----- Plans ----- */
// Plan triggered when the agent is created
+!start : true <- .print("I'm a civilian.").

// Belief about the agent's position
// +at(X,Y) : true <- .print("---> Updated position: at(", X, ",", Y, ").").

// Belief about the agent's ID
// +myId(ID) : true <- .print("My ID is: ", ID).


// Belief about closest clue position
+closeClueAgent(A,B,C,D) : true <-
    .print("I have a close clue at: (", A, ",", B, ")");
    +lastClueLocation(A, B, C, D).


// Belief added when the civilian is found by a police, civilian sends to him the clue location
+foundYouAt(X,Y)[source(AgentId)] : true <-
    .print("I'm a civilian and I was found by agent: ", AgentId, " at: ", X, ",", Y);
    ?lastClueLocation(A, B, C, D);
    // Send the clue location to the police agent
    .print("Sending position of a clue to agent: ", AgentId);
    .send(AgentId, tell, clueInfo(A, B, C, D)).


// Belief added when police agent broadcasts that a criminal has been found
// Civlian agent sends a message to the police agent thanking him
+criminal_found_broadcast(Xc, Yc, ID)[source(AgentId)] : true <-
    .print("I feel safer! Tank you agent: ", AgentId).


// Belief that triggers the agent destruction
+destroyAllAgents(NAME) : true <-
    .print("Agent with name: ", NAME, " has been destroyed.");
    .kill_agent(NAME).


