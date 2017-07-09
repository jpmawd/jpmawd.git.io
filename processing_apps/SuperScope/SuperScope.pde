import processing.serial.*;
Serial port;

String serialdata = "";
String[] serialsplit = {
  "", ""
};
float lastedit = 0;
float editincrement = 1;
int keylast;
int lastX;
int lastY;
int currentX;
int currentY;
int dmouseX;
int dmouseY;
int tolerance = 0;
int particleslastgenerate = 0;
int particleslastupdate = 0;
boolean mousestatelast;
boolean holding = false;
boolean editing = false;
boolean overduino = false;
int arduinoalpha = 0;
color graphpointcolor = color(0, 255, 150);
float graphdomain = 1;
float graphrange = 1;
float desval = 0;
float phasortime = 0;
float phasortimecoefficient = -1;
float[] phasorthetas = new float[5];
float[] phasormagnitudes = new float[5];
color[] phasorcolors = new color[5];
String[] phasorlabels = new String[5];
String desunits = "NONE";
String mode = "Low-Pass Filter (RC)";
String modelast = "NONE";

PImage Arduino;
PFont font;
PFont font2;
Button modeswitch;
Button addcap;
Button addind;
Button addres;
Button addacs;
Button decreasedomain;
Button increasedomain;
Button decreaserange;
Button increaserange;
Button increasephasortimecoefficient;
Button decreasephasortimecoefficient;
Button graphicaldisplay;
Capacitor capacitor1;
Capacitor capacitor2;
Inductor inductor1;
ACSource ac1;
Resistor resistor1;
ArrayList particles;
ArrayList capacitors;
ArrayList inductors;
ArrayList resistors;
ArrayList acsources;
ArrayList schemwires;
ArrayList schemouts;
ArrayList schemgrounds;
ArrayList schemcapacitors;
ArrayList scheminductors;
ArrayList schemresistors;
ArrayList schemacsources;
//SchemInd sind1;
//SchemCap scap1;
//SchemRes sres1;
//SchemACS sacs1;
//SchemWire swire1;
//SchemOUT sout1;

boolean noArduino;

void setup() {
  size(800, 500);
  try {
    port = new Serial(this, "COM4", 9600);
  }
  catch(Exception e) {noArduino = true;}
  if(!noArduino) port.bufferUntil('\n');
  Arduino = loadImage("arduino_icon.png");
  Arduino.resize(0, 50);
  font = loadFont("EurostileBold-20.vlw");
  font2 = loadFont("EurostileRegular-12.vlw"); 
  textFont(font); 

  modeswitch = new Button(color(150, 100, 0), 200, 400, 100, 30, "NextCircuit");
  addcap = new Button(color(0, 0, 120), 100, 15, 30, 30, "C");
  addind = new Button(color(200, 100, 0), 200, 15, 30, 30, "L");
  addres = new Button(color(200, 0, 0), 300, 15, 30, 30, "R");
  addacs = new Button(color(0, 200, 100), 400, 15, 30, 30, "AC");
  decreasedomain = new Button(color(0, 255, 150), 550, 340, 90, 30, " -Domain");
  increasedomain = new Button(color(0, 255, 150), 640, 340, 90, 30, " +Domain");
  decreaserange = new Button(color(0, 255, 150), 550, 370, 90, 30, " -Range");
  increaserange = new Button(color(0, 255, 150), 640, 370, 90, 30, " +Range");
  increasephasortimecoefficient = new Button(color(0, 255, 150), 640, 370, 90, 30, " +Omega");
  decreasephasortimecoefficient = new Button(color(0, 255, 150), 550, 370, 90, 30, " -Omega");
  graphicaldisplay = new Button(color(0, 255, 125), 550, 15, 90, 30, "PLOT");
  capacitor1 = new Capacitor(color(0, 175, 255), 85, 60, .001); 
  capacitor2 = new Capacitor(color(0, 0, 255), 200, 10, .002);
  inductor1 = new Inductor(color(255, 125, 0), 200, 60, .001);
  ac1 = new ACSource(color(200, 200, 200), 400, 60, 1000);
  resistor1 = new Resistor(color(255, 0, 0), 300, 60, 100);
  particles = new ArrayList();
  capacitors = new ArrayList();
  inductors = new ArrayList();
  resistors = new ArrayList();
  acsources = new ArrayList();
  schemwires = new ArrayList();
  schemouts = new ArrayList();
  schemgrounds = new ArrayList();
  schemcapacitors = new ArrayList();
  scheminductors = new ArrayList();
  schemresistors = new ArrayList();
  schemacsources = new ArrayList();
  capacitors.add((Capacitor) capacitor1);
  inductors.add((Inductor) inductor1);
  resistors.add((Resistor) resistor1);
  acsources.add((ACSource) ac1);
  //sind1 = new SchemInd(200, 200, 1);
  //scap1 = new SchemCap(300, 300, 1);
  //sres1 = new SchemRes(300, 200, 1);
  //sacs1 = new SchemACS(400, 300, 1);
  //swire1 = new SchemWire(100, 200, 100, 250, 1);
  //sout1 = new SchemOUT(100, 100, 200, 200, 1);
  //scheminductors.add((SchemInd) sind1);
  //schemcapacitors.add((SchemCap) scap1);
  //schemresistors.add((SchemRes) sres1);
  //schemacsources.add((SchemACS) sacs1);
  //schemwires.add((SchemWire) swire1);
}

