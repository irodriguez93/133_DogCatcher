//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\\ 
// Joe Martinez
// Fall 2015 CSC 133
// Dog Catcher Game
// 
// This class is where most of the control takes place.
// This is where most data is instantiated and manipulated.
// It is also where most functions are performed or called.
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\\ 

package a3;

import java.util.ArrayList;
import java.util.Scanner;
import java.awt.Color;
import java.util.Random;

import javax.swing.JOptionPane;

public class GameWorld implements IObservable{
	private int gameClock;
	private int numOfCats; // INPUT FROM USER
	private int numOfDogs; // INPUT FROM USER
	private int catsCaptured;
	private int dogsCaptured;
	private int totalCats;
	private int totalDogs;
	private int totalPoints;
	@SuppressWarnings("unused")
	private int totalScratches;

	private Scanner qIn;
	private String qInput;
	//private Scanner initIn;
	//private String initInput;

	private Net net;
	private Cat cat;
	private Dog dog;
	
	private Sounds sounds = Sounds.getInstance(); 

	private ArrayList<Object> observerList = new ArrayList<Object>();

	Random rand = new Random();
	
	private boolean sound;
	public boolean flashNet = false;
	GameObjectCollection gObjects = new GameObjectCollection(); 

	// CODE TO CREATE THE INITIAL GAME OBJECTS/LAYOUTS
	public void initLayout() {

		sound = false; 
		
//		initIn = new Scanner(System.in);
//		// GET NUMBER OF CATS AND DOGS
//		System.out.print("Enter an integer for number of cats: ");
//		initInput = initIn.nextLine();
//		numOfCats = Integer.parseInt(initInput);
//		System.out.print("Enter an integer for number of dogs: ");
//		initInput = initIn.nextLine();
//		numOfDogs = Integer.parseInt(initInput);
		
		numOfCats = rand.nextInt(10 - 2) + 2;
		numOfDogs = rand.nextInt(30 - 3) + 3;
		//numOfCats = 7;
		//numOfDogs = 1;
		
		// TOTAL POINTS STARTS AT 0
		totalPoints = 0;
		totalCats = 0;
		totalDogs = 0;
		totalScratches = 0;

		// CREATE NET
		net = new Net();
		net.setSize(100);
		net.setColor(Color.RED);
		net.randLocation();
		gObjects.add(net);

		// CREATE CATS BASED ON NUMBER FROM USER
		for (int i = 0; i < numOfCats; i++) {
			cat = new Cat();
			cat.randSize();
			cat.setSpeed(5);
			cat.setColor(Color.GREEN);
			cat.randLocation();
			cat.randDirection();
			cat.resetCollisionCount();
			cat.setKitten(false);
			gObjects.add(cat);
			totalCats++;
		}

		// CREATE DOGS BASED ON NUMBER FROM USER
		for (int i = 0; i < numOfDogs; i++) {
			dog = new Dog();
			dog.randSize();
			dog.setSpeed(5);
			dog.setColor(Color.BLUE);
			dog.randLocation();
			dog.randDirection();
			dog.resetCollisionCount();
			cat.setKitten(false);
			gObjects.add(dog);
			totalDogs++;
		}
		notifyObservers();
	}
	
	public void pauseGame(){
		
	}

	public void printMap() {
		IIterator iterator = gObjects.getIterator();
		Object curObj = new Object();
		while(iterator.hasNext()){
			curObj = iterator.getNext();
			System.out.println(curObj.toString());
		}
	}

	public void expandNet() {
		net.expandNet();
	}

	public void contractNet() {
		net.contractNet();
	}

	public void scoop() {
		//PLAY SOUND
		if(isSoundOn()){
			sounds.playScoopNetClip();
		}
		
		float netLeftB  = net.getXLoc() - (net.getSize() / 2);
		float netRightB = net.getXLoc() + (net.getSize() / 2);
		float netBotB   = net.getYLoc() - (net.getSize() / 2);
		float netTopB   = net.getYLoc() + (net.getSize() / 2);
		
		IIterator iterator = gObjects.getIterator();
		Object curObj = new Object();
		
		while(iterator.hasNext()){
			curObj = iterator.getNext();
			if(curObj instanceof Cat){
				// CHECK IF CAT IS WITHIN BOUNDARIES OF THE NET
				if(((Cat) curObj).getXLoc() > netLeftB
					&& ((Cat) curObj).getXLoc() < netRightB
					&& ((Cat) curObj).getYLoc() > netBotB
					&& ((Cat) curObj).getYLoc() < netTopB){

					catsCaptured++;
					totalCats--;
					iterator.remove();
				}
			}else if(curObj instanceof Dog){
				// CHECK IF DOG IS WITHIN BOUNDARIES OF THE NET
				if(((Dog) curObj).getXLoc() > netLeftB
						&& ((Dog) curObj).getXLoc() < netRightB
						&& ((Dog) curObj).getYLoc() > netBotB
						&& ((Dog) curObj).getYLoc() < netTopB){

					dogsCaptured++;
					totalDogs--;
					iterator.remove();
					//GAME OVER IF ZERO DOGS
					if (totalDogs == 0) {		
						gameFinished();
					}
				}
			}
		}
		flashNet = true;
		notifyObservers();
	}

