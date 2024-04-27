// Agent police in project criminals_and_agents
/* Initial beliefs and rules */
!start.


/* Plans */
+!start : true <- .print("I'm a police agent.").

/* ------ OLD -------
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
    elif (arrestedCriminal(Xc, Yc) & jailPos(Xj, Yj)) {
        .print("----- POLICE HAS A CRIMINAL ------");
        path.FindPath(ID, A, B, Xj, Yj, Path);
        .print("PATH TO JAIL: ", Path);
        -arrestedCriminal(Xc, Yc);
    }

    // ABOUT HAVING CLUES
    elif (foundClueX(CIDX,CX) & foundClueY(CIDY,CY)) {
        // check if CIDX and CIDY are equal
        .print("----- POLICE HAS BOTH CLUES ------ ");
        if (CIDX == CIDY) {
            .print("----- GOING TOWORDS CRIMINAL ------ ");
            // call to FindPath internal action towards the clue and print the path
            path.FindPath(ID, A, B, CX, CY, Path);
            -haveClueX(CIDX,CX); // Remove the clue after moving towards it
            -haveClueY(CIDY,CY);
        }
    }
     else {
        .print("--WARNING--> No path found.");
    }
    .print("Path found: ", Path);
    // Create a new belief "arrivedAtDestination" to signal the agent has arrived at the destination
    +arrivedAtDestination.
*/





// ------- PROVA --------
+!explore : startPos(A,B) & endPos(C,D) & myId(ID) <-
    .print("Starting exploration.");

    // ___Priority 1___: Check if both clues are found and move towards the criminal
    if (haveClueX(CIDX,CX) & haveClueY(CIDY,CY) & jailPos(Xj, Yj)) {
        // check if CIDX and CIDY are equal
        .print("----- POLICE HAS BOTH CLUES ------ ");
        if (CIDX == CIDY) {
            .print("----- GOING TOWORDS CRIMINAL ------ ");
            // call to FindPath internal action towards the clue and print the path
            path.FindPath(ID, A, B, CX, CY, Path);
            -haveClueX(CIDX,CX); // Remove the clue after moving towards it
            -haveClueY(CIDY,CY);
            //-foundClueX(CIDX,CX);
            //-foundClueY(CIDY,CY);
            -haveClue(X,Y);
        }
    }

    /* ___Priority 2___: If a criminal has been arrested, move to jail
    elif (arrestedCriminal(Xc, Yc) & jailPos(Xj, Yj)) {
        .print("----- POLICE HAS A CRIMINAL ------");
        path.FindPath(ID, A, B, Xj, Yj, Path);
        .print("PATH TO JAIL: ", Path);
        -arrestedCriminal(Xc, Yc);
    }*/

    /* ___Priority 3___: If a criminal has been arrested, move to jail
    elif (arrestedCriminal(Xc, Yc) & jailPos(Xj, Yj)) {
        .print("----- POLICE HAS A CRIMINAL ------");
        if (not jailOccupied(T,K)) {
            path.FindPath(ID, A, B, Xj, Yj, Path);
            .print("PATH TO JAIL: ", Path);
            -arrestedCriminal(Xc, Yc);
            +jailOccupied(T,K);
            -startPos(A,B);
            -endPos(C,D);
            -myId(ID);
            .drop_all_intentions;
            .drop_all_events;
        } else {
            // Directly calculate alternative positions without internal actions
            path.FindPath(ID, A, B, (Xj+1), (Yj-1), Path);
            .print("PATH TO ALTERNATIVE JAIL: ", Path);
            -arrestedCriminal(Xc, Yc);
            +jailOccupied(newX, newY);
            -startPos(A,B);
            -endPos(C,D);
            -myId(ID);
            .drop_all_intentions;
            .drop_all_events;
        }
    }*/
    elif (arrestedCriminal(Xc, Yc) & jailPos(Xj, Yj)) {
        .print("----- POLICE HAS A CRIMINAL ------");
        path.FindPath(ID, A, B, Xj, Yj, Path);
        .print("PATH TO JAIL: ", Path);
        -arrestedCriminal(Xc, Yc);
        -myId(ID);
        path.EnterJail(ID, Xj, Yj);
    }


    // ___Priority 3___: If the police has a clue and no criminal has been arrested, move to the clue
    elif (haveClue(X,Y) & not arrestedCriminal(X, Y)) {
        .print("----- POLICE GOING TO CLUE ------ ", ID, " from (", A, ", ", B, ") to (", X, ", ", Y, ")");
        // call to FindPath internal action towards the clue and print the path
        path.FindPath(ID, A, B, X, Y, Path);
        -haveClue(X,Y); // Remove the clue after moving towards it
    }

    // ___Priority 4___: If no clues and no criminal, move randomly
    elif (not haveClue(X,Y) & not arrestedCriminal(Xc, Yc)) {
        .print("Finding path for police ", ID, " from (", A, ", ", B, ") to (", C, ", ", D, ")");
        // call to FindPath internal action for normal exploration and print the path
        path.FindPath(ID, A, B, C, D, Path);
    }


     else {
        .print("--WARNING--> No path found.");
    }
    .print("Path found: ", Path);
    // Create a new belief "arrivedAtDestination" to signal the agent has arrived at the destination
    +arrivedAtDestination.


