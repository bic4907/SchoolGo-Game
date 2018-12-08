import java.awt.Point;
import java.util.Vector;

import javax.swing.ImageIcon;

public class Building {

	private ImageIcon image;
	private String name;
	private int price;
	private int capacity;
	private Point position;
	private Vector<Effect> effects;
	
	// data
	public String getName() {return name;}
	public Point getposition() {return position;}
	public int getPrice() {return price;}
	public int getCapacity() {return capacity;}


	
	public void setPosition(Point p) {this.position = p;}

	public void setname(String name) {this.name = name;}
	public void setPrice(int price) {
		if(price < 0)
			System.out.println("MINOR!");
		else
			this.price = price;
	}
	public void setCapacity(int capacity)
	{
		if(capacity < 0)
			System.out.println("MINOR!");
		else
			this.capacity = capacity;
	}

	// get / set method
	
	public Building() {	
		this.name = "Unknown";
		this.price = 0;
		this.capacity = 0;
		this.position = new Point(0, 0);
		this.image = new ImageIcon();
		this.effects = new Vector<Effect>();
	} //Building()
	
	public Building(String name, int price, int capacity, Point position, String imagePath) {
		this.name = name;
		this.price = price;
		this.capacity = capacity;
		this.position = position;
		this.image = new ImageIcon(imagePath);
		this.effects = new Vector<Effect>();
	} //Building(parameter)
	
	public Building(String name) {
		super();
		this.name = name;
	}
	public Building(Building b) {
		this.name = b.name;
		this.price =b. price;
		this.capacity = b.capacity;
		this.position = b.position;
		this.image = new ImageIcon(b.getImage().toString());
		this.effects = b.effects;
	}
	
	
	public ImageIcon getImage() {
		return this.image;
	}
	public void setImage(ImageIcon image) {
		this.image = image;
	}
	public Vector<Effect> getEffects() {
		return effects;
	}
	public void addEffect(Effect e) {
		this.effects.add(e);
	}

} //Building class
