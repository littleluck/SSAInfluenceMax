package org.social.graph;

public class Graph {

	private int n ;		//�ڵ����
	private double adj[][] ;	//�ڽӾ���
	
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
