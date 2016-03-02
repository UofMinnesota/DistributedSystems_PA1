package project1;

import java.util.ArrayList;

public class FingerTable {
		
	private int start;
	private int intervalBegin;
	private int intervalEnd;
	private NodeName successor;
	
	//Setters
		public void setStart(int newStart){
			this.start = newStart;
		}	

		public void setInterval(int begin, int end){
			this.intervalBegin = begin;
			this.intervalEnd = end;
		}

		public void setSuccessor(NodeName newSuccessor){
			this.successor = newSuccessor;
		}

		//Getters
		public int getStart(){
			return this.start;
		}

		public int getIntervalBegin(){
			return this.intervalBegin;
		}

		public int getIntervalEnd(){
			return this.intervalEnd;
		}

		public NodeName getSuccessor(){
			return this.successor;
		}
		
		public void findSuccessor(NodeName N, ArrayList<NodeName> ListOfNodes){
			
			for(int i=0; i< ListOfNodes.size();i++){
				System.out.println("Node ID is "+ ListOfNodes.get(i).getID()+"My start ID is "+start);
				if(ListOfNodes.get(i).getID() >= start ){
					successor = ListOfNodes.get(i);
					break;
				}
			}
			
		}

		public FingerTable(){
		}

		public FingerTable(int startID, NodeName succ) {
			start = startID;
			successor = succ;
		}
}
