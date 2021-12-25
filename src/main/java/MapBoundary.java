
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Comparator;
//
//class comparatorX implements Comparator<Pair<Vector2d,Class>>{
//
//    @Override
//    public int compare(Pair<Vector2d ,Class> thisPair, Pair<Vector2d ,Class> thatPair){
//        Vector2d thisPosition = thisPair.getKey();
//        Vector2d thatPosition = thatPair.getKey();
//        String thisPositionType = thisPair.getValue().getName();
//        String thatPositionType = thatPair.getValue().getName();
//
//        IWorldMap map = null;
//        Animal animal = new Animal(map, new Vector2d(0,0));
//
//        if (thisPosition.x < thatPosition.x) return -1;
//        else if(thisPosition.x == thatPosition.x){
//            if(thisPosition.y > thatPosition.y) return 1;
//            else if(thisPosition.y < thatPosition.y) return -1;
//            else{
//                if (thisPositionType.equals(thatPositionType)) return 0;
//                else if (thatPositionType.equals(animal.getClass().getName())) return -1;
//                else if (thisPositionType.equals(animal.getClass().getName())) return 1;
//                else throw new IllegalArgumentException("Objects " + thisPositionType + " and " + thatPositionType + " are not comparable.");
//                }
//            }
//        return 1;
//    }
//}
//
//class comparatorY implements Comparator<Pair<Vector2d,Class>>{
//
//    @Override
//    public int compare(Pair<Vector2d ,Class> thisPair, Pair<Vector2d ,Class> thatPair){
//        Vector2d thisPosition = thisPair.getKey();
//        Vector2d thatPosition = thatPair.getKey();
//        String thisPositionType = thisPair.getValue().getName();
//        String thatPositionType = thatPair.getValue().getName();
//
//        IWorldMap map = null;
//        Animal animal = new Animal(map, new Vector2d(0,0));
//
//        if (thisPosition.y < thatPosition.y) return -1;
//        else if(thisPosition.y == thatPosition.y){
//            if(thisPosition.x > thatPosition.x) return 1;
//            else if(thisPosition.x < thatPosition.x) return -1;
//            else{
//                if (thisPositionType.equals(thatPositionType)) return 0;
//                else if (thatPositionType.equals(animal.getClass().getName())) return -1;
//                else if (thisPositionType.equals(animal.getClass().getName())) return 1;
//                else throw new IllegalArgumentException("Objects " + thisPositionType + " and " + thatPositionType + " are not comparable.");
//            }
//        }
//        return 1;
//    }
//}
//
//public class MapBoundary implements IPositionChangeObserver {
//
//    private final SortedSet<Pair<Vector2d,Class>> setX = new TreeSet<>(new comparatorX());
//    private final SortedSet<Pair<Vector2d,Class>> setY = new TreeSet<>(new comparatorY());
//    private final AbstractWorldMap map;
//
//    public MapBoundary(AbstractWorldMap map) {
//        this.map = map;
//    }
//
//    public void addElement(IMapElement element) {
//        Pair<Vector2d,Class> newPairToAdd = new Pair<>(element.getPosition(),element.getClass());
//        this.setY.add(newPairToAdd);
//        this.setX.add(newPairToAdd);
//    }
//
//    public void removeElement(IMapElement element) {
//        Pair<Vector2d,Class> newPairToDelete = new Pair<>(element.getPosition(),element.getClass());
//        this.setX.remove(newPairToDelete);
//        this.setY.remove(newPairToDelete);
//    }
//
//    public Vector2d getUpperRight(){
//        if (this.setX.size() == 0) {
//            return new Vector2d(0, 0);
//        }
//        return setX.last().getKey().upperRight(setY.last().getKey());
//    }
//
//    public Vector2d getLowerLeft(){
//        if (this.setX.size() == 0) {
//            return new Vector2d(0, 0);
//        }
//        return setX.first().getKey().lowerLeft(setY.first().getKey());
//    }
//
//    @Override
//    public void positionChanged() {};
//
//    @Override
//    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
//        IMapElement element = (AbstractWorldMapElement) map.objectAt(newPosition);
//
//        Pair<Vector2d,Class> newPairToDelete = new Pair<>(oldPosition, element.getClass());
//        setX.remove(newPairToDelete);
//        setY.remove(newPairToDelete);
//
//        Pair<Vector2d,Class> newPairToAdd = new Pair<>(newPosition, element.getClass());
//        this.setX.add(newPairToAdd);
//        this.setY.add(newPairToAdd);
//    }
//
//    // for testing purposes
//
//    protected int getSetXSize() {
//        return this.setX.size();
//    }
//
//    protected int getSetYSize() {
//        return this.setY.size();
//    }
//
//    protected SortedSet<Pair<Vector2d,Class>> getSetX(){ return this.setX;}
//
//    protected SortedSet<Pair<Vector2d,Class>> getSetY(){ return this.setY;}
//
//}