void draw() {
  background(255);
  color(0);
  //text(serialsplit[0], 100, 100);
  serialdata = "";
  strokeWeight(1);
  keylast = 0;
  if (keyPressed) keylast = key;
  displayAll();
  updateAll();
  currentX = mouseX;
  currentY = mouseY;
  dmouseX = (currentX - lastX); 
  dmouseY = (currentY - lastY);
  lastX = mouseX;
  lastY = mouseY;
  modeswitch.display();
  addcap.display();
  addind.display();
  addres.display();
  addacs.display();
  if (modeswitch.isPressed() && !mousestatelast) {
    if (mode == "Low-Pass Filter (RC)") mode = "High-Pass Filter (RC)";
  else if (mode=="High-Pass Filter (RC)") {
      mode = "Low-Pass Filter (RL)";
  }
  else if (mode=="Low-Pass Filter (RL)") {
    mode = "High-Pass Filter (RL)";
  }
  else if (mode=="High-Pass Filter (RL)") {
    mode = "Band-Reject (Notch): Driven LC Series";
  }
  else if (mode=="Band-Reject (Notch): Driven LC Series") {
    mode = "Band-Pass: Driven LC Parallel";
  }
  else if (mode=="Band-Pass: Driven LC Parallel") {
    mode = "Resonance (LC Tank)";
  }
  else if (mode=="Resonance (LC Tank)") {
    mode = "Dual Resonant Air-Core Step-Up Transformer (Tesla Coil)";
  }
  else if (mode=="Dual Resonant Air-Core Step-Up Transformer (Tesla Coil)") {
    mode = "Low-Pass Filter (RC)";
  }
  }
  if (addcap.isPressed() && !mousestatelast) capacitors.add(new Capacitor(color(random(0, 100), random(0, 100), 55+random(0, 200)), 85, 60, .001));
  if (addind.isPressed() && !mousestatelast) inductors.add(new Inductor(color(200+random(-50, 50), 100+random(-50, 50), 0), 200, 60, .001));
  if (addres.isPressed() && !mousestatelast) resistors.add(new Resistor(color(100+random(0, 100), random(0, 50), 0), 300, 60, .001));
  if (addacs.isPressed() && !mousestatelast) acsources.add(new ACSource(color(50+random(0, 200), 50+random(0, 200), 50+random(0, 200)), 400, 60, .001));
  graphicaldisplay.display();
  if (graphicaldisplay.isPressed() && !mousestatelast) {
    if (graphicaldisplay.symbol == "PLOT") graphicaldisplay.symbol = "VECTOR";
    else if (graphicaldisplay.symbol == "VECTOR") graphicaldisplay.symbol = "PHASOR";
    else if (graphicaldisplay.symbol == "PHASOR") graphicaldisplay.symbol = "PLOT";
  }
  if (graphicaldisplay.symbol == "PHASOR") {
    phasortime += .000000001;
  }
  else phasortime = 0;
  if (graphicaldisplay.symbol == "PLOT") {
    decreasedomain.display();
    increasedomain.display();
    decreaserange.display();
    increaserange.display();
    if (decreasedomain.isPressed() && !mousestatelast) graphdomain = (graphdomain * .1);
    if (increasedomain.isPressed() && !mousestatelast) graphdomain = (graphdomain * 10);
    if (decreaserange.isPressed() && !mousestatelast) graphrange = (graphrange * .1);
    if (increaserange.isPressed() && !mousestatelast) graphrange = (graphrange * 10);
  }
  if (graphicaldisplay.symbol == "PHASOR") {
    decreasephasortimecoefficient.display();
    increasephasortimecoefficient.display();
    if (decreasephasortimecoefficient.isPressed() && !mousestatelast) phasortimecoefficient *= .1;
    if (increasephasortimecoefficient.isPressed() && !mousestatelast) phasortimecoefficient *= 10;
  }
  if (!holding) {
    desval = 0;
    desunits = "NONE";
  }
  description();
  updateSchematic();
  if(overduino) {
    arduinoalpha += 10;
    if(arduinoalpha>250) arduinoalpha = 0;
  }
  else {
    arduinoalpha = 255;
  }
  tint(255, arduinoalpha);
  image(Arduino, 0, 200);
  overduino = false;
  mouseStateUpdate(mousePressed);
}

void serialEvent(Serial port) {
  if(noArduino) return;
  serialdata = port.readStringUntil('\n');
  serialsplit = (String[])split(serialdata, '_');
}
boolean overArduino(float x, float y) {
  if ((sqrt(sq(25-x)+sq(225-y)))<30) {
    overduino = true;
    return true;
  }
  else return false;
}
void requestImport(String units) {
  if(noArduino) return;
  port.write("request"+units);
  port.write('\n');
}
class Button {

  boolean pressed = false;
  int lastclicktime = 0;
  float xpos;
  float ypos;
  float widthv;
  float heightv;
  float leftbound;
  float rightbound;
  float upperbound;
  float lowerbound;
  color colorv;
  String symbol;

  Button(color Tempcolorv, float Tempxpos, float Tempypos, float Tempwidthv, float Tempheightv, String Tempsymbol) {
    xpos = Tempxpos;
    ypos = Tempypos;
    widthv = Tempwidthv;
    heightv = Tempheightv;
    leftbound = xpos - widthv/2;
    rightbound = xpos + widthv/2;
    upperbound = ypos - heightv/2;
    lowerbound = ypos + heightv/2;
    colorv = Tempcolorv;
    symbol = Tempsymbol;
  }

  void display() {
    stroke(0);
    fill(colorv);
    rectMode(CENTER);
    rect(xpos, ypos, widthv, heightv);
    fill(255);
    textFont(font);
    text(symbol, leftbound, ypos);
  }

  boolean isMouseOver() {
    if (mouseX > leftbound && mouseX < rightbound
      && mouseY > upperbound && mouseY < lowerbound) {
      return true;
    }
    else return false;
  }
  boolean isPressed() {
    if (mousePressed && isMouseOver()) {
      if (pressed) return true;
      else if (mousePressed && mousestatelast == false) {
        lastclicktime = millis()/100;
        pressed = true;
        return true;
      }
      else return false;
    }
    else return false;
  }
}

class Element { 
  String units;
  float value;
  color c;
  float xpos;
  float ypos;
  float centerx;
  float centery;
  int area = 50;
  boolean held;
  boolean edit = false;
  boolean applied;
  boolean isfinished = false;



  void update() {
    if ((centerx > 50) && (centerx < 70) && (centery > 450) && (centery < 480)) {
      isfinished = true;
      held = false;
      holding = false;
    }
    if (held && !mousePressed)
    {
      holding = false;
    }
    if (edit && (keylast == 32)) {

      editing = false;
    }
    if (held && mousePressed) 
    {

      centerx = mouseX;
      centery = mouseY;
      if (!overArduino(centerx, centery)) {
        generateSparks(0, .7, .04, .04, 3, color(0, 245, 245));
      }
      else {
        generateSparks(random(0, 1), random(-1, 1), .10, .10, 3, color(0, 245, 245));
        //if (serialsplit[0] == units) {
          if (units == "Farads" || units == "Henrys") value = float(serialsplit[0])/1000000000;
          else value = float(serialsplit[0]);
        //}
        requestImport(units);
      }
    }
    else 
    {
      held = false;
    }

    if (edit) {
      desunits = units;
      desval = value;
      description();
      stroke(color(0, 255, 55+((sin(float(millis())/1000)+1)*.5)*200));
      noFill();
      ellipse(centerx, centery, area, area);
      ellipse(centerx, centery, 
      (area + (3*sin(millis()/100))), 
      (area + (3*cos(millis()/100))));
    }

    if (edit && (lastedit < ((millis()/10) - 10))) {
      if (keyPressed) {
        if (keylast == 100) {
          value += editincrement;
        }
        if (keylast == 97) {
          value -= editincrement;
        }
        if (lastedit < ((millis()/10) - 20)) {
          if (keylast == 119) {
            editincrement = (editincrement*10);
          }
          if (keylast == 115) {
            editincrement = (editincrement/10);
          }
        }
        lastedit = (millis()/10);
      }
    }


    if ( mouseX > ((centerx - 15) - tolerance)
      && mouseX < ((centerx + 15) + tolerance)
      && mouseY > ((centery - 20) - tolerance)
      && mouseY < ((centery + 20) + tolerance)
      && !held)
    { 
      noFill();
      if (true) {
        if (keylast == 32) {
          editing = true;
          edit = true;
        }
      }
      if (edit) stroke(color(0, 255, 55+((sin(float(millis())/1000)+1)*.5)*200));
      else stroke(color(0, 0, 0));
      ellipse(centerx, centery, area, area);
      ellipse(centerx, centery, 
      (area + (3*sin(millis()/100))), 
      (area + (3*cos(millis()/100))));
      if (mousePressed && mousestatelast == false) {
        if (holding == false) {
          holding = true;
          held = true;
          desval = value;
          desunits = units;
        }
      }
    }
    else {
      if (edit && keylast == 32) {
        editing = false;
        edit = false;
      }
    }
  }
}








