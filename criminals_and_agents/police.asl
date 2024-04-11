// Agent police in project criminals_and_agents
/* Initial beliefs and rules */
!start.


/* Plans */
+!start : true <- .print("I'm a police agent.").

/*
+!explore : startPos(A,B) & endPos(C,D) & myId(ID) <-
    .print("Starting exploration.");
    if (haveClue(X,Y)) {
        .print("----- POLICE GOING TO CLUE ------ ", ID, " from (", A, ", ", B, ") to (", X, ", ", Y, ")");
        // call to FindPath internal action towards the clue and print the path
        path.FindPath(ID, A, B, X, Y, Path);
        -haveClue(X,Y); // Remove the clue after moving towards it
    } else {
        .print("Finding path for police ", ID, " from (", A, ", ", B, ") to (", C, ", ", D, ")");
        // call to FindPath internal action for normal exploration and print the path
        path.FindPath(ID, A, B, C, D, Path);
    }
    .print("Path found: ", Path);
    // Create a new belief "arrivedAtDestination" to signal the agent has arrived at the destination
    +arrivedAtDestination.
*/

+!explore : startPos(A,B) & endPos(C,D) & myId(ID) <-
    .print("Starting exploration.");
    if (haveClue(X,Y) & not arrestedCriminal(X, Y)) {
        .print("----- POLICE GOING TO CLUE ------ ", ID, " from (", A, ", ", B, ") to (", X, ", ", Y, ")");
        // call to FindPath internal action towards the clue and print the path
        path.FindPath(ID, A, B, X, Y, Path);
        -haveClue(X,Y); // Remove the clue after moving towards it
    }
    elif (not haveClue(X,Y) & not arrestedCriminal(Xc, Yc)) {
        .print("Finding path for police ", ID, " from (", A, ", ", B, ") to (", C, ", ", D, ")");
        // call to FindPath internal action for normal exploration and print the path
        path.FindPath(ID, A, B, C, D, Path);
    }
    elif (arrestedCriminal(Xc, Yc) & jailPos(Xj, Yj) & not haveClue(X,Y)) {
        .print("----- POLICE HAS A CRIMINAL ------");
        path.FindPath(ID, A, B, Xj, Yj, Path);
        .print("PATH TO JAIL: ", Path);
        -arrestedCriminal(Xc, Yc);
    } else {
        .print("--WARNING--> No path found.");
    }
    .print("Path found: ", Path);
    // Create a new belief "arrivedAtDestination" to signal the agent has arrived at the destination
    +arrivedAtDestination.


// Plan triggered when the agent's position is updated
// +at(X,Y) : true <- .print("---> Updated position: at(", X, ",", Y, ").").

//+currentPos(A,B) : true <-
//    .print("#############Current position : currentPos(", A, ",", B, ").").

// +myId(ID) : true <- .print("My ID is: ", ID).


+startPos(A,B) : true <-
    .print("Initial position : startPos(", A, ",", B, ").").


+endPos(A,B) : true <-
    //.print("Final position : endPos(", A, ",", B, ").");
    !explore.


+jailPos(Xj,Yj) : true <- .print("------Jail POLICE position ------ : jailPos(", Xj, ",", Yj, ").").


+closeAgentAt(A,B,C,D) : true <-
    .print("Close agent - ID: ", C, " Type: ", D);
    .concat(D, C, AgentName);
    .print("Sending message to ", AgentName);
    .send(AgentName, tell, foundYouAt(A,B)).


// Police agent got a clue from a civilian
+clueInfo(X, Y, C, D) : true <-
    .print("A civilian gave me a clue.");
    .print("Clue position: ", X, ", ", Y, " Agent ID: ", C, " Type: ", D);
    +haveClue(X, Y).


// Police arrested a criminal
+arrestedCriminal(Xc, Yc) : true <-
    .print("ARRESTED CRIMINAL at position: ", Xc, ", ", Yc).


+arrivedAtDestination : true <-
    .print("Arrived at the destination, generating new path.").