// Plan triggered when the agent's position is updated
// +at(X,Y) : true <- .print("---> Updated position: at(", X, ",", Y, ").").

//+currentPos(A,B) : true <-
//    .print("#############Current position : currentPos(", A, ",", B, ").").

+myId(ID) : true <- .print("My ID is: ", ID).


+startPos(A,B) : true <-
    .print("Initial position : startPos(", A, ",", B, ").").


+endPos(A,B) : true <-
    //.print("Final position : endPos(", A, ",", B, ").");
    !explore.


+jailPos(Xj,Yj) : true <- .print("------Jail POLICE position ------ : jailPos(", Xj, ",", Yj, ").").


+closeAgentAt(A,B,C,D) : not escorting(ID) <-
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
+arrestedCriminal(Xc, Yc) : myId(ID) <-
    .print("YOU ARE A CRIMINAL AND YOU ARE UNDER ARREST AT: ", Xc, ", ", Yc);
    +escorting(ID);
    path.Escorting(ID, true).


// Jail reached
+reachedJail(T,K) : myId(ID) & arrestedCriminal(Xc,Yc) <-
    .print("X jail: " , T, " Y jail: ", K); // X jail: 34 Y jail: 35
    .print("+++++++ARRIVED AT JAIL+++++++");
    path.Escorting(ID, false); // To stop escorting
    -escorting(ID);
    +jailOccupied(T,K);
    -arrestedCriminal(Xc,Yc);
    //-reachedJail(T,K);
    -myId(ID);
    -startPos(A,B);
    -endPos(C,D).


+arrivedAtDestination : true <-
    .print("Arrived at the destination, generating new path.").


/* Stop exploration
+stopExploring : true <-
   .print("()()()()()()()()()()()()()()()Stopping exploration.");
   -reachedJail(T,K);
   -startPos(A,B);
   -endPos(C,D);
   -myId(ID);
   .drop_all_intentions.
*/

/*
+destroyMe(NAME) : true <-
    .print("________________-Destroying agent: _________________", NAME);
    .kill_agent(NAME).
*/



// _____ CLUES ______
+foundClueX(CIDX,CX) : true <-
    .print("POLICE ------> Found clue X: ", CX, " with ID: ", CIDX);
    +haveClueX(CIDX,CX).

+foundClueY(CIDY,CY) : true <-
    .print("POLICE ------> Found clue Y: ", CY, " with ID: ", CIDY);
    +haveClueY(CIDY,CY).