class Capacitor extends Element {

  Capacitor(color tempC, float tempXpos, float tempYpos, float tempValue) {
    units = "Farads";
    value = tempValue;
    c = tempC;
    xpos = tempXpos;
    ypos = tempYpos;
    centerx = xpos + 10;
    centery = ypos;
  }




  void display() {
    //centerx = xpos + 10;
    //centery = ypos;
    stroke(0);
    fill(c);
    rectMode(CENTER);
    rect(centerx - 5, centery, 5, 30);
    rect(centerx - 5 + 15, centery, 5, 30);
  }
}

class Inductor extends Element {

  Inductor(color tempC, float tempXpos, float tempYpos, float tempValue) {
    units = "Henrys";
    value = tempValue;
    c = tempC;
    xpos = tempXpos;
    ypos = tempYpos;
    centerx = xpos;
    centery = ypos;
  }




  void display() {
    stroke(0);
    fill(c);
    //beginShape();
    /* curveVertex(centerx - 20, centery - 10);
     curveVertex(centerx - 15, centery);
     curveVertex(centerx - 10, centery - 10);
     curveVertex(centerx - 5, centery);
     curveVertex(centerx, centery - 10);
     curveVertex(centerx + 5, centery);
     curveVertex(centerx + 10, centery - 10);
     curveVertex(centerx + 15, centery);
     curveVertex(centerx + 15, centery + 10);
     curveVertex(centerx + 10, centery);
     curveVertex(centerx + 5, centery + 10);
     curveVertex(centerx, centery);
     curveVertex(centerx - 5, centery + 10);
     curveVertex(centerx - 10, centery);
     curveVertex(centerx - 15, centery + 10);
     curveVertex(centerx - 20, centery);
     */
    for (int i = 0; i < 3; i++) {
      fill(c);
      arc(centerx + (-10 + (10 * i)), centery + 5, 10, 40, PI, TWO_PI);
      noFill();
      arc(centerx + (-10 + (10 * i)), centery + 5, 5, 30, PI, TWO_PI);
    }
    //endShape(CLOSE);
  }
}

class Resistor extends Element {

  Resistor(color tempC, float tempXpos, float tempYpos, float tempValue) {
    units = "Ohms";
    value = tempValue;
    c = tempC;
    xpos = tempXpos;
    ypos = tempYpos;
    centerx = xpos;
    centery = ypos;
  }




  void display() {
    stroke(0);
    fill(c);
    beginShape();
    curveVertex(centerx - 20, centery - 10);
    curveVertex(centerx - 15, centery);
    curveVertex(centerx - 10, centery - 10);
    curveVertex(centerx - 5, centery);
    curveVertex(centerx, centery - 10);
    curveVertex(centerx + 5, centery);
    curveVertex(centerx + 10, centery - 10);
    curveVertex(centerx + 15, centery);
    curveVertex(centerx + 15, centery + 10);
    curveVertex(centerx + 10, centery);
    curveVertex(centerx + 5, centery + 10);
    curveVertex(centerx, centery);
    curveVertex(centerx - 5, centery + 10);
    curveVertex(centerx - 10, centery);
    curveVertex(centerx - 15, centery + 10);
    curveVertex(centerx - 20, centery);
    endShape(CLOSE);
  }
}

class ACSource extends Element {

  ACSource(color tempC, float tempXpos, float tempYpos, float tempValue) {
    units = "Hertz";
    value = tempValue;
    c = tempC;
    xpos = tempXpos;
    ypos = tempYpos;
    centerx = xpos;
    centery = ypos;
  }




  void display() {
    //centerx = xpos + 10;
    //centery = ypos;
    stroke(0);
    fill(c);
    ellipse(centerx, centery, 30, 30);
    for (int i = 0; i < 20; i++) {
      /*set(-10 + i + int(centerx), int(((sin((i*TWO_PI)/20) * -10) + int(centery))), color(255*(-1*sin((i*TWO_PI)/20) + 1), 0, 255*(sin((i*TWO_PI)/20) + 1)));
       set(-10 + i + int(centerx), int(((sin((i*TWO_PI)/20) * -10) + int(centery) - 1)), color(255*(-1*sin((i*TWO_PI)/20) + 1), 0, 255*(sin((i*TWO_PI)/20) + 1)));
       set(-10 + i + int(centerx), int(((sin((i*TWO_PI)/20) * -10) + int(centery) + 1)), color(255*(-1*sin((i*TWO_PI)/20) + 1), 0, 255*(sin((i*TWO_PI)/20) + 1)));
       set(-10 + i + int(centerx), int(((sin((i*TWO_PI)/20) * -10) + int(centery) - 2)), color(255*(-1*sin((i*TWO_PI)/20) + 1), 0, 255*(sin((i*TWO_PI)/20) + 1)));
       set(-10 + i + int(centerx), int(((sin((i*TWO_PI)/20) * -10) + int(centery) + 2)), color(255*(-1*sin((i*TWO_PI)/20) + 1), 0, 255*(sin((i*TWO_PI)/20) + 1)));
       set(-10 + i + int(centerx), int(((sin((i*TWO_PI)/20) * -10) + int(centery) - 3)), color(255*(-1*sin((i*TWO_PI)/20) + 1), 0, 255*(sin((i*TWO_PI)/20) + 1)));
       set(-10 + i + int(centerx), int(((sin((i*TWO_PI)/20) * -10) + int(centery) + 3)), color(255*(-1*sin((i*TWO_PI)/20) + 1), 0, 255*(sin((i*TWO_PI)/20) + 1)));
       */
      set(-10 + i + int(centerx), int(((sin((i*TWO_PI)/20) * -10) + int(centery))), color(0, 255, 125));
      set(-10 + i + int(centerx), int(((sin((i*TWO_PI)/20) * -10) + int(centery) - 1)), color(0, 255, 125));
      set(-10 + i + int(centerx), int(((sin((i*TWO_PI)/20) * -10) + int(centery) + 1)), color(0, 255, 125));
      set(-10 + i + int(centerx), int(((sin((i*TWO_PI)/20) * -10) + int(centery) - 2)), color(0, 255, 125));
      set(-10 + i + int(centerx), int(((sin((i*TWO_PI)/20) * -10) + int(centery) + 2)), color(0, 255, 125));
      set(-10 + i + int(centerx), int(((sin((i*TWO_PI)/20) * -10) + int(centery) - 3)), color(0, 255, 125));
      set(-10 + i + int(centerx), int(((sin((i*TWO_PI)/20) * -10) + int(centery) + 3)), color(0, 255, 125));
      set(-10 + i + int(centerx), int(((sin((i*TWO_PI)/20) * -10) + int(centery) - 4)), color(0, 0, 0));
      set(-10 + i + int(centerx), int(((sin((i*TWO_PI)/20) * -10) + int(centery) + 4)), color(0, 0, 0));
      set(-10 + i + int(centerx), int(((sin((i*TWO_PI)/20) * -10) + int(centery) - 5)), color(0, 0, 0));
      set(-10 + i + int(centerx), int(((sin((i*TWO_PI)/20) * -10) + int(centery) + 5)), color(0, 0, 0));
    }
    /*ellipse(centerx, centery, 20
     arc(centerx - 5, centery, 10, 10, 0, PI);
     arc(centerx +5, centery, 10, 10, PI, TWO_PI);
     */
  }
}

