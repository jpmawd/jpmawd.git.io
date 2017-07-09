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
      radarWidth = .5;
      
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
    
    void update(World world) {
      lifeTime++;
      float timeStep = world.timeStep;
      position.add(PVector.mult(velocity, timeStep));
      //velocity.sub(PVector.mult(velocity.get(),.1));
      velocity.mult(.97);
      heading.rotate(angularV*timeStep);
      //angularV -= angularV*.1;
      angularV *= .97;
      radarAmplifier *= .99;
      radarWidth += .01;
      radarAmplifier += .1;
      radarWidth = constrain(radarWidth, 0.00, 1.00);
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
      health -= .1;
    }
    
    void render() {
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
    
    void radar(World world) {
      
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
    
    Creature reproduce() {
      // asexual reproduction
      if (lifeTime > 1000 && random(1) < 0.004 * health/MAX_HEALTH) {
        // Child is exact copy of single parent
        DNA childDNA = dna.copy();
        // Child DNA can mutate
        childDNA.mutate(0.01);
        return new Creature(position, childDNA);
      } 
      else {
        return null;
      }
    }
    
    boolean dead() {
    if (health < 0.0) {
      return true;
    } 
    else {
      return false;
    }
  }
    
    NeuralNetwork createNetwork(ArrayList<Neuron> inputs, ArrayList<Neuron> outputs) {
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

