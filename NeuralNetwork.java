package me.Goldensang;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Vector;

public class NeuralNetwork {
	private static double eps = 1e2;
	public static double normalized(double x) {
		return x / eps;
	}
	
	private Rectangle bird;
	private Pipe closest;
	private Vector<Vector<Integer>> v;
	double output[] = new double[30];
    double weight[][] = new double[30][30];
	double input[] = new double[10];
	private int outputNode = 20;
	public double mutateRate = 10 / eps;
	int velo;
	public void def() {
		for(int i = 1;i <= 30;i++)
			v.add(new Vector<Integer>());
		for(int i = 1;i <= 8;i++) 
			v.get(i).add(outputNode);
		for(int i = 1;i <= outputNode;i++)
			for(int j = 1;j <= outputNode;j++)
				if(i != j) weight[i][j] = 0;
	}
	
	public NeuralNetwork(Rectangle b, boolean first) {
		this.bird = b;
		v = new Vector<Vector<Integer>>();
		if(first) def();
		this.mutateweight();
		
	}
	public void push_input() {
		input[1] = bird.y - closest.p1upy + closest.p1h;
		input[2] = closest.p2downy - bird.y - bird.height;
		input[3] = closest.p1upx - bird.x;
		input[4] = closest.p1upx + closest.w - bird.x;
		input[5] = closest.p1upx - bird.x - bird.width;
		input[6] = closest.p1upx + closest.w - bird.x;
		input[7] = velo;
		input[8] = normalized(10);
	}
	
	public void update(Rectangle b, Pipe p,int velo) {
		this.bird = b;
		this.closest = p;
		this.velo = velo;
		push_input();
	}
	public boolean jumpornot() {
		Queue<Integer> que = new LinkedList<>();
		for(int i = 1;i <= 8;i++) {
			que.add(i); 
			output[i] = input[i];
		}
		while(que.size() > 0) {
			int u = que.peek();
			for(int i = 0;i < v.get(u).size();i++) {
				int V = v.get(u).get(i);
				output[V] += output[u] * weight[u][V];
				que.add(V);
			}
			que.remove();
		}
		for(int i = 1;i <= outputNode;i++) 
			output[i] = normalized(output[i] - input[8]); 
		if(output[outputNode] >= 0) 
			return true;
		
		return false;
	}
	public void crossOver(NeuralNetwork n) {
		for(int i = 1;i <= n.outputNode;i++)
			for(int j = 1;j <= n.outputNode;j++)
				this.weight[i][j] = normalized((this.weight[i][j] + n.weight[i][j]) / 2);
		for(int i = 1;i <= n.outputNode;i++)
			for(int j  = 0;j < n.v.get(i).size();j++)
				if(!this.v.get(i).contains(n.v.get(i).get(j))) this.v.get(i).add(n.v.get(i).get(j));
		
	}
	public void mutateweight() {
		Queue<Integer> que = new LinkedList<>();
		for(int i = 1;i <= 8;i++) que.add(i);
		while(que.size() > 0) {
			int top = que.peek();
			for(int i = 0;i < v.get(top).size();i++) {
				int  V = v.get(top).get(i);
				double d = Math.random();
				Random rangen = new Random();
				int x = (rangen.nextInt(3) - 1);
				weight[top][V] = normalized(weight[top][V] + Math.min(d, mutateRate) * x);
			}
			que.remove(); 
		}
	}
	public void mutateNode() {
		Random rangen = new Random();
		int x = rangen.nextInt(11) + 9;
		for(int i = 1;i <= 8;i++)
			v.get(i).add(x);
		v.get(x).add(outputNode);
	}
	void printWeight() {
		for(int i = 1;i <= 20;i++) {
			for(int j = 1;j <= 20;j++) {
				System.out.print(weight[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
		
	}
}