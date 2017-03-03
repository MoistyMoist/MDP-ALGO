package model;

import java.util.List;

public class AlgoGraph {
	
	private final List<AlgoNode> vertexes;
    private final List<AlgoEdge> edges;
	
    
    public AlgoGraph(List<AlgoNode> vertexes, List<AlgoEdge> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
    }


	public List<AlgoNode> getVertexes() {
		return vertexes;
	}


	public List<AlgoEdge> getEdges() {
		return edges;
	}
    
    
    
   
}
