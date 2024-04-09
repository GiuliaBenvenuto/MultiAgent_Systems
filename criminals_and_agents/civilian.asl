// Agent civilian in project criminals_and_agents
/* Initial beliefs and rules */
!start.

/* Plans */
+!start : true <- .print("I'm a civilian.").

// Plan triggered when the agent's position is updated
// +at(X,Y) : true <- .print("---> Updated position: at(", X, ",", Y, ").").

//+myId(ID) : true <- .print("My ID is: ", ID).

+closeClueAgent(A,B,C,D) : true <-
    .print("I have a close clue at: ", A, ",", B, " with ID: ", C, " and type: ", D);
    +lastClueLocation(A, B, C, D).

+foundYouAt(X,Y)[source(AgentId)] : true <-
    .print("Found at: ", X, ",", Y, " from agent: ", AgentId);
    ?lastClueLocation(A, B, C, D);
    .send(AgentId, tell, clueInfo(A, B, C, D)).

