package org.social.ssa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.social.graph.Graph;


public class SSAIMax {

	private static final double C = 2*(Math.E - 2) ;
	
	
	/**入口函数
	 * @param args
	 */
	public static void main(String[] args) {
		
		
	}
	
	
	/**
	 *  log(C(n,k)) = log(n!) - log((n-k)!) - log(k!)
	 * @return C(n,k)组合数的对数
	 */
	public static double logCombination(int n, int k){
		if(n < k){
			System.err.println("n要不小于k") ;
			System.exit(1) ;
		}
		double sum = 0 ;
		for(int i=1;i<=k; i++){
			sum = sum + Math.log(n-k+i) - Math.log(i) ;
		}
		return sum ;
	}
	

	/**
	 * 
	 * @param g 基于图g采样
	 * @param x 结点集合的个数
	 * @return 返回反向影响采样RIS的结点集合的集合
	 */
	public static Map<List<List<Integer>>, Set<Integer>> RIS(Graph g, int x){
		
		//result返回RR Sets 集合，set集合，set中保存着RR Sets中的每个节点
		Map<List<List<Integer>>, Set<Integer>> result = new HashMap<List<List<Integer>>, Set<Integer>>() ;
		
		List<List<Integer>> RRSets = new ArrayList<List<Integer>>() ;
		List<Integer> list = null ;
		
		//set保存加入到RRSets集合中的结点
		Set<Integer> set = new HashSet<Integer>() ;	//整个函数中用的都是同一个set
		
		//用LinkedList初始化队列
		Queue<Integer> queue = new LinkedList<Integer>() ;	//整个函数中用的也都是同一个queue
		
		//标记每个结点是否入队列，初始均为false
		int n = g.getN() ;
		boolean visited[]  = new boolean[n] ;
		
		//System.out.println("生成"+x+"个集合") ;
		
		//生成x个RR set
		Random randInt = new Random() ;
		while(x!= 0){
			--x ;
			list = new ArrayList<Integer>() ;	//注意：这里一定要重新new一个ArrayList,否则加入到RRSets中的所有List均为同一个List
			queue.clear() ;
			for(int i=0; i<n; i++){	//初始化访问标志
				visited[i] = false ;
			}
			
			//随机生成均匀分布的一个结点编号v，从此节点开始采样一个RR set集合
			int v = randInt.nextInt(n) ;
			
			//System.out.println("此次生成的结点编号为："+v) ;
			
			queue.offer(v) ;	//将v结点加入到队列和list中
			visited[v] = true ;
			
			list.add(v) ;
			set.add(v) ;
			
			while(!queue.isEmpty()){
				v = queue.poll() ;	//出队列
				//访问邻接矩阵
				double adj[][] = g.getAdj() ;
				Random randDouble = new Random() ;
				for(int i=0; i<n; i++){		//这里如果想知道哪些结点直接指向结点v，就得进行一次遍历，
					//可以在读入图数据后，将每个结点的入度结点作为一个集合提前保存起来，以后就不用每次进行遍历了
					if((adj[i][v] > 0) && (visited[i] == false)){	//如果结点i和v之间有边存在且结点i没入过队列
						double probability = randDouble.nextDouble() ;
						if(probability < adj[i][v]){	//生成的概率小于这条边的权值，就将结点i加入到队列中
							queue.offer(i) ;
							visited[i] = true ;
							
							list.add(i) ;
							set.add(i) ;
						}
					}
				}
				
			}
						
			RRSets.add(list) ;
		}		
		
		result.put(RRSets, set) ;
		return result ;
	}
	
	/**
	 * 
	 * @param RRSets
	 * @param map
	 * @param k
	 * @param n
	 * @return 返回初始种子集合及其最终的影响的节点个数
	 */
	public static Map<List<Integer>, Integer> maxCoverage(List<List<Integer>> RRSets, Set<Integer> set, int k, int n){
		Map<List<Integer>, Integer> seedAndSpread = new HashMap<List<Integer>, Integer>() ;
		
		int maxCov = 0 ;
		int maxSeed = -1 ;
		
		Set<Integer> copySet = new HashSet<Integer>() ;
		copySet.addAll(set) ;	//对set复制，以免下面remove操作对被调用中的set产生影响
		
		List<Integer> sk = new ArrayList<Integer>() ;
		int count = 0 ;
		while(!copySet.isEmpty()){
			maxCov = 0 ;
			maxSeed = -1 ;
			for (Integer ele : copySet) {
				sk.add(ele);
				int currentCov = covR(RRSets, sk);
				if (currentCov > maxCov) {
					maxCov = currentCov;
					maxSeed = ele;
				}
				sk.remove(ele);
			}
			if(maxSeed >=0){	//找到一个最大覆盖增量结点
				sk.add(maxSeed);
				copySet.remove(maxSeed);	//这里删除set中的一个元素会对被调用的set产生影响,所以上面重新复制了一份
				count++;
			}
			if(count == k){	//已找到k个种子结点
				break ;
			}
		}
						
		if(count < k){	//随机生成k-count个结点加入到sk中
			Random rand = new Random() ;
			int fill = k - count ;
			for(int i=fill; i>0; i--){
				int randInt = rand.nextInt(n) ;
				while(sk.contains(randInt)){	//直到生成一个不在sk中的结点跳出循环
					randInt = rand.nextInt(n) ;
				}
				sk.add(randInt) ;
			}
		}
		
		Collections.sort(sk) ;
		System.out.println("种子集合为："+sk) ;
		System.out.println("种子集合sk与RR Sets集合的交集个数："+maxCov) ;
		
		seedAndSpread.put(sk, maxCov*n/RRSets.size()) ;
		
		return seedAndSpread ;
	}
	
