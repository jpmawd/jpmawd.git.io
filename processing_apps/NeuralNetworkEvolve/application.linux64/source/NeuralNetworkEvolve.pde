World world; 
int dnaLength = 90;
Creature selected;
import javax.swing.JOptionPane;

void mousePressed() {
  world.creatures.add(new Creature(new PVector(mouseX, mouseY), new DNA(dnaLength)));
}

void keyReleased() {
    if(key == 'c') {
      if(selected != null) {
        DNA selectedDNA = selected.dna.copy();
        Creature clone = new Creature(new PVector(random(width), random(height)), selectedDNA);
        world.creatures.add(clone);
        fill(color(0,100,100,100));
        noStroke();
        ellipse(clone.position.x, clone.position.y, clone.radius*2, clone.radius*2);
      }
    }
    else if(key == 'k') {
      if(selected != null) {
        selected.health = 0;
      }
    }
    else if(key == 'i') {
      selected = importCreature();
    }
}

void setup() {
  size(1200, 600);
  world = new World(width, height, .01, 10, 40);
}

void draw() {
  background(255);
  fill(100);
  text("BEST: " + world.bestFitness + " ; AVG: " + world.avgFitness + " ; STEPS: " + world.steps + " ; DEATHS: " + world.deaths, 100, 100);
  
  world.run();
  
  if(keyPressed) {
    if(key == ' ') {
      selected = getIndividualUnderMouse();
    }
    else if(key == 'n') {
      if(selected != null) {
        fill(color(255,255,255,100));
        rect(0, 0, width, height);
        fill(0);
        println("/*weights:");
        for(int i = 0; i < selected.network.getConnections().size(); i++) {
          Connection c = selected.network.getConnections().get(i);
          float weight = c.getWeight();
          String n1 = c.source.name;
          String n2 = c.terminal.name;
          text("("+i+"): "+n1+"-"+n2+": "+weight, 300*int(i*10/height), 10+(10*i)%height);
          println("("+i+"): "+n1+"-"+n2+": "+weight);
        }
        println("*/");
      }
    }
    else if(key == 'a') {
      if(selected != null) {
        int numCopies = 0;
        for(Creature creature : world.matingPool) {
          if(creature == selected) {
            numCopies++;
          }
        }
        fill(0);
        text("GENEPOOL:"+numCopies+"/"+world.matingPool.size(), 0, 10);
        noFill();
        for(Creature creature : world.creatures) {
          float similarity = computeGeneticSimilarity(creature, selected);
          float percentIdentical = computePercentIdentical(creature, selected);
          stroke(color(255*(1-similarity), 255*similarity, 0));
          ellipse(creature.position.x, creature.position.y, creature.radius+1, creature.radius+1);
          text(similarity*100+"%similar/"+percentIdentical*100+"%identical", creature.position.x, creature.position.y+2*creature.radius);
        }
      }
    }
  }
  
  if(selected != null) {
    fill(color(0,100,0,100));
    noStroke();
    ellipse(selected.position.x, selected.position.y, selected.radius*2, selected.radius*2);
  }
}

Creature getIndividualUnderMouse() {
  for(Creature c : world.creatures) {
    if(dist(c.position.x, c.position.y, mouseX, mouseY) < c.radius) {
      return c;
    }
  }
  return null;
}

float computeGeneticSimilarity(Creature c1, Creature c2) {
  float difference = 0;
  for(int i = 0; i < c1.network.getConnections().size(); i++) {
    difference += abs(c1.network.getConnections().get(i).getWeight()-c2.network.getConnections().get(i).getWeight())/2;
  }
  difference /= c1.network.getConnections().size();
  return 1-difference;
}

float computePercentIdentical(Creature c1, Creature c2) {
  int numDifferences = 0;
  for(int i = 0; i < c1.network.getConnections().size(); i++) {
    if(abs(c1.network.getConnections().get(i).getWeight()-c2.network.getConnections().get(i).getWeight()) > .001) {
      numDifferences++;
    }
  }
  return 1-float(numDifferences)/c1.network.getConnections().size();
}

