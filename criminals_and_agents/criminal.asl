// Agent criminal in project criminals_and_agents
/* Initial beliefs and rules */
/* Initial goals */

!start.

/* Plans */
+!start : true <- .print("I'm a criminal.").

// Plan triggered when the agent's position is updated
+at(A,B) : true <- .print("---> Updated position: at(", A, ",", B, ").").

// print agent name
+myName(NAME) : true <- .print("My name is: ", NAME).

// +myId(ID) : true <- .print("My ID is: ", ID).

//+foundYouAt(X, Y) : true <-
//  .print("You found a criminal!").

//+foundYouAt(X,Y)[source(AgentId)] : true <-
//    .print("Found at: ", X, ",", Y, " from agent: ", AgentId).

+foundYouAt(X,Y)[source(AgentId)] : at(A,B) <-
    .print("Found at: ", X, ",", Y, " from agent: ", AgentId);
    !escape.


+!escape : at(A,B) & myId(ID) <-
    .print("CRIMINAL ESCAPING");
    path.Escape(ID, A, B, Path).