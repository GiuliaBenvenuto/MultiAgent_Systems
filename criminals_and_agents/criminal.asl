// Agent criminal in project criminals_and_agents

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */
+!start : true <- .print("I'm a criminal.").

// Plan triggered when the agent's position is updated
// +at(X,Y) : true <- .print("---> Updated position: at(", X, ",", Y, ").").

// +myId(ID) : true <- .print("My ID is: ", ID).

//+foundYouAt(X, Y) : true <-
//  .print("You found a criminal!").

+foundYouAt(X,Y)[source(AgentId)] : true <-
    .print("Found at: ", X, ",", Y, " from agent: ", AgentId).

// print agent name
+myName(NAME) : true <- .print("My name is: ", NAME).