Creature importCreature() {
  String neuraldata = JOptionPane.showInputDialog(null, "NeuralNetworkEvolve", "IMPORT CREATURE NEURAL DATA", JOptionPane.QUESTION_MESSAGE);
  ArrayList<String> datalist = new ArrayList();
  DNA dna = new DNA(dnaLength);
  int numConnections = 0;
  int index = neuraldata.substring(0).indexOf("(");
  
  while(index != -1) {
    index = neuraldata.indexOf("(", index+1);
    numConnections++;
  }
  
  
  if(numConnections == 0) {
    return null;
  }
  neuraldata = neuraldata.substring(neuraldata.indexOf("(")+1);
  for(int i = 0; i < numConnections-1; i++) {
    datalist.add(neuraldata.substring(0, neuraldata.indexOf("(")));
    neuraldata = neuraldata.substring(neuraldata.indexOf("(")+1);
  }
  datalist.add(neuraldata.substring(0, neuraldata.indexOf("*")-1));
  for(int i = 0; i < datalist.size(); i++) {
    String data = datalist.get(i);
    data = data.substring(data.indexOf(":")+1);
    data = data.substring(data.indexOf(":")+1);
    data.trim();
    datalist.set(i, data);
  }
  for(int i = 0; i < dna.genes.length; i++) {
    float weight;
    if(i < datalist.size()) {
      weight = Float.parseFloat(datalist.get(i));
    }
    else {
      weight = random(-1, 1);
    }
    dna.genes[i] = weight;
  }
  return new Creature(new PVector(0,0), dna);
}

/*weights:
(0): HEALTH-n/a: -0.37049925
(1): MOVE-n/a: -0.91064215
(2): TURN_LEFT-n/a: 0.8806993
(3): TURN_RIGHT-n/a: -0.53423524
(4): LEFT_EYE-n/a: 0.25864482
(5): RIGHT_EYE-n/a: -0.033191085
(6): HEALTH-n/a: -0.103137136
(7): MOVE-n/a: -0.14711261
(8): TURN_LEFT-n/a: -0.100932956
(9): TURN_RIGHT-n/a: 0.12094605
(10): LEFT_EYE-n/a: -0.293401
(11): RIGHT_EYE-n/a: -0.43414056
(12): HEALTH-n/a: -0.8545631
(13): MOVE-n/a: 0.15976453
(14): TURN_LEFT-n/a: 0.6082128
(15): TURN_RIGHT-n/a: 0.7350002
(16): LEFT_EYE-n/a: -0.54676545
(17): RIGHT_EYE-n/a: 0.28445673
(18): HEALTH-n/a: -0.5817112
(19): MOVE-n/a: 0.010672569
(20): TURN_LEFT-n/a: 0.8297541
(21): TURN_RIGHT-n/a: -0.09893024
(22): LEFT_EYE-n/a: 0.3654728
(23): RIGHT_EYE-n/a: -0.0061792135
(24): HEALTH-n/a: 0.049046874
(25): MOVE-n/a: 0.021283984
(26): TURN_LEFT-n/a: 0.24890804
(27): TURN_RIGHT-n/a: 0.033009052
(28): LEFT_EYE-n/a: 0.6815876
(29): RIGHT_EYE-n/a: -0.62387884
(30): HEALTH-n/a: -0.3884307
(31): MOVE-n/a: -0.96248496
(32): TURN_LEFT-n/a: 0.080491066
(33): TURN_RIGHT-n/a: 0.277233
(34): LEFT_EYE-n/a: -0.5396701
(35): RIGHT_EYE-n/a: 0.63282394
(36): HEALTH-n/a: 0.4256339
(37): MOVE-n/a: 0.9553795
(38): TURN_LEFT-n/a: -0.29870605
(39): TURN_RIGHT-n/a: 0.40292573
(40): LEFT_EYE-n/a: 0.20485246
(41): RIGHT_EYE-n/a: 0.19921505
(42): HEALTH-n/a: 0.7261145
(43): MOVE-n/a: -0.3558414
(44): TURN_LEFT-n/a: -0.41653252
(45): TURN_RIGHT-n/a: 0.9552821
(46): LEFT_EYE-n/a: -0.83565795
(47): RIGHT_EYE-n/a: -0.07948494
(48): HEALTH-n/a: -0.9979751
(49): MOVE-n/a: 0.50909853
(50): TURN_LEFT-n/a: 0.7961929
(51): TURN_RIGHT-n/a: 0.42401242
(52): LEFT_EYE-n/a: 0.7329824
(53): RIGHT_EYE-n/a: -0.14436054
(54): HEALTH-n/a: 0.99364316
(55): MOVE-n/a: 0.25891757
(56): TURN_LEFT-n/a: 0.8584571
(57): TURN_RIGHT-n/a: -0.17520249
(58): LEFT_EYE-n/a: 0.22581613
(59): RIGHT_EYE-n/a: 0.6465771
(60): n/a-MOVE: 0.03218746
(61): n/a-MOVE: 0.92425966
(62): n/a-MOVE: -0.5660987
(63): n/a-MOVE: 0.9250014
(64): n/a-MOVE: -0.021986961
(65): n/a-MOVE: -0.79936886
(66): n/a-MOVE: 0.57903713
(67): n/a-MOVE: -0.8584089
(68): n/a-MOVE: 0.80795205
(69): n/a-MOVE: 0.7327496
(70): n/a-TURN_LEFT: 0.8643966
(71): n/a-TURN_LEFT: 0.2485671
(72): n/a-TURN_LEFT: 0.92512083
(73): n/a-TURN_LEFT: -0.27656054
(74): n/a-TURN_LEFT: 0.7236141
(75): n/a-TURN_LEFT: 0.10727227
(76): n/a-TURN_LEFT: -0.82368314
(77): n/a-TURN_LEFT: -0.85685694
(78): n/a-TURN_LEFT: 0.80174625
(79): n/a-TURN_LEFT: -0.8787254
(80): n/a-TURN_RIGHT: 0.8583318
(81): n/a-TURN_RIGHT: -0.87210155
(82): n/a-TURN_RIGHT: 0.28781283
(83): n/a-TURN_RIGHT: -0.8898827
(84): n/a-TURN_RIGHT: 0.63949823
(85): n/a-TURN_RIGHT: -0.9967902
(86): n/a-TURN_RIGHT: -0.20753801
(87): n/a-TURN_RIGHT: 0.37071884
(88): n/a-TURN_RIGHT: -0.931638
(89): n/a-TURN_RIGHT: 0.0839237
*/

