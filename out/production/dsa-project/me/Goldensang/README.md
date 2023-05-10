# Flappy bird

[What is Flappy Bird](https://en.wikipedia.org/wiki/Flappy_Bird)

[What is NEAT](https://en.wikipedia.org/wiki/Neuroevolution_of_augmenting_topologies)

[What is NeuralNetwork](https://en.wikipedia.org/wiki/Artificial_neural_network)
## Overview
I'm using NEAT - a Genetic Algorithm to make an AI that plays FlappyBird for me. I tweaked a lot of stuff and coded through my understanding of the algorithm itself.

## General Ideas


1. We will try to direct the "evolution" toward the goal that we want. In this case, it is the bird that we want to go through every settings of pipe that the game have to offer. I manipulated the algorithm a little bit, but the main structure is still NEAT
2. Let say that K is the amount of the most optimal genes that we have so far for each generations, **finest_genes** is an array that is sorted in descending order and contains the best set of genes that we have so far. This can be achieved by following these steps:

	- Choose the **K** of the most optimal genes, and then save a copy for each
	- Choose the **K** again, but this time, we mutate their connections a little bit
	- Simultaneously, we pick out the finest genes until we have collected all of them. We then cross them over with the better genes
	- Then we create a random population, cross them over with the random genes in **finest_genes**
	- Repeat until we have achieved the desire bird
3. In my case, i decided to choose **K** as 4

## Detail description

Let's start with the basic function
```java
public Main() {
		this.init();
	}
public static void main(String[] args) {
		main = new Main();
}	
```
Just a normal initilization, here we can see the main function point to **init()**
```java
public void init() {

		gen++;
		gravScale = new ArrayList<Integer>();
        
		if(firstTime) {
			bird = new ArrayList<Pair<Rectangle,Integer>>();
			id = new ArrayList<Pair<NeuralNetwork, Integer>>();
			score = new ArrayList<Integer>();
			firstTime = false;
			genbird(population);
		}else {
			findBest();
			score = new ArrayList<Integer>();
			replicate(finest);
		}
        
		idx = new ArrayList<Integer>();
		tick = 0;
		gameOver = false;
		xpos = w / 2 - 100;
		
		t = new Timer(5, this); // tempo of the game, lower = faster
		pipe = new ArrayList<Pipe>();
		map = new HashMap<Integer,Boolean>();
		
		
		background = new Rectangle(0 , 0, w, h);
		dirt = new Rectangle(0, h - 150, w, 150);

		fc = new FComp();
		j.add(fc);
		
		addPipe();
		addPipe();
		addPipe();
		addPipe();
		
		t.start();
	}
```
##### Varible description
- **gen**: number of generation
- **gravScale**: contain a gravity scale value for each bird
	+ gravity scale is basically a value that determine how fast the bird drop to the ground
	+ the higher the value the faster the bird falls
- **id**: contains the bird's id, and its neuralnetwork
- **bird**: contains the bird "physical body" and its score (the score which is suppose to be displayed on the screen)
- **score**: a.k.a the finest function, basically a duration value, the longer the bird lives the better its performance is
- **genbird()**: generate a population of birds
- **findbest()**: find the best bird which is described in the general idea section
- **pipe**: contains all the pipe 
- **map**: a marking array, it will tell if the pipe has been scored or not. Details below
- **fcomp**: is a class that contains all of the graphic section of the game.

That is basically it, all are initilizations.



Since main.java **extends** ActionListener, it must has **actionPerformed()** build in it
```java
	@Override
	public void actionPerformed(ActionEvent e) {
		int speed = 10;
		tick++;
		for(int i = 0;i < id.size();i++) {
			if(!idx.contains(i)) 
				score.set(i, tick);
			if(tick % 2 == 0 && bird.get(i).getValue() < 15) 
				gravScale.set(i, gravScale.get(i) + 2);
			
		}
		Pipe closest = findClosest();
		for(int i = 0;i < bird.size();i++) {
			if(!idx.contains(i)) {
				NeuralNetwork n = id.get(i).getKey();
				n.update(bird.get(i).getKey(), closest, gravScale.get(i));
				if(n.jumpornot()) moveUp(i);
			}
		}
		for(int i = 0;i < pipe.size();i++) {
			Pipe p = this.pipe.get(i);
			p.transLeft(speed);
		}
		for(int i = 0;i < pipe.size();i++) {
			Pipe p = pipe.get(i);
			if(p.p1upx + p.w < 0) {
				pipe.remove(p);
				map.remove(p.id);
				addPipe();
			}
		}
		for(Pipe p : pipe) {
			for(int i = 0;i < bird.size();i++) {
				Pair<Rectangle, Integer> b = bird.get(i);
				if(idx.contains(i)) continue;
				int t = inter(p, b.getKey());
				if(t == -1) idx.add(i);
				else {
					b = new Pair<>(b.getKey(), b.getValue() + t);
				}
				cnt++;
			}
		}
		for(int i = 0;i < bird.size();i++) {
			if(idx.contains(i)) continue;
			addY(i, gravScale.get(i)); 
		}
		if(idx.size() == bird.size()) stopGame();
		this.repaint();
		
		
	}
````
##### Varible description
- **tick**: indicate how long has the game past

##### Code explanation
```java
for(int i = 0;i < id.size();i++) {
            if(!idx.contains(i)) 
                score.set(i, tick);
            if(tick % 2 == 0 && bird.get(i).getValue() < 15) 
                gravScale.set(i, gravScale.get(i) + 2);
            
        }
```
Loop through all of the bird, set their score to the current time, also increase their gravity scale

```java
  Pipe closest = findClosest();
  for(int i = 0;i < bird.size();i++) {
       if(!idx.contains(i)) {
            NeuralNetwork n = id.get(i).getKey();
            n.update(bird.get(i).getKey(), closest, gravScale.get(i));
            if(n.jumpornot()) moveUp(i);
          }
     }
```
In NeuralNetwork class we have a closest pipe value, so we update it here

```java
for(int i = 0;i < pipe.size();i++) {
     Pipe p = this.pipe.get(i);
     p.transLeft(speed);
  }
for(int i = 0;i < pipe.size();i++) {
     Pipe p = pipe.get(i);
     if(p.p1upx + p.w < 0) {
          pipe.remove(p);
          map.remove(p.id);
          addPipe();
      }
  }
```
We move the pipe to the left, and check whether the pipe has got out of the screen or not, if so we just simply remove it, add one more
```java
for(Pipe p : pipe) {
   for(int i = 0;i < bird.size();i++) {
  	     Pair<Rectangle, Integer> b = bird.get(i);
         if(idx.contains(i)) continue;
         int t = inter(p, b.getKey());
         if(t == -1) idx.add(i);
         else {
            b = new Pair<>(b.getKey(), b.getValue() + t);
         }
         cnt++;
     }
 }
```
We iterate all of the pipes and birds, check if they collide into each other or not, if so we just simply remove the bird from the game. Otherwise we add the score to the current bird, sometimes it is 0, sometimes it is 1 we will se in details in the **inter()** function.
```java
for(int i = 0;i < bird.size();i++) {
	if(idx.contains(i)) continue;
	addY(i, gravScale.get(i)); 
}
if(idx.size() == bird.size()) stopGame();
this.repaint();
```
We loop through all of the bird, if it still alive we add some gravitational force to it. And check if all the bird had died or not, then we will call the **stopGame()** function which is restart the training process.

```java
public int inter(Pipe p, Rectangle bird) {

	if(bird.y > h - dirt.height || bird.y < 0) return -1;
	if(bird.intersects(p.r1) || bird.intersects(p.r2)) return -1;
	if(bird.x + bird.width - eps >= p.p1upx && bird.x + bird.width <= p.p1upx + p.w) {
	if(map.containsKey(p.id)) return 0;
		map.put(p.id, true);
		return 1;
	}
	return 0;
		
}
```
If the pipe collides with the bird, we return -1.

If the pipe has been scored before, we return 0

Else we will return 1 as the score.

### NeuralNetwork
I hard coded from scratch, which was white a challenge for me, but in the end it works out perfectly

```java
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
```
##### Varible description
+ 
