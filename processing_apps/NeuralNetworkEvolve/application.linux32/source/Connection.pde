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
  
  void update() {
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