class Sparks {

  PVector location;
  PVector movement;
  PVector acceleration;
  float radiusv[] = new float[6];
  float thetav[] = new float[6];
  float thetao;
  float omega;
  float sizev;
  color colorv;
  int birth;
  int lifetime;

  Sparks(float dx, float dy, float ddx, float ddy, float sizecoefficient, color sparkcolor) {
    location = new PVector(mouseX + random(-2, 2), mouseY + random(-2, 2), 0);
    movement = new PVector(dx + random(-.1, .1), dy + random(-.1, .1), 0);
    acceleration = new PVector(1 + ddx + random(-.01, .01), 1 + ddy + random(-.01, .01), 0);
    radiusv[0] = 0;
    radiusv[1] = 2;
    radiusv[2] = sqrt(20);
    radiusv[3] = sqrt(2);
    radiusv[4] = sqrt(5);
    radiusv[5] = sqrt(20);
    thetav[0] = 0;
    thetav[1] = PI;
    thetav[2] = atan(2);
    thetav[3] = QUARTER_PI;
    thetav[4] = atan(.5);
    thetav[5] = PI + atan(2);
    omega = 0;
    sizev = sizecoefficient;
    colorv = sparkcolor;
    birth = millis()/1000;
    lifetime = 10;
  }

  void update() {
    physics();
  }

  void physics() {
    location.add(movement);
    //movement.dot(acceleration);
    movement = PVector.mult(movement,1.04,acceleration);
    omega = constrain(tan((movement.y)*(movement.x)*(acceleration.y)*(acceleration.x)), -.1, .1);
    thetao = thetao + omega;
  }

  void render() {
    stroke(0);
    fill(colorv);
    beginShape();
    for (int i = 0; i < 6; i++) {
      vertex(location.x + (sizev * (cos(thetav[i] + thetao) * radiusv[i])), location.y + (sizev * (sin(thetav[i] + thetao) * radiusv[i])));
    }
    endShape(CLOSE);
    //ellipse(location.x, location.y, 12, 12);
  }

  boolean isFinished() {
    if (lifetime < ((millis()/1000) - birth)) {
      return true;
    }
    else return false;
  }
}


void displayAll() {
  //capacitor1.display();
  // capacitor2.display();
  //inductor1.display();
  //ac1.display();
  //resistor1.display();
  renderCapacitors();
  renderInductors();
  renderResistors();
  renderACSources();
  renderParticles();
  displayTrashCan();
}

void updateAll() {
  // updateSchematic();
  updateCapacitors();
  updateInductors();
  updateResistors();
  updateACSources();
  updateParticles();
  updateSchemCap();
  updateSchemInd();
  updateSchemRes();
  updateSchemACS();
  updateSchemWire();
  updateSchemOUT();
  //capacitor1.update();
  //capacitor2.update();
  // inductor1.update();
  // ac1.update();
  //resistor1.update();
}
void updateCapacitors() {
  for (int i = capacitors.size()-1; i >= 0; i--) {
    Capacitor cap = (Capacitor) capacitors.get(i);
    if (cap.isfinished) {
      capacitors.remove(i);
    }
    else {
      cap.update();
      capacitors.add((Capacitor) cap);
      capacitors.remove(i);
    }
  }
}
void updateInductors() {
  for (int i = inductors.size()-1; i >= 0; i--) {
    Inductor ind = (Inductor) inductors.get(i);
    if (ind.isfinished) {
      inductors.remove(i);
    }
    else {
      ind.update();
      inductors.add((Inductor) ind);
      inductors.remove(i);
    }
  }
}
void updateResistors() {
  for (int i = resistors.size()-1; i >= 0; i--) {
    Resistor res = (Resistor) resistors.get(i);
    if (res.isfinished) {
      resistors.remove(i);
    }
    else {
      res.update();
      resistors.add((Resistor) res);
      resistors.remove(i);
    }
  }
}
void updateACSources() {
  for (int i = acsources.size()-1; i >= 0; i--) {
    ACSource acs = (ACSource) acsources.get(i);
    if (acs.isfinished) {
      acsources.remove(i);
    }
    else {
      acs.update();
      acsources.add((ACSource) acs);
      acsources.remove(i);
    }
  }
}
void updateParticles() {
  if (particleslastupdate < (millis()/10) - 1) {
    for (int i = particles.size()-1; i >= 0; i--) {
      Sparks spark = (Sparks) particles.get(i);
      if (spark.isFinished()) {
        particles.remove(i);
      }
      else {
        spark.update();
        particles.add((Sparks) spark);
        particles.remove(i);
      }
    }
    particleslastupdate = (millis()/100);
  }
}

void renderCapacitors() {
  for (int i = capacitors.size()-1; i >= 0; i--) {
    Capacitor cap = (Capacitor) capacitors.get(i);
    cap.display();
  }
}
void renderInductors() {
  for (int i = inductors.size()-1; i >= 0; i--) {
    Inductor ind = (Inductor) inductors.get(i);
    ind.display();
  }
}
void renderResistors() {
  for (int i = resistors.size()-1; i >= 0; i--) {
    Resistor res = (Resistor) resistors.get(i);
    res.display();
  }
}
void renderACSources() {
  for (int i = acsources.size()-1; i >= 0; i--) {
    ACSource acs = (ACSource) acsources.get(i);
    acs.display();
  }
}
void renderParticles() {
  for (int i = particles.size()-1; i >= 0; i--) {
    Sparks spark = (Sparks) particles.get(i);
    spark.render();
  }
}


void generateSparks(float dx, float dy, float ddx, float ddy, float sizecoefficient, color sparkcolor) {
  if (particleslastgenerate < (millis()/100) - .25) {
    particles.add(new Sparks(dx, dy, ddx, ddy, sizecoefficient, sparkcolor));
    particleslastgenerate = (millis()/100);
  }
}

