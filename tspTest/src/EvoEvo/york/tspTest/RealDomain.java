package EvoEvo.york.tspTest;
import EvoEvo.york.machineMetaModel.Domain;
import EvoEvo.york.machineMetaModel.Kloner;
import EvoEvo.york.machineMetaModel.Pearl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;

import EvoEvo.york.metaMutation.Mutations;
import org.apache.commons.math3.distribution .NormalDistribution;
public class RealDomain extends Domain {

    public  class LockBasedCounter {
        private int i = 0;
        public synchronized void addOne()
        {
            i++;
        }
        public synchronized int get()
        {
            return i;
        }
        public synchronized void set(int j)
        {
            i=j;
        }
    }
    public  LockBasedCounter getFitCount = new LockBasedCounter();
    public  LockBasedCounter calcFitCount = new LockBasedCounter();

    private double[][] _cityDistances;
    private int _numCities = 0;

    private List<RealPearl> _realPearls; //may not be necessary

    public RealDomain(String name) {
        super(name, RTSPCalculator.class);

    }

    /**
     * Add a pearl to this domain
     */
    public RealPearl addRealPearl(double value) {
        RealPearl result = new RealPearl(value, this);
        _realPearls.add(result);
        return result;
    }

    public double[][] getCityDistances() {
        return _cityDistances;
    }

    public double getCityDistance(int city1, int city2) {
        return _cityDistances[city1][city2];
    }

    public void setCityDistances(double[][] cityDistances) {
        this._cityDistances = cityDistances;
        this._numCities = cityDistances.length;
    }

    public int getNumCities() {
        return _numCities;
    }


   /* unused but left in as may be usefull*/
    public static List<Pearl> normalise(List<Pearl> l) {
        double summ = 0;
        for (int i = 0; i < l.size(); i++) {
            RealPearl rp = (RealPearl) l.get(i);
            summ += rp.getValue();
        }
        if (summ != 0) {
            for (int i = 0; i < l.size(); i++) {
                RealPearl rp = (RealPearl) l.get(i);
                rp.setValue(rp.getValue() / summ);
            }
        }
        return l;
    }
    /*performs the mutations encoded in the genome. To add new primitive mutation type: add a new case,
     increment NUM_MUTATIONS, and within the case set nArgs to the number of arguments this primitive mutation will take.
        (There is probably a nicer way to do this)*/
    public static List<Pearl> realMutate(List<Pearl> l, Kloner k) {
        List<Pearl> kGenome = k.getCode();

        int NUM_MUTATIONS = 2;
        int i =0;
        while (i < kGenome.size()) {
            BiFunction<List<Pearl>, List<Double>, List<Pearl>> mut;
            List<Double> args = new ArrayList<>();
            int nArgs = 0;
            switch ((int) (((RealPearl) kGenome.get(i)).getValue() * NUM_MUTATIONS)) {
                case 0:
                    mut = (list, a) -> Mutations.gaussMutate(list, a);
                    nArgs = 2;
                    break;

                case 1:
                    mut = (list, a) -> Mutations.translocationMutate(list, a);
                    nArgs = 2;
                    break;
                default:
                    mut = (list,a) -> list;
                    throw new Error("no mutation selected in realMutate");
                }
            for (int j = 1; j <= nArgs; j++) {
                if(i+j>=kGenome.size())
                    break;
                args.add(((RealPearl) kGenome.get(i + j)).getValue());
            }
            l = mut.apply(l,args);
            i = i + nArgs + 1;
        }
        return l;

    }
}
