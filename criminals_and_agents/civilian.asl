/** ---------- CIVILIAN AGENT in project criminals_and_agents ----------
 *  This is a particular type of agent that represents a civilian in the simulation.
 *  Each civilian agent has the belief about its closest clue location:
 *  - civilian0 has the belief of the clue at (5,5)
 *  - civilian1 has the belief of the clue at (5,35)
 *  - civilian2 has the belief of the clue at (35,5)
 *  - civilian3 has the belief of the clue at (35,30)
 *
 * When a civilian agent is found by a police agent, it sends the clue location to the police agent.
 * 
 * Every time a police agent finds a criminal, the civilian agent is notified and sends a message to the police agent,
 * thanking him for making the city safer.
 */


/* Initial beliefs and rules */
!start.

/* Plans */
+!start : true <- .print("I'm a civilian.").

// Plan triggered when the agent's position is updated
// +at(X,Y) : true <- .print("---> Updated position: at(", X, ",", Y, ").").

// +myId(ID) : true <- .print("My ID is: ", ID).

// Knowledge about clue location
+closeClueAgent(A,B,C,D) : true <-
    .print("I have a close clue at: ", A, ",", B, " with ID: ", C, " and type: ", D);
    +lastClueLocation(A, B, C, D).

// Civilian found by a police and sends to him the clue location
+foundYouAt(X,Y)[source(AgentId)] : true <-
    .print("Found at: ", X, ",", Y, " from agent: ", AgentId);
    ?lastClueLocation(A, B, C, D);
    .send(AgentId, tell, clueInfo(A, B, C, D)).


+destroyAllAgents(NAME) : true <-
    .print("________________ Destroying ALLLL agents: _________________", NAME);
    .kill_agent(NAME).


+criminal_found_broadcast(Xc, Yc, ID)[source(AgentId)] : true <-
    .print("I feel safer! Tank you agent: ", AgentId).