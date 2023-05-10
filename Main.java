package me.Goldensang;

import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;


public class Main implements ActionListener {
    public static Main main;
    public final int dist = 300;
    public final int w = 800;
    public final int h = 800;
    public final int eps = 6;
    public int cnt;
    public boolean gameOver;
    public HashMap<Integer, Boolean> map;
    public int gen;
    public int tick;
    public Rectangle background;
    public Rectangle dirt;
    public int bestScore;
    public ArrayList<Pair<Rectangle, Integer>> bird;
    public ArrayList<Pair<NeuralNetwork, Integer>> id;
    public ArrayList<Integer> score;
    public ArrayList<Integer> gravScale;
    public ArrayList<Integer> idx;
    public ArrayList<Pair<NeuralNetwork, Integer>> finest_gen;
    public JFrame j;
    public ArrayList<Pipe> pipe;
    public FComp fc;
    public NeuralNetwork finest;
    boolean bo;
    int speed;
    private Timer t;
    private int xpos;
    private boolean firstTime;
    private int population;

    public Main() {
        this.cnt = 0;
        this.gen = 0;
        this.bo = true;
        this.bestScore = 1;
        this.firstTime = true;
        this.population = 200;
        this.speed = 10;
        this.init();
    }

    public static void main(final String[] args) {
        Main.main = new Main();
    }

    public void genbird(final int amount) {
        this.finest_gen = new ArrayList<Pair<NeuralNetwork, Integer>>();
        for (int i = 0; i < amount; ++i) {
            final Rectangle tmp = new Rectangle(300, 300, 50, 40);
            this.bird.add((Pair<Rectangle, Integer>) new Pair((Object) tmp, (Object) 0));
            this.id.add((Pair<NeuralNetwork, Integer>) new Pair((Object) new NeuralNetwork(tmp, true), (Object) 0));
            this.gravScale.add(0);
            this.score.add(0);
        }
        (this.j = new JFrame()).setDefaultCloseOperation(3);
        this.j.setVisible(true);
        this.j.setSize(800, 800);
        this.j.setResizable(false);
    }

    public void replicate(final NeuralNetwork finest) {
        this.bird = new ArrayList<Pair<Rectangle, Integer>>();
        this.id = new ArrayList<Pair<NeuralNetwork, Integer>>();
        for (int i = 0; i < this.population; ++i) {
            final Rectangle tmp = new Rectangle(300, 300, 50, 40);
            this.bird.add((Pair<Rectangle, Integer>) new Pair((Object) tmp, (Object) 0));
            this.id.add((Pair<NeuralNetwork, Integer>) new Pair((Object) new NeuralNetwork(tmp, true), (Object) 0));
            this.gravScale.add(0);
            this.score.add(0);
        }
        this.finest_gen.sort(new Comparator<Pair<NeuralNetwork, Integer>>() {
            @Override
            public int compare(final Pair<NeuralNetwork, Integer> o1, final Pair<NeuralNetwork, Integer> o2) {
                return ((int) o1.getValue() > (int) o2.getValue()) ? -1 : (((int) o1.getValue() < (int) o2.getValue()) ? 1 : 0);
            }
        });
        final int k = Math.min(3, this.finest_gen.size());
        this.id.set(0, (Pair<NeuralNetwork, Integer>) new Pair((Object) this.finest_gen.get(0).getKey(), (Object) 0));
        for (int j = 1; j < k; ++j) {
            this.id.set(j, (Pair<NeuralNetwork, Integer>) new Pair((Object) this.finest_gen.get(j).getKey(), (Object) 0));
        }
        int cnt = 0;
        for (int l = k; l < 2 * k; ++l) {
            final NeuralNetwork tmp2 = (NeuralNetwork) this.finest_gen.get(cnt).getKey();
            tmp2.mutateweight();
            this.id.set(l, (Pair<NeuralNetwork, Integer>) new Pair((Object) tmp2, (Object) 0));
            ++cnt;
        }
        for (int l = 2 * k; l < 4 * k; ++l) {
            final NeuralNetwork tmp2 = (NeuralNetwork) this.id.get((int) Math.random() * 4).getKey();
            for (int m = 0; m < Math.random() * l; ++m) {
                tmp2.crossOver((NeuralNetwork) this.finest_gen.get((int) Math.random() * this.finest_gen.size()).getKey());
            }
            this.id.set(l, (Pair<NeuralNetwork, Integer>) new Pair((Object) tmp2, (Object) 0));
        }
        for (int l = 4 * k; l < this.population; ++l) {
            final NeuralNetwork tmp2 = (NeuralNetwork) this.id.get(l).getKey();
            tmp2.crossOver((NeuralNetwork) this.id.get((int) Math.random() * 4 * k).getKey());
            tmp2.mutateweight();
            this.id.set(l, (Pair<NeuralNetwork, Integer>) new Pair((Object) tmp2, (Object) 0));
        }
    }

    public void findBest() {
        int max = 0;
        int pmax = -1;
        for (int i = 0; i < this.population; ++i) {
            if (this.score.get(i) > max) {
                max = this.score.get(i);
                pmax = i;
            }
        }
        this.finest = (NeuralNetwork) this.id.get(pmax).getKey();
        this.finest_gen.add((Pair<NeuralNetwork, Integer>) new Pair((Object) this.finest, (Object) max));
    }

    public NeuralNetwork bestbird() {
        int max = 0;
        int pmax = -1;
        for (int i = 0; i < this.bird.size(); ++i) {
            final Pair<Rectangle, Integer> b = this.bird.get(i);
            if (!this.idx.contains(i)) {
                if ((int) b.getValue() > max) {
                    max = (int) b.getValue();
                    pmax = i;
                }
            }
        }
        if (pmax == -1) {
            return null;
        }
        final NeuralNetwork best = (NeuralNetwork) this.id.get(pmax).getKey();
        return best;
    }

