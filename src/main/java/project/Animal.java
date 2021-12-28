package project;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Animal extends AbstractWorldMapElement implements IMapElement{
    private MapDirection direction;
    private Vector2d position;
    private final AbstractWorldMap map;
    private int energy,moveEnergy,epoch, childrenAmount;
    private ArrayList<Integer> genotype;
    private final ArrayList<IPositionChangeObserver> observers = new ArrayList<>();

    public Animal(AbstractWorldMap map,int startEnergy,int moveEnergy,Vector2d position,ArrayList<Integer> genotype){
        this.map =  map;
        this.position = position;
        this.direction = setDirection();
        this.energy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.epoch = 0;
        this.childrenAmount = 0;
        this.genotype = genotype;
    }

    public Animal(AbstractWorldMap map,int startEnergy,Vector2d position){
        this.map =  map;
        this.position = position;
        this.direction = setDirection();
        this.energy = startEnergy;
        this.epoch = 0;
        this.genotype = setGenotype();
    }

    public void setEnergy(int energy){
        this.energy = energy;
    }

    // ANIMALS' INITIALIZATION

    public MapDirection setDirection(){
        int direction = (int)Math.floor(Math.random()*8);
        return switch (direction){
            case 0 -> MapDirection.NORTH;
            case 1 -> MapDirection.WEST;
            case 2 -> MapDirection.SOUTH;
            case 3 -> MapDirection.EAST;
            case 4 -> MapDirection.SOUTH_WEST;
            case 5 -> MapDirection.NORTH_EAST;
            case 6 -> MapDirection.SOUTH_EAST;
            case 7 -> MapDirection.NORTH_WEST;
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        };
    }

    public ArrayList<Integer> setGenotype(){
        ArrayList<Integer> genotype = new ArrayList<>();
        Random random = new Random();
        for(int i =0; i<32;i++){
            genotype.add(random.nextInt(8));
        }
        Collections.sort(genotype);
        return genotype;
    }

    public void setBabysGenotype(ArrayList<Integer> genotype){
        Collections.sort(genotype);
        setGenotype(genotype);
    }

    public void setGenotype(ArrayList<Integer> genotype){
        this.genotype = genotype;
    }

    public int generateMoveDirection(){
        Random random = new Random();
        int randomIndex = random.nextInt(this.genotype.size());
        return this.genotype.get(randomIndex);
    }

    public void incrementEpoch(){
        this.epoch+=1;
    }

    public void incrementChildrenAmount(){
        this.childrenAmount +=1;
    }

    public int getChildrenAmount(){
        return this.childrenAmount;
    }

    public int getEpoch(){
        return this.epoch;
    }

    public void turn(int number){
        if ((number>7)||(number<0)) {
            throw new IllegalArgumentException("Illegal direction specification: " + number);
        }
        if ((number == 0)|| (number == 4)) return;
        MapDirection temporaryDirection = this.getDirection();
        for(int i=0; i<number;i++){
            temporaryDirection = temporaryDirection.next();
        }
        this.direction = temporaryDirection;
    }


    public void move() {
        int direction = generateMoveDirection();

        switch (direction) {
            case 0 -> {
                Vector2d newPosition = this.position.add(this.direction.toUnitVector());
                newPosition = this.map.canMoveTo(this.position,newPosition);
                if (!(newPosition.equals(this.position))){
                    this.energy -= moveEnergy;
                    positionChanged(this,this.position, newPosition);
                }
                this.position = newPosition;
            }
            case 4 -> {
                Vector2d newPosition = this.position.subtract(this.direction.toUnitVector());
                newPosition = this.map.canMoveTo(this.position,newPosition);
                if (!(newPosition.equals(this.position))){
                    this.energy -= moveEnergy;
                    positionChanged(this,this.position, newPosition);
                }
                this.position = newPosition;

            }
            case 1,2,3,5,6,7 -> {
                this.energy -= moveEnergy;
                turn(direction);
            }
        }
    }


    // OBSERVERS

    public void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer){
        observers.remove(observer);
    }

    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition){
        for (IPositionChangeObserver observer : this.observers)
            observer.positionChanged(animal, oldPosition, newPosition);
    }

    //UTILITIES

    @Override
    public String toString(){
        return switch (this.direction){
            case NORTH -> "^";
            case NORTH_EAST -> "NE";
            case EAST -> ">";
            case SOUTH_EAST -> "SE";
            case WEST -> "<";
            case SOUTH_WEST -> "SW";
            case SOUTH -> "v";
            case NORTH_WEST -> "NW";
        };
    }

    boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    public boolean isDead(){
        return this.getEnergy() <= 0;
    }

    // FOR TESTING PURPOSES

    public MapDirection getDirection() {return this.direction;}

    public Vector2d getPosition() {return position;}

    public int getEnergy(){return this.energy;}

    public ArrayList<Integer> getGenotype(){return this.genotype;}

}

