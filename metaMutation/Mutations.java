package EvoEvo.york.metaMutation;

import EvoEvo.york.machineMetaModel.Kloner;
import EvoEvo.york.machineMetaModel.Pearl;
import EvoEvo.york.tspTest.RKlonerDomain;
import EvoEvo.york.tspTest.RealPearl;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Mutations {



    public static List<Pearl> gaussMutate(List<Pearl> l, List<Double> args ) {
        double indivMutationRate = 0.01;
        double stndDevi = 0.01;
        if (args.size()>0){
            indivMutationRate = args.get(0);
            if(args.size()>1) stndDevi = args.get(1);
        }

        for (int i=0; i< l.size();i++){
            RealPearl rp = (RealPearl)l.get(i);
            if(ThreadLocalRandom.current().nextDouble(1) < indivMutationRate){
                NormalDistribution nd = new NormalDistribution(rp.getValue(),stndDevi);
                double newVal = nd.sample();
                if(newVal<=1 && newVal>=0) { //IF NOT INSIDE BOUND DISCARD MUTATION
                    rp.setValue(newVal);
                }
            }

        }

        return l;
    }
    public static List<Pearl> translocationMutate(List<Pearl> l, List<Double> args ) {
        double mutRate = 0.1;

        double lengthProp = 0.2;
        if (args.size()>0){
            mutRate = args.get(0);
            if(args.size()>1) lengthProp = args.get(1);

        }

        if(ThreadLocalRandom.current().nextDouble(1)< mutRate)
            return l;
        int genLength = l.size();
        int transLength = (int)(lengthProp*l.size());

        int start = ThreadLocalRandom.current().nextInt(genLength); // 0 to len-1
        int end = start + transLength;
        //remove selection
        List<Pearl> selection;
        if(end<genLength){
            selection = new ArrayList<>(l.subList(start,end+1)); //here start=end => just 1 selected

        }else {
            selection = new ArrayList<>(l.subList(start,genLength));
            List<Pearl> tempSel = new ArrayList<>(l.subList(0,end-genLength+1));
            selection.addAll(tempSel);
        }
        l.removeAll(selection);

        //insert selection
        int newStart = ThreadLocalRandom.current().nextInt(0,l.size()+1); //value in (0,l.size)
        l.addAll(newStart,selection);
        return l;
    }
}

