package org.social.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.social.dssa.DSSAIMax;
import org.social.graph.Graph;
import org.social.ssa.SSAIMax;

public class SSATestCase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	
	@Test
	public void test(){
		Set<Integer> set = new HashSet<Integer>() ;
		set.add(1) ;
		set.add(2) ;
		
		Integer ele = 1 ;
		Set<Integer> set2 = new HashSet<Integer>() ;
		set2.addAll(set) ;
		set.remove(ele) ;
		
		//reset(set) ;
		
		System.out.println("test�е�set��"+set) ;
		System.out.println("test�е�set2��"+set2) ;
	}
	
	public void reset(Set<Integer> set){
		Integer ele = 1 ;
		Set<Integer> set2 = new HashSet<Integer>() ;
		set2.addAll(set) ;
		set2.remove(ele) ;
		System.out.println("reset�е�set��"+set) ;
		System.out.println("reset�е�set2��"+set2) ;
	}
	
	
	public static final String FILEPATH = "" ;
	
	@Test
	public void testSSA(){
		File file = new File(FILEPATH) ;
		BufferedReader reader = null ;
		try {
			reader = new BufferedReader(new FileReader(file)) ;
			String temp = null ; 
			while((temp = reader.readLine()) != null){
				System.out.println(temp) ;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(reader != null){
				try {
					reader.close() ;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	@Test
	public void testDSSA(){
		
	}
	
	/** 
     * ����Ϊ��λ��ȡ�ļ��������ڶ������еĸ�ʽ���ļ� 
     * @param fileName �ļ��� 
     */  
	public static void readFileByLines(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
			while ((tempString = reader.readLine()) != null) {
				// ��ʾ�к�
				System.out.println("line " + line + ": " + tempString);
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	
	@Test
	public void testRISAndMaxCoverage(){
		int n = 2000 ;	//�������
		Graph g = new Graph(n) ;
		double a[][] = g.getAdj() ;
		Random rand = new Random() ;
		
		for(int i=0; i<n/2; i++){
			for(int j=0; j<n/2; j++){
					a[i][j] = rand.nextDouble() ;
			}
		}
		
		//����SSA����
		List<Integer> seed = SSAIMax.SSA(g, 0.6, 0.2, 50) ;		
		Collections.sort(seed) ;
		System.out.println("���յ����Ӽ��ϣ�"+seed) ;
		
		System.out.println("*********************") ;		
		
		List<Integer> seed2 = DSSAIMax.DSSA(g, 0.6, 0.2, 50) ;
		Collections.sort(seed2) ;
		System.out.println("���յ����Ӽ��ϣ�"+seed2) ;
		
		
		//����RIS����
//		Map<List<List<Integer>>, Set<Integer>> map = SSAIMax.RIS(g, 3) ;
//		
//		List<List<Integer>> RRSets = null  ;
//		Set<Integer> set = null ;
//		for(Map.Entry<List<List<Integer>>, Set<Integer>> entry : map.entrySet()){
//			RRSets = entry.getKey() ;
//			set = entry.getValue() ;
//		}
//		
//		System.out.println("RRSets���ϣ�"+RRSets) ;
//		System.out.println("set���ϣ�"+set) ;
//		
//		//�����ǲ���maxCoverage����
//		Map<List<Integer>, Integer> seedMap = SSAIMax.maxCoverage(RRSets, set, 3, g.getN()) ;	//��3�����ӽ��
//		List<Integer> sk = null ;
//		Integer IISk = 0 ;
//		for(Map.Entry<List<Integer>, Integer> entry : seedMap.entrySet()){
//			sk = entry.getKey() ;
//			IISk = entry.getValue() ;
//		}
//		
//		System.out.println("���Ӽ���sk��"+sk) ;
//		System.out.println("���Ӽ�������Ӱ��ڵ�����"+IISk) ;
//		System.out.println("set���ϣ�"+set) ;
//		
//		//�������estimateInf����
//		int spread = SSAIMax.estimateInf(g, sk, 0.5, 0.3, 30) ;
//		
//		System.out.println("estimateInf�������ص�Ӱ�췶Χ��"+spread) ;
		
		
	}
	
	
	@Test
	public void testCovR() {
		List<List<Integer>> RRSets = new ArrayList<List<Integer>>() ;
		List<Integer> list = new ArrayList<Integer>() ;
		list.add(1) ;
		list.add(2) ;
		list.add(3) ;
		RRSets.add(list) ;	//1��2��3
		
		list = new ArrayList<Integer>() ;
		list.add(5) ;
		list.add(1) ;
		RRSets.add(list) ;	//1��5
		
		list = new ArrayList<Integer>() ;
		list.add(2) ;
		list.add(5) ;
		RRSets.add(list) ;	//2��5
		
		
		
		System.out.println(RRSets) ;
		
		List<Integer> seed = new ArrayList<Integer>() ;
		seed.add(2) ;
		seed.add(6) ;
		
		System.out.println(seed) ;
		
		int c = SSAIMax.covR(RRSets, seed) ;
		
		System.out.println(c) ;
		
	}

}
