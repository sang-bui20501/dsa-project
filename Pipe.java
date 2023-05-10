package me.Goldensang;

import java.awt.Rectangle;

public class Pipe{
	public int p1upx, p1upy, p2downx, p2downy, p1h, p2h, w, id;
	public Rectangle r1, r2;
	public final int eps = 150;
	public Pipe(int p1ux, int p1height,int dirty, int width,int id) {
		this.p1upx = p1ux;
		this.p1upy = 0;
		this.p2downx = p1ux;
		
		this.p1h = p1height;
		this.w = width;

		this.p2downy = p1upy + eps + p1height;
		this.p2h = dirty - p2downy;
		this.id = id;

		r1 = new Rectangle(p1upx, p1upy, w, p1h);
		r2 = new Rectangle(p2downx, p2downy, w, p2h);
	}
	public void transLeft(int k) {
		p1upx -= k;
		p2downx -= k;
		r1 = new Rectangle(p1upx, p1upy, w, p1h);
		r2 = new Rectangle(p2downx, p2downy, w, p2h);
	}
}
