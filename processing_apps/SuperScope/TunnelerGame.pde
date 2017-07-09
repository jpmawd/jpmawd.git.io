int W = 400;
int H = 400;
float y = W/2;
int s=10;
float vy=0;
float windowY = H / 2;
float windowV = 0;
float[] windows = new float[W / 4];
int desiredWindowWidth = 80;
int windowWidth = 800;
boolean crashed = false;
int score = 0;
int gameDelay = 20;
long timeLast;

void mouseClicked() {
  vy -=200;
  if(crashed) {
    startLoop();
  }
}

void setup() {
  size(W, H);
  timeLast = millis();
  startLoop();
}

void startLoop() {
  for(int i = 0; i < W/4; i++) {
    windows[i] = H / 2 - desiredWindowWidth;
  }
 y = W/2;
 vy=0;
 windowY = H / 2;
 windowV = 0;
 desiredWindowWidth = 80;
 windowWidth = 800;
 score = 0;
 crashed = false;
}

void draw() {
  if(millis() - timeLast > gameDelay) {
  background(0);
  if(!crashed) {
    fill(color(0,255,100));
    stroke(color(0,255,100));
  }
  else {
    fill(color(255,0,0));
    stroke(color(255,0,0));
  }
  for(int ii = 0; ii < W / 4; ii++) {
    int c = int(windows[ii]);
    rect(ii * 4,c-windowWidth/2,4,windowWidth);
  }
  fill(155);
  //stroke(color(0, 50, 255));
  stroke(0);
  rect(s, int(y) - s/2, 2*s, s);
  if(!crashed) {
    y += vy * .01;
    windowY += windowV;
    windowY = max(windowWidth/2, windowY);
    windowY = min(H - windowWidth/2, windowY);
    windowV += randomGaussian() / 10;
    windowV *= 1 / (sq(windowV / 10) + 1);
    windows[windows.length - 1] = windowY;
    for(int i = 0; i < W/4 - 1; i++) {
      windows[i] = windows[i + 1];
    }
    
    vy += 8;
    windowWidth += float(desiredWindowWidth - windowWidth) / sq(desiredWindowWidth + 1);
    score++;
  }
  if(abs(windows[0] - y) > abs(windowWidth / 2 - s/2)) {
    crashed = true;
  }
  text(score, W - 80, H - 20);
  
  timeLast = millis();
  }
}
