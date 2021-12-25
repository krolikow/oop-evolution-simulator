import java.util.*;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected Map<Vector2d, Grass> grass = new HashMap<>();
    protected Map<Vector2d, LinkedList<Animal>> animals = new HashMap<>();
    protected int width, height;
    protected MapVisualizer visualizer = new MapVisualizer(this);
    protected Vector2d upperRight;
    protected Vector2d lowerLeft;
    protected int plantEnergy=10;


    @Override
    public AbstractWorldMapElement objectAt(Vector2d position) {
        if ((animals.get(position) == null) || animals.get(position).size() == 0) {
            return grass.get(position);
        }
        return animals.get(position).get(0);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }


    @Override
    public boolean canMoveTo(Vector2d position) {
        return (0 <= position.x) && (position.x <= width - 1) && (0 <= position.y) && (position.y <= height - 1);
    }

    @Override
    public boolean placeAnimal(Animal animal) {
        Vector2d position = animal.getPosition();
        if (canMoveTo(position)) {
            this.addAnimalToAnimals(animal,position);
            return true;
        }
        return false;
    }

    public void addAnimalToAnimals(Animal animal,Vector2d newPosition){
        if (animals.get(newPosition) == null){
            LinkedList<Animal> listOfAnimals = new LinkedList<>();
            listOfAnimals.add(animal);
            animals.put(newPosition,listOfAnimals); //? rly
        }
        else{
            animals.get(newPosition).add(animal);
        }
    }

    public void removeAnimalFromAnimals(Animal animal,Vector2d oldPosition){
        if ((animals.get(oldPosition) == null)||(animals.get(oldPosition).size() == 0)) return;
        animals.get(oldPosition).remove(animal);
        if(animals.get(oldPosition).size() == 0) animals.remove(oldPosition);
    }

    @Override
    public String toString() {
        return this.visualizer.draw(new Vector2d(0, 0), new Vector2d(width - 1, height - 1));
    }


    @Override
    public void positionChanged(Animal animal ,Vector2d oldPosition, Vector2d newPosition) {
        this.removeAnimalFromAnimals(animal,oldPosition);
        this.addAnimalToAnimals(animal,newPosition);
    }

    public Animal animalWooHoo(Vector2d position){ // assumes that there's at least 2 animals on the position, musi byc tez minimalna ilosc energii
        LinkedList<Animal> potentialParents = animals.get(position);
        List<Integer>mamasPart;
        List<Integer>tatasPart;
        List<Integer> babysGenotype;
        Random random = new Random();

        potentialParents.sort(new energyComparator());
        Animal mama = potentialParents.get(0);
        Animal tata = potentialParents.get(1);
        Animal baby = new Animal(this, (int) (Math.floor(0.25*mama.getEnergy() + 0.25* tata.getEnergy())),position);

        int randomizedSide = random.nextInt(2);
        int divisor= (int) Math.floor( (float) mama.getEnergy()/(mama.getEnergy() + tata.getEnergy())*32);

        if(randomizedSide == 0){ // left side
            mamasPart = mama.getGenotype().subList(0,divisor);
            tatasPart = tata.getGenotype().subList(divisor,32);
        }
        else{ //right side
            mamasPart = mama.getGenotype().subList(32-divisor,32);
            tatasPart =  tata.getGenotype().subList(0,32-divisor);
        }

        mamasPart.addAll(tatasPart);
        babysGenotype = mamasPart;
        Collections.sort(babysGenotype);
        baby.setBabysGenotype(babysGenotype);

        return baby;
    }

    public boolean isNoAnimalThere(Vector2d position){
        return ((animals.get(position) == null)||(animals.get(position).size() == 0));
    }

    public void divideAndEat(List<Animal> feastingAnimals){
        int toDivideFor = feastingAnimals.size();
        for(Animal animal: feastingAnimals){
            animal.setEnergy(animal.getEnergy() + (int) Math.floor((float)this.plantEnergy/toDivideFor));
//            removeAnimalFromAnimals(animal,animal.getPosition());
//            addAnimalToAnimals(animal,animal.getPosition());
        }
    }

    public List<Animal> getFeastingAnimals(Vector2d position){
        this.animals.get(position).sort(new energyComparator());
        int i = 0;
        while((i < this.animals.get(position).size())&&(this.animals.get(position).get(i).getEnergy()==this.animals.get(position).get(0).getEnergy())){
            i++;
        }
        return this.animals.get(position).subList(0,i);
    }

    public void eating(){
        List<Grass> grassToRemove = new LinkedList<>();
        for(Vector2d grassPosition: grass.keySet()){
            if (this.isNoAnimalThere(grassPosition)){
                continue;
            }
            List<Animal> feastingAnimals = this.getFeastingAnimals(grassPosition);
            System.out.println(feastingAnimals.size());
            this.divideAndEat(feastingAnimals);
            grassToRemove.add(grass.get(grassPosition));
        }

        for(Grass grassTuft: grassToRemove){
            grass.remove(grassTuft.getPosition());
        }

    }

    public void removeDeadBodies(){
        List<Animal> bodiesToRemove = new LinkedList<>();
        for(Vector2d animalsPosition: animals.keySet()){
            for(Animal potentiallyDeadAnimal: animals.get(animalsPosition)){
                if(potentiallyDeadAnimal.isDead()){
                    bodiesToRemove.add(potentiallyDeadAnimal);
                }
            }
        }

        for(Animal deadBody: bodiesToRemove){
            removeAnimalFromAnimals(deadBody,deadBody.getPosition());
        }
    }

    public void addNewPlants(){
        //todo;
    }
}