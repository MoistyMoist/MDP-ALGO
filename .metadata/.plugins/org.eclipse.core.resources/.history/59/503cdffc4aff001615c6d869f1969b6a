package model;

import com.sg.ntu.mdp.Direction;

public class AlgoNode {
	
	private int nodeColIndex;
	private int nodeRowIndex;
	private String id;
	
	private AlgoNode parentNode;
	private AlgoNode leftNode;
	private AlgoNode rightNode;
	private AlgoNode frontNode;
	private AlgoNode backNode;
	
	public AlgoNode(int row, int col){
		this.leftNode = null;
		this.rightNode = null;
		this.frontNode = null;
		this.nodeRowIndex = row;
		this.nodeColIndex = col;
		this.backNode = null;
		this.parentNode = null;
		this.id = row+","+col;
	}
	
	public boolean hasParentNode(){
		return parentNode==null?false:true;
	}
	public boolean hasBackPath(){
		return backNode==null?false:true;
	}
	public boolean hasLeftPath(){
		return leftNode==null?false:true;
	}
	public boolean hasRightPath(){
		return rightNode==null?false:true;
	}
	public boolean hasForwardPath(){
		return frontNode==null?false:true;
	}
	
	public AlgoNode getBackNode() {
		return backNode;
	}

	public void setBackNode(AlgoNode backNode) {
		this.backNode = backNode;
	}

	public AlgoNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(AlgoNode parentNode) {
		this.parentNode = parentNode;
	}

	public int getNodeColIndex() {
		return nodeColIndex;
	}


	public void setNodeColIndex(int nodeColIndex) {
		this.nodeColIndex = nodeColIndex;
	}


	public int getNodeRowIndex() {
		return nodeRowIndex;
	}


	public void setNodeRowIndex(int nodeRowIndex) {
		this.nodeRowIndex = nodeRowIndex;
	}

	
	public AlgoNode getLeftNode() {
		return leftNode;
	}


	public void setLeftNode(AlgoNode leftNode) {
		this.leftNode = leftNode;
	}


	public AlgoNode getRightNode() {
		return rightNode;
	}


	public void setRightNode(AlgoNode rightNode) {
		this.rightNode = rightNode;
	}


	public AlgoNode getFrontNode() {
		return frontNode;
	}


	public void setFrontNode(AlgoNode frontNode) {
		this.frontNode = frontNode;
	}
	 @Override
     public boolean equals(Object obj) {
             if (this == obj)
                     return true;
             if (obj == null)
                     return false;
             if (getClass() != obj.getClass())
                     return false;
             AlgoNode other = (AlgoNode) obj;
             if (id == null) {
                     if (other.id != null)
                             return false;
             } else if (!id.equals(other.id))
                     return false;
             return true;
     }

	 @Override
     public String toString() {
             return id;
     }
	
}
