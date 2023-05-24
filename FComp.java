package me.Goldensang;

import javafx.util.Pair;
import me.Goldensang.gamelogic.GameState;
import me.Goldensang.gameobject.Pipe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FComp extends JPanel {

	private ArrayList<Pair<Integer, Integer>> cords;
	
	private static final long serialVersionUID = 1L;
	@Override
	public void paint(final Graphics g) {
		this.cords = new ArrayList<Pair<Integer, Integer>>();
		BufferedImage i1 = null;
		BufferedImage i2 = null;
		try {
			i1 = ImageIO.read(new File("content/background-day.png"));
			i2 = ImageIO.read(new File("content/base.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		final Rectangle background = GameState.getBackground();
		final Rectangle dirt = GameState.getDirt();
		final BufferedImage bufferedImage = i1;

		final int x2 = background.x;
		final int y = background.y;
		final int n = 800;
		g.drawImage(bufferedImage, x2, y, n, 800, null);
		g.drawImage(i2, dirt.x, dirt.y, dirt.width, dirt.height, null);
		BufferedImage pipeup = null;
		BufferedImage pipedown = null;
		try {
			pipeup = ImageIO.read(new File("content/pipe-greenfu.png"));
			pipedown = ImageIO.read(new File("content/pipe-greenfd.png"));
		}
		catch (IOException e2) {
			e2.printStackTrace();
		}
		for (int j = 0; j < GameState.getPipes().size(); ++j) {
			final Pipe p = GameState.getPipes().get(j);
			g.drawImage(pipedown, p.p1upx, p.p1upy, p.w, p.p1h, null);
			g.drawImage(pipeup, p.p2downx, p.p2downy, p.w, p.p2h, null);
		}
//		BufferedImage bird = null;
//		try {
//			bird = ImageIO.read(new File("content/yellowbird-midflap.png"));
//		}
//		catch (IOException e3) {
//			e3.printStackTrace();
//		}
//		for (int k = 0; k < Main.main.bird.size(); ++k) {
//			final Bird bi = Main.main.bird.get(k);
//			if (!Main.main.idx.contains(k)) {
//				final Rectangle b = bi.getBird();
//				g.drawImage(bird, b.x, b.y, b.width, b.height, null);
//			}
//		}
//		g.setColor(Color.white);
//		g.setFont(new Font("Arial", 1, 100));
//		if (!Main.main.gameOver) {
//			g.setFont(new Font("Arial", 1, 30));
//			g.drawString("Score " + String.valueOf(Main.main.bestScore), 50, 50);
//			g.drawString("Gen:  " + String.valueOf(Main.main.gen), 50, 100);
//		}
//		final NeuralNetwork bestBird = Main.main.bestbird();
//		if (bestBird != null) {
//			this.cords.add((Pair<Integer, Integer>)new Pair((Object)20, (Object)300));
//			this.cords.add((Pair<Integer, Integer>)new Pair((Object)20, (Object)350));
//			this.cords.add((Pair<Integer, Integer>)new Pair((Object)20, (Object)400));
//			this.cords.add((Pair<Integer, Integer>)new Pair((Object)20, (Object)450));
//			this.cords.add((Pair<Integer, Integer>)new Pair((Object)20, (Object)500));
//			this.cords.add((Pair<Integer, Integer>)new Pair((Object)20, (Object)550));
//			this.cords.add((Pair<Integer, Integer>)new Pair((Object)20, (Object)600));
//			this.cords.add((Pair<Integer, Integer>)new Pair((Object)20, (Object)650));
//			this.cords.add((Pair<Integer, Integer>)new Pair((Object)150, (Object)450));
//			g.setColor(Color.WHITE);
//			for (int l = 0; l < this.cords.size(); ++l) {
//				g.fillOval((int)this.cords.get(l).getKey(), (int)this.cords.get(l).getValue(), 20, 20);
//			}
//			for (int l = 1; l <= 8; ++l) {
//				final double x = bestBird.weight[l][20];
//				if (bestBird.weight[l][20] >= 0.0) {
//					g.setColor(Color.red);
//				}
//				else {
//					g.setColor(Color.blue);
//				}
//				final Graphics2D g2 = (Graphics2D)g;
//				g2.setStroke(new BasicStroke((float)(int)(Math.abs(x) * 5000.0)));
//				g2.draw(new Line2D.Float((float)((int)this.cords.get(l - 1).getKey() + 15), (float)((int)this.cords.get(l - 1).getValue() + 10), 150.0f, 460.0f));
//			}
//		}
	}
}