	private void gameFinished() {
		//STOP MUSIC
		stopBackGroundClip();
		
		//STOP ANIMATION
		IIterator iterator = gObjects.getIterator();
		Object curObj = new Object();
		while(iterator.hasNext()){
			curObj = iterator.getNext();
			if(curObj instanceof Cat){
				((Cat)curObj).setSpeed(0);
			}
		}
		
		//SHOW POINTS
		JOptionPane.showMessageDialog(null, "ALL DOGS CAUGHT \n" + "Total Points: " + getTotalPoints());
	}

	public void netRight() {
		net.netRight();
	}

	public void netLeft() {
		net.netLeft();
	}

	public void netUp() {
		net.netUp();
	}

	public void netDown() {
		net.netDown();
	}

	// TWO CATS COLLIDE
	public void catCollision() {
		
		// IF NOT ENOUGH CATS TO MAKE KITTEN
		if (totalCats < 2) {
			System.out.println("Less than two cats in game - Cannot make kitten.");
		} else if(totalCats < 30) {
			//PLAY SOUND
			if(isSoundOn()){
				sounds.playCatCollisionClip();
			}
			
			float x = this.cat.getXLoc();
			float y = this.cat.getYLoc();
			cat = new Cat();
			cat.randSize();
			cat.setSpeed(5);
			cat.setColor(Color.GREEN);
			cat.randDirection();
			cat.setLocation(x + rand.nextFloat() * 12, y + rand.nextFloat() * 12);
			//cat.randLocation();
			cat.setKitten(true);
			cat.incCollisionCount();
			gObjects.add(cat);
			totalCats++;
		}
		notifyObservers();
	}

	// CAT AND DOG COLLIDE
	public void fightCollision() {
		
		ArrayList<Integer> dogList = new ArrayList<Integer>();
		
		IIterator iterator = gObjects.getIterator();
		Object curObj = new Object();

		// IF NO CATS TO COLLIDE WITH
		if (totalCats == 0){
			System.out.println("No cats in game - No scratch recorded");
		}
		//IF NO DOGS TO SCRATCH
		if(totalDogs == 0){
			System.out.println("No dogs in game - No scratch recorded");
		}
		else{
			//PLAY SOUND
			if(isSoundOn()){
				sounds.playFightCollisionClip();
			}
			
			while(iterator.hasNext()){
				curObj = iterator.getNext();
				if(curObj instanceof Dog){
					dogList.add(iterator.getIndex());
				}
			}
		}
		// RANDOM INTEGER BASED ON SIZE OF DOG LIST --- NEED THIS TO GET VALUE
		// FROM DOGLIST
		int randDog = rand.nextInt(dogList.size());
		// RANDOM POSITION VALUE FROM GOBJECT LIST
		int randPos = dogList.get(randDog);
		
		//AFFECT RANDOM DOG
		((Dog) iterator.objectAt(randPos)).incScratches();
		((Dog) iterator.objectAt(randPos)).incColor();
		((Dog) iterator.objectAt(randPos)).decSpeed();
		
		notifyObservers();
	}

	public void tickClock() {
		gameClock = gameClock + 1;
		
		IIterator iterator = gObjects.getIterator();
		Object curObj = new Object();
		
		//MOVE EACH ANIMAL
		while(iterator.hasNext()){
			curObj = iterator.getNext();
			if(curObj instanceof Animal){
				((Animal) curObj).move(20);
			}
		}
		notifyObservers();
	}

