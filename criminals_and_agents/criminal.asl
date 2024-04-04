// Agent criminal in project criminals_and_agents

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */
+!start : true <- .print("I'm a criminal.").

// Plan triggered when the agent's position is updated
+at(X,Y) : true <- .print("Updated position: at(", X, ",", Y, ").").