package project;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    protected ConcurrentHashMap<Vector2d, Grass> grass = new ConcurrentHashMap<>();
    protected ConcurrentHashMap<Vector2d, LinkedList<Animal>> animals = new ConcurrentHashMap<>();
    protected int width, height, plantEnergy, startEnergy, moveEnergy, widthJungle, heightJungle, initialNumberOfAnimals;
    protected MapVisualizer visualizer = new MapVisualizer(this);
    protected Vector2d upperRight, lowerLeft, lowerLeftJungle, upperRightJungle;
    protected double jungleRatio;
    protected ArrayList<Animal> animalsList = new ArrayList<>();
    protected ConcurrentHashMap<ArrayList<Integer>, Integer> allGenotypes = new ConcurrentHashMap<>();
    protected int lifeSpanSum = 0;
    protected int deadAnimalsAmount = 0;


    public Animal copyAnimal(){
        int range = animalsList.size();
        int randomIndex = (int) Math.floor(Math.random() * (range));
        return new Animal(this,startEnergy,animalsList.get(randomIndex).getPosition(),animalsList.get(randomIndex).getGenotype());
    }

    @Override
    public AbstractWorldMapElement objectAt(Vector2d position) {
        if (isNoAnimalThere(position)) {
            return grass.get(position);
        }
        return animals.get(position).get(0);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) != null;
    }

    public void updateAllGenotypes(ArrayList<Integer> genotype) {
        if (!this.allGenotypes.containsKey(genotype)) {
            this.allGenotypes.put(genotype, 1);
        } else {
            int currentAmountOfSuchGenotypes = this.allGenotypes.get(genotype);
            this.allGenotypes.put(genotype, currentAmountOfSuchGenotypes + 1);
        }
    }

    @Override
    public abstract Vector2d canMoveTo(Vector2d oldPosition, Vector2d newPosition);


    public boolean isInJungle(Vector2d position) {
        return (position.follows(this.getLowerLeftJungle())) && (position.precedes(this.getUpperRightJungle()));
    }

    public boolean isOffTheMap(Vector2d position) {
        return ((position.x >= this.width) || (position.y >= height) || (position.x < 0) || (position.y < 0));
    }

    public boolean isXInBounds(Vector2d position) {
        return ((position.x >= 0) && (position.x < this.width));
    }

    public boolean isYInBounds(Vector2d position) {
        return ((position.y >= 0) && (position.y < this.height));
    }

    @Override
    public boolean place(Animal animal) {
        Vector2d position = animal.getPosition();
        if (!(this.isOffTheMap(position))) {
            this.addAnimalToAnimals(animal, position);
            this.animalsList.add(animal);
            animal.addObserver(this);
            this.updateAllGenotypes(animal.getGenotype());
            return true;
        }
        return false;
    }

    public void addAnimalToAnimals(Animal animal, Vector2d newPosition) {
        if (animals.get(newPosition) == null) {
            LinkedList<Animal> listOfAnimals = new LinkedList<>();
            listOfAnimals.add(animal);
            animals.put(newPosition, listOfAnimals);
        } else {
            animals.get(newPosition).add(animal);
        }
    }

    public void removeAnimalFromAnimals(Animal animal, Vector2d oldPosition) {
        if (isNoAnimalThere(oldPosition)) return;
        animals.get(oldPosition).remove(animal);
        if (animals.get(oldPosition).size() == 0) animals.remove(oldPosition);
    }


    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        this.removeAnimalFromAnimals(animal, oldPosition);
        this.addAnimalToAnimals(animal, newPosition);
    }

    public void initializeMap() {
        int i = 0;
        while (i < this.initialNumberOfAnimals) {
            int x = (int) Math.floor(Math.random() * (this.width));
            int y = (int) Math.floor(Math.random() * (this.height));
            Vector2d newPosition = new Vector2d(x, y);
            if (!(this.isOccupied(newPosition))) {
                Animal animal = new Animal(this, this.startEnergy, newPosition);
                this.place(animal);
                i++;
            }
        }
    }


    public boolean isNoAnimalThere(Vector2d position) {
        return (animals.get(position) == null) || (animals.get(position).size() == 0);
    }

    public void updateDeadAnimals() {
        this.deadAnimalsAmount += 1;
    }

    public void updateLifeSpan(Animal animal) {
        this.lifeSpanSum += animal.getEpoch();
    }


    public void removeDeadBodies() {
        CopyOnWriteArrayList<Animal> bodiesToRemove = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<Animal> bodiesToRemoveList = new CopyOnWriteArrayList<>();
        for (Vector2d animalsPosition : animals.keySet()) {
            for (Animal potentiallyDeadAnimal : animals.get(animalsPosition)) {
                if (potentiallyDeadAnimal.isDead()) {
                    this.updateLifeSpan(potentiallyDeadAnimal);
                    this.updateDeadAnimals();
                    bodiesToRemove.add(potentiallyDeadAnimal);
                    bodiesToRemoveList.add(potentiallyDeadAnimal);
                }
            }
        }

        for (Animal deadBody : bodiesToRemove) {
            removeAnimalFromAnimals(deadBody, deadBody.getPosition());
        }

        for (Animal deadBody : bodiesToRemoveList) {
            bodiesToRemoveList.remove(deadBody);
        }
    }

    public void newDay() {
        for (Vector2d position : animals.keySet()) {
            for (Animal animal : animals.get(position)) {
                animal.incrementEpoch();
            }
        }
    }
    // REPRODUCTION

    public Animal animalWooHoo(List<Animal> animals) {
        Animal mum = animals.get(0);
        Animal dad = animals.get(1);
        ArrayList<Integer> mumsPart;
        ArrayList<Integer> dadsPart;
        ArrayList<Integer> babysGenotype;
        Random random = new Random();

        Animal baby = new Animal(this, (int) (Math.floor(0.25 * mum.getEnergy() + 0.25 * dad.getEnergy())), mum.getPosition());

        int randomizedSide = random.nextInt(2);
        int divisor = (int) Math.floor((float) mum.getEnergy() / (mum.getEnergy() + dad.getEnergy()) * 32);

        if (randomizedSide == 0) { // left side
            mumsPart = new ArrayList<>(mum.getGenotype().subList(0, divisor));
            dadsPart = new ArrayList<>(dad.getGenotype().subList(divisor, 32));
        } else { // right side
            mumsPart = new ArrayList<>(mum.getGenotype().subList(32 - divisor, 32));
            dadsPart = new ArrayList<>(dad.getGenotype().subList(0, 32 - divisor));
        }

        mumsPart.addAll(dadsPart);
        babysGenotype = mumsPart;
        baby.setBabysGenotype(babysGenotype);
        mum.incrementChildrenAmount();
        dad.incrementChildrenAmount();
        return baby;
    }

    public void reproduction() {
        List<Animal> incubator = new LinkedList<>();
        for (Vector2d position : animals.keySet()) {
            if (animals.get(position).size() >= 2) {
                List<Animal> parents = this.getPotentialParents(position);
                if (!(this.isAbleToWooHoo(parents))) continue;
                incubator.add(this.animalWooHoo(parents));
            }
        }

        for (Animal baby : incubator) {
            this.place(baby);
        }
    }

    public List<Animal> getPotentialParents(Vector2d position) {
        List<Animal> potentialParents = animals.get(position);
        potentialParents.sort(new EnergyComparator());
        return potentialParents.subList(0, 2);
    }

    public boolean isAbleToWooHoo(List<Animal> animals) {
        return ((animals.get(0).getEnergy() >= 0.5 * this.startEnergy) && (animals.get(1).getEnergy() >= 0.5 * this.startEnergy));
    }

    // MOVING

    public void moving() {
        for (Animal animal : this.animalsList) {
            animal.move();
        }
    }

    // EATING

    public void divideAndEat(List<Animal> feastingAnimals) {
        List<Animal> animalsToUpdate = new ArrayList<>();
        int toDivideFor = feastingAnimals.size();
        for (Animal animal : feastingAnimals) {
            animal.setEnergy(animal.getEnergy() + (int) Math.floor((float) this.plantEnergy / toDivideFor));
            animalsToUpdate.add(animal);
        }

        for (Animal animal : animalsToUpdate) {
            removeAnimalFromAnimals(animal,animal.getPosition());
            addAnimalToAnimals(animal,animal.getPosition());
        }
    }

    public List<Animal> getFeastingAnimals(Vector2d position) {
        this.animals.get(position).sort(new EnergyComparator());
        int i = 0;
        while ((i < this.animals.get(position).size()) && (this.animals.get(position).get(i).getEnergy() == this.animals.get(position).get(0).getEnergy())) {
            i++;
        }
        return this.animals.get(position).subList(0, i);
    }


    public void eating() {
        List<Grass> grassToRemove = new LinkedList<>();
        for (Vector2d grassPosition : grass.keySet()) {
            if (this.isNoAnimalThere(grassPosition)) {
                continue;
            }
            List<Animal> feastingAnimals = this.getFeastingAnimals(grassPosition);
            this.divideAndEat(feastingAnimals);
            grassToRemove.add(grass.get(grassPosition));
        }

        for (Grass grassTuft : grassToRemove) {
            grass.remove(grassTuft.getPosition());
        }

    }

    // PLANTS

    public void addPlantInJungle() {
        int n = heightJungle * widthJungle;
        Random random = new Random();
        for (int i = 0; i <= n; i++) {
            int randomX = random.nextInt(upperRightJungle.x + 1 - lowerLeftJungle.x) + lowerLeftJungle.x;
            int randomY = (int) ((Math.random() * (upperRightJungle.y + 1 - lowerLeftJungle.y)) + lowerLeftJungle.y);
            Grass newGrass = new Grass(new Vector2d(randomX, randomY));

            if ((objectAt(newGrass.getPosition()) == null)) {
                grass.put(newGrass.getPosition(), newGrass);
                return;
            }
        }
    }


    public void addPlantInSteppe() {
        int n = height - heightJungle;
        int randomY;
        Random random = new Random();
        int randomX = (int) ((Math.random() * (upperRight.x + 1 - lowerLeft.x)) + lowerLeft.x);

        if ((randomX >= lowerLeftJungle.x) && (randomX <= upperRightJungle.x)) {

            for (int i = 0; i <= n; i++) {
                int lowerOrUpperPart = random.nextInt(2);

                if (lowerOrUpperPart == 0) {
                    randomY = (int) ((Math.random() * (lowerLeftJungle.y)));
                } else {
                    randomY = (int) ((Math.random() * (upperRight.y + 1 - upperRightJungle.y + 1)) + upperRightJungle.y + 1);
                }

                Grass newGrass = new Grass(new Vector2d(randomX, randomY));

                if ((objectAt(newGrass.getPosition()) == null)) {
                    grass.put(newGrass.getPosition(), newGrass);
                    return;
                }
            }
        } else {
            randomY = (int) ((Math.random() * (upperRight.y + 1)));
            for (int i = 0; i <= n; i++) {

                Grass newGrass = new Grass(new Vector2d(randomX, randomY));

                if ((objectAt(newGrass.getPosition()) == null)) {
                    grass.put(newGrass.getPosition(), newGrass);
                    return;
                }
            }
        }
    }


    public void addNewPlants() {
        this.addPlantInSteppe();
        this.addPlantInJungle();
    }


    // GETTERS & SETTERS

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Vector2d getUpperRight() {
        return upperRight;
    }

    public Vector2d getLowerLeft() {
        return lowerLeft;
    }

    public Vector2d getLowerLeftJungle() {
        return lowerLeftJungle;
    }

    public Vector2d getUpperRightJungle() {
        return upperRightJungle;
    }

    @Override
    public String toString() {
        return this.visualizer.draw(new Vector2d(0, 0), new Vector2d(width - 1, height - 1));
    }
}