/*weights:
(0): HEALTH-n/a: -0.37049925
(1): MOVE-n/a: -0.91064215
(2): TURN_LEFT-n/a: 0.8806993
(3): TURN_RIGHT-n/a: -0.53423524
(4): LEFT_EYE-n/a: 0.25864482
(5): RIGHT_EYE-n/a: -0.033191085
(6): HEALTH-n/a: -0.103137136
(7): MOVE-n/a: -0.14711261
(8): TURN_LEFT-n/a: -0.100932956
(9): TURN_RIGHT-n/a: 0.12094605
(10): LEFT_EYE-n/a: -0.293401
(11): RIGHT_EYE-n/a: -0.43414056
(12): HEALTH-n/a: -0.8545631
(13): MOVE-n/a: 0.15976453
(14): TURN_LEFT-n/a: 0.6082128
(15): TURN_RIGHT-n/a: 0.7350002
(16): LEFT_EYE-n/a: -0.54676545
(17): RIGHT_EYE-n/a: 0.28445673
(18): HEALTH-n/a: -0.5817112
(19): MOVE-n/a: 0.010672569
(20): TURN_LEFT-n/a: 0.8297541
(21): TURN_RIGHT-n/a: -0.09893024
(22): LEFT_EYE-n/a: 0.3654728
(23): RIGHT_EYE-n/a: -0.0061792135
(24): HEALTH-n/a: 0.049046874
(25): MOVE-n/a: 0.021283984
(26): TURN_LEFT-n/a: 0.24890804
(27): TURN_RIGHT-n/a: 0.033009052
(28): LEFT_EYE-n/a: 0.6815876
(29): RIGHT_EYE-n/a: -0.62387884
(30): HEALTH-n/a: -0.3884307
(31): MOVE-n/a: -0.96248496
(32): TURN_LEFT-n/a: 0.080491066
(33): TURN_RIGHT-n/a: 0.277233
(34): LEFT_EYE-n/a: -0.5396701
(35): RIGHT_EYE-n/a: 0.63282394
(36): HEALTH-n/a: 0.4256339
(37): MOVE-n/a: 0.9553795
(38): TURN_LEFT-n/a: -0.29870605
(39): TURN_RIGHT-n/a: 0.40292573
(40): LEFT_EYE-n/a: 0.20485246
(41): RIGHT_EYE-n/a: 0.19921505
(42): HEALTH-n/a: 0.7261145
(43): MOVE-n/a: -0.3558414
(44): TURN_LEFT-n/a: -0.41653252
(45): TURN_RIGHT-n/a: 0.9552821
(46): LEFT_EYE-n/a: -0.83565795
(47): RIGHT_EYE-n/a: -0.07948494
(48): HEALTH-n/a: -0.9979751
(49): MOVE-n/a: 0.50909853
(50): TURN_LEFT-n/a: 0.7961929
(51): TURN_RIGHT-n/a: 0.42401242
(52): LEFT_EYE-n/a: 0.7329824
(53): RIGHT_EYE-n/a: -0.14436054
(54): HEALTH-n/a: 0.99364316
(55): MOVE-n/a: 0.25891757
(56): TURN_LEFT-n/a: 0.8584571
(57): TURN_RIGHT-n/a: -0.17520249
(58): LEFT_EYE-n/a: 0.22581613
(59): RIGHT_EYE-n/a: 0.6465771
(60): n/a-MOVE: 0.03218746
(61): n/a-MOVE: 0.92425966
(62): n/a-MOVE: 0.83114576
(63): n/a-MOVE: 0.9250014
(64): n/a-MOVE: -0.021986961
(65): n/a-MOVE: -0.79936886
(66): n/a-MOVE: 0.57903713
(67): n/a-MOVE: -0.8584089
(68): n/a-MOVE: 0.80795205
(69): n/a-MOVE: 0.7327496
(70): n/a-TURN_LEFT: 0.8643966
(71): n/a-TURN_LEFT: 0.2485671
(72): n/a-TURN_LEFT: 0.92512083
(73): n/a-TURN_LEFT: -0.27656054
(74): n/a-TURN_LEFT: 0.7236141
(75): n/a-TURN_LEFT: 0.10727227
(76): n/a-TURN_LEFT: -0.82368314
(77): n/a-TURN_LEFT: -0.85685694
(78): n/a-TURN_LEFT: 0.80174625
(79): n/a-TURN_LEFT: -0.8787254
(80): n/a-TURN_RIGHT: 0.8583318
(81): n/a-TURN_RIGHT: -0.87210155
(82): n/a-TURN_RIGHT: 0.28781283
(83): n/a-TURN_RIGHT: -0.8898827
(84): n/a-TURN_RIGHT: 0.63949823
(85): n/a-TURN_RIGHT: -0.9967902
(86): n/a-TURN_RIGHT: -0.20753801
(87): n/a-TURN_RIGHT: 0.37071884
(88): n/a-TURN_RIGHT: -0.931638
(89): n/a-TURN_RIGHT: 0.0839237
*/

