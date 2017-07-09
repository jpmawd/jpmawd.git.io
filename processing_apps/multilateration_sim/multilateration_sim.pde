ArrayList<Sensor> sensors;
Beacon tx;
Movable selected;
SignalGraphics sg;
PVector mousePos;
int W = 400;
int H = 400;
float scale = 1;

/** Simulation Parameters **/
double c = 3e8;
double f0 = 1e5;
double fc = 500e6;

void mouseClicked() {
  if(selected == null) {
    selected = grabMovable(mousePos);
    if(selected == null) {
      sensors.add(new Sensor(mousePos));
    }
  }
  else {
    selected = null;
  }
}

void keyPressed() {
  if(key == ENTER) {
    generateDelays();
  }
  else if((key == DELETE || key == BACKSPACE) && selected instanceof Sensor) {
    removeSensor((Sensor)selected);
  }
  else if(key == 'i') {
    scale -= 1;
    scale = max(1,scale);
  }
  else if(key == 'o') {
    scale += 1;
    scale = min(10,scale);
  }
}

void setup() {
  size(100,100);
  getSurface().setSize(W,H);
  getSurface().setResizable(true);
  sensors = new ArrayList<Sensor>();
  tx = new Beacon(0,0);
  mousePos = new PVector(0,0);
  sg = new SignalGraphics();
}

void plotHyperbola(PVector p1, PVector p2,float dd) {
    // Calculate vector between stations
    PVector delta = PVector.sub(p1,p2);
    
    // Express in polar form
    float phi = atan2(delta.y,delta.x);
    float r = delta.mag();
    
    // Split the vector between stations such that the distance differs by dd
    float rd = (r+dd)/2;        
    
    // Radius rd gives distance between hyperbola focus and transverse 
    // axis crossing. Radius r give the distance between foci. Use this
    // to compute hyperbola parameters a,b,c and e. 
    float a = (r/2)-rd;
    float e = r/(2*a);
    float c = a*e;
    float b = sqrt(sq(c)-sq(a));
    
    /* Use parametric equation for the hyperbola in terms of a, b and 
     parameter mu. [x,y] is initially calculated for a hyperbola whose
     transverse axis is the x-axis and whose foci are +/- c. The foci
     is returned to the origin by subtracting c, the hyperbola is rotated
     such that the transverse axis lies along vector delta joining the
     stations, and finally the hyperbola is translated such that the 
     foci lies on the position of station 1. */
    PVector[] points = new PVector[500];
    for(int k = 0; k < 500; k++) {
        points[k] = new PVector();
        float mu = (k-250)/50F;      
        points[k].x = (float)(a*Math.cosh(mu));
        points[k].y = (float)(b*Math.sinh(mu));      
        points[k].x = points[k].x-c;
        //y(k) = y(k);                
        points[k].rotate(phi);
        points[k].x = points[k].x + p1.x;
        points[k].y = points[k].y + p1.y;
    }
    beginShape(LINES);
      for (int i = 0; i < points.length - 1; i += 2) {
        vertex(points[i].x, points[i].y);
        vertex(points[i+1].x, points[i+1].y);
      }
    endShape();
}

void draw() {
  W = width;
  H = height;
  clear();
  mousePos.set(scale*(mouseX-W/2),-scale*(mouseY-H/2));
  if(selected != null) {
    selected.setPos(mousePos);
  }
  pushMatrix();
  translate(W/2,H/2);
  scale(1/scale,-1/scale);
  sg.plot();
  tx.plot();
  for(Sensor s : sensors) {
    s.plot();
    if(s.equals(selected)) {
      for (Sensor s2 : sensors) {
        noFill();
        stroke(255);
        for(int m = 0; m <= (int)((sqrt(sq(W)+sq(H))/2)/(c/f0)); m++) {
          stroke(255,255,255,255/(m+1));
          plotHyperbola(s.pos,s2.pos,s.pos.dist(tx.pos)-s2.pos.dist(tx.pos)-m*(float)(c/f0));
        }  
      }
    }
  }
  popMatrix();
  fill(255);
  text("# Sensors = " + sensors.size() + "\n"
     + "Tone = " + f0/1000F + " KHz" + "\n"
     + "Carrier = " + fc/1000000F + " MHz" + "\n"
     ,20,20);
     text(mousePos.toString(),mouseX,mouseY);
     if(selected != null) {
       if(selected.equals(tx)) {
         
       }
       else {
         text("TOA="+selected.pos.dist(tx.pos)/c,mouseX,mouseY+20);
       }
     }
}

Movable grabMovable(PVector pos) {
  if(tx.pos.dist(pos) < 10*scale) {
      return tx;
    }
  for(Sensor s : sensors) {
    if(s.pos.dist(pos) < 10*scale) {
      return s;
    }
  }
  return null;
}

void removeSensor(Sensor sensor) {
  sensors.remove(sensor);
  sensor = null;
}

void generateDelays() {
  String data_matrix = "sensor_coords=[";
  for(Sensor s : sensors) {
    float delay = (float) (s.pos.dist(tx.pos)/(c));
    data_matrix += "[" + s.pos.x + ";" + s.pos.y + ";" + "0],";
  }
  data_matrix += "];tx_coords=[" + tx.pos.x + ";" + tx.pos.y + ";" + "0;];";
  println(data_matrix);
}

abstract class Movable {
  PVector pos;
  
  public Movable(float x_param, float y_param) {
    pos = new PVector(x_param,y_param);
  }
  public Movable(PVector pos_param) {
    pos = pos_param.copy();
  }
  
  void setPos(PVector pos_param) {
    pos = pos_param.copy();
  }
  
  abstract void plot();
}

class Sensor extends Movable {
  
  public Sensor(float x_param, float y_param) {
    super(x_param,y_param);
  }
  public Sensor(PVector pos_param) {
    super(pos_param);
  }
  
  void plot() {
    fill(color(200,200,200));
    stroke(color(100,100,100));
    rectMode(RADIUS);
    rect(pos.x,pos.y,5,5);
    if(this.equals(selected)) {
      fill(255);
      stroke(255);
      line(pos.x,pos.y,tx.pos.x,tx.pos.y);
    }
  }
}

class Beacon extends Movable {
  
  public Beacon(float x_param, float y_param) {
    super(x_param, y_param);
  }
  public Beacon(PVector pos_param) {
    super(pos_param);
  }
  
  void plot() {
    fill(color(200,200,200));
    stroke(color(100,100,100));
    ellipseMode(RADIUS);
    ellipse(pos.x,pos.y,10,5);
  }
}

class SignalGraphics {
  
  int counter = 0;
  boolean showNoise = false;
  boolean showDecay = false;
  boolean showTone = true;
  boolean showCarrier = false;
  
  void plot() {
    if(showCarrier) {
      plotCarrier();
    }
    if(showTone) {
      plotTone();
    }
    counter++;
    if(counter >= sqrt(sq(W)+sq(H))) {
      counter = 0;
    }
  }
  
  void plotCarrier() {
    noFill();
    stroke(color(255*cos((float)(TWO_PI*fc/c*scale*counter)),-255*cos((float)(TWO_PI*fc/c*scale*counter)),0));
    ellipseMode(RADIUS);
    ellipse(tx.pos.x,tx.pos.y,counter,counter);
  }
  
  void plotTone() {
    noFill();
    stroke(color(255*cos((float)(TWO_PI*f0/c*scale*counter)),-255*cos((float)(TWO_PI*f0/c*scale*counter)),0));
    ellipseMode(RADIUS);
    ellipse(tx.pos.x,tx.pos.y,(float)(counter*scale),(float)(counter*scale));
  }
}