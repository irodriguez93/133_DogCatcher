//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\\ 
// Joe Martinez
// Fall 2015 CSC 133
// Dog Catcher Game
// 
// This class contains all functions and data
// for the Animal Object.
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\\ 

package a3;

import java.util.ArrayList;

public abstract class Animal extends GameObject implements IMovable{
	private int speed;
	private int direction;
	private int collisionCount;
	
	private ArrayList<Object> collisions = new ArrayList<Object>();

	public void move(int ms) {
		float x = getXLoc();
		float y = getYLoc();
		float dx;
		float dy;

		dx = (float) Math.cos(90 - getXLoc()) * this.speed;
		dy = (float) Math.sin(90 - getYLoc()) * this.speed;
		
		dx = dx * ((float) ms/15);
		dy = dy * ((float) ms/15);

		x = x + dx; // NEW X LOCATION
		y = y + dy; // NEW Y LOCATION

		// CHECK LEFT SIDE
		if (x < getSize() / 2) {
			x = getSize() / 2 + rand.nextFloat() * getSize() / 2;
		}
		// CHECK RIGHT SIDE
		else if (x > getGameWidth() - (getSize() / 2)) {
			x = getGameWidth() - (getSize() / 2 + rand.nextFloat() * getSize() / 2);
		}

		// CHECK BOTTOM
		if (y < getSize() / 2) {
			y = getSize() / 2 + rand.nextFloat() * getSize() / 2;
		}
		// CHECK TOP
		else if (y > getGameHeight() - (getSize() / 2)) {
			y = getGameHeight()
					- (getSize() / 2 + rand.nextFloat() * getSize() / 2);
		}

//		 //TESTING FOR X AND Y OUTSIDE OF BOUNDARIES
//		 if(x < 0 || x > getGameWidth() || y < 0 || y > getGameHeight()){
//		 System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
//		 System.out.println("Size: " + getSize() + " ~~~ Dir: " +
//		 getDirection() + " ~~~ x: " + x + " ~~~ getSize/2: " +
//		 (getSize()/2));
//		 }

		setLocation(x, y);
		setDirection(getDirection() + rand.nextInt(7));
	}

