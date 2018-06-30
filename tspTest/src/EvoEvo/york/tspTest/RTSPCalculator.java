package EvoEvo.york.tspTest;
import EvoEvo.york.machineMetaModel.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RTSPCalculator extends Machine {
    protected Optional<Double> _journeyTime;
    private static Logger _Logger = Logger.getLogger("EvoEvo");

    public RTSPCalculator(Space environment, List<Pearl> code, Domain domain) throws MetaModelException {
        super(environment, code, domain);
        assert(CityType.class.isAssignableFrom(domain.getClass()));
        _journeyTime = Optional.empty();
    }

    /** In the context of this particular machine, doIt is overridden to calculate the journey time from real valued pearls. */
    @Override
    public Structure doIt() {
        if (_code.size() == 0 ) throw new RuntimeException("Error: Route with 0 pearls");

        double result = 0;
        RealDomain rd = (RealDomain)this._domain;
        int numCities = rd.getNumCities();
        double[] realList =  new double[Integer.min(_code.size(),numCities)]; //if more pearls than cities then extra pearls are ignored
        for(int i=0; i<realList.length; i++){
            realList[i] = ((RealPearl)_code.get(i)).getValue();
        }
        double[] sortedList = realList.clone();
        Arrays.sort(sortedList);
        HashMap<Double,ArrayList<Integer>> sLPos = new HashMap<>(realList.length);

        for (int i=0;i<realList.length;i++){
            if(!sLPos.containsKey(sortedList[i])) {
                sLPos.put(sortedList[i], new ArrayList<Integer>(Arrays.asList(i)));
            }
            else{
                sLPos.get(sortedList[i]).add(i);
            }
            //sLPos maps realnumber -> position(s) in sorted list

        }
        int[] permutation = new int[realList.length];
        for (int i=0;i<realList.length;i++){
            permutation[i] = sLPos.get(realList[i]).remove(0);
        }

        for(int i = 0; i<permutation.length -1;i++){
            result += rd.getCityDistance(permutation[i],permutation[i+1]);
        }
        if (permutation.length< numCities){ //if more cites then pearls, left over cities are just added to the end of route in the order they are stored
            result += rd.getCityDistance(permutation[permutation.length-1],permutation.length);
            for(int i=permutation.length +1;i<numCities;i++){
                  result += rd.getCityDistance(i-1,i);
            }
            result += rd.getCityDistance(numCities -1, permutation[0]); //add distance back to first city
        }
        else {
            result += rd.getCityDistance(permutation[permutation.length-1], permutation[0]);
        }
        _journeyTime = Optional.of(result);
        return this;
    }

    public Double getJourneyTime() {
        ((RealDomain)_domain).getFitCount.addOne();
        if (!_journeyTime.isPresent()){

            this.doIt();

            ((RealDomain)_domain).calcFitCount.addOne();
        }
        return _journeyTime.get();
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder(_code.size());
        Pearl first = _code.stream().findFirst().get();
        _code.stream().forEach((u) -> b.append(u.toString() + ","));
        //b.append(first); UNWANTED IN THIS IMPLEMENTATION
        return b.toString();
    }
}
