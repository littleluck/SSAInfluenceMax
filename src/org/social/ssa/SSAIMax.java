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
	
	
	/**��ں���
	 * @param args
	 */
	public static void main(String[] args) {
		
		
	}
	
	
	/**
	 *  log(C(n,k)) = log(n!) - log((n-k)!) - log(k!)
	 * @return C(n,k)������Ķ���
	 */
	public static double logCombination(int n, int k){
		if(n < k){
			System.err.println("nҪ��С��k") ;
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
	 * @param g ����ͼg����
	 * @param x ��㼯�ϵĸ���
	 * @return ���ط���Ӱ�����RIS�Ľ�㼯�ϵļ���
	 */
	public static Map<List<List<Integer>>, Set<Integer>> RIS(Graph g, int x){
		
		//result����RR Sets ���ϣ�set���ϣ�set�б�����RR Sets�е�ÿ���ڵ�
		Map<List<List<Integer>>, Set<Integer>> result = new HashMap<List<List<Integer>>, Set<Integer>>() ;
		
		List<List<Integer>> RRSets = new ArrayList<List<Integer>>() ;
		List<Integer> list = null ;
		
		//set������뵽RRSets�����еĽ��
		Set<Integer> set = new HashSet<Integer>() ;	//�����������õĶ���ͬһ��set
		
		//��LinkedList��ʼ������
		Queue<Integer> queue = new LinkedList<Integer>() ;	//�����������õ�Ҳ����ͬһ��queue
		
		//���ÿ������Ƿ�����У���ʼ��Ϊfalse
		int n = g.getN() ;
		boolean visited[]  = new boolean[n] ;
		
		//System.out.println("����"+x+"������") ;
		
		//����x��RR set
		Random randInt = new Random() ;
		while(x!= 0){
			--x ;
			list = new ArrayList<Integer>() ;	//ע�⣺����һ��Ҫ����newһ��ArrayList,������뵽RRSets�е�����List��Ϊͬһ��List
			queue.clear() ;
			for(int i=0; i<n; i++){	//��ʼ�����ʱ�־
				visited[i] = false ;
			}
			
			//������ɾ��ȷֲ���һ�������v���Ӵ˽ڵ㿪ʼ����һ��RR set����
			int v = randInt.nextInt(n) ;
			
			//System.out.println("�˴����ɵĽ����Ϊ��"+v) ;
			
			queue.offer(v) ;	//��v�����뵽���к�list��
			visited[v] = true ;
			
			list.add(v) ;
			set.add(v) ;
			
			while(!queue.isEmpty()){
				v = queue.poll() ;	//������
				//�����ڽӾ���
				double adj[][] = g.getAdj() ;
				Random randDouble = new Random() ;
				for(int i=0; i<n; i++){		//���������֪����Щ���ֱ��ָ����v���͵ý���һ�α�����
					//�����ڶ���ͼ���ݺ󣬽�ÿ��������Ƚ����Ϊһ��������ǰ�����������Ժ�Ͳ���ÿ�ν��б�����
					if((adj[i][v] > 0) && (visited[i] == false)){	//������i��v֮���бߴ����ҽ��iû�������
						double probability = randDouble.nextDouble() ;
						if(probability < adj[i][v]){	//���ɵĸ���С�������ߵ�Ȩֵ���ͽ����i���뵽������
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
	 * @return ���س�ʼ���Ӽ��ϼ������յ�Ӱ��Ľڵ����
	 */
	public static Map<List<Integer>, Integer> maxCoverage(List<List<Integer>> RRSets, Set<Integer> set, int k, int n){
		Map<List<Integer>, Integer> seedAndSpread = new HashMap<List<Integer>, Integer>() ;
		
		int maxCov = 0 ;
		int maxSeed = -1 ;
		
		Set<Integer> copySet = new HashSet<Integer>() ;
		copySet.addAll(set) ;	//��set���ƣ���������remove�����Ա������е�set����Ӱ��
		
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
			if(maxSeed >=0){	//�ҵ�һ����󸲸��������
				sk.add(maxSeed);
				copySet.remove(maxSeed);	//����ɾ��set�е�һ��Ԫ�ػ�Ա����õ�set����Ӱ��,�����������¸�����һ��
				count++;
			}
			if(count == k){	//���ҵ�k�����ӽ��
				break ;
			}
		}
						
		if(count < k){	//�������k-count�������뵽sk��
			Random rand = new Random() ;
			int fill = k - count ;
			for(int i=fill; i>0; i--){
				int randInt = rand.nextInt(n) ;
				while(sk.contains(randInt)){	//ֱ������һ������sk�еĽ������ѭ��
					randInt = rand.nextInt(n) ;
				}
				sk.add(randInt) ;
			}
		}
		
		Collections.sort(sk) ;
		System.out.println("���Ӽ���Ϊ��"+sk) ;
		System.out.println("���Ӽ���sk��RR Sets���ϵĽ���������"+maxCov) ;
		
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
	 * @return �������Ӽ���seed����һ��Ӱ�췶Χ
	 */
	public static int estimateInf(Graph g, List<Integer> seed, double e2, double q2, int Tmax){
		
		if(e2<0 || e2>1 || q2<0 || q2>1){
			System.err.println("������0~1��e2��q2��Χ") ;
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
			 if(cov >= Math.ceil(A2)){	//����ȡ��
				 return g.getN()*cov/T ;
			 }
		}
		System.out.println("cov="+cov) ;
		System.out.println("A2="+A2) ;
		System.out.println("A2����ȡ��="+Math.ceil(A2)) ;
		
		return -1 ;
	}
	
	/**
	 * 
	 * @param g
	 * @param e
	 * @param q
	 * @param k
	 * @return ����maxCoverage��estimateInf�����������յ����Ӽ���
	 */
	public static List<Integer> SSA(Graph g, double e, double q, int k){
		if(e<0 || e>1 || q<0 || q>1){
			System.err.println("������0~1��Χ��e��q") ;
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
		
		System.out.println("���RRSets���ϣ�"+RRSets) ;
		System.out.println("���set���ϣ�"+set) ;
		
		int n = g.getN() ;
		double logCnk = logCombination(n, k) ;
		int bound = (int) Math.ceil((8+2*e)*n*(Math.log(2/q) + logCnk)/(e*e)) ;	//����ȡ��
		
		//�������Ӽ��ϼ���Ӱ�췶Χ��������
		List<Integer> sk = null ;
		Integer IISk = 0 ;
		
		System.out.println("RRSets���ϴ�СΪ"+RRSets.size()) ;
		System.out.println("ѭ���߽�bound="+bound) ;
		
		int count = 0 ;
		
		while(RRSets.size() < bound){
			//����maxCoverage�õ����������Ӽ��ϼ���Ӱ�췶Χ						
			
			System.out.println("����maxCoverage֮ǰset����Ϊ��"+set) ;
			System.out.println("----------------") ;
			
			Map<List<Integer>, Integer> seedMap = maxCoverage(RRSets, set, k, n) ;
			
			System.out.println("----------------") ;			
			System.out.println("����maxCoverage֮��set����Ϊ��"+set) ;
			
			
			
			for(Map.Entry<List<Integer>, Integer> entry : seedMap.entrySet()){
				sk = entry.getKey() ;
				IISk = entry.getValue() ;
			}
			count++ ;
			if(covR(RRSets, sk) >= A1){
				int Tmax = (int) Math.ceil(RRSets.size()*(1+e2)*e3*e3/((1-e2)*(e2*e2))) ;
				int IISk2 = estimateInf(g, sk, e2, q/3, Tmax) ;
				if(IISk <= ((1+e1)*IISk2)){
					System.out.println("SSA������ִ����ѭ��"+count+"��") ;
					System.out.println("���Ӽ��ϵ�Ӱ�췶Χ��"+IISk) ;
					return sk ;
				}
			}
			//������RRSets.size()��RR Set���ϣ�ͬʱ����RRSets��set
			map = RIS(g, RRSets.size()) ;
			for(Map.Entry<List<List<Integer>>, Set<Integer>> entry : map.entrySet()){
				RRSets.addAll(entry.getKey()) ;
				set.addAll(entry.getValue()) ;
			}
		}
		
		System.out.println("ִ����ѭ��"+count+"��") ;
		System.out.println("���Ӽ��ϵ�Ӱ�췶Χ��"+IISk) ;
		
		return sk ;		
	}
	
	
	/**
	 * 
	 * @param RRSets 
	 * @param s 
	 * @return ����s��RRSets���н����ļ��ϸ���
	 */
	public static int covR(List<List<Integer>> RRSets, List<Integer> seed){
		int cov = 0 ;
		
//		for(List<Integer> list : RRSets){
//			for(Integer element : list){
//				if(seed.contains(element)){	//������Ӽ����뵱ǰ�����н�����������һ��������һ��ѭ��
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
