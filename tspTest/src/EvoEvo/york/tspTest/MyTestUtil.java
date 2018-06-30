package EvoEvo.york.tspTest;

import EvoEvo.york.machineMetaModel.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class MyTestUtil {
    static void MakeJourneyInToroidalSpace(RealTSP2DSpace world, int xPos, int yPos, int numCities,
                                           RealDomain realDomain, Domain transcriberDomain, Domain translatorDomain, Domain reproducerDomain) {
        RKlonerDomain kDomain = new RKlonerDomain("Kloner domain", Kloner.class);
        RKloner2Domain kloner2Domain = new RKloner2Domain("Kloner2 domain");
        List<Pearl> route = CreateInitialRandomRoute(realDomain, numCities);
        Space js = world.getSubspace(xPos, yPos);
        RealJourney j = MakeJourneyWithMutatingCopier(route, js, realDomain, kDomain, transcriberDomain, translatorDomain, reproducerDomain,kloner2Domain);
    }

    /** Create a single route that visits all cities in the domain by constructing a sequential route and using a 10-opt algorithm to
     *  shuffle it about. */
    static List<Pearl> CreateInitialRandomRoute(RealDomain realDomain, int numCities) {
        List<Pearl> result = new ArrayList<>();
        for (int i = 0; i < numCities; i++) {
            RealPearl rp = new RealPearl(( ThreadLocalRandom.current().nextDouble(0,1)),realDomain);
           result.add(rp);
        }
        return result;
    }
    /*number of kloner and kloner2 pearls are hardcoded to 6, change this if you want*/
    static RealJourney MakeJourneyWithMutatingCopier(List<Pearl> route, Space world,
                                                 Domain tspDomain, Domain klonerDomain, Domain transcriberDomain, Domain translatorDomain, Domain reproducerDomain,Domain kloner2Domain) {
        RealJourney j = new RealJourney(Optional.of(world));
        List<Pearl> klonerCode = new ArrayList<>();
        for (int i = 0; i < 6; i++) { //TODO Make kloner geno length an argument
            klonerCode.add(new RealPearl(ThreadLocalRandom.current().nextDouble(),klonerDomain));

        }
        List<Pearl> kloner2Code = new ArrayList<>();
        for (int i = 0; i < 6; i++) { //TODO Make kloner2 geno length an argument
            kloner2Code.add(new RealPearl(ThreadLocalRandom.current().nextDouble(),kloner2Domain));

        }
        Util.AddTTMachines(j, transcriberDomain, translatorDomain, reproducerDomain, klonerDomain, klonerCode);
        Structure kloner2Template = new Structure(j, kloner2Code, kloner2Domain);
        j.addMachineTemplate(kloner2Template);

        j.addMachineTemplate(new Structure(j, route, tspDomain));

        return j;
    }
}