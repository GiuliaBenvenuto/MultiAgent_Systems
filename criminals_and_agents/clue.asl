// ---------- CLUE AGENT in project criminals_and_agents ----------

/* Initial beliefs and rules */

/* Initial goals */

!start.


/* Plans */
+!start : true <- .print("I'm a clue.").

// Plan triggered when the agent's position is updated
// +at(X,Y) : true <- .print("---> Updated position: at(", X, ",", Y, ").").

+myId(ID) : true <- .print("My ID is: ", ID).

//+foundYouAt(X, Y) : true <-
//    .print("You found a clue!").

//+foundYouAt(X,Y)[source(AgentId)] : true <-
//    .print("__________ Found a CLUE ________ at: ", X, ",", Y, " from agent: ", AgentId);
//    ?criminalXcoord(CID,CX);
//    ?criminalYcoord(CID,CY);
//    .send(AgentId, tell, foundClue(CID,CX));
//    .send(AgentId, tell, foundClue(CID,CY)).


+foundYouAt(X,Y)[source(AgentId)] : criminalXcoord(CIDX,CX) <-
    .send(AgentId, tell, foundClueX(CIDX,CX));
    .print("_____SENDING X CLUE_____").


+foundYouAt(X,Y)[source(AgentId)] : criminalYcoord(CIDY,CY) <-
    .send(AgentId, tell, foundClueY(CIDY,CY));
    .print("_____SENDING Y CLUE_____").


+criminalXcoord(CIDX,CX) : true <-
    .print("############# Criminal ", CIDX, " is at X: ", CX).


+criminalYcoord(CIDY,CY) : true <-
    .print("############# Criminal ", CIDX, " is at Y: ", CY).