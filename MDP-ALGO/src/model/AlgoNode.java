package model;

public class AlgoNode {
	
	private int nodeColIndex;
	private int nodeRowIndex;
	private String id;
	private boolean isVisited=false;
	
	
	
	public AlgoNode(int row, int col){
		
		this.nodeRowIndex = row;
		this.nodeColIndex = col;
		this.id = row+","+col;
	}
	
	

	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public boolean isVisited() {
		return isVisited;
	}



	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
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
