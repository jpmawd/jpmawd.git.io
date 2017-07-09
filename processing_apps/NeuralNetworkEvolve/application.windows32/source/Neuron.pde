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
  
  void update(float timeStep) {
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