	public String printPoints() {
		String output = null;
		
		IIterator iterator = gObjects.getIterator();
		Object curObj = new Object();
		
		// TAKE AWAY ONE POINT FOR EACH SCRATCH
		// GET TOTAL AMOUNT OF SCRATCHES
		while(iterator.hasNext()){
			curObj = iterator.getNext();
			if(curObj instanceof Dog){
				if(((Dog) curObj).getScratches() > 0){
					totalScratches += ((Dog) curObj).getScratches();
					totalPoints += -1*((Dog) curObj).getScratches();
				}
			}
		}

		totalPoints += catsCaptured * -10;// CAT = -10 POINTS
		totalPoints += dogsCaptured * 10; // UNSCRATCHED DOG = 10 POINTS

//		System.out.println("Cats Captured:  " + catsCaptured); 	// CATS CAPTURED
//		System.out.println("Dogs Captured:  " + dogsCaptured); 	// DOGS CAPTURED
//		System.out.println("Cats Remaining: " + totalCats); 	// CATS REMAINING
//		System.out.println("Dogs Remaining: " + totalDogs); 	// DOGS REMAINING
//		System.out.println("Scratches:      " + totalScratches);// TOTAL SCRATCHES
//		System.out.println("TOTAL POINTS:   " + totalPoints); 	// TOTAL POINTS

//		// RESET TOTAL POINTS
//		totalPoints = 0;
//		totalScratches = 0;
		
		output = "TOTAL POINTS:   " + totalPoints    + "\n"; 	// TOTAL POINTS
		
		return output;
	}

	public void quitGame() {
		qIn = new Scanner(System.in);

		System.out.print("Are you sure you want to quit? Enter y/n: ");

		qInput = qIn.nextLine();
		qInput.toUpperCase();

		switch (qInput) {
		case "y":
			System.exit(0);
		case "Y":
			System.exit(0);
		default:
		}
	}
	
	public int getTotalPoints(){
		IIterator iterator = gObjects.getIterator();
		Object curObj = new Object();
		
		totalPoints = 0;
		
		while(iterator.hasNext()){
			curObj = iterator.getNext();
			if(curObj instanceof Dog){
				if(((Dog) curObj).getScratches() > 0){
					totalPoints += -1*((Dog) curObj).getScratches();
				}
			}
		}

		totalPoints += catsCaptured * -10;// CAT = -10 POINTS
		totalPoints += dogsCaptured * 10; // UNSCRATCHED DOG = 10 POINTS
		return totalPoints;
	}
	
	public int getCatsCaptured(){
		return catsCaptured;
	}
	
	public int getDogsCaptured(){
		return dogsCaptured;
	}
	
	public int getCatsRemaining(){
		return totalCats;
	}
	
	public int getDogsRemaining(){
		return totalDogs;
	}
	
	public void turnOnSound(){
		sound = true;
		sounds.playBackGroundClip(); //PLAY SOUND
		notifyObservers();
	}
	
	public void turnOffSound(){
		sound = false;
		sounds.stopBackGroundClip(); //PLAY SOUND
		notifyObservers();
	}
	
	public void toggleSound(){
		if(sound){
			sound = false;
		}else{
			sound = true;
		}
		notifyObservers();
	}
	
	public boolean isSoundOn(){
		return sound;
	}

	public void addObserver(IObserver o) {
		observerList.add(o);	
	}
	
	public void notifyObservers() {
		if(!observerList.isEmpty()){
			for(int i = 0; i < observerList.size(); i++){
				((IObserver) observerList.get(i)).update(this);
			}
		}
	}

	public void playBackGroundClip() {
		sounds.playBackGroundClip();
	}
	public void stopBackGroundClip() {
		sounds.stopBackGroundClip();
	}

	public void detectCollision() {
		Object array[] = gObjects.toArray();
		for(int i = 0; i < array.length - 1; i++){
			for(int j = i+1; j < array.length; j++){
				if(((ICollider)array[i]).collidesWith((ICollider)array[j])){
					((Animal)array[i]).handleCollision((ICollider)array[i], (ICollider)array[j], this);
				}
			}
		}
	}

	public void healDogs() {
		IIterator iterator = gObjects.getIterator();
		Object curObj = new Object();
		
		//ONLY HEAL SELECTED DOGS
		while(iterator.hasNext()){
			curObj = iterator.getNext();
			if(curObj instanceof Dog && ((Dog)curObj).isSelected()){
				((Dog) curObj).setSelected(false);
				((Dog) curObj).resetScratches();
				((Dog) curObj).setSpeed(5);
				((Dog) curObj).setColor(Color.BLUE);
				notifyObservers();
			}
		}	
	}
}