	public void decSpeed() { // DECREASE SPEED
		speed = speed - 1;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void randSpeed() {
		// (maxAnimalSpeed - minAnimalSpeed) + minAnimalSpeed;
		speed = rand.nextInt(5 - 1) + 1;
	}

	public void setSize() {
		// BLANK. CAN'T CHANGE SIZE
	}

	public void randDirection() {
		direction = rand.nextInt(360);
	}
	
	public void incCollisionCount(){
		collisionCount++;
	}
	
	public int getCollisionCount(){
		return collisionCount;
	}
	
	public void resetCollisionCount(){
		collisionCount = 0;
	}
	
	public void handleCollision(ICollider obj1, ICollider obj2, GameWorld gw){
		((Animal)obj1).collisions.add(obj2);
		
		int allowedCollisions = 100;
		@SuppressWarnings("unused")
		boolean prevCollision = false;
		
//		/*
		//FIRST TRY - BEST CASE
		if(this.getType() == "Cat"){
			if(obj2 instanceof Cat){
				//System.out.println("Cat with Cat");
				if(((Cat)obj2).getCollisionCount() == 0){
					((Cat)obj2).incCollisionCount();
					gw.catCollision();	
				}else if(((Cat)obj2).getCollisionCount() > allowedCollisions){
					((Cat)obj2).resetCollisionCount();
				}else if(((Cat)obj2).isKitten()){
					((Cat)obj2).incCollisionCount();
					((Cat)obj2).setKitten(false);
				}
				else{
					((Cat)obj2).incCollisionCount();
				}
			}
			if(obj2 instanceof Dog){
				//System.out.println("Cat with Dog");
				if(((Dog)obj2).getCollisionCount() == 0){
					((Dog)obj2).incCollisionCount();
					gw.fightCollision();	
				}else if(((Dog)obj2).getCollisionCount() > allowedCollisions){
					((Dog)obj2).resetCollisionCount();
				}else{
					((Dog)obj2).incCollisionCount();
				}
			}
		}
	}
	//END FIRST TRY
//	*/
	
	/*
	//SECOND TRY - NOT REALLY ANY GOOD	
	if(obj1.getType() == "Cat"){
		if(obj2 instanceof Cat){
			//OBJ2 IS CAT AND A KITTEN
//				if(((Cat)obj2).isKitten()){
//					((Cat)obj2).incCollisionCount();
//					if(((Cat)obj2).getCollisionCount() > allowedCollisions){
//						((Cat)obj2).resetCollisionCount();
//						((Cat)obj2).setKitten(false);
//					}
//				}
			//OBJ1 IS A CAT AND A KITTEN
			if(((Cat)obj1).isKitten()){
				((Cat)obj1).incCollisionCount();
				((Cat)obj1).setKitten(false);
				if(((Cat)obj1).getCollisionCount() > allowedCollisions){
					((Cat)obj1).resetCollisionCount();
				}
			}
		}
		if(!prevCollision){
			for(int i = 0; i < collisions.size(); i++){
				if(collisions.get(i) == obj2){
					prevCollision = true;
					//CAT WITH CAT - ALREADY COLLIDED
					if(obj2 instanceof Cat){
						if(((Cat)obj2).isKitten()){
							((Cat)obj2).incCollisionCount();
							((Cat)obj2).setKitten(false);
						}
						else if(((Cat)obj2).getCollisionCount() > allowedCollisions){
							((Cat)obj2).resetCollisionCount();
							((Animal)obj1).collisions.remove(obj2);
//								if(((Cat)obj2).isKitten()){
//									((Cat)obj2).setKitten(false);
//								}
						}
						else{
							((Cat)obj2).incCollisionCount();
						}
					}
					//CAT WITH DOG - ALREADY COLLIDED
					if(obj2 instanceof Dog){

					}
				}
				if(!prevCollision){
					if(obj2 instanceof Cat){
						//NOT A KITTEN, HAS NOT COLLIDED BEFORE, 
						//System.out.println("1:" + ((Cat)obj1).getCollisionCount() + " 2:" + ((Cat)obj1).getCollisionCount());
						if(((Cat)obj2).isKitten()){
							((Cat)obj2).incCollisionCount();
							((Cat)obj2).setKitten(false);
						}
						if(((Cat)obj2).getCollisionCount() == 0){
							System.out.println("1:" + obj1.getType() + " 2:" + obj2.getType() + " Col:" + ((Animal)obj2).getCollisionCount());
							System.out.println("Cat with Cat");

							((Cat)obj1).incCollisionCount();
							((Cat)obj2).incCollisionCount();
							gw.catCollision();
						}else if(((Cat)obj2).getCollisionCount() > allowedCollisions){
							((Cat)obj2).resetCollisionCount();
						}
//							else if(((Cat)obj2).isKitten()){
//								((Cat)obj2).incCollisionCount();
//								((Cat)obj2).setKitten(false);
//							}
						else{
							((Cat)obj2).incCollisionCount();
						}
					}

					//CAT WITH DOG - NOT COLLIDED BEFORE
					if(obj2 instanceof Dog){
						//System.out.println("Cat with Dog");
						if(((Dog)obj2).getCollisionCount() == 0){
							((Dog)obj2).incCollisionCount();
							gw.fightCollision();	
						}else if(((Dog)obj2).getCollisionCount() > allowedCollisions){
							((Dog)obj2).resetCollisionCount();
						}else{
							((Dog)obj2).incCollisionCount();
						}
					}
				}
				prevCollision = false;
			}
		}
	}
	//END SECOND TRY
	*/
		
	/*
	//THIRD TRY - WORKING OK BUT HAS TOO MUCH GOING ON
	if(obj1.getType() == "Cat"){
		if(obj2 instanceof Cat){
			//OBJ2 IS CAT, IS KITTEN
			if(((Cat)obj2).isKitten()){
				((Cat)obj2).incCollisionCount();
				if(((Cat)obj2).getCollisionCount() > allowedCollisions){
					((Cat)obj2).resetCollisionCount();
					((Cat)obj2).setKitten(false);
				}
			}
			else if(((Cat)obj1).isKitten()){
				((Cat)obj1).incCollisionCount();
				if(((Cat)obj1).getCollisionCount() > allowedCollisions){
					((Cat)obj1).resetCollisionCount();
					((Cat)obj1).setKitten(false);
				}
			}
			else if(!prevCollision){
				for(int i = 0; i < collisions.size(); i++){
					if(collisions.get(i) == obj2){
						prevCollision = true;
						//CAT WITH CAT - ALREADY COLLIDED
						if(obj2 instanceof Cat){
							if(((Cat)obj2).isKitten()){
								((Cat)obj2).incCollisionCount();
							}
							else if(((Cat)obj2).getCollisionCount() > allowedCollisions){
								((Cat)obj2).resetCollisionCount();
								((Animal)obj1).collisions.remove(obj2);
								if(((Cat)obj2).isKitten()){
									((Cat)obj2).setKitten(false);
								}
							}
							else{
								((Cat)obj2).incCollisionCount();
							}
						}
						//CAT WITH DOG - ALREADY COLLIDED
						if(obj2 instanceof Dog){
							
						}
					}
				}
			}
			if(prevCollision){
				if(obj2 instanceof Cat){
					//NOT A KITTEN, HAS NOT COLLIDED BEFORE, 
					System.out.println("1:" + ((Cat)obj1).getCollisionCount() + " 2:" + ((Cat)obj1).getCollisionCount());
					if(((Cat)obj2).getCollisionCount() == 0){
						((Cat)obj1).incCollisionCount();
						((Cat)obj2).incCollisionCount();
						gw.catCollision();
						System.out.println("1:" + obj1.getType() + " 2:" + obj2.getType() + " Col:" + ((Animal)obj2).getCollisionCount());
						System.out.println("Cat with Cat");
					}else if(((Cat)obj2).getCollisionCount() > allowedCollisions){
						((Cat)obj2).resetCollisionCount();
					}else if(((Cat)obj2).isKitten()){
						((Cat)obj2).incCollisionCount();
						((Cat)obj2).setKitten(false);
					}
					else{
						((Cat)obj2).incCollisionCount();
					}
				}
				
				//CAT WITH DOG - NOT COLLIDED
				if(obj2 instanceof Dog){
					System.out.println("Cat with Dog");
				}
			}
		}
	}
	//END THIRD TRY
//	*/
}
