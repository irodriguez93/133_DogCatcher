//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\\ 
// Joe Martinez
// Fall 2015 CSC 133
// Assignment 2 of the Dog Catcher Game
// 
// This class handles the functions only used by Dog.
//	Mainly regarding scratches. 
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\\ 

package a3;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Dog extends Animal implements ISelectable{
	private int scratches;
	private boolean selected;

	public void incScratches() {
		scratches++;

		// SET SPEED TO ZERO IF SCRATCHES = 5
		if (scratches >= 5) {
			setSpeed(0);// DOG STOPS MOVING
		}
	}

	public int getScratches() {
		return scratches;
	}
	
	public void resetScratches() {
		scratches = 0;
	}

	// PRINT DOG INFORMATION
	public String toString() {
		String dogInfo = "Dog:";
		dogInfo += " loc=" + fToString(getXLoc()) + ", " + fToString(getYLoc());
		dogInfo += " color=[" + getRed() + "," + getGreen() + "," + getBlue()
				+ "]";
		dogInfo += " size=" + getSize();
		dogInfo += " speed=" + getSpeed();
		dogInfo += " direction=" + getDirection();
		dogInfo += " scratches=" + getScratches();

		return dogInfo;
	}

	public String getType() {
		return "Dog";
	}
	
//	public void handleCollision(ICollider obj, GameWorld gw){
//		if(obj.getType() == "Cat"){
//			if(((Cat)obj).getCollisionCount() == 0){
//				//System.out.println("Dog collide with Cat - 1");
//				((Cat)obj).incCollisionCount();
//				gw.fightCollision();	
//			}else if(((Cat)obj).getCollisionCount() > 8){
//				//System.out.println("Dog collide with Cat - 2");
//				((Cat)obj).resetCollisionCount();
//			}else{
//				//System.out.println("Dog collide with Cat - 3");
//				((Cat)obj).incCollisionCount();
//			}
//		}
//	}

	public int getLeft() {
		return (int)(getXLoc() - getSize()/2); 
	}

	public int getRight() {
		return (int)(getXLoc() + getSize()/2);
	}

	public int getTop() {
		return (int)(getYLoc() + getSize()/2);
	}

	public int getBottom() {
		return (int)(getYLoc() - getSize()/2);
	}

	public void draw(Graphics g) {
		int halfSize = getSize()/2;
		
		if(isSelected()){
			g.setColor(Color.CYAN);
			g.fillOval((int)getXLoc()- halfSize, (int)getYLoc() - halfSize, getSize(), getSize());
		}else{
			g.setColor(getColor());
			g.fillOval((int)getXLoc()- halfSize, (int)getYLoc() - halfSize, getSize(), getSize());
		}
	}

	public void setSelected(boolean select) {
		selected = select;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public boolean contains(Point p) {
		// get my x and y bounds
		float x1, x2, y1, y2;
		x1 = getXLoc()-(getSize()/2);
		x2 = getXLoc()+(getSize()/2);
		y1 = getYLoc()-(getSize()/2);
		y2 = getYLoc()+(getSize()/2);
		
		if(p.getX() >= x1 && p.getX() <= x2){
			if(p.getY() >= y1 && p.getY() <= y2){ 
				return true;
			}
		}
		return false;
	}
}