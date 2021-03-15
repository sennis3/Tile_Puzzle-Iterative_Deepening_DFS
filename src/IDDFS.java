import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class IDDFS {
	
	public static void main(String[] args) {
		
		//Set up initial state and goal state
		ArrayList<Integer> goalState = getGoalState();
		ArrayList<Integer> inputState = getInputState();
		HashSet<ArrayList<Integer>> goalStateSet = new HashSet<ArrayList<Integer>>();
		goalStateSet.add(goalState);
		
		//Create initial node
		Node initialNode = new Node(inputState, null, "None");
		int nodeSize = initialNode.calculateNodeSize(); //Gets memory size per node
		
		//Set up hashset for state history
		HashSet<ArrayList<Integer>> exploredStates = new HashSet<ArrayList<Integer>>();
		
		//Create arraylist object for nodeCount to pass as a parameter (by reference)
		ArrayList<Integer> nodeCount = new ArrayList<Integer>();
		nodeCount.add(1);
		
		//initialize variables
		boolean isGoalFound = false;
		boolean isFailure = false;
		int depthLimit = 0;
		String result = "";
		String goalPath = "";
		
		double startTime = System.nanoTime(); //Gets start time
		
		//Search - IDDFS
		while (isGoalFound == false && isFailure == false) {
			//Resets the hashset of states explored
			exploredStates.clear();
			exploredStates.add(initialNode.getState());
			
			result = recursiveDLS(initialNode, depthLimit, goalStateSet, exploredStates, nodeCount);
			
			if (result.equals("FAILURE")) { //Neither the goal nor cutoff was reached
				isFailure = true;
			}
			else if (!(result.equals("CUTOFF"))) { //Goal found
				isGoalFound = true;
				goalPath = result;
			}
			
			depthLimit++; //increase limit to the next tree level
		}
		
		double endTime = System.nanoTime(); //Gets end time
		double timeTaken = (endTime - startTime) / 1000000000.00; //Finds time elapsed
		
		//When goal is found
		if (isGoalFound) {
			System.out.println("Goal Found!");
			//Print the moves made
			System.out.println("Moves: " + goalPath);
			//Print number of nodes
			System.out.println("Number of Nodes Expanded: " + nodeCount.get(0));
			//Print memory taken
			int memUsed = nodeCount.get(0) * nodeSize / 1000; //Calculates total memory used during search
			System.out.println("Memory Used: " + memUsed + " kB");
			//Print time elapsed
			System.out.println("Time Taken: " + timeTaken + " seconds or " + (timeTaken*1000) + " milliseconds");
		}

		//When there are no more nodes to search
		if (isFailure) {
			System.out.println("No solution found");
		}
	}
	
	//Recursively implements the Depth-Limited Search algorithm
	public static String recursiveDLS(Node currNode, int limit, HashSet<ArrayList<Integer>> goalStateSet, HashSet<ArrayList<Integer>> exploredStates, ArrayList<Integer> nodeCount) {
		if (goalStateSet.contains(currNode.getState())) { //If the current state is the goal
			return getGoalPath(currNode);
		}
		else if (limit == 0) { //if it's at the base depth
			return "CUTOFF";
		}
		else {
			boolean hitCutoff = false;
			String result;
			
			//Makes child node for moving up
			if (currNode.canMoveUp()) { //checks if move is possible
				ArrayList<Integer> upState = currNode.moveUp();
				if (!(exploredStates.contains(upState))) { //checks if its a repeated state
					Node upChild = new Node(upState, currNode, "U");
					nodeCount.set(0, nodeCount.get(0)+1); //increase node count
					result = recursiveDLS(upChild, limit-1, goalStateSet, exploredStates, nodeCount); //recursive call
					if (result.equals("CUTOFF")) {
						hitCutoff = true;
					}
					else if (!(result.equals("FAILURE"))) { //Goal was found
						return result;
					}
				}
			}
			
			//Makes child node for moving down
			if (currNode.canMoveDown()) { //checks if move is possible
				ArrayList<Integer> downState = currNode.moveDown();
				if (!(exploredStates.contains(downState))) { //checks if its a repeated state
					Node downChild = new Node(downState, currNode, "D");
					nodeCount.set(0, nodeCount.get(0)+1); //increase node count
					result = recursiveDLS(downChild, limit-1, goalStateSet, exploredStates, nodeCount); //recursive call
					if (result.equals("CUTOFF")) {
						hitCutoff = true;
					}
					else if (!(result.equals("FAILURE"))) { //Goal was found
						return result;
					}
				}
			}
			
			//Makes child node for moving left
			if (currNode.canMoveLeft()) { //checks if move is possible
				ArrayList<Integer> leftState = currNode.moveLeft();
				if (!(exploredStates.contains(leftState))) { //checks if its a repeated state
					Node leftChild = new Node(leftState, currNode, "L");
					nodeCount.set(0, nodeCount.get(0)+1); //increase node count
					result = recursiveDLS(leftChild, limit-1, goalStateSet, exploredStates, nodeCount); //recursive call
					if (result.equals("CUTOFF")) {
						hitCutoff = true;
					}
					else if (!(result.equals("FAILURE"))) { //Goal was found
						return result;
					}
				}
			}
			
			//Makes child node for moving right
			if (currNode.canMoveRight()) { //checks if move is possible
				ArrayList<Integer> rightState = currNode.moveRight();
				if (!(exploredStates.contains(rightState))) { //checks if its a repeated state
					Node rightChild = new Node(rightState, currNode, "R");
					nodeCount.set(0, nodeCount.get(0)+1); //increase node count
					result = recursiveDLS(rightChild, limit-1, goalStateSet, exploredStates, nodeCount); //recursive call
					if (result.equals("CUTOFF")) {
						hitCutoff = true;
					}
					else if (!(result.equals("FAILURE"))) { //Goal was found
						return result;
					}
				}
			}
			
			if (hitCutoff) {
				return "CUTOFF";
			}
			else {
				return "FAILURE"; //Neither found the goal nor reached the cutoff
			}
		}
		
	}
	
	//Sets up the goal state and returns it
	public static ArrayList<Integer> getGoalState() {
		String goalString = "1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 0";
		String[] tokens = goalString.split(" ");
		
		//Adds each goal state value into ArrayList
		ArrayList<Integer> goalState = new ArrayList<Integer>();
		for (int i = 0; i < tokens.length; i++) {
			goalState.add(Integer.parseInt(tokens[i]));
		}
		
		return goalState;
	}
	
	//Receives initial configuration state and returns it as ArrayList
	public static ArrayList<Integer> getInputState() {
		ArrayList<Integer> inputState = new ArrayList<Integer>();

		String userResponse;

		//userResponse = "1 0 3 4 5 2 6 8 9 10 7 11 13 14 15 12";

		//Receives user input from console
		Scanner in = new Scanner(System.in);
		System.out.println("\nEnter list of 16 numbers as starting configuration (0 = empty space):");
		userResponse = in.nextLine();
		in.close();
		
		//Checks if user entered any values
		if (userResponse.length() == 0) {
			System.out.print("Nothing entered - exiting...");
			System.exit(0);
		}
		
		//Checks if user entered correct number of values
		String[] tokensEntered = userResponse.split(" ");
		if (tokensEntered.length != 16) {
			System.out.print("There must be 16 entries! - exiting...");
			System.exit(0);
		}
		
		//Checks for any repeats in the values
		Set<String> setEntered = new HashSet<String>();
		for (int i=0; i<tokensEntered.length; i++)
		{
			setEntered.add(tokensEntered[i]); //Fills hashset
		}
		if (setEntered.size() != 16) //Checks if correct number of values are in hashset
		{
			System.out.print("Duplicate entries! - exiting...");
			System.exit(0);
		}
		
		//Checks if all values are integers in range
		for (int i = 0; i < tokensEntered.length; i++) {
			int numEntered=0;
			try {
				numEntered = Integer.parseInt(tokensEntered[i]); //Checks if they're integers
				
				if(numEntered < 0 || numEntered > 15) {  //Checks if they're in range
					System.out.println("Number out of range! - exiting...");
					System.exit(0);
				}
			}
			catch (NumberFormatException e){
				System.out.println("Invalid number entered! - exiting...");
				System.exit(0);
			}
			inputState.add(numEntered); //Adds value to initial state ArrayList
		}
		
		return inputState;
	}
	
	//Parses back through the parent nodes to return the path to the goal
	public static String getGoalPath(Node goalNode) {
		Node currNode = goalNode;
		String goalPath = "";
		
		while (!(currNode.getPrevMove().equals("None"))) { //while it isn't the root node
			goalPath = currNode.getPrevMove() + " " + goalPath;
			currNode = currNode.getParentNode();
		}
		
		return goalPath;
	}
}
