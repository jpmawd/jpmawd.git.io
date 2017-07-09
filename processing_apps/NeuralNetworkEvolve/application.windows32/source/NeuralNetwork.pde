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
  
  void update(float timeStep) {
    for(Neuron neuron : neurons) {
      neuron.update(timeStep);
    }
    for(Connection connection : connections) {
      connection.update();
    }
  }
  
  void assignRandomWeights() {
    for(Connection connection : connections) {
      connection.setWeight(random(-1,1));
    }
  }
  
  void assignRandomStates() {
    for(Neuron neuron : neurons) {
      neuron.setCurrentPotential(Neuron.ACTION_POTENTIAL*random(-1,1));
    }
  }
  
  void readDNA(DNA dna) {
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
