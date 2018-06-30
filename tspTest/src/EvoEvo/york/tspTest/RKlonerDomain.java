package EvoEvo.york.tspTest;
import EvoEvo.york.machineMetaModel.*;
import EvoEvo.york.metaMutation.Mutations;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;

import static java.lang.Math.abs;
public class RKlonerDomain extends Domain {
    /**
     * Construct a new instance. This initialises the superclass's mutation operator to the local
     * implementation of the  mutateCodings operation.
     */
    public double klonerMutRate = 0.1    ; //todo:encode in pearls
    public double gaussVar = 0.1;
    public RKlonerDomain(String name, Class<? extends Machine> machineType) {
        super(name, machineType, (l, k) -> mutateCodings(l,k));
    }

    /**
     * Answer the degree of this kloner which is the degree of the first (and possibly only) unit in the supplied kloner's code
     */
    /*public int getDegree(Kloner kloner) {
        return ((RKlonerPearl)kloner.getCode().get(0)).getDegree();
    }*/

    @Override
    public String description(Machine machine) {
        StringBuilder sb = new StringBuilder("Kloner pearls: [");
        List<Pearl> kCode = machine.getCode();
        RKloner2 k2 = (RKloner2) ((Individual)machine.getEnvironment()).locateMachine(RKloner2.class);
        List<Pearl> k2Pearls = k2.getCode();
        for (Pearl p :kCode) {
            sb.append(((RealPearl)p).getValue());
            sb.append(", ");    
            
        }
        sb.append("], Kloner2pearls:[");

        for (Pearl p :k2Pearls) {
            sb.append(((RealPearl)p).getValue());
            sb.append(", ");

        }
        sb.append("]");
        return sb.toString();
    }

    /** Method used to mutate the kloner itself. */
    private static List<Pearl> mutateCodings(List<Pearl> l, Kloner k) {
        RKloner2 k2 = (RKloner2) ((Individual)k.getEnvironment()).locateMachine(RKloner2.class);
        List<Pearl> k2Pearls = k2.getCode();
        double[] k2Genome = new double[k2Pearls.size()];
        for (int i = 0; i < l.size(); i++) {
            k2Genome[i] =  ((RealPearl)k2Pearls.get(i)).getValue();

        }


        int NUM_MUTATIONS = 2;
        int i =0;
        while (i < k2Genome.length) {
            BiFunction<List<Pearl>, List<Double>, List<Pearl>> mut;
            List<Double> args = new ArrayList<>();
            int nArgs = 0;
            switch ((int) (k2Genome[i] * NUM_MUTATIONS)) {
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
                if(i+j>=k2Genome.length)
                    break;
                args.add(k2Genome[i + j]);
            }
            l = mut.apply(l,args);
            i = i + nArgs + 1;
        }
        return l;
    }
}