void mouseStateUpdate(boolean mousestatecurrent) 
{
  mousestatelast = mousestatecurrent;
}
void description() {
  if (!(desunits == "NONE")) {
    textFont(font);
    noFill();
    rectMode(CORNER);
    rect(100, 450, 400, 50);
    fill(color(0, 0, 0));
    text(str(desval)+ ' ' + desunits, 100, 475);
  }
}
void graph(String xlable, String ylable, float domainmin, float domainmax, float rangemin, float rangemax, float xpoint, float ypoint) {
  if (graphicaldisplay.symbol == "PLOT") {
    stroke(color(0, 255, 150));
    fill(color(0, 0, 0));
    rectMode(CORNER);
    rect(500, 100, 200, 200);
    stroke(0, 255, 255);
    fill(graphpointcolor);
    ellipse(constrain((map(xpoint, domainmin, domainmax*graphdomain, 0, 200)+500), 500, 700), constrain((map(ypoint, rangemin, rangemax*graphrange, 0, -200)+300), 100, 300), 10, 10);
    fill(color(0, 255, 150));
    text(str(ypoint) + "V", 500, 150);
    text(str(xpoint) + "Hz", 550, 300);
    text(xlable + "(0Hz - "+str(10*graphdomain)+"Hz)", 550, 320);
    text(ylable + "(0V - "+str(5*graphrange)+"Vss)", 450, 90);
  }
}
void vector(String xlable, String ylabel, PVector vector) {
  if (graphicaldisplay.symbol == "VECTOR") {
    stroke(color(0, 0, 0));
    strokeWeight(4);
    line(500, 200, 500, 200-map(vector.y, 0, 1000, 0, 100));
    line(500, 200, 500+map(vector.x, 0, 1000, 0, 100), 200);
    strokeWeight(2);
    line(500, 200, 500+map(vector.x, 0, 1000, 0, 100), 200-map(vector.y, 0, 1000, 0, 100));
    text("(X)"+xlable+" "+str(abs(vector.x)), 525, 90);
    text("(Y)"+ylabel+" "+str(abs(vector.y)), 525, 100);
    text("PhaseShift:", 525, 125);
    if ((phaseShift(vector.x, vector.y))>0) {
      text("Voltage"+" "+"IN PHASE", 525, 135);
      text("Current"+" "+"LAG"+" "+str(degrees(phaseShift(vector.x, vector.y))*-1)+"degrees", 525, 145);
    }
    else if ((phaseShift(vector.x, vector.y))<0) {
      text("Voltage"+" "+"LAG"+" "+str(degrees(phaseShift(vector.x, vector.y))*-1)+"degrees", 525, 135);
      text("Current"+" "+"IN PHASE", 525, 145);
    }
    else {
      text("Voltage"+" "+"IN PHASE", 525, 135);
      text("Current"+" "+"IN PHASE", 525, 145);
    }
    strokeWeight(1);
  }
}

void phasor(int nphasors, float omega, float[] thetas, float[] magnitudes, color[] colors,  String[] labels) {
  strokeWeight(4);
  for (int i = 0; i<nphasors; i++) {
    stroke(colors[i]);
    fill(colors[i]);
    line(500, 200, 500 + cos(phasortimecoefficient*phasortime*omega + thetas[i])*magnitudes[i]*20, 200 + sin(phasortimecoefficient*phasortime*omega + thetas[i])*magnitudes[i]*20);
    text(labels[i], 500, 400+10*i);
    text("[" + (magnitudes[i]) + "]" + "[" + "e^(j" + (thetas[i]) + ")" + "]" + "e^(j" + (omega) + (phasortimecoefficient*phasortime) + ")" + "]", 500, 300+10*i);
  }
}

