
  ____|               \  |               |     _)                   
  __| \ \   /  _ \   |\/ |   _` |   __|  __ \   |  __ \    _` |     
  |    \ \ /  (   |  |   |  (   |  (     | | |  |  |   |  (   |     
 _____| \_/  \___/  _|  _| \__,_| \___| _| |_| _| _|  _| \__,_|     

This implementation of EvoMachina uses 11 new java class files, though there is some dependency on the original tsp solving implementation at EvoEvo.york.tspTest . The majority of these files mirror a file from the original tspTest but with slight changes io order to use the new representation. For these files, you can see which original file it is mirroring by removing "Real" , "R" or "RTSP" from the beginning of the name. The exception to this is RealDomain and RealPearl which replace CityType and City respectively.   

To use, run RTSPMain with the properties file as the argument (as in the original implementation). 
