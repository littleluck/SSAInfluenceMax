package org.social.dssa;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.social.graph.Graph;
import org.social.ssa.SSAIMax;

public class DSSAIMax {
	
	private static final double C = 2*(Math.E - 2) ;	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 
	 * @param g
	 * @param e
	 * @param q
	 * @param k
	 * @return 用D-SSA算法计算影响力最大的k个结点
	 */
	public static List<Integer> DSSA(Graph g, double e, double q, int k){
		
		if(e<0 || e>1 || q<0 || q>1){
			System.err.println("请输入0~1范围的e、q") ;
			System.exit(1) ;
		}
		
		int A = (int)Math.ceil(2*C*(1+e)*(1+e)*Math.log(2/q)/(e*e)) ;
		Map<List<List<Integer>>, Set<Integer>> map = SSAIMax.RIS(g, A) ;
		List<List<Integer>> RRSets = null ;
		Set<Integer> set = null ;
		for(Entry<List<List<Integer>>, Set<Integer>> entry : map.entrySet()){
			RRSets = entry.getKey() ;
			set = entry.getValue() ;
		}
		
		int n = g.getN() ;
		Map<List<Integer>, Integer> seedMap = SSAIMax.maxCoverage(RRSets, set, k, n) ;
		//声明种子集合及其影响范围两个变量
		List<Integer> sk = null;
		Integer IISk = 0;
		for(Entry<List<Integer>, Integer> entry : seedMap.entrySet()){
			sk = entry.getKey() ;
			IISk = entry.getValue() ;
		}
		
		System.out.println("RRSets集合大小为"+RRSets.size()) ;
		int bound = (int)Math.ceil((8+2*e)*n*(Math.log(2/q) + SSAIMax.logCombination(n, k))/(e*e)) ;
		int count = 0 ;
		while(RRSets.size() < bound){
			count++ ;
			map = SSAIMax.RIS(g, RRSets.size()) ;
			List<List<Integer>> RRSets2 = null ;
			Set<Integer> set2 = null ;
			for(Entry<List<List<Integer>>, Set<Integer>> entry : map.entrySet()){
				RRSets2 = entry.getKey() ;
				set2 = entry.getValue() ;
			}
			int IISk2 = SSAIMax.covR(RRSets2, sk)*n/RRSets2.size() ;
			double e1 = 1.0*IISk/IISk2 - 1 ;
			if(e1 <= e){
				double e2 = (e-e1)/(2*(1+e1)) ;
				double e3 = (e-e1)/(2*(1-1/Math.E)) ;
				double exponentialQ1 = -SSAIMax.covR(RRSets, sk)*e3*e3/(2*C*(1+e1)*(1+e2)) ;
				double exponentialQ2 = -(SSAIMax.covR(RRSets2, sk)-1)*e2*e2/(2*C*(1+e2)) ;
				double q1 = Math.pow(Math.E, exponentialQ1) ;
				double q2 = Math.pow(Math.E, exponentialQ2) ;
				
				if((q1+q2) <= q){
					System.out.println("执行了循环"+count+"次") ;
					System.out.println("种子集合的影响范围："+IISk) ;
					
					return sk ;
				}				
			}
			//更新RRSets和set集合
			RRSets.addAll(RRSets2) ;
			set.addAll(set2) ;
			seedMap = SSAIMax.maxCoverage(RRSets, set, k, n) ;
			for(Entry<List<Integer>, Integer> entry : seedMap.entrySet()){
				sk = entry.getKey() ;
				IISk = entry.getValue() ;
			}
		}
		
		System.out.println("执行了循环"+count+"次") ;
		System.out.println("种子集合的影响范围："+IISk) ;
		
		return sk ;
	}
	

}