void updateSchematic() {
  updateSchemGround();
  updateSchemOUT();
  updateSchemWire();
  updateSchemCap();
  updateSchemInd();
  updateSchemRes();
  updateSchemACS();
  if (mode != modelast) {
    for (int i=schemwires.size()-1;i>=0;i--) {
      schemwires.remove(i);
    }
    for (int i=schemouts.size()-1;i>=0;i--) {
      schemouts.remove(i);
    }
    for (int i=schemgrounds.size()-1;i>=0;i--) {
      schemgrounds.remove(i);
    }
    for (int i=schemcapacitors.size()-1;i>=0;i--) {
      schemcapacitors.remove(i);
    }
    for (int i=scheminductors.size()-1;i>=0;i--) {
      scheminductors.remove(i);
    }
    for (int i=schemresistors.size()-1;i>=0;i--) {
      schemresistors.remove(i);
    }
    for (int i=schemacsources.size()-1;i>=0;i--) {
      schemacsources.remove(i);
    }
    
    if (mode=="Low-Pass Filter (RC)") {
      graphdomain = 1;
      graphrange = 1;
      SchemCap c1 = new SchemCap(200, 300, 1);
      SchemRes r1 = new SchemRes(300, c1.leadly, 1);
      SchemACS a1 = new SchemACS(250, 200, 1);
      schemcapacitors.add((SchemCap) c1);
      schemresistors.add((SchemRes) r1);
      schemacsources.add((SchemACS) a1);
      schemwires.add(new SchemWire(c1.leadrx, c1.leadry, r1.leadlx, r1.leadly, 1));
      schemwires.add(new SchemWire((c1.leadlx) - 10, c1.leadly, (c1.leadlx) - 10, a1.leadly, 2));
      schemwires.add(new SchemWire((r1.leadrx) + 10, r1.leadry, (r1.leadrx) + 10, a1.leadry, 3));
      schemwires.add(new SchemWire((c1.leadlx) - 10, c1.leadly, c1.leadlx, c1.leadly, 4));
      schemwires.add(new SchemWire((r1.leadrx) + 10, r1.leadry, r1.leadrx, r1.leadry, 5));
      schemwires.add(new SchemWire((c1.leadlx) - 10, a1.leadly, a1.leadlx, a1.leadly, 6));
      schemwires.add(new SchemWire((r1.leadrx) + 10, a1.leadry, a1.leadrx, a1.leadry, 7));
      schemouts.add(new SchemOUT(((c1.leadrx + r1.leadlx)/2), c1.leadry, ((c1.leadrx+r1.leadlx)/2), c1.leadry + 50, 1));
      schemgrounds.add(new SchemGround((c1.leadlx) - 10, c1.leadly, (c1.leadlx) - 10, (c1.leadly) +50, 1));
    }
    else if (mode=="High-Pass Filter (RC)") {
    }
    else if (mode=="Low-Pass Filter (RL)") {
    }
    else if (mode=="High-Pass Filter (RL)") {
    }
    else if (mode=="Band-Reject (Notch): Driven LC Series") {
    }
    else if (mode=="Band-Pass: Driven LC Parallel") {
      graphdomain = 1;
      graphrange = 1;
      SchemCap c1 = new SchemCap(200, 300, 1);
      SchemInd i1 = new SchemInd(210, 250, 1);
      SchemRes r1 = new SchemRes(300, c1.leadly, 1);
      SchemACS a1 = new SchemACS(250, 200, 1);
      schemcapacitors.add((SchemCap) c1);
      scheminductors.add((SchemInd) i1);
      schemresistors.add((SchemRes) r1);
      schemacsources.add((SchemACS) a1);
      schemwires.add(new SchemWire(c1.leadrx, c1.leadry, r1.leadlx, r1.leadly, 1));
      schemwires.add(new SchemWire((c1.leadlx) - 10, c1.leadly, (c1.leadlx) - 10, a1.leadly, 2));
      schemwires.add(new SchemWire((r1.leadrx) + 10, r1.leadry, (r1.leadrx) + 10, a1.leadry, 3));
      schemwires.add(new SchemWire((c1.leadlx) - 10, c1.leadly, c1.leadlx, c1.leadly, 4));
      schemwires.add(new SchemWire((r1.leadrx) + 10, r1.leadry, r1.leadrx, r1.leadry, 5));
      schemwires.add(new SchemWire((c1.leadlx) - 10, a1.leadly, a1.leadlx, a1.leadly, 6));
      schemwires.add(new SchemWire((r1.leadrx) + 10, a1.leadry, a1.leadrx, a1.leadry, 7));
      schemwires.add(new SchemWire((i1.leadlx) - 15, i1.leadly, i1.leadlx, i1.leadly, 8));
      schemwires.add(new SchemWire(((c1.leadrx + r1.leadlx)/2), c1.leadry, ((c1.leadrx+r1.leadlx)/2), i1.leadry, 9));
      schemwires.add(new SchemWire(i1.leadrx, i1.leadry, ((c1.leadrx+r1.leadlx)/2), i1.leadry, 10));
      schemouts.add(new SchemOUT(((c1.leadrx + r1.leadlx)/2), c1.leadry, ((c1.leadrx+r1.leadlx)/2), c1.leadry + 50, 1));
      schemgrounds.add(new SchemGround((c1.leadlx) - 10, c1.leadly, (c1.leadlx) - 10, (c1.leadly) +50, 1));
    }
    else if (mode=="Resonance (LC Tank)") {
    }
    else if (mode=="Dual Resonant Air-Core Step-Up Transformer (Tesla Coil)") {
    }
    modelast = mode;
  }

  
  if (mode=="Low-Pass Filter (RC)") {

    for (int i = schemcapacitors.size()-1; i >= 0; i--) {
      SchemCap capval = (SchemCap) schemcapacitors.get(i);
      if (capval.id == 1) {
        for (int ii = schemresistors.size()-1; ii >= 0; ii--) {
          SchemRes resval = (SchemRes) schemresistors.get(i);
          if (resval.id == 1) {
            for (int iii = schemacsources.size()-1; iii >= 0; iii--) {
              SchemACS acsval = (SchemACS) schemacsources.get(i);
              if (acsval.id == 1) {
                if (graphicaldisplay.symbol == "PLOT") {
                  if (!bandWidth(5,voltagePoint(5, impedance(resval.value, 0, capacitiveReactance(capval.value, acsval.value)), capacitiveReactance(capval.value, acsval.value)))) {
                    graphpointcolor = color(255, 0, 0);
                  }
                  else {
                    graphpointcolor = color(0, 255, 150);
                  }
                  graph("Frequency", "OUT1 Amplitude", 0, 10, 0, 5, acsval.value, voltagePoint(5, impedance(resval.value, 0, capacitiveReactance(capval.value, acsval.value)), capacitiveReactance(capval.value, acsval.value)));
                }
                //vector(new PVector(map(resval.value,0,1000,0,100),map(capacitiveReactance(capval.value, acsval.value),0,1000,0,-100)));
                else if (graphicaldisplay.symbol == "VECTOR") vector("Resistance", "CapacitiveReactance", new PVector(resval.value, capacitiveReactance(capval.value, acsval.value)*-1));
                else if (graphicaldisplay.symbol == "PHASOR") {
                  phasorthetas[0] = 0;
                  phasorthetas[1] = phaseShift(resval.value, capacitiveReactance(capval.value, acsval.value));
                  phasormagnitudes[0] = 5;
                  phasormagnitudes[1] = voltagePoint(5, impedance(resval.value, 0, capacitiveReactance(capval.value, acsval.value)), capacitiveReactance(capval.value, acsval.value));
                  phasorcolors[0] = color(0,255,100);
                  phasorcolors[1] = color(255,0,255);
                  phasorlabels[0] = "SignalVoltage";
                  phasorlabels[1] = "OUT1Voltage";
                  phasor(2, acsval.value*TWO_PI, phasorthetas, phasormagnitudes, phasorcolors, phasorlabels);
                }
              }
            }
          }
        }
      }
    }
  }
  else if (mode=="High-Pass Filter (RC)") {
  }
  else if (mode=="Low-Pass Filter (RL)") {
  }
  else if (mode=="High-Pass Filter (RL)") {
  }
  else if (mode=="Band-Reject (Notch): Driven LC Series") {
  }
  else if (mode=="Band-Pass: Driven LC Parallel") {
    for (int i = schemcapacitors.size()-1; i >= 0; i--) {
      SchemCap capval = (SchemCap) schemcapacitors.get(i);
      if (capval.id == 1) {
        for (int ii = scheminductors.size()-1; ii >= 0; ii--) {
      SchemInd indval = (SchemInd) scheminductors.get(ii);
      if (indval.id == 1) {
        for (int iii = schemresistors.size()-1; iii >= 0; iii--) {
          SchemRes resval = (SchemRes) schemresistors.get(iii);
          if (resval.id == 1) {
            for (int iiii = schemacsources.size()-1; iiii >= 0; iiii--) {
              SchemACS acsval = (SchemACS) schemacsources.get(iiii);
              if (acsval.id == 1) {
                if (graphicaldisplay.symbol == "PLOT") {
                  if (!bandWidth(5,voltagePoint(5, impedance(resval.value, 1/ ((1/inductiveReactance(indval.value, acsval.value))-(1/capacitiveReactance(capval.value, acsval.value))), 0), abs(1/ ((1/inductiveReactance(indval.value, acsval.value))-(1/capacitiveReactance(capval.value, acsval.value))))))) {
                    graphpointcolor = color(255, 0, 0);
                  }
                  else {
                    graphpointcolor = color(0, 255, 150);
                  }
                  if (acsval.value > resonantFrequency(capval.value, indval.value)-.001 && acsval.value < resonantFrequency(capval.value, indval.value)+.001) graphpointcolor = color(255, 255, 255);
                  graph("Frequency", "OUT1 Amplitude", 0, 10, 0, 5, acsval.value, voltagePoint(5, impedance(resval.value, 1/ ((1/inductiveReactance(indval.value, acsval.value))-(1/capacitiveReactance(capval.value, acsval.value))), 0), abs(1/ ((1/inductiveReactance(indval.value, acsval.value))-(1/capacitiveReactance(capval.value, acsval.value))))));
                }
                //vector(new PVector(map(resval.value,0,1000,0,100),map(capacitiveReactance(capval.value, acsval.value),0,1000,0,-100)));
                else if (graphicaldisplay.symbol == "VECTOR") vector("Resistance", "Reactance", new PVector(resval.value, ((1/inductiveReactance(indval.value, acsval.value))-(1/capacitiveReactance(capval.value, acsval.value)))));
                else if (graphicaldisplay.symbol == "PHASOR") {
                  phasorthetas[0] = 0;
                  phasorthetas[1] = phaseShift(resval.value, 1/inductiveReactance(indval.value, acsval.value) - 1/capacitiveReactance(capval.value, acsval.value));
                  phasormagnitudes[0] = 5;
                  phasormagnitudes[1] = voltagePoint(5, impedance(resval.value, 1/ ((1/inductiveReactance(indval.value, acsval.value))-(1/capacitiveReactance(capval.value, acsval.value))), 0), abs(1/ ((1/inductiveReactance(indval.value, acsval.value))-(1/capacitiveReactance(capval.value, acsval.value)))));
                  phasorcolors[0] = color(0,255,100);
                  phasorcolors[1] = color(255,0,255);
                  phasorlabels[0] = "SignalVoltage";
                  phasorlabels[1] = "OUT1Voltage";
                  phasor(2, acsval.value*TWO_PI, phasorthetas, phasormagnitudes, phasorcolors, phasorlabels);
                }
              }
            }
              }
            }
          }
        }
      }
    }
  }
  else if (mode=="Resonance (LC Tank)") {
  }
  else if (mode=="Dual Resonant Air-Core Step-Up Transformer (Tesla Coil)") {
  }
}

