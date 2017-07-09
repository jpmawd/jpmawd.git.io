

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
  void run() {
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
        avgFitness = avgFitness*.9 + b.lifeTime*.1;
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
      if(fitnessNormal > 0 && random(1) < .01) {
        matingPool.add(b);
        
        //println("added@" + steps);
        if(random(matingPool.size()) > 100) {
          int x = int(random(matingPool.size()));
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
  
  void reproduction() {
    // Sping the wheel of fortune to pick two parents
    int m = int(random(matingPool.size()));
    int d = int(random(matingPool.size()));
    // Pick two parents
    Creature mom = matingPool.get(m);
    Creature dad = matingPool.get(d);
    // Get their genes
    DNA momgenes = mom.dna;
    DNA dadgenes = dad.dna;
    // Mate their genes
    DNA childgenes = momgenes.crossover(dadgenes);
    // Mutate their genes
    childgenes.mutate(.01);
    // Fill the new population with the new child
    PVector position = new PVector(random(width), random(height));
    Creature spawn = new Creature(position, childgenes);
    creatures.add(spawn);
  }
}

