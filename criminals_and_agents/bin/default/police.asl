/** ---------- POLICE AGENT in project criminals_and_agents ----------
 *  This is the most complex agent in the project because it has to do various actions depending on its percepts.
 *  First of all, the police agent has to explore the environment starting from a given position near the jail and every
 *  time the end position is reached, a new path to a random position is computed and the agent moves there such that
 *  police agents are never stopped.
 *
 *  While moving in the environment the police agent looks around itself in order to discover neighboring agents and
 *  depending on the type of agent found, something different happens:
 *  1) If a civilian agent is found, the police agent receives the coordinates of a clue from it and move towards the clue.
 *  2) If a clue is found, the police agent receives the X or Y coordinate of a criminal.
 *  3) If a criminal agent is found, the police agent arrests the criminal and moves towards the jail.
 */


/* ----- Initial beliefs and rules ------ */
// Start the agent lifecycle 
!start.


/* ----- Plans ----- */
// Plan triggered when the agent is created
+!start : true <- .print("I'm a police agent.").


// Plan triggered when the agent must explore the environment
// Executed if a start position, an end position and the agent's ID are available
+!explore : startPos(A,B) & endPos(C,D) & myId(ID) <-
    .print("I'm exploring the city.");

    // ___Priority 1___: If a criminal has been arrested, move to jail
    if (arrestedCriminal(Xc, Yc) & jailPos(Xj, Yj)) {
        .print("----- POLICE HAS A CRIMINAL ------");
        .print("----- ESCORTING CRIMINAL AT JAIL ------");
        // Execute internal action FindPath from (A,B) to (Xj,Yj) of the jail and print the path
        path.FindPath(ID, A, B, Xj, Yj, Path);
        .print("PATH TO JAIL from (", A, ", ", B, ") to (", Xj, ", ", Yj, "): ", Path);
        // Remove the arrested criminal belief and the agent's ID belief
        -arrestedCriminal(Xc, Yc);
        -myId(ID);
        // Execute internal action EnterJail to signal the police agent has reached the jail escorting a criminal
        path.EnterJail(ID, Xj, Yj);
    }

    // ___Priority 2___: If both clues (X,Y coordinates) have been found move towards the criminal
    elif (haveClueX(CIDX,CX) & haveClueY(CIDY,CY) & jailPos(Xj, Yj)) {
        // check if CIDX and CIDY are equal (ids of the criminal relative to the clues X and Y)
        if (CIDX == CIDY) {
            .print("----- POLICE HAS BOTH X and Y of CRIMINAL ------");
            .print("----- GOING TOWORDS CRIMINAL ------");
            // Execute internal action FindPath from (A,B) to (CX,CY) of the criminal and print the path
            path.FindPath(ID, A, B, CX, CY, Path);
            .print("PATH TO CRIMINAL from (", A, ", ", B, ") to (", CX, ", ", CY, "): ", Path);
            // Remove the X and Y coordinates of the criminal and the haveClue belief
            -haveClueX(CIDX,CX); 
            -haveClueY(CIDY,CY); 
            -haveClue(X,Y); 
        } else {
            // If police has both clues but they are not related to the same criminal, move randomly
            path.FindPath(ID, A, B, C, D, Path);
        }
    }

    // ___Priority 3___: If the police has a clue and no criminal has been arrested, move to the clue
    elif (haveClue(X,Y) & not arrestedCriminal(X, Y)) {
        .print("----- POLICE GOING TO CLUE ------");
        // Execute FindPath internal action from (A,B) to (X,Y) of the clue and print the path
        path.FindPath(ID, A, B, X, Y, Path);
        .print("PATH TO CLUE from (", A, ", ", B, ") to (", X, ", ", Y, "): ", Path);
        // Remove the clue after moving towards it
        -haveClue(X,Y);
    }

    // ___Priority 4___: If the police has no clues and no criminal, move randomly
    elif (not haveClue(X,Y) & not arrestedCriminal(Xc, Yc)) {
        .print("----- POLICE MOVING RANDOMLY ------");
        // Execute FindPath internal action from (A,B) to (C,D) random position for normal exploration and print the path
        path.FindPath(ID, A, B, C, D, Path);
        .print("PATH TO RANDOM POSITION from (", A, ", ", B, ") to (", C, ", ", D, "): ", Path);
    }

    else {
        // If no other condition is met, move randomly
        .print("----- POLICE MOVING RANDOMLY ------");
        // Execute FindPath internal action from (A,B) to (C,D) random position for normal exploration and print the path
        path.FindPath(ID, A, B, C, D, Path);
        .print("PATH TO RANDOM POSITION from (", A, ", ", B, ") to (", C, ", ", D, "): ", Path);
    }
    // New belief to signal the agent has arrived at the destination
    +arrivedAtDestination.


