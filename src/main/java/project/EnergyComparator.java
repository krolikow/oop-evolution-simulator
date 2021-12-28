package project;
import java.util.Comparator;

public class EnergyComparator implements Comparator<Animal>
{
    @Override
    public int compare(Animal o1, Animal o2) {
        return Integer.compare(o2.getEnergy(), o1.getEnergy());
    }
}