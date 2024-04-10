// Agent police in project criminals_and_agents
/* Initial beliefs and rules */
!start.


/* Plans */
+!start : true <- .print("I'm a police agent.").


+!explore : startPos(A,B) & endPos(C,D) & myId(ID) <-
    .print("Starting exploration.");
    .print("Finding path for police ", ID, " from (", A, ", ", B, ") to (", C, ", ", D, ")");
    // call to FindPath internal action and print the path
    path.FindPath(ID, A, B, C, D, Path);
    .print("Path found: ", Path);
    // Create a new belief "arrivedAtDestination" to signal the agent has arrived at the destination
    +arrivedAtDestination(Path).


// Plan triggered when the agent's position is updated
// +at(X,Y) : true <- .print("---> Updated position: at(", X, ",", Y, ").").


+startPos(A,B) : true <-
    .print("Initial position : startPos(", A, ",", B, ").").


+endPos(A,B) : true <-
    //.print("Final position : endPos(", A, ",", B, ").");
    !explore.


// +myId(ID) : true <- .print("My ID is: ", ID).


+jailPos(A,B) : true <- .print("Jail position : jailPos(", A, ",", B, ").").


+closeAgentAt(A,B,C,D) : true <-
    .print("Close agent - ID: ", C, " Type: ", D);
    .concat(D, C, AgentName);
    .print("Sending message to ", AgentName);
    .send(AgentName, tell, foundYouAt(A,B)).


// Police agent got a clue from a civilian
+clueInfo(A, B, C, D) : true <-
    .print("A civilian gave me a clue.");
    .print("Clue position: ", A, ", ", B, " Agent ID: ", C, " Type: ", D).
    //+haveClue(A, B);
    //+goToClue.


+arrivedAtDestination(Path) : true <-
    .print("Arrived at the destination, generating new path.").


/*
+arrivedAtDestination(Path) : not goToClue <-
    .print("Arrived at the destination, generating new path.").

+arrivedAtDestination(Path) : goToClue & haveClue(X, Y) <-
    .print("I HAVE A CLUE AND I WANT TO MOVE THERE");
    -goToClue;
    -haveClue(X, Y);
    -+endPos(X,Y);
    !explore.
*/