void displayTrashCan() {
  stroke(0);
  fill(color(100, 100, 100));
  rectMode(CORNER);
  rect(50, 450, 20, 30);
  rect(48, 445, 24, 5);
  line(55, 450, 55, 480);
  line(60, 450, 60, 480);
  line(65, 450, 65, 480);
}

class SchemWire {
  float x1;
  float y1;
  float x2;
  float y2;
  int id;
  boolean available;

  SchemWire(float tx1, float ty1, float tx2, float ty2, int tid) {
    x1 = tx1;
    x2 = tx2;
    y1 = ty1;
    y2 = ty2;
    id = tid;
    available = true;
  }

  void display() {
    fill(color(0, 0, 0));
    stroke(color(0, 0, 0));
    line(x1, y1, x2, y2);
    ellipse(x1, y1, 4, 4);
    ellipse(x2, y2, 4, 4);
  }
}
class SchemOUT {
  float x1;
  float y1;
  float x2;
  float y2;
  int id;
  boolean available;

  SchemOUT(float tx1, float ty1, float tx2, float ty2, int tid) {
    x1 = tx1;
    x2 = tx2;
    y1 = ty1;
    y2 = ty2;
    id = tid;
    available = true;
  }

  void display() {
    fill(color(0, 0, 0));
    stroke(color(0, 0, 0));
    line(x1, y1, x2, y2);
    ellipse(x1, y1, 4, 4);
    textFont(font2);
    text("OUT"+str(id), x2, y2);
  }
}

class SchemGround {
  float x1;
  float y1;
  float x2;
  float y2;
  int id;
  boolean available;

  SchemGround(float tx1, float ty1, float tx2, float ty2, int tid) {
    x1 = tx1;
    x2 = tx2;
    y1 = ty1;
    y2 = ty2;
    id = tid;
    available = true;
  }

  void display() {
    fill(color(0, 0, 0));
    stroke(color(0, 0, 0));
    line(x1, y1, x2, y2);
    ellipse(x1, y1, 4, 4);
    line(x2 - 10, y2, x2 + 10, y2);
    line(x2 - 7, y2 + 5, x2 + 7, y2 + 5);
    line(x2 - 3, y2 + 10, x2 + 3, y2 + 10);
  }
}

class SchemCap {
  float xpos;
  float ypos;
  float cxpos;
  float cypos;
  float leadlx;
  float leadly;
  float leadrx;
  float leadry;
  float value;
  int id;
  boolean available;
  int check;

  SchemCap(float txpos, float typos, int tid) {
    xpos = txpos;
    ypos = typos;
    cxpos = xpos;
    cypos = ypos + 15;
    leadlx = xpos - 5;
    leadly = ypos + 15;
    leadrx = xpos + 10;
    leadry = ypos + 15;
    id = tid;
    available = true;
  }

  void update() {
    //value = ((Capacitor) capacitors.get(check)).value;
    if (available) value = 0;

    //available = true;
    //value = 0;
    for (int i = capacitors.size()-1; i >= 0; i--) {
      Capacitor testcap = (Capacitor) capacitors.get(i);
      if (testcap.applied) {
        if (testcap.held) {
          available = true; 
          value = 0;
          testcap.applied = false;
        }
        else {
          value = testcap.value;
        }
      }
      if ((testcap.centerx > cxpos - 15)&&(testcap.centerx < cxpos + 15)
        &&(testcap.centery > cypos - 15)&&(testcap.centery < cypos + 15)
        &&(!testcap.held)&&(available)) {
        testcap.centerx = cxpos;
        testcap.centery = cypos;
        value = testcap.value;
        check = i;
        available = false;
        testcap.applied = true;
      }
      capacitors.add((Capacitor) testcap);
      capacitors.remove(i);
    }
    display();

    /*desunits = "test";
     desval = value;
     description();
     */
    /* //TEST:
     if(available) desunits = "true";
     else desunits = "false";
     description(); */
  }

  void display() {
    stroke(0, 0, 0);
    line(xpos - 5, ypos, xpos - 5, ypos + 30);
    line(xpos + 10, ypos, xpos + 10, ypos + 30);
  }
}

class SchemInd {
  float xpos;
  float ypos;
  float leadlx;
  float leadly;
  float leadrx;
  float leadry;
  float value;
  int id;
  boolean available;
  int check;

  SchemInd(float txpos, float typos, int tid) {
    xpos = txpos;
    ypos = typos;
    leadlx = xpos - 10;
    leadly = ypos;
    leadrx = xpos + 10;
    leadry = ypos;
    id = tid;
    available = true;
  }

  void update() {
    //value = ((Inductor) inductors.get(check)).value;
    if (available) value = 0;
    //available = true;
    //value = 0;

    for (int i = inductors.size()-1; i >= 0; i--) {
      Inductor testind = (Inductor) inductors.get(i);
      if (testind.applied) {
        if (testind.held) {
          available = true; 
          value = 0;
          testind.applied = false;
        }
        else {
          value = testind.value;
        }
      }
      if ((testind.centerx > xpos - 10)&&(testind.centerx < xpos + 10)
        &&(testind.centery > ypos - 10)&&(testind.centery < ypos + 10)
        &&(!testind.held)&&(available)) {
        testind.centerx = xpos;
        testind.centery = ypos;
        value = testind.value;
        check = i;
        available = false;
        testind.applied = true;
      }
      inductors.add((Inductor) testind);
      inductors.remove(i);
    }
    display();
  }