	/**
	 * 
	 * @param g
	 * @param seed
	 * @param e2
	 * @param q2
	 * @param Tmax
	 * @return 返回种子集合seed的另一个影响范围
	 */
	public static int estimateInf(Graph g, List<Integer> seed, double e2, double q2, int Tmax){
		
		if(e2<0 || e2>1 || q2<0 || q2>1){
			System.err.println("请输入0~1的e2、q2范围") ;
			System.exit(1) ;
		}
		
		double A2 = 1 - 2*C*(1+e2)*Math.log(q2)/(e2*e2) ;
		int cov = 0 ;
		for(int T=1; T<=Tmax; T++){
			 Map<List<List<Integer>>, Set<Integer>> map = RIS(g, 1) ;
			 List<List<Integer>> RRSets = null ;
			 for(List<List<Integer>> list : map.keySet()){
				 RRSets = list ;
			 }
			 cov += covR(RRSets, seed) ;
			 if(cov >= Math.ceil(A2)){	//向上取整
				 return g.getN()*cov/T ;
			 }
		}
		System.out.println("cov="+cov) ;
		System.out.println("A2="+A2) ;
		System.out.println("A2向上取整="+Math.ceil(A2)) ;
		
		return -1 ;
	}
	
	/**
	 * 
	 * @param g
	 * @param e
	 * @param q
	 * @param k
	 * @return 调用maxCoverage和estimateInf函数生成最终的种子集合
	 */
	public static List<Integer> SSA(Graph g, double e, double q, int k){
		if(e<0 || e>1 || q<0 || q>1){
			System.err.println("请输入0~1范围的e、q") ;
			System.exit(1) ;
		}
		
		double e1 = e/6, e2 = e/2, e3 = e/(4*(1 - 1/Math.E)) ;
		int A1 = (int) Math.ceil((1+e1)*(1+e2)*2*C*Math.log(3/q)/(e3*e3)) ;
		Map<List<List<Integer>>, Set<Integer>> map = RIS(g, A1) ;
		
		List<List<Integer>> RRSets = null  ;
		Set<Integer> set = null ;
		for(Map.Entry<List<List<Integer>>, Set<Integer>> entry : map.entrySet()){
			RRSets = entry.getKey() ;
			set = entry.getValue() ;
		}
		
		System.out.println("检测RRSets集合："+RRSets) ;
		System.out.println("检测set集合："+set) ;
		
		int n = g.getN() ;
		double logCnk = logCombination(n, k) ;
		int bound = (int) Math.ceil((8+2*e)*n*(Math.log(2/q) + logCnk)/(e*e)) ;	//向上取整
		
		//声明种子集合及其影响范围两个变量
		List<Integer> sk = null ;
		Integer IISk = 0 ;
		
		System.out.println("RRSets集合大小为"+RRSets.size()) ;
		System.out.println("循环边界bound="+bound) ;
		
		int count = 0 ;
		
		while(RRSets.size() < bound){
			//调用maxCoverage得到初步的种子集合及其影响范围						
			
			System.out.println("调用maxCoverage之前set集合为："+set) ;
			System.out.println("----------------") ;
			
			Map<List<Integer>, Integer> seedMap = maxCoverage(RRSets, set, k, n) ;
			
			System.out.println("----------------") ;			
			System.out.println("调用maxCoverage之后set集合为："+set) ;
			
			
			
			for(Map.Entry<List<Integer>, Integer> entry : seedMap.entrySet()){
				sk = entry.getKey() ;
				IISk = entry.getValue() ;
			}
			count++ ;
			if(covR(RRSets, sk) >= A1){
				int Tmax = (int) Math.ceil(RRSets.size()*(1+e2)*e3*e3/((1-e2)*(e2*e2))) ;
				int IISk2 = estimateInf(g, sk, e2, q/3, Tmax) ;
				if(IISk <= ((1+e1)*IISk2)){
					System.out.println("SSA函数中执行了循环"+count+"次") ;
					System.out.println("种子集合的影响范围："+IISk) ;
					return sk ;
				}
			}
			//再生成RRSets.size()个RR Set集合，同时更新RRSets和set
			map = RIS(g, RRSets.size()) ;
			for(Map.Entry<List<List<Integer>>, Set<Integer>> entry : map.entrySet()){
				RRSets.addAll(entry.getKey()) ;
				set.addAll(entry.getValue()) ;
			}
		}
		
		System.out.println("执行了循环"+count+"次") ;
		System.out.println("种子集合的影响范围："+IISk) ;
		
		return sk ;		
	}
	
	
	/**
	 * 
	 * @param RRSets 
	 * @param s 
	 * @return 返回s与RRSets中有交集的集合个数
	 */
	public static int covR(List<List<Integer>> RRSets, List<Integer> seed){
		int cov = 0 ;
		
//		for(List<Integer> list : RRSets){
//			for(Integer element : list){
//				if(seed.contains(element)){	//如果种子集合与当前集合有交集，计数加一并进行下一轮循环
//					cov++ ;
//					break ;
//				}
//			}
//		}
		
		for(List<Integer> list : RRSets){
			for(Integer s : seed){
				if(list.contains(s)){
					cov++ ;
					break ;
				}
			}
		}
		
		return cov ;
	}
	
}
