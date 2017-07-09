import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import javax.swing.JOptionPane; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class NeuralNetworkEvolve extends PApplet {

World world; 
int dnaLength = 90;
Creature selected;


public void mousePressed() {
  world.creatures.add(new Creature(new PVector(mouseX, mouseY), new DNA(dnaLength)));
}

public void keyReleased() {
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

public void setup() {
  size(1200, 600);
  world = new World(width, height, .01f, 10, 40);
}

public void draw() {
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
          text("("+i+"): "+n1+"-"+n2+": "+weight, 300*PApplet.parseInt(i*10/height), 10+(10*i)%height);
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

public Creature getIndividualUnderMouse() {
  for(Creature c : world.creatures) {
    if(dist(c.position.x, c.position.y, mouseX, mouseY) < c.radius) {
      return c;
    }
  }
  return null;
}

public float computeGeneticSimilarity(Creature c1, Creature c2) {
  float difference = 0;
  for(int i = 0; i < c1.network.getConnections().size(); i++) {
    difference += abs(c1.network.getConnections().get(i).getWeight()-c2.network.getConnections().get(i).getWeight())/2;
  }
  difference /= c1.network.getConnections().size();
  return 1-difference;
}

public float computePercentIdentical(Creature c1, Creature c2) {
  int numDifferences = 0;
  for(int i = 0; i < c1.network.getConnections().size(); i++) {
    if(abs(c1.network.getConnections().get(i).getWeight()-c2.network.getConnections().get(i).getWeight()) > .001f) {
      numDifferences++;
    }
  }
  return 1-PApplet.parseFloat(numDifferences)/c1.network.getConnections().size();
}

public Creature importCreature() {
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


class Connection {
  
  float weight;
  Neuron source;
  Neuron terminal;
  
  Connection(Neuron source, Neuron terminal) {
    this(source, terminal, random(-1,1));
  }
  
  Connection(Neuron source, Neuron terminal, float weight) {
    this.source = source;
    this.terminal = terminal;
    this.weight = weight;
  }
  
  public void update() {
    if(source.isFiring()) {
      terminal.addPotential(Neuron.ACTION_POTENTIAL*weight);
    }
  }
  
  public void setWeight(float weight) {
    this.weight = weight;
  }
  
  public float getWeight() {
    return weight;
  }
}
class Creature {
  
 int lifeTime;
 PVector position;
 PVector heading;
 PVector velocity; 
 float angularV;
 static final float IMPULSE = 100.0F;
 float radius;
 static final float MAX_HEALTH = 100;
 float health;
 float radarAmplifier;
 float radarWidth;
  NeuralNetwork network;
  DNA dna;
  
  Neuron eyeLeft;
  Neuron eyeRight;
  //Neuron radar;
  Neuron healthNeuron;
  Neuron velocityNeuron;
  Neuron clockNeuron;
  Neuron turnLeft;
  Neuron turnRight;
  Neuron moveForward;
  //Neuron radarWidthNeuron;
  //Neuron radarAmpNeuron;
  /*
  Neuron inter1;
  Neuron inter2;
  Neuron inter3;
  Neuron inter4;
  Neuron inter5;
  Neuron inter6;
  Connection conn_a1;
  Connection conn_a2;
  Connection conn_a3;
  Connection conn_a4;
  Connection conn_a5;
  Connection conn_a6;
  Connection conn_b1;
  Connection conn_b2;
  Connection conn_b3;
  Connection conn_b4;
  Connection conn_b5;
  Connection conn_b6;
  Connection conn_1a;
  Connection conn_2a;
  Connection conn_3a;
  Connection conn_4a;
  Connection conn_5a;
  Connection conn_6a;
  Connection conn_1b;
  Connection conn_2b;
  Connection conn_3b;
  Connection conn_4b;
  Connection conn_5b;
  Connection conn_6b;
  Connection conn_1c;
  Connection conn_2c;
  Connection conn_3c;
  Connection conn_4c;
  Connection conn_5c;
  Connection conn_6c;
  */
    
    Creature(PVector pos, DNA dna) {
      lifeTime = 0;
      position = pos.get();
      velocity = new PVector(0,0);
      float randAngle = random(TWO_PI);
      heading = new PVector(cos(randAngle), sin(randAngle));
      angularV = 0;
      radius = 10;
      health = MAX_HEALTH;
      radarAmplifier = 1;
      radarWidth = .5f;
      
      this.dna = dna;
      
      eyeLeft = new Neuron("LEFT_EYE");
      eyeRight = new Neuron("RIGHT_EYE");
      //radar = new Neuron();
      healthNeuron = new Neuron("HEALTH");
      velocityNeuron = new Neuron();
      clockNeuron = new Neuron("CLOCK");
      turnLeft = new Neuron("TURN_LEFT");
      turnRight = new Neuron("TURN_RIGHT");
      moveForward = new Neuron("MOVE");
      //radarWidthNeuron = new Neuron();
      //radarAmpNeuron = new Neuron();
      /*
      inter1 = new Neuron();
      inter2 = new Neuron();
      inter3 = new Neuron();
      inter4 = new Neuron();
      inter5 = new Neuron();
      inter6 = new Neuron();
      conn_a1 = new Connection(eyeLeft, inter1);
      conn_a2 = new Connection(eyeLeft, inter2);
      conn_a3 = new Connection(eyeLeft, inter3);
      conn_a4 = new Connection(eyeLeft, inter4);
      conn_a5 = new Connection(eyeLeft, inter5);
      conn_a6 = new Connection(eyeLeft, inter6);
      conn_b1 = new Connection(eyeRight, inter1);
      conn_b2 = new Connection(eyeRight, inter2);
      conn_b3 = new Connection(eyeRight, inter3);
      conn_b4 = new Connection(eyeRight, inter4);
      conn_b5 = new Connection(eyeRight, inter5);
      conn_b6 = new Connection(eyeRight, inter6);
      conn_1a = new Connection(inter1, turnLeft);
      conn_2a = new Connection(inter2, turnLeft);
      conn_3a = new Connection(inter3, turnLeft);
      conn_4a = new Connection(inter4, turnLeft);
      conn_5a = new Connection(inter5, turnLeft);
      conn_6a = new Connection(inter6, turnLeft);
      conn_1b = new Connection(inter1, turnRight);
      conn_2b = new Connection(inter2, turnRight);
      conn_3b = new Connection(inter3, turnRight);
      conn_4b = new Connection(inter4, turnRight);
      conn_5b = new Connection(inter5, turnRight);
      conn_6b = new Connection(inter6, turnRight);
      conn_1c = new Connection(inter1, moveForward);
      conn_2c = new Connection(inter2, moveForward);
      conn_3c = new Connection(inter3, moveForward);
      conn_4c = new Connection(inter4, moveForward);
      conn_5c = new Connection(inter5, moveForward);
      conn_6c = new Connection(inter6, moveForward);
      neurons.add(eyeLeft);
      neurons.add(eyeRight);
      neurons.add(turnLeft);
      neurons.add(turnRight);
      neurons.add(moveForward);
      neurons.add(inter1);
      neurons.add(inter2);
      neurons.add(inter3);
      neurons.add(inter4);
      neurons.add(inter5);
      neurons.add(inter6);
      connections.add(conn_a1);
      connections.add(conn_a2);
      connections.add(conn_a3);
      connections.add(conn_a4);
      connections.add(conn_a5);
      connections.add(conn_a6);
      connections.add(conn_b1);
      connections.add(conn_b2);
      connections.add(conn_b3);
      connections.add(conn_b4);
      connections.add(conn_b5);
      connections.add(conn_b6);
      connections.add(conn_1a);
      connections.add(conn_2a);
      connections.add(conn_3a);
      connections.add(conn_4a);
      connections.add(conn_5a);
      connections.add(conn_6a);
      connections.add(conn_1b);
      connections.add(conn_2b);
      connections.add(conn_3b);
      connections.add(conn_4b);
      connections.add(conn_5b);
      connections.add(conn_6b);
      connections.add(conn_1c);
      connections.add(conn_2c);
      connections.add(conn_3c);
      connections.add(conn_4c);
      connections.add(conn_5c);
      connections.add(conn_6c);
      */
      ArrayList<Neuron> inputs = new ArrayList();
      //inputs.add(radar);
      inputs.add(healthNeuron);
      inputs.add(moveForward);
      inputs.add(turnLeft);
      inputs.add(turnRight);
      inputs.add(eyeLeft);
      inputs.add(eyeRight);
      //inputs.add(radarAmpNeuron);
      //inputs.add(radarWidthNeuron);
      ArrayList<Neuron> outputs = new ArrayList();
      outputs.add(moveForward);
      outputs.add(turnLeft);
      outputs.add(turnRight);
      //outputs.add(radarAmpNeuron);
      //outputs.add(radarWidthNeuron);
      network = createNetwork(inputs, outputs);
      
      if(dna == null) {
        network.assignRandomWeights();
        network.assignRandomStates();
      }
      else {
        network.readDNA(dna);
      }
    }
    
    public void update(World world) {
      lifeTime++;
      float timeStep = world.timeStep;
      position.add(PVector.mult(velocity, timeStep));
      //velocity.sub(PVector.mult(velocity.get(),.1));
      velocity.mult(.97f);
      heading.rotate(angularV*timeStep);
      //angularV -= angularV*.1;
      angularV *= .97f;
      radarAmplifier *= .99f;
      radarWidth += .01f;
      radarAmplifier += .1f;
      radarWidth = constrain(radarWidth, 0.00f, 1.00f);
      for(int i = 0; i < 4; i++) {
      radar(world);
      healthNeuron.addPotential(health/MAX_HEALTH);
      velocityNeuron.addPotential(velocity.mag()/pow(IMPULSE, 1));
      if(clockNeuron.isFiring()) {
        clockNeuron.setCurrentPotential(Neuron.ACTION_POTENTIAL);
      }
      network.update(timeStep);
      if(moveForward.isFiring()) {
        velocity.add(PVector.mult(heading, IMPULSE / pow(radius, 1)));
      }
      if(turnLeft.isFiring()) {
        angularV += IMPULSE / pow(radius, 1);
      }
      if(turnRight.isFiring()) {
        angularV -= IMPULSE / pow(radius, 1);
      }
      /*
      if(radarWidthNeuron.isFiring()) {
        radarWidth -= .1;
      }
      if(radarAmpNeuron.isFiring()) {
        radarAmplifier -= 1;
      }
      */
      }
      health -= .1f;
    }
    
    public void render() {
      noStroke();
      fill(color(100, 200, 200, 100));
      arc(position.x, position.y, 3*radius, 3*radius, heading.heading()-QUARTER_PI, heading.heading()+HALF_PI, PIE);
      fill(color(200, 100, 200, 100));
      arc(position.x, position.y, 3*radius, 3*radius, heading.heading()-HALF_PI, heading.heading()+QUARTER_PI, PIE);
      stroke(0);
      fill(255);
      ellipseMode(RADIUS);
      ellipse(position.x, position.y, radius, radius);
      line(position.x, position.y, position.x+radius*heading.x, position.y+radius*heading.y);
    }
    
    public void radar(World world) {
      
      float signalLeft = 0;
      float signalRight = 0;
      for(Food food : world.foods) {
        
        PVector bearing = new PVector(food.location.x-position.x, food.location.y-position.y);        
        float angle = bearing.heading()-heading.heading();
        if(angle > -(HALF_PI) && angle < QUARTER_PI) {
          signalRight += radius/bearing.mag();
        }
        if(angle > -QUARTER_PI && angle < HALF_PI) {
          signalLeft += radius/bearing.mag();
        }
      }
      eyeLeft.addPotential(Neuron.ACTION_POTENTIAL*signalLeft);
      eyeRight.addPotential(Neuron.ACTION_POTENTIAL*signalRight);
      /*
      float signal = 0;
      for(Food food : world.foods) {
        
        PVector bearing = new PVector(food.location.x-position.x, food.location.y-position.y);        
        float angle = bearing.heading()-heading.heading();
        if(angle > -radarWidth * PI && angle < radarWidth * PI) {
          //signal += exp(-pow(bearing.mag()/radarAmplifier,2));
          if(bearing.mag() < radarAmplifier) {
            signal = 1;
            break;
          }
        }
      }
      radar.addPotential(Neuron.ACTION_POTENTIAL*signal);
      */
      eyeLeft.addPotential(Neuron.ACTION_POTENTIAL*signalLeft);
      eyeRight.addPotential(Neuron.ACTION_POTENTIAL*signalRight);
    }
    
    public Creature reproduce() {
      // asexual reproduction
      if (lifeTime > 1000 && random(1) < 0.004f * health/MAX_HEALTH) {
        // Child is exact copy of single parent
        DNA childDNA = dna.copy();
        // Child DNA can mutate
        childDNA.mutate(0.01f);
        return new Creature(position, childDNA);
      } 
      else {
        return null;
      }
    }
    
    public boolean dead() {
    if (health < 0.0f) {
      return true;
    } 
    else {
      return false;
    }
  }
    
    public NeuralNetwork createNetwork(ArrayList<Neuron> inputs, ArrayList<Neuron> outputs) {
      int numInputs = inputs.size();
      int numOutputs = outputs.size();
      int numInters1 = 10;
      ArrayList<Neuron> inters1 = new ArrayList();
      ArrayList<Neuron> neurons = new ArrayList();
      ArrayList<Connection> connections = new ArrayList(); //size=numInputs*numInters1+numInters1*numOutputs
      
      for(int n = 0; n < numInputs; n++) {
        //Neuron input = new Neuron();
        if(!neurons.contains(inputs.get(n))) {
          neurons.add(inputs.get(n));
        }
        //inputs[n] = input;
      }
      for(int n = 0; n < numOutputs; n++) {
        //Neuron output = new Neuron();
        if(!neurons.contains(outputs.get(n))) {
          neurons.add(outputs.get(n));
        }
        ///outputs[n] = output;
      }
      for(int n = 0; n < numInters1; n++) {
        Neuron inter = new Neuron();
        neurons.add(inter);
        inters1.add(inter);
      }
      
      
      for(int n = 0; n < numInters1; n++) {
        for(int nn = 0; nn < numInputs; nn++) {
          Connection connection = new Connection(inputs.get(nn), inters1.get(n));
          connections.add(connection);
        }
      }
      for(int n = 0; n < numOutputs; n++) {
        for(int nn = 0; nn < numInters1; nn++) {
          Connection connection = new Connection(inters1.get(nn), outputs.get(n));
          connections.add(connection);
        }
      }
      
      return new NeuralNetwork(neurons, connections);
    }
    
  }

// Evolution EcoSystem
// Daniel Shiffman <http://www.shiffman.net>

// Class to describe DNA
// Has more features for two parent mating (not used in this example)

class DNA {

  // The genetic sequence
  float[] genes;
  
  // Constructor (makes a random DNA)
  DNA(int size) {
    // DNA is random floating point values between -1 and 1 (!!)
    genes = new float[size];
    for (int i = 0; i < size; i++) {
      genes[i] = random(-1,1);
    }
  }
  
  DNA(float[] newgenes) {
    genes = newgenes;
  }
  
  public DNA copy() {
    float[] newgenes = new float[genes.length];
    //arraycopy(genes,newgenes);
    // JS mode not supporting arraycopy
    for (int i = 0; i < newgenes.length; i++) {
      newgenes[i] = genes[i];
    }
    
    return new DNA(newgenes);
  }
  
  // CROSSOVER
  // Creates new DNA sequence from two (this & and a partner)
  public DNA crossover(DNA partner) {
    float[] child = new float[genes.length];
    // Pick a midpoint
    int crossover = PApplet.parseInt(random(genes.length));
    // Take "half" from one and "half" from the other
    for (int i = 0; i < genes.length; i++) {
      if (i > crossover) child[i] = genes[i];
      else               child[i] = partner.genes[i];
    }    
    DNA newgenes = new DNA(child);
    return newgenes;
  }
  
  // Based on a mutation probability, picks a new random character in array spots
  public void mutate(float m) {
    for (int i = 0; i < genes.length; i++) {
      if (random(1) < m) {
         genes[i] = random(0,1);
      }
    }
  }
}
class Food {
  
  PVector location;
  float quantity;
  
  Food(PVector l, float q) {
    location = l.get();
    quantity = q;
  }
  
}
class NeuralNetwork {
  
  ArrayList<Neuron> neurons;
  ArrayList<Connection> connections;
  
  NeuralNetwork() {
    neurons = null;
    connections = null;
  }
  
  NeuralNetwork(ArrayList<Neuron> neurons, ArrayList<Connection> connections) {
    this.neurons = neurons;
    this.connections = connections;
  }
  
  public void update(float timeStep) {
    for(Neuron neuron : neurons) {
      neuron.update(timeStep);
    }
    for(Connection connection : connections) {
      connection.update();
    }
  }
  
  public void assignRandomWeights() {
    for(Connection connection : connections) {
      connection.setWeight(random(-1,1));
    }
  }
  
  public void assignRandomStates() {
    for(Neuron neuron : neurons) {
      neuron.setCurrentPotential(Neuron.ACTION_POTENTIAL*random(-1,1));
    }
  }
  
  public void readDNA(DNA dna) {
    for(int i = 0; i < dna.genes.length; i++) {
      connections.get(i).setWeight(dna.genes[i]);
    }
  }
  
  public void setNeurons(ArrayList<Neuron> neurons) {
    this.neurons = neurons;
  }
  
  public ArrayList<Neuron> getNeurons() {
    return neurons;
  }
  
  public void setConnections(ArrayList<Connection> connections) {
    this.connections = connections;
  }
  
  public ArrayList<Connection> getConnections() {
    return connections;
  }
}
class Neuron {
  
  static final float ACTION_POTENTIAL = 1;
  static final float REFRACTORY_PERIOD = 0;
  float thresholdPotential = 1;
  float currentPotential;
  float refractoryTime;
  boolean firing;
  String name;
  
  Neuron(String name) {
    currentPotential = 0;
    firing = false;
    this.name = name;
  }
  
  Neuron() {
    this("n/a");
  }
  
  public void update(float timeStep) {
    if(firing) {
      firing = false;
      refractoryTime = 0;
    }
    else if(refractoryTime < REFRACTORY_PERIOD) {
      refractoryTime += timeStep;
    }
    else if(currentPotential >= thresholdPotential) {
      firing = true;
      currentPotential = -thresholdPotential;
    }
  }
  
  public void setFiring(boolean firing) {
    this.firing = firing;
  }
  
  public boolean isFiring() {
    return firing;
  }
  
  public void setCurrentPotential(float potential) {
    currentPotential = potential;
  }
  
  public float getCurrentPotential() {
    return currentPotential;
  }
  
  public void addPotential(float potential) {
    currentPotential += potential;
  }
}


class World {

  long steps;
  int deaths;
  ArrayList<Creature> creatures;    
  ArrayList<Food> foods;
  float timeStep;
  int w;
  int h;
  int bestFitness;
  float avgFitness;
  Creature fittest;
  ArrayList<Creature> matingPool;
  int foodCap;
  int populationCap;

  World(int w, int h, float timeStep, int populationCap, int foodCap) {
    steps = 0;
    deaths = 0;
    
    foods = new ArrayList();
    creatures = new ArrayList<Creature>();              // Initialize the arraylist
    
    this.timeStep = timeStep;
    this.w = w;
    this.h = h;
    bestFitness = 1000;
    avgFitness = 1000;
    fittest = null;
    matingPool = new ArrayList();
    this.foodCap = foodCap;
    this.populationCap = populationCap;
  }

  // Make a new creature
  

  // Run the world
  public void run() {
    steps++;
    stroke(color(200, 100, 0));
    fill(color(200, 100, 0));
    for(Food food : foods) {
      rect(food.location.x, food.location.y, 2, 2);
    }
    // Cycle through the ArrayList backwards b/c we are deleting
    for (int i = creatures.size()-1; i >= 0; i--) {
      // All bloops run and eat
      Creature b = creatures.get(i);
      b.update(this);
      b.render();
      for(int ii = foods.size() - 1; ii >= 0; ii--) {
        Food food = foods.get(ii);
        if(PVector.dist(food.location, b.position) < b.radius) {
          b.health += food.quantity;
          foods.remove(ii);
        }
      }
      // If it's dead, kill it and make food
      
      if (b.dead()) {
        deaths++;
        if(b.lifeTime > bestFitness) {
          bestFitness = b.lifeTime;
          fittest = b;
        }
        avgFitness = avgFitness*.9f + b.lifeTime*.1f;
        creatures.remove(i);
        //foods.add(new Food(b.position, 19));
      }
      // Perhaps this bloop would like to make a baby?
      if(creatures.size() < populationCap) {
      Creature child = b.reproduce();
      if (child != null) creatures.add(child);
      
    if(world.matingPool.size() > 0) {
      world.reproduction();
      //println("sexualreproduction");
    }
    else {
      world.creatures.add(new Creature(new PVector(random(width), random(height)), new DNA(dnaLength)));
    }
      }
      
  
  
      float fitnessNormal = map(b.lifeTime, 1000, avgFitness, 0, 1); 
      if(fitnessNormal > 0 && random(1) < .01f) {
        matingPool.add(b);
        
        //println("added@" + steps);
        if(random(matingPool.size()) > 100) {
          int x = PApplet.parseInt(random(matingPool.size()));
        if(matingPool.get(x).lifeTime < b.lifeTime) {
          matingPool.remove(x);
        }
        }
      }
      
      
      if(b.position.x > w) {
        b.position.x = w;
      }
      if(b.position.x < 0) {
        b.position.x = 0;
      }
      if(b.position.y > h) {
        b.position.y = h;
      }
      if(b.position.y < 0) {
        b.position.y = 0;
      }
  }
    if(foods.size() < foodCap) {
      foods.add(new Food(new PVector(random(w),random(h)),50));
    }
  }
  
  public void reproduction() {
    // Sping the wheel of fortune to pick two parents
    int m = PApplet.parseInt(random(matingPool.size()));
    int d = PApplet.parseInt(random(matingPool.size()));
    // Pick two parents
    Creature mom = matingPool.get(m);
    Creature dad = matingPool.get(d);
    // Get their genes
    DNA momgenes = mom.dna;
    DNA dadgenes = dad.dna;
    // Mate their genes
    DNA childgenes = momgenes.crossover(dadgenes);
    // Mutate their genes
    childgenes.mutate(.01f);
    // Fill the new population with the new child
    PVector position = new PVector(random(width), random(height));
    Creature spawn = new Creature(position, childgenes);
    creatures.add(spawn);
  }
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "NeuralNetworkEvolve" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