/*weights:
(0): HEALTH-n/a: -0.37049925
(1): MOVE-n/a: -0.91064215
(2): TURN_LEFT-n/a: 0.8806993
(3): TURN_RIGHT-n/a: -0.53423524
(4): LEFT_EYE-n/a: 0.25864482
(5): RIGHT_EYE-n/a: -0.033191085
(6): HEALTH-n/a: -0.103137136
(7): MOVE-n/a: -0.14711261
(8): TURN_LEFT-n/a: -0.100932956
(9): TURN_RIGHT-n/a: 0.12094605
(10): LEFT_EYE-n/a: -0.293401
(11): RIGHT_EYE-n/a: -0.43414056
(12): HEALTH-n/a: -0.8545631
(13): MOVE-n/a: 0.15976453
(14): TURN_LEFT-n/a: 0.59278935
(15): TURN_RIGHT-n/a: -0.45663774
(16): LEFT_EYE-n/a: -0.54676545
(17): RIGHT_EYE-n/a: 0.28445673
(18): HEALTH-n/a: -0.5817112
(19): MOVE-n/a: 0.010672569
(20): TURN_LEFT-n/a: 0.8297541
(21): TURN_RIGHT-n/a: -0.09893024
(22): LEFT_EYE-n/a: 0.3654728
(23): RIGHT_EYE-n/a: -0.0061792135
(24): HEALTH-n/a: 0.049046874
(25): MOVE-n/a: 0.021283984
(26): TURN_LEFT-n/a: 0.24890804
(27): TURN_RIGHT-n/a: 0.033009052
(28): LEFT_EYE-n/a: 0.9496182
(29): RIGHT_EYE-n/a: -0.62387884
(30): HEALTH-n/a: -0.3884307
(31): MOVE-n/a: -0.96248496
(32): TURN_LEFT-n/a: 0.080491066
(33): TURN_RIGHT-n/a: 0.277233
(34): LEFT_EYE-n/a: -0.5396701
(35): RIGHT_EYE-n/a: 0.63282394
(36): HEALTH-n/a: 0.4256339
(37): MOVE-n/a: 0.9553795
(38): TURN_LEFT-n/a: -0.29870605
(39): TURN_RIGHT-n/a: 0.40292573
(40): LEFT_EYE-n/a: 0.20485246
(41): RIGHT_EYE-n/a: 0.19921505
(42): HEALTH-n/a: 0.7261145
(43): MOVE-n/a: -0.3558414
(44): TURN_LEFT-n/a: -0.41653252
(45): TURN_RIGHT-n/a: 0.03988439
(46): LEFT_EYE-n/a: -0.83565795
(47): RIGHT_EYE-n/a: -0.07948494
(48): HEALTH-n/a: -0.9979751
(49): MOVE-n/a: 0.50909853
(50): TURN_LEFT-n/a: 0.7961929
(51): TURN_RIGHT-n/a: 0.42401242
(52): LEFT_EYE-n/a: 0.7329824
(53): RIGHT_EYE-n/a: -0.14436054
(54): HEALTH-n/a: 0.99364316
(55): MOVE-n/a: 0.25891757
(56): TURN_LEFT-n/a: 0.8584571
(57): TURN_RIGHT-n/a: -0.17520249
(58): LEFT_EYE-n/a: 0.22581613
(59): RIGHT_EYE-n/a: 0.92785835
(60): n/a-MOVE: 0.03218746
(61): n/a-MOVE: 0.92425966
(62): n/a-MOVE: -0.5660987
(63): n/a-MOVE: 0.9250014
(64): n/a-MOVE: -0.021986961
(65): n/a-MOVE: -0.79936886
(66): n/a-MOVE: 0.57903713
(67): n/a-MOVE: -0.8584089
(68): n/a-MOVE: 0.80795205
(69): n/a-MOVE: 0.7327496
(70): n/a-TURN_LEFT: 0.8643966
(71): n/a-TURN_LEFT: 0.2485671
(72): n/a-TURN_LEFT: 0.38444686
(73): n/a-TURN_LEFT: -0.27656054
(74): n/a-TURN_LEFT: 0.7236141
(75): n/a-TURN_LEFT: 0.10727227
(76): n/a-TURN_LEFT: -0.82368314
(77): n/a-TURN_LEFT: 0.30325007
(78): n/a-TURN_LEFT: 0.80174625
(79): n/a-TURN_LEFT: -0.8787254
(80): n/a-TURN_RIGHT: 0.8583318
(81): n/a-TURN_RIGHT: -0.87210155
(82): n/a-TURN_RIGHT: 0.28781283
(83): n/a-TURN_RIGHT: -0.8898827
(84): n/a-TURN_RIGHT: 0.63949823
(85): n/a-TURN_RIGHT: -0.9967902
(86): n/a-TURN_RIGHT: -0.20753801
(87): n/a-TURN_RIGHT: 0.37071884
(88): n/a-TURN_RIGHT: -0.931638
(89): n/a-TURN_RIGHT: 0.0839237
*/