    public void init() {
        this.bestScore = 0;
        this.speed = 10;
        ++this.gen;
        this.gravScale = new ArrayList<Integer>();
        if (this.firstTime) {
            this.bird = new ArrayList<Pair<Rectangle, Integer>>();
            this.id = new ArrayList<Pair<NeuralNetwork, Integer>>();
            this.score = new ArrayList<Integer>();
            this.firstTime = false;
            this.genbird(this.population);
        } else {
            this.findBest();
            this.score = new ArrayList<Integer>();
            this.replicate(this.finest);
        }
        this.idx = new ArrayList<Integer>();
        this.tick = 0;
        this.gameOver = false;
        this.xpos = 300;
        this.t = new Timer(3, this);
        this.pipe = new ArrayList<Pipe>();
        this.map = new HashMap<Integer, Boolean>();
        this.background = new Rectangle(0, 0, 800, 800);
        this.dirt = new Rectangle(0, 650, 800, 150);
        this.fc = new FComp();
        this.j.add(this.fc);
        this.addPipe();
        this.addPipe();
        this.addPipe();
        this.addPipe();
        this.t.start();
    }

    public void addY(final int index, final int val) {
        final Rectangle rectangle;
        final Rectangle b = rectangle = (Rectangle) this.bird.get(index).getKey();
        rectangle.y += val;
    }

    public void decY(final int index, final int val) {
        final Rectangle rectangle;
        final Rectangle b = rectangle = (Rectangle) this.bird.get(index).getKey();
        rectangle.y -= val;
    }

    public void repaint() {
        this.j.repaint();
    }

    public void setPipe(final ArrayList<Pipe> p) {
        this.pipe = p;
    }

    public void addPipe() {
        final int x = (int) (Math.random() * 300.0) + 1;
        ++this.cnt;
        Pipe p;
        if (this.pipe.size() == 0) {
            final Pipe last = p = new Pipe(800, 100 + x, 800 - 150, 100, this.cnt);
        } else {
            final Pipe last = this.pipe.get(this.pipe.size() - 1);
            p = new Pipe(last.p1upx + 400, 100 + x, 800 - 150, 100, this.cnt);
        }
        this.pipe.add(p);
    }

    public void moveUp(final int index) {
        if (!this.gameOver) {
            int gravity = this.gravScale.get(index);
            if (gravity > 0) {
                gravity = 0;
            }
            gravity = Math.max(-10, gravity - 10);
            this.gravScale.set(index, gravity);
            this.decY(index, 20);
        }
    }

    public void stopGame() {
        this.t.stop();
        this.init();
    }

    public int inter(final Pipe p, final Rectangle bird) {
        if (bird.y > 800 - this.dirt.height || bird.y < 0) {
            return -1;
        }
        if (bird.intersects(p.r1) || bird.intersects(p.r2)) {
            return -1;
        }
        if (bird.x + bird.width - 6 < p.p1upx || bird.x + bird.width > p.p1upx + p.w) {
            return 0;
        }
        if (this.map.containsKey(p.id)) {
            return 0;
        }
        this.map.put(p.id, true);
        return 1;
    }

    public Pipe findClosest() {
        for (int i = 0; i < this.pipe.size(); ++i) {
            if (this.pipe.get(i).p1upx - this.xpos >= 0 || this.pipe.get(i).p1upx + this.pipe.get(i).w - this.xpos >= 0) {
                return this.pipe.get(i);
            }
        }
        return null;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        ++this.tick;
        for (int i = 0; i < this.id.size(); ++i) {
            if (!this.idx.contains(i)) {
                this.score.set(i, this.tick);
            }
            if (this.tick % 2 == 0 && this.gravScale.get(i) < 15) {
                this.gravScale.set(i, this.gravScale.get(i) + 2);
            }
        }
        final Pipe closest = this.findClosest();
        for (int j = 0; j < this.bird.size(); ++j) {
            if (!this.idx.contains(j)) {
                final NeuralNetwork n = (NeuralNetwork) this.id.get(j).getKey();
                n.update((Rectangle) this.bird.get(j).getKey(), closest, this.gravScale.get(j));
                if (n.jumpornot()) {
                    this.moveUp(j);
                }
            }
        }
        for (int j = 0; j < this.pipe.size(); ++j) {
            final Pipe p = this.pipe.get(j);
            p.transLeft(this.speed);
        }
        for (int j = 0; j < this.pipe.size(); ++j) {
            final Pipe p = this.pipe.get(j);
            if (p.p1upx + p.w < 0) {
                this.pipe.remove(p);
                this.map.remove(p.id);
                this.addPipe();
            }
        }
        for (final Pipe p2 : this.pipe) {
            for (int k = 0; k < this.bird.size(); ++k) {
                Pair<Rectangle, Integer> b = this.bird.get(k);
                if (!this.idx.contains(k)) {
                    final int t = this.inter(p2, (Rectangle) b.getKey());
                    if (t == -1) {
                        this.idx.add(k);
                    } else {
                        b = (Pair<Rectangle, Integer>) new Pair((Object) b.getKey(), (Object) ((int) b.getValue() + t));
                        this.bird.set(k, b);
                    }
                    this.bestScore = Math.max(this.bestScore, (int) b.getValue());
                    ++this.cnt;
                }
            }
        }
        for (int j = 0; j < this.bird.size(); ++j) {
            if (!this.idx.contains(j)) {
                this.addY(j, Math.max(-10, this.gravScale.get(j)));
            }
        }
        if (this.idx.size() == this.bird.size()) {
            this.stopGame();
        }
        this.repaint();
    }
}