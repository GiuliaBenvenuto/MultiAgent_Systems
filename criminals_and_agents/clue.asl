// Agent clue in project criminals_and_agents

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */
+!start : true <- .print("I'm a clue.").

// Plan triggered when the agent's position is updated
+at(X,Y) : true <- .print("---> Updated position: at(", X, ",", Y, ").").

+myId(ID) : true <- .print("My ID is: ", ID).

