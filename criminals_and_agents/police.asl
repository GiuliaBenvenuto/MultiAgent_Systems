// Agent police in project criminals_and_agents
/* Initial beliefs and rules */
!start.

/* Plans */
+!start : true <- .print("I'm a police agent.").
+!explore : startPos(A,B) & endPos(C,D) & myId(ID) <-
    .print("!!!! ---- Starting exploration.");
    .print("A: ", A, " B: ", B, " C: ", C, " D: ", D);
    // call to FindPath internal action and print the path
    path.FindPath(ID, A, B, C, D, Path);
    .print("Path found: ", Path).


// Plan triggered when the agent's position is updated
+at(X,Y) : true <- .print("---> Updated position: at(", X, ",", Y, ").").

+startPos(A,B) : true <-
    .print("*** Initial position : startPos(", A, ",", B, ").").

+endPos(A,B) : true <-
    //.print("*** Final position : endPos(", A, ",", B, ").");
    !explore.

+myId(ID) : true <- .print("My ID is: ", ID).






