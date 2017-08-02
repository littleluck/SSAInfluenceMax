package org.social.graph;

public class Graph {

	private int n ;		//节点个数
	private double adj[][] ;	//邻接矩阵
	
	public Graph(int n) {
		this.n = n;
		this.adj = new double[n][n] ;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public double[][] getAdj() {
		return adj;
	}

	public void setAdj(double[][] adj) {
		this.adj = adj;
	}
	
	
	
}