// Beliefs about the agent's position
// +at(X,Y) : true <- .print("Updated position: at(", X, ",", Y, ").").
// +currentPos(A,B) : true <- .print("Current position : currentPos(", A, ",", B, ").").

// Belief about the agent's ID
// +myId(ID) : true <- .print("My ID is: ", ID).

// Belief about the agent's start position
// +startPos(A,B) : true <- .print("Initial position : startPos(", A, ",", B, ").").

// Belief about the agent's end position that triggers the exploration
+endPos(A,B) : true <- !explore.


// Belief about the jail position
+jailPos(Xj,Yj) : true <- .print("------ JAIL POSITION: (", Xj, ",", Yj, ") ------").


// Belief about a close agent that triggers the sending of a message
+closeAgentAt(A,B,C,D) : not escorting(ID) <-
    .print("----- FOUND A CLOSE AGENT at (", A, ", ", B, ") - Type: ", D, " ID: ", C, " ------");
    //.print("Close agent - ID: ", C, " Type: ", D);
    // Concatenate the agent's type and ID to send a message
    .concat(D, C, AgentName);
    // Send a message to the close agent
    .print("Sending a message to: ", AgentName);
    .send(AgentName, tell, foundYouAt(A,B)).


// Belief added when the police agent receives a clue from a civilian
+clueInfo(X, Y, C, D) : true <-
    .print("A civilian gave me a clue.");
    .print("Clue position: (", X, ", ", Y, ") - Type: ", D, " ID: ", C);
    // Add the clue to the agent's beliefs
    +haveClue(X, Y).


// Belief added when a criminal is arrested
+arrestedCriminal(Xc, Yc) : myId(ID) <-
    .print("----- YOU ARE A CRIMINAL AND YOU ARE UNDER ARREST AT: (", Xc, ", ", Yc, ") ------");
    // Add the belief that the agent is escorting the criminal
    +escorting(ID);
    // Send a broadcast message to all agents to signal the criminal has been arrested
    .broadcast(tell, criminal_found_broadcast(Xc, Yc, ID));
    // Execute the Escorting internal action to set the police state to escorting
    path.Escorting(ID, true).


// Belief added when a criminal is arrested and a broadcast message is received
+criminal_found_broadcast(Xc, Yc, ID)[source(AgentId)] : true <-
    .print("Well done collegue: ", AgentId, " you did a great job.").


// Belief added when the police agent reaches the jail escorting a criminal
+reachedJail(T,K) : myId(ID) & arrestedCriminal(Xc,Yc) <-
    .print("!!!! ARRIVED AT JAIL WITH A CRIMINAL !!!!");
    // Execute the Escorting internal action to set the police state to not escorting
    path.Escorting(ID, false); 
    -escorting(ID);
    //+jailOccupied(T,K);
    -arrestedCriminal(Xc,Yc);
    -myId(ID);
    -startPos(A,B);
    -endPos(C,D).


+arrivedAtDestination : true <-
    .print("Arrived at the destination, generating new path.").


// _____ X and Y Clues ______
+foundClueX(CIDX,CX) : true <-
    .print("POLICE ------> Found clue X: ", CX, " with ID: ", CIDX);
    +haveClueX(CIDX,CX).

+foundClueY(CIDY,CY) : true <-
    .print("POLICE ------> Found clue Y: ", CY, " with ID: ", CIDY);
    +haveClueY(CIDY,CY).


// _____Destroy agent_____
+destroyMe(NAME) : true <-
    .print("Agent with name: ", NAME, " has been destroyed.");
    .kill_agent(NAME).

+destroyAllAgents(NAME) : true <-
    .print("Agent with name: ", NAME, " has been destroyed.");
    .kill_agent(NAME).






