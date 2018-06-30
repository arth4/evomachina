package EvoEvo.york.tspTest;

import EvoEvo.york.machineMetaModel.Domain;
import EvoEvo.york.machineMetaModel.Machine;
import EvoEvo.york.machineMetaModel.Pearl;
import EvoEvo.york.metaMutation.Mutations;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class RKloner2Domain extends Domain {
    /**
     * Construct a new instance. This initialises the superclass's mutation operator to the local
     * implementation of the  mutateCodings operation.
     */

    public RKloner2Domain(String name) {
        super(name, RKloner2.class, (l, k) -> mutateCodings(l));
    }

    /**
     * Answer the degree of this kloner which is the degree of the first (and possibly only) unit in the supplied kloner's code
     */
    /*public int getDegree(Kloner kloner) {
        return ((RKlonerPearl)kloner.getCode().get(0)).getDegree();
    }*/

    @Override
    public String description(Machine machine) {
        StringBuilder sb = new StringBuilder("Kloner2 pearls: [");
        List<Pearl> kCode = machine.getCode();
        for (Pearl p :kCode) {
            sb.append(((RealPearl)p).getValue());
            sb.append(", ");

        }
        sb.append("]");
        return sb.toString();
    }

    /** Method used to mutate the kloner itself. */
    private static List<Pearl> mutateCodings(List<Pearl> l) {
        //store kloner pearls values for mutation so not mutated while using them
        //List<Pearl> kGenome = l;
        double[] kGenome = new double[l.size()];
        for (int i = 0; i < l.size(); i++) {
            kGenome[i] =  ((RealPearl)l.get(i)).getValue();

        }


        int NUM_MUTATIONS = 2;
        int i =0;
        while (i < kGenome.length) {
            BiFunction<List<Pearl>, List<Double>, List<Pearl>> mut;
            List<Double> args = new ArrayList<>();
            int nArgs = 0;
            switch ((int) (kGenome[i] * NUM_MUTATIONS)) {
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
                if(i+j>=kGenome.length)
                    break;
                args.add(kGenome[i + j]);
            }
            l = mut.apply(l,args);
            i = i + nArgs + 1;
        }
        return l;
    }
}