  void display() {
    noFill();
    stroke(color(0, 0, 0));
    for (int i = 0; i < 3; i++) {
      arc(xpos + (-10 + (10 * i)), ypos, 10, 30, PI, TWO_PI);
    }
  }
}

class SchemRes {
  float xpos;
  float ypos;
  float cxpos;
  float cypos;
  float leadlx;
  float leadly;
  float leadrx;
  float leadry;
  float value;
  int id;
  boolean available;
  int check;

  SchemRes(float txpos, float typos, int tid) {
    xpos = txpos;
    ypos = typos;
    cxpos = xpos + 15;
    cypos = ypos;
    leadlx = xpos;
    leadly = ypos;
    leadrx = xpos + 30;
    leadry = ypos;
    id = tid;
    available = true;
  }

  void update() {
    //value = ((Resistor) resistors.get(check)).value;
    if (available) value = 0;
    //available = true;
    //value = 0;

    for (int i = resistors.size()-1; i >= 0; i--) {
      Resistor testres = (Resistor) resistors.get(i);
      if (testres.applied) {
        if (testres.held) {
          available = true; 
          value = 0;
          testres.applied = false;
        }
        else {
          value = testres.value;
        }
      }
      if ((testres.centerx > cxpos - 10)&&(testres.centerx < cxpos + 10)
        &&(testres.centery > cypos - 10)&&(testres.centery < cxpos + 10)
        &&(!testres.held)&&(available)) {
        testres.centerx = cxpos;
        testres.centery = cypos;
        value = testres.value;
        check = i;
        available = false;
        testres.applied = true;
      }
      resistors.add((Resistor) testres);
      resistors.remove(i);
    }
    display();
  }

  void display() {
    stroke(color(0, 0, 0));
    line(xpos, ypos, xpos+5, ypos-10);
    line(xpos+5, ypos-10, xpos+10, ypos);
    line(xpos+10, ypos, xpos+15, ypos-10);
    line(xpos+15, ypos-10, xpos+20, ypos);
    line(xpos+20, ypos, xpos+25, ypos-10);
    line(xpos+25, ypos-10, xpos+30, ypos);
  }
}

class SchemACS {
  float xpos;
  float ypos;
  float leadlx;
  float leadly;
  float leadrx;
  float leadry;
  float value;
  int id;
  boolean available;
  int check;

  SchemACS(float txpos, float typos, int tid) {
    xpos = txpos;
    ypos = typos;
    leadlx = xpos - 15;
    leadly = ypos;
    leadrx = xpos + 15;
    leadry = ypos;
    id = tid;
    available = true;
  }

  void update() {
    //value = ((ACSource) acsources.get(check)).value;
    if (available) value = 0;
    //available = true;
    //value = 0;
    for (int i = acsources.size()-1; i >= 0; i--) {
      ACSource testacs = (ACSource) acsources.get(i);
      if (testacs.applied) {
        if (testacs.held) {
          testacs.applied = false;
          available = true; 
          value = 0;
        }
        else {
          value = testacs.value;
        }
      }
      if ((testacs.centerx > xpos - 10)&&(testacs.centerx < xpos + 10)
        &&(testacs.centery > ypos - 10)&&(testacs.centery < xpos + 10)
        &&(!testacs.held)&&(available)) {
        testacs.centerx = xpos;
        testacs.centery = ypos;
        value = testacs.value;
        check = i;
        available = false;
        testacs.applied = true;
      }
      acsources.add((ACSource) testacs);
      acsources.remove(i);
    }

    display();
  }

  void display() {
    noFill();
    stroke(color(0, 0, 0));
    ellipse(xpos, ypos, 30, 30);
    for (int i=0;i<20;i++) {
      set(int(xpos)-10+i, int(ypos)+int((-10*sin((TWO_PI*i)/20))), color(0, 0, 0));
    }
  }
}

void updateSchemCap() {
  for (int i = schemcapacitors.size()-1; i >= 0; i--) {
    SchemCap update = (SchemCap) schemcapacitors.get(i);
    update.update();
    schemcapacitors.add((SchemCap) update);
    schemcapacitors.remove(i);
  }
}

void updateSchemInd() {
  for (int i = scheminductors.size()-1; i >= 0; i--) {
    SchemInd update = (SchemInd) scheminductors.get(i);
    update.update();
    scheminductors.add((SchemInd) update);
    scheminductors.remove(i);
  }
}

void updateSchemRes() {
  for (int i = schemresistors.size()-1; i >= 0; i--) {
    SchemRes update = (SchemRes) schemresistors.get(i);
    update.update();
    schemresistors.add((SchemRes) update);
    schemresistors.remove(i);
  }
}

void updateSchemACS() {
  for (int i = schemacsources.size()-1; i >= 0; i--) {
    SchemACS update = (SchemACS) schemacsources.get(i);
    update.update();
    schemacsources.add((SchemACS) update);
    schemacsources.remove(i);
  }
}
void updateSchemWire() {
  for (int i = schemwires.size()-1; i >= 0; i--) {
    SchemWire update = (SchemWire) schemwires.get(i);
    update.display();
  }
}
void updateSchemOUT() {
  for (int i = schemouts.size()-1; i >= 0; i--) {
    SchemOUT update = (SchemOUT) schemouts.get(i);
    update.display();
  }
}
void updateSchemGround() {
  for (int i = schemgrounds.size()-1; i >= 0; i--) {
    SchemGround update = (SchemGround) schemgrounds.get(i);
    update.display();
  }
}



/***************************************************************************************************/
//////////  /////////    //     //  ////////  ////////  //////   //////////   ///     //  /////////
///         //     //    //     //  //    //     //       //     //      //   // //   //  ///
//////////  //     //    //     //  ////////     //       //     //      //   //  //  //  /////////
///         //   //////  //     //  //    //     //       //     //      //   //   // //        ///
//////////  /////////    /////////  //    //     //     //////   //////////   //    ////  /////////
/***************************************************************************************************/

float impedance(float R, float Xl, float Xc) {
  return(sqrt(sq(R)+sq(Xl-Xc)));
}

float inductiveReactance(float L, float f) {
  return(TWO_PI*f*L);
}

float capacitiveReactance(float C, float f) {
  return(1 / (TWO_PI*f*C));
}

float resonantFrequency(float L, float C) {
  return(1 / (TWO_PI*sqrt(L*C)));
}

float phaseShift(float R, float X) {
  return atan(X/R);
}

float voltagePoint(float Vt, float Rt, float R) {
  return(Vt*(R / Rt));
}

boolean bandWidth(float amplitudein, float amplitudeout) {
  if (amplitudeout < (amplitudein/2)) {
    return false;
  }
  else return true;
}