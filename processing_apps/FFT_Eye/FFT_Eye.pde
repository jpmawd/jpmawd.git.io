import processing.video.*;
import javax.swing.JOptionPane;

Capture cam;
int mode = 0;
boolean useCamera = true;
PImage img;

void mouseClicked() {
  if(mode == 13) {
    mode = 0;
  }
  else {
    mode++;
  }
}

void keyReleased() {
  if(key == ' ') {
    useCamera = !useCamera;
    if(!useCamera) {
      String path = JOptionPane.showInputDialog("Image path", "Image path");
      String format = JOptionPane.showInputDialog("Image format", "Image format");
      img = loadImage(path, format);
    }
  }
}

Capture getCam() {
  Capture webcam;
  String[] cameras = Capture.list();
  
  if (cameras.length == 0) {
    println("There are no cameras available for capture.");
    webcam = null;
    //exit();
  } else {
    println("Available cameras:");
    for (int i = 0; i < cameras.length; i++) {
      println(cameras[i]);
    }
    
    webcam = new Capture(this, cameras[0]);
    webcam.start();     
  } 
  return webcam;  
}

void setup() {
  size(640, 640);
  stroke(0);
  fill(0);
  cam = getCam();
}

void draw() {
  if (cam != null) {
    if(cam.available()) {
      cam.read();
    }
    
    background(255);
    
    
    if(useCamera) {
      //img = cam;
      try {
        img = (PImage) cam.clone();
      } catch(CloneNotSupportedException e){}
    }
    if(img != null && img.width > 0) {
      img.resize(256,256);
      image(img, img.width, 0);
      
      
      float r = -1 + mag(img.width, img.height) * (mag(mouseX, mouseY) / mag(width, height));
      
      
      PImage filter = new PImage();
      if(mode == 0) {
        filter = filterColorImage(img, r, true);
        text("MODE (0): " + "HIGH PASS (EDGE DETECTION)", 20, 20 + img.height);
      }
      else if(mode == 1) {
        filter = filterColorImage(img, r, false);
        text("MODE (1): " + "LOW PASS (BLURRING)", 20, 20 + img.height);
      }
      else if(mode == 2) {
        filter = filterEffect1(img, r);
        text("MODE (2): " + "BAND PASS", 20, 20 + img.height);
      }
      else if(mode == 3) {
        filter = filterEffect2(img, r);
        text("MODE (3): " + "Trippy", 20, 20 + img.height);
      }
      else if(mode == 4) {
        filter = filterEffect3(img, r);
        text("MODE (4): " + "Contours", 20, 20 + img.height);
      }
      else if(mode == 5) {
        filter = filterImg(img, r, true);
        text("MODE (5): " + "GRAYSCALE HIGH PASS", 20, 20 + img.height);
      }
      else if(mode == 6) {
        filter = hysteresis(img, r);
        text("MODE (6): " + "Hysteresis", 20, 20 + img.height);
      }
      else if(mode == 7) {
        filter = threshold(img, r);
        text("MODE (7): " + "Binary Threshold", 20, 20 + img.height);
      }
      else if(mode == 8) {
        filter = hysteresis(img, r);
        filter = filterImg(filter, r, true);
        filter = threshold(filter, r);
        text("MODE (8): " + "Simplify", 20, 20 + img.height);
      }
      else if(mode == 9) {
        filter = enhance(img, r);
        //filter = threshold(filter,r);
        filter = filterColorImage(filter, r, false);
        filter = enhance(filter, r);
        //filter = filterColorImage(filter, r, false);
        filter = enhance(filter, r);
        filter = simplePalette(filter);
        text("MODE (9): " + "Topographic", 20, 20 + img.height);
      }
      else if(mode == 10) {
        filter = posterize(img, r);
        text("MODE (10): " + "Posterize", 20, 20 + img.height);
      }
      else if(mode == 11) {
        filter = simplePalette(img);
        text("MODE (11): " + "SimplePalette", 20, 20 + img.height);
      }
      else if(mode == 12) {
        
        filter = enhance(img, r);
        filter.filter(ERODE);
        //filter = threshold(filter,r);
        filter = filterColorImage(filter, r, false);
        filter = enhance(filter, r);
        //filter = filterColorImage(filter, r, false);
        filter = enhance(filter, r);
        filter = simplePalette(filter, new color[]{color(0), color(255), color(100,0,0), color(100,100,0), color(0,0,100)});
        text("MODE (12): " + "Blogger", 20, 20 + img.height);
      }
      else if(mode == 13) {
        filter = compress(img, int(r));
        text("MODE (13): " + "Compress", 20, 20 + img.height);
      }
      
      image(filter, 0, 0);
      println(r);
    }
  }
  else {
   cam = getCam();
  }
}

PImage compress(PImage source, int numTerms) {
  
  int widthFFT = int(pow(2, ceil(log(source.width) / log(2))));
  int heightFFT = int(pow(2, ceil(log(source.height) / log(2))));
  PImage newImage = createImage(source.width, source.height, RGB);
  float[][] r = new float[widthFFT + 1][heightFFT + 1];
  float[][] g = new float[widthFFT + 1][heightFFT + 1];
  float[][] b = new float[widthFFT + 1][heightFFT + 1];
  int n = 0;
  source.loadPixels();
  for(int y = 0; y < source.height; y++) {
    for(int x = 0; x < source.width; x++) {
      r[x][y] = red(source.pixels[n]);
      g[x][y] = green(source.pixels[n]);
      b[x][y] = blue(source.pixels[n]);
      n++;
    }
  }
  float[][] rFFT, gFFT, bFFT;
  rFFT = fourPixelArray(r, widthFFT+1, heightFFT+1, 1, false);
  gFFT = fourPixelArray(g, widthFFT+1, heightFFT+1, 1, false);
  bFFT = fourPixelArray(b, widthFFT+1, heightFFT+1, 1, false);
  r = new float[widthFFT + 1][heightFFT + 1];
  g = new float[widthFFT + 1][heightFFT + 1];
  b = new float[widthFFT + 1][heightFFT + 1];
  for(int y = 0; y < heightFFT; y++) {
    for(int x = 0; x < widthFFT; x++) {
      r[x][y] = rFFT[x][y];
      g[x][y] = gFFT[x][y];
      b[x][y] = bFFT[x][y];
    }
  }
  
  
  for(int y = 1; y < heightFFT; y++) {
    for(int x = 1; x < widthFFT; x++) {
      float magSq = sq(r[x][y])+sq(g[x][y])+sq(b[x][y]);
      float n1 = sq(r[x+1][y])+sq(g[x+1][y])+sq(b[x+1][y]);
      float n2 = sq(r[x+1][y+1])+sq(g[x+1][y+1])+sq(b[x+1][y+1]);
      float n3 = sq(r[x][y+1])+sq(g[x][y+1])+sq(b[x][y+1]);
      float n4 = sq(r[x-1][y+1])+sq(g[x-1][y+1])+sq(b[x-1][y+1]);
      float n5 = sq(r[x-1][y])+sq(g[x-1][y])+sq(b[x-1][y]);
      float n6 = sq(r[x-1][y-1])+sq(g[x-1][y-1])+sq(b[x-1][y-1]);
      float n7 = sq(r[x][y-1])+sq(g[x][y-1])+sq(b[x][y-1]);
      float n8 = sq(r[x+1][y-1])+sq(g[x+1][y-1])+sq(b[x+1][y-1]);
      float avgN = (n1+n2+n3+n4+n5+n6+n7+n8)/8; 
        r[x][y] *= magSq/avgN;
        b[x][y] *= magSq/avgN;
        g[x][y] *= magSq/avgN;
      
    }
  }
  
  
  
  r = fourPixelArray(r, widthFFT+1, heightFFT+1, -1, true);
  g = fourPixelArray(g, widthFFT+1, heightFFT+1, -1, true);
  b = fourPixelArray(b, widthFFT+1, heightFFT+1, -1, true);
  newImage.loadPixels();
  n = 0;
  for(int y = 0; y < source.height; y++) {
    for(int x = 0; x < source.width; x++) {
      newImage.pixels[n] = color(r[x][y], g[x][y], b[x][y]);
      n++;
    }
  }
  newImage.updatePixels();
  return newImage;
}

PImage simplePalette(PImage source) {
  color[] colors = new color[14];
  colors[0] = color(255,0,0);
  colors[1] = color(255,255,0);
  colors[2] = color(0,0,255);
  colors[3] = lerpColor(colors[0],colors[1],.50);
  colors[4] = lerpColor(colors[0],colors[2],.50);
  colors[5] = lerpColor(colors[2],colors[1],.50);
  colors[6] = lerpColor(colors[3],colors[1],.50);
  colors[7] = lerpColor(colors[3],colors[0],.50);
  colors[8] = lerpColor(colors[4],colors[0],.50);
  colors[9] = lerpColor(colors[4],colors[2],.50);
  colors[10] = lerpColor(colors[5],colors[1],.50);
  colors[11] = lerpColor(colors[5],colors[2],.50);
  colors[12] = color(0);
  colors[13] = color(255);
  return simplePalette(source, colors);
}

PImage simplePalette(PImage source, color[] colors) {
  int numFinalColors = colors.length;
  float[] weights = new float[numFinalColors];
  
  PImage newImg = createImage(source.width, source.height, RGB);
  newImg.loadPixels();
  for(int n = 0; n < source.pixels.length; n++) {
    float red = red(source.pixels[n]);
    float green = green(source.pixels[n]);
    float blue = blue(source.pixels[n]);
    for(int i = 0; i < numFinalColors; i++) {
      weights[i] = sqrt(sq(red(colors[i])-red)+sq(green(colors[i])-green)+sq(blue(colors[i])-blue));
  
    }
  }
  float[] greatest = new float[numFinalColors];
  color[] finalColors = new color[numFinalColors];
  for(int i = 0; i < numFinalColors; i++) {
    for(int ii = 0; ii < greatest.length; ii++) {
      if(weights[i] > greatest[ii]) {
        for(int iii = greatest.length-1; iii > ii; iii--) {
          greatest[iii] = greatest[iii-1];
          finalColors[iii] = finalColors[iii-1];
        }
        greatest[ii] = weights[i];
        finalColors[ii] = colors[i];
        break;
      }
    }
  }
  for(int n = 0; n < source.pixels.length; n++) {
    float[] differences = new float[finalColors.length];
    float red = red(source.pixels[n]);
    float green = green(source.pixels[n]);
    float blue = blue(source.pixels[n]);
    for(int i = 0; i < finalColors.length; i++) {
      differences[i] = sqrt(sq(red(finalColors[i])-red)+sq(green(finalColors[i])-green)+sq(blue(finalColors[i])-blue));
    }
    float closest = differences[0];
    color closestColor = finalColors[0];
    for(int i = 0; i < finalColors.length; i++) {
      if(differences[i] < closest) {
        closest = differences[i];
        closestColor = finalColors[i];
      }
    }
    
    newImg.pixels[n] = closestColor;
  }
  newImg.updatePixels();
  return newImg;
}


PImage posterize(PImage source, float radius) {
  int widthFFT = int(pow(2, ceil(log(source.width) / log(2))));
  int heightFFT = int(pow(2, ceil(log(source.height) / log(2))));
  PImage newImage = createImage(source.width, source.height, RGB);
  float[][] r = new float[widthFFT + 1][heightFFT + 1];
  float[][] g = new float[widthFFT + 1][heightFFT + 1];
  float[][] b = new float[widthFFT + 1][heightFFT + 1];
  int n = 0;
  source.loadPixels();
  for(int y = 0; y < source.height; y++) {
    for(int x = 0; x < source.width; x++) {
      r[x][y] = red(source.pixels[n]);
      g[x][y] = green(source.pixels[n]);
      b[x][y] = blue(source.pixels[n]);
      n++;
    }
  }
  float[][] rFFT, gFFT, bFFT;
  rFFT = fourPixelArray(r, widthFFT+1, heightFFT+1, 1, false);
  gFFT = fourPixelArray(g, widthFFT+1, heightFFT+1, 1, false);
  bFFT = fourPixelArray(b, widthFFT+1, heightFFT+1, 1, false);
  r = new float[widthFFT + 1][heightFFT + 1];
  g = new float[widthFFT + 1][heightFFT + 1];
  b = new float[widthFFT + 1][heightFFT + 1];
  for(int y = 0; y < heightFFT; y++) {
    for(int x = 0; x < widthFFT; x++) {
      r[x][y] = rFFT[x][y];
      g[x][y] = gFFT[x][y];
      b[x][y] = bFFT[x][y];
    }
  }
  
  
  for(int y = 0; y < heightFFT; y++) {
    for(int x = 0; x < widthFFT; x++) {
      float a = dist(x,y,widthFFT/2,heightFFT/2)/10000000;
      r[x][y] *= a;
      b[x][y] *= a;
      g[x][y] *= a;
    }
  }
  
  
  
  r = fourPixelArray(r, widthFFT+1, heightFFT+1, -1, true);
  g = fourPixelArray(g, widthFFT+1, heightFFT+1, -1, true);
  b = fourPixelArray(b, widthFFT+1, heightFFT+1, -1, true);
  newImage.loadPixels();
  n = 0;
  for(int y = 0; y < source.height; y++) {
    for(int x = 0; x < source.width; x++) {
      newImage.pixels[n] = color(r[x][y], g[x][y], b[x][y]);
      n++;
    }
  }
  newImage.updatePixels();
  return newImage;
}


PImage enhance(PImage source, float radius) {
  PImage newImg = createImage(source.width, source.height, RGB);
  newImg.loadPixels();
  radius/=100;
  for(int n = 0; n < newImg.pixels.length; n++) {
    float red = red(source.pixels[n]);
    float green = green(source.pixels[n]);
    float blue = blue(source.pixels[n]);
    float ms = max(red,max(green,blue));
    red = red*pow(red/ms,radius);
    green = green*pow(green/ms,radius);
    blue = blue*pow(blue/ms,radius);
    newImg.pixels[n] = color(red,green,blue);
  }
  newImg.updatePixels();
  return newImg;
}


PImage hysteresis(PImage source, float radius) {
  PImage newImg = createImage(source.width, source.height, ALPHA);
  PImage hysteresis_x = hysteresis_x(source, radius);
  PImage hysteresis_y = hysteresis_y(source, radius);
  newImg.loadPixels();
  for(int n = 0; n < newImg.pixels.length; n++) {
    newImg.pixels[n] = (brightness(hysteresis_x.pixels[n]) == 255 && brightness(hysteresis_y.pixels[n]) == 255) ? color(255) : color(0);
  }
  newImg.updatePixels();
  return newImg;
}



PImage threshold(PImage source, float radius) {
  PImage newImg = createImage(source.width, source.height, ALPHA);
  newImg.loadPixels();
  for(int n = 0; n < newImg.pixels.length; n++) {
    newImg.pixels[n] = (brightness(source.pixels[n]) > radius) ? color(255) : color(0);
  }
  newImg.updatePixels();
  return newImg;
}



PImage hysteresis_y(PImage source, float radius) {
  PImage newImg = createImage(source.width, source.height, ALPHA);
  boolean on = false;
  newImg.loadPixels();
  for(int x = 0; x < newImg.width; x++) {
    for(int y = 0; y < newImg.height; y++) {
      if(on) {
        if(brightness(source.pixels[x + newImg.width * y]) < radius - radius / 4) {
          on = false;
        }
      }
      else {
        if(brightness(source.pixels[x + newImg.width * y]) > radius + radius / 4) {
          on = true;
        }
      }
      newImg.pixels[x + newImg.width * y] = (on) ? color(255) : color(0);
    }
  }
  newImg.updatePixels();
  return newImg;
}



PImage hysteresis_x(PImage source, float radius) {
  PImage newImg = createImage(source.width, source.height, ALPHA);
  boolean on = false;
  newImg.loadPixels();
  for(int y = 0; y < newImg.height; y++) {
    for(int x = 0; x < newImg.width; x++) {
      if(on) {
        if(brightness(source.pixels[x + newImg.width * y]) < radius - radius / 4) {
          on = false;
        }
      }
      else {
        if(brightness(source.pixels[x + newImg.width * y]) > radius + radius / 4) {
          on = true;
        }
      }
      newImg.pixels[x + newImg.width * y] = (on) ? color(255) : color(0);
    }
  }
  newImg.updatePixels();
  return newImg;
}




PImage filterEffect3(PImage source, float radius) {
  PImage newImg = filterImg(source, radius, true);
  newImg = filterImg(newImg, radius, false);
  for(int n = 0; n < newImg.pixels.length; n++) {
    if(brightness(newImg.pixels[n]) > 50) {
      newImg.pixels[n] = color(255, 255, 255);
    }
    else {
      newImg.pixels[n] = color(0, 0, 0);
    }
  }
  newImg = filterImg(newImg, radius, true);
  return newImg;
}




PImage filterEffect2(PImage source, float radius) {
  int widthFFT = int(pow(2, ceil(log(source.width) / log(2))));
  int heightFFT = int(pow(2, ceil(log(source.height) / log(2))));
  PImage newImage = createImage(source.width, source.height, RGB);
  float[][] r = new float[widthFFT + 1][heightFFT + 1];
  float[][] g = new float[widthFFT + 1][heightFFT + 1];
  float[][] b = new float[widthFFT + 1][heightFFT + 1];
  int n = 0;
  source.loadPixels();
  for(int y = 0; y < source.height; y++) {
    for(int x = 0; x < source.width; x++) {
      r[x][y] = red(source.pixels[n]);
      g[x][y] = green(source.pixels[n]);
      b[x][y] = blue(source.pixels[n]);
      n++;
    }
  }
  float[][] rFFT, gFFT, bFFT;
  rFFT = fourPixelArray(r, widthFFT+1, heightFFT+1, 1, false);
  gFFT = fourPixelArray(g, widthFFT+1, heightFFT+1, 1, false);
  bFFT = fourPixelArray(b, widthFFT+1, heightFFT+1, 1, false);
  r = new float[widthFFT + 1][heightFFT + 1];
  g = new float[widthFFT + 1][heightFFT + 1];
  b = new float[widthFFT + 1][heightFFT + 1];
  for(int y = 0; y < heightFFT; y++) {
    for(int x = 0; x < widthFFT; x++) {
      r[x][y] = rFFT[x][y];
      g[x][y] = gFFT[x][y];
      b[x][y] = bFFT[x][y];
    }
  }
  
  
  for(int y = 0; y < heightFFT; y++) {
    for(int x = 0; x < widthFFT; x++) {
      float temp = cos(mag(x, y) / radius);
      r[x][y] *= temp;
      b[x][y] *= temp;
      g[x][y] *= temp;
    }
  }
  
  
  
  r = fourPixelArray(r, widthFFT+1, heightFFT+1, -1, true);
  g = fourPixelArray(g, widthFFT+1, heightFFT+1, -1, true);
  b = fourPixelArray(b, widthFFT+1, heightFFT+1, -1, true);
  newImage.loadPixels();
  n = 0;
  for(int y = 0; y < source.height; y++) {
    for(int x = 0; x < source.width; x++) {
      newImage.pixels[n] = color(r[x][y], g[x][y], b[x][y]);
      n++;
    }
  }
  newImage.updatePixels();
  return newImage;
}





PImage filterEffect1(PImage source, float radius) {
  int widthFFT = int(pow(2, ceil(log(source.width) / log(2))));
  int heightFFT = int(pow(2, ceil(log(source.height) / log(2))));
  PImage newImage = createImage(source.width, source.height, RGB);
  float[][] r = new float[widthFFT + 1][heightFFT + 1];
  float[][] g = new float[widthFFT + 1][heightFFT + 1];
  float[][] b = new float[widthFFT + 1][heightFFT + 1];
  int n = 0;
  source.loadPixels();
  for(int y = 0; y < source.height; y++) {
    for(int x = 0; x < source.width; x++) {
      r[x][y] = red(source.pixels[n]);
      g[x][y] = green(source.pixels[n]);
      b[x][y] = blue(source.pixels[n]);
      n++;
    }
  }
  float[][] rFFT, gFFT, bFFT;
  rFFT = fourPixelArray(r, widthFFT+1, heightFFT+1, 1, false);
  gFFT = fourPixelArray(g, widthFFT+1, heightFFT+1, 1, false);
  bFFT = fourPixelArray(b, widthFFT+1, heightFFT+1, 1, false);
  r = new float[widthFFT + 1][heightFFT + 1];
  g = new float[widthFFT + 1][heightFFT + 1];
  b = new float[widthFFT + 1][heightFFT + 1];
  for(int y = 0; y < heightFFT; y++) {
    for(int x = 0; x < widthFFT; x++) {
      r[x][y] = rFFT[x][y];
      g[x][y] = gFFT[x][y];
      b[x][y] = bFFT[x][y];
    }
  }
  
  
  for(int y = 0; y < heightFFT; y++) {
    for(int x = 0; x < widthFFT; x++) {
      float temp = radius / (1 + abs(mag(x, y) - radius));
      r[x][y] *= temp;
      b[x][y] *= temp;
      g[x][y] *= temp;
    }
  }
  
  
  
  r = fourPixelArray(r, widthFFT+1, heightFFT+1, -1, true);
  g = fourPixelArray(g, widthFFT+1, heightFFT+1, -1, true);
  b = fourPixelArray(b, widthFFT+1, heightFFT+1, -1, true);
  newImage.loadPixels();
  n = 0;
  for(int y = 0; y < source.height; y++) {
    for(int x = 0; x < source.width; x++) {
      newImage.pixels[n] = color(r[x][y], g[x][y], b[x][y]);
      n++;
    }
  }
  newImage.updatePixels();
  return newImage;
}





PImage filterColorImage(PImage source, float radius, boolean highPass) {
  int widthFFT = int(pow(2, ceil(log(source.width) / log(2))));
  int heightFFT = int(pow(2, ceil(log(source.height) / log(2))));
  PImage newImage = createImage(source.width, source.height, RGB);
  float[][] r = new float[widthFFT + 1][heightFFT + 1];
  float[][] g = new float[widthFFT + 1][heightFFT + 1];
  float[][] b = new float[widthFFT + 1][heightFFT + 1];
  int n = 0;
  source.loadPixels();
  for(int y = 0; y < source.height; y++) {
    for(int x = 0; x < source.width; x++) {
      r[x][y] = red(source.pixels[n]);
      g[x][y] = green(source.pixels[n]);
      b[x][y] = blue(source.pixels[n]);
      n++;
    }
  }
  float[][] rFFT, gFFT, bFFT;
  rFFT = fourPixelArray(r, widthFFT+1, heightFFT+1, 1, false);
  gFFT = fourPixelArray(g, widthFFT+1, heightFFT+1, 1, false);
  bFFT = fourPixelArray(b, widthFFT+1, heightFFT+1, 1, false);
  r = new float[widthFFT + 1][heightFFT + 1];
  g = new float[widthFFT + 1][heightFFT + 1];
  b = new float[widthFFT + 1][heightFFT + 1];
  for(int y = 0; y < heightFFT; y++) {
    for(int x = 0; x < widthFFT; x++) {
      r[x][y] = rFFT[x][y];
      g[x][y] = gFFT[x][y];
      b[x][y] = bFFT[x][y];
    }
  }
  for(int y = 0; y < heightFFT; y++) {
    for(int x = 0; x < widthFFT; x++) {
      if(dist(x, y, 0, 0) < radius || dist(x, y, widthFFT, heightFFT) < radius || dist(x, y, widthFFT, 0) < radius || dist(x, y, 0, heightFFT) < radius) {
        if(highPass) {
          r[x][y] = 0;
          g[x][y] = 0;
          b[x][y] = 0;
        }
      }
      else if(!highPass) {
        r[x][y] = 0;
        g[x][y] = 0;
        b[x][y] = 0;
      }
    }
  }
  
  r = fourPixelArray(r, widthFFT+1, heightFFT+1, -1, true);
  g = fourPixelArray(g, widthFFT+1, heightFFT+1, -1, true);
  b = fourPixelArray(b, widthFFT+1, heightFFT+1, -1, true);
  newImage.loadPixels();
  n = 0;
  for(int y = 0; y < source.height; y++) {
    for(int x = 0; x < source.width; x++) {
      newImage.pixels[n] = color(r[x][y], g[x][y], b[x][y]);
      n++;
    }
  }
  newImage.updatePixels();
  return newImage;
}





PImage fourColorImage(PImage source, boolean log) {
  int widthFFT = 2 * int(pow(2, ceil(log(source.width) / log(2))));
  int heightFFT = 2 * int(pow(2, ceil(log(source.height) / log(2))));
  PImage newImage = createImage(widthFFT / 2, heightFFT / 2, RGB);
  float[][] r = new float[widthFFT + 1][heightFFT + 1];
  float[][] g = new float[widthFFT + 1][heightFFT + 1];
  float[][] b = new float[widthFFT + 1][heightFFT + 1];
  int n = 0;
  source.loadPixels();
  for(int y = 0; y < source.height; y++) {
    for(int x = 0; x < source.width; x++) {
      r[x][y] = red(source.pixels[n]);
      g[x][y] = green(source.pixels[n]);
      b[x][y] = blue(source.pixels[n]);
      n++;
    }
  }
  
  r = fourPixelArray(r, widthFFT, heightFFT, 1, log);
  g = fourPixelArray(g, widthFFT, heightFFT, 1, log);
  b = fourPixelArray(b, widthFFT, heightFFT, 1, log);
  newImage.loadPixels();
  n = 0;
  for(int y = 0; y < heightFFT / 2; y++) {
    for(int x = 0; x < widthFFT / 2; x++) {
      newImage.pixels[n] = color(r[x][y], g[x][y], b[x][y]);
      n++;
    }
  }
  newImage.updatePixels();
  return newImage;
}






float[][] fourPixelArray(float[][] array, int w, int h, int sign, boolean log) {
  
  //int widthFFT = int(pow(2, ceil(log(w) / log(2))));
  //int heightFFT = int(pow(2, ceil(log(h) / log(2))));
  int widthFFT = 1;
  int heightFFT = 1;
  while(widthFFT * 4 < w) {
    widthFFT *= 2;
  }
  while(heightFFT * 4 < h) {
    heightFFT *= 2;
  }
  
  float[][] xgsVals = new float[widthFFT * 4 + 1][heightFFT * 4 + 1];
  
  for(int y = 0; y < h; y++) {
    for(int x = 0; x < w; x++) {
      float val = array[x][y];
      xgsVals[x][y] = val;
    }
  }
  
  float xgsVals2[][];
  xgsVals2 = four2(xgsVals, widthFFT * 2, heightFFT * 2, sign);
  
    
    float maxValx = 0;
    for(int y = 0; y < heightFFT * 2; y++) {
      for(int x = 0; x < widthFFT * 2; x++) {
        if(abs(xgsVals2[x][y]) > maxValx) {
          maxValx = abs(xgsVals2[x][y]);
        }
      }
    }
    for(int yy = 0; yy < heightFFT * 2; yy++) {
      for(int xx = 0; xx < widthFFT * 2; xx++) {
        if(log) {
          float xval = 255 * log(1 + abs(xgsVals2[xx][yy])) / log(1 + maxValx);
          xgsVals2[xx][yy] = xval;
        }
      }
    }
    
  
  return xgsVals2;
}





public PImage crossCorrelation(PImage img1, PImage img2) {
  PImage cross;
  int maxWidth = max(img1.width, img2.width);
  int maxHeight = max(img1.height, img2.height);
  img1.loadPixels();
  img2.loadPixels();
  
  int widthFFT = 1, heightFFT = 1;
  while(widthFFT * 4 < maxWidth) {
    widthFFT *= 2;
  }
  while(heightFFT * 4 < maxHeight) {
    heightFFT *= 2;
  }
  
  float[][] gsVals = new float[widthFFT * 4 + 1][heightFFT * 4 + 1];
  
  for(int y = 0; y < heightFFT * 4 + 1; y++) {
    for(int x = 0; x < widthFFT * 4 + 1; x++) {
      if(x < img1.width && y < img1.height) {
        gsVals[x][y] = brightness(img1.pixels[y * img1.width + x]);
      }
    }
  }
  
  
    for(int row = 0; row < heightFFT * 4 + 1; row++) {
      float[] temp = new float[widthFFT * 4 + 1];
      for(int i = 0; i < widthFFT * 4 + 1; i++) {
        temp[i] = gsVals[i][row];
      }
      four1(temp, widthFFT * 2, 1);
      for(int i = 0; i < widthFFT * 4 + 1; i++) {
        gsVals[i][row] = temp[i];
      }
    }
    for(int col = 0; col < widthFFT * 4 + 1; col++) {
      float[] temp = new float[heightFFT * 4 + 1];
      for(int i = 0; i < heightFFT * 4 + 1; i++) {
        temp[i] = gsVals[col][i];
      }
      four1(temp, heightFFT * 2, 1);
      for(int i = 0; i < heightFFT * 4 + 1; i++) {
        gsVals[col][i] = temp[i];
      }
    }
    
  
  
  float[][] gsVals2 = new float[widthFFT * 4 + 1][heightFFT * 4 + 1];
  
  for(int y = 0; y < heightFFT * 4 + 1; y++) {
    for(int x = 0; x < widthFFT * 4 + 1; x++) {
      if(x < img2.width && y < img2.height) {
        gsVals2[x][y] = brightness(img2.pixels[y * img2.width + x]);
      }
    }
  }
  
  
    for(int row = 0; row < heightFFT * 4 + 1; row++) {
      float[] temp = new float[widthFFT * 4 + 1];
      for(int i = 0; i < widthFFT * 4 + 1; i++) {
        temp[i] = gsVals2[i][row];
      }
      four1(temp, widthFFT * 2, 1);
      for(int i = 0; i < widthFFT * 4 + 1; i++) {
        gsVals2[i][row] = temp[i];
      }
    }
    for(int col = 0; col < widthFFT * 4 + 1; col++) {
      float[] temp = new float[heightFFT * 4 + 1];
      for(int i = 0; i < heightFFT * 4 + 1; i++) {
        temp[i] = gsVals2[col][i];
      }
      four1(temp, heightFFT * 2, 1);
      for(int i = 0; i < heightFFT * 4 + 1; i++) {
        gsVals2[col][i] = temp[i];
      }
    }
  
  
  for(int y = 0; y < heightFFT * 4 + 1; y++) {
    for(int x = 0; x < widthFFT * 4 + 1; x++) {
      gsVals[x][y] = (gsVals[x][y] * gsVals2[widthFFT * 4 - x][heightFFT * 4 - y]) / (widthFFT * heightFFT);
    }
  }
  
  for(int row = 0; row < heightFFT * 4 + 1; row++) {
      float[] temp = new float[widthFFT * 4 + 1];
      for(int i = 0; i < widthFFT * 4 + 1; i++) {
        temp[i] = gsVals[i][row];
      }
      four1(temp, widthFFT * 2, -1);
      for(int i = 0; i < widthFFT * 4 + 1; i++) {
        gsVals[i][row] = temp[i];
      }
    }
    for(int col = 0; col < widthFFT * 4 + 1; col++) {
      float[] temp = new float[heightFFT * 4 + 1];
      for(int i = 0; i < heightFFT * 4 + 1; i++) {
        temp[i] = gsVals[col][i];
      }
      four1(temp, heightFFT * 2, -1);
      for(int i = 0; i < heightFFT * 4 + 1; i++) {
        gsVals[col][i] = temp[i];
      }
    }
    
  cross = createImage(maxWidth, maxHeight, ALPHA);

  cross.loadPixels();
  {
    float maxVal = 0;
    for(int y = 0; y < maxHeight; y++) {
      for(int x = 0; x < maxWidth; x++) {
        gsVals[x][y] /= (pow(widthFFT * heightFFT, 2));
        if(abs(gsVals[x][y]) > maxVal) {
          maxVal = abs(gsVals[x][y]);
        }
      }
    }
    int n = 0;
    for(int y = 0; y < maxHeight; y++) {
      for(int x = 0; x < maxWidth; x++) {
        float val;
        val = 255 * log(1 + abs(gsVals[x][y])) / log(1 + maxVal);
        cross.pixels[n] = color(val, val, val);
        n++;
      }
    }
    
  }
  cross.updatePixels();
  
  return cross;
}







public PImage spFilterImg(PImage img, float radius, boolean high) {
  
  img.loadPixels();
  
  int widthFFT = 1, heightFFT = 1;
  while(widthFFT * 4 < img.width) {
    widthFFT *= 2;
  }
  while(heightFFT * 4 < img.height) {
    heightFFT *= 2;
  }
  
  float[][] gsVals = new float[widthFFT * 4 + 1][heightFFT * 4 + 1];
  
  for(int y = 0; y < heightFFT * 4 + 1; y++) {
    for(int x = 0; x < widthFFT * 4 + 1; x++) {
      if(x < img.width && y < img.height) {
        gsVals[x][y] = brightness(img.pixels[y * img.width + x]);
      }
    }
  }
  
  

  for(int row = 0; row < heightFFT * 4 + 1; row++) {
    float[] temp = new float[widthFFT * 4 + 1];
    for(int i = 0; i < widthFFT * 4 + 1; i++) {
      temp[i] = gsVals[i][row];
    }
    four1(temp, widthFFT * 2, 1);
    for(int i = 0; i < widthFFT * 4 + 1; i++) {
      gsVals[i][row] = temp[i];
    }
  }
  for(int col = 0; col < widthFFT * 4 + 1; col++) {
    float[] temp = new float[heightFFT * 4 + 1];
    for(int i = 0; i < heightFFT * 4 + 1; i++) {
      temp[i] = gsVals[col][i];
    }
    four1(temp, heightFFT * 2, 1);
    for(int i = 0; i < heightFFT * 4 + 1; i++) {
      gsVals[col][i] = temp[i];
    }
  }
  
  float threshVal = 0;
  for(int y = 0; y < heightFFT * 4 + 1; y++) {
    for(int x = 0; x < widthFFT * 4 + 1; x++) {
      if(abs(gsVals[x][y]) > threshVal) {
        threshVal = abs(gsVals[x][y]);
      }
    }
  }
  for(int y = 0; y < heightFFT * 4 + 1; y++) {
    for(int x = 0; x < widthFFT * 4 + 1; x++) {
      if(abs(gsVals[x][y]) < threshVal / radius) {
        gsVals[x][y] = 0;
      }
    }
  }
  
  for(int row = 0; row < heightFFT * 4 + 1; row++) {
    float[] temp = new float[widthFFT * 4 + 1];
    for(int i = 0; i < widthFFT * 4 + 1; i++) {
      temp[i] = gsVals[i][row];
    }
    four1(temp, widthFFT * 2, -1);
    for(int i = 0; i < widthFFT * 4 + 1; i++) {
      gsVals[i][row] = temp[i];
    }
  }
  for(int col = 0; col < widthFFT * 4 + 1; col++) {
    float[] temp = new float[heightFFT * 4 + 1];
    for(int i = 0; i < heightFFT * 4 + 1; i++) {
      temp[i] = gsVals[col][i];
    }
    four1(temp, heightFFT * 2, -1);
    for(int i = 0; i < heightFFT * 4 + 1; i++) {
      gsVals[col][i] = temp[i];
    }
  }

  
  PImage fourImg = createImage(img.width, img.height, ALPHA);

  fourImg.loadPixels();
  {
    float maxVal = 0;
    for(int y = 0; y < img.height; y++) {
      for(int x = 0; x < img.width; x++) {
        gsVals[x][y] /= (pow(widthFFT * heightFFT, 2));
        if(abs(gsVals[x][y]) > maxVal) {
          maxVal = abs(gsVals[x][y]);
        }
      }
    }
    int n = 0;
    for(int y = 0; y < img.height; y++) {
      for(int x = 0; x < img.width; x++) {
        float val;
        val = 255 * log(1 + abs(gsVals[x][y])) / log(1 + maxVal);
        fourImg.pixels[n] = color(val, val, val);
        n++;
      }
    }
    
  }
  fourImg.updatePixels();
  return fourImg;
}







public PImage filterImg(PImage img, float radius, boolean high) {
  
  img.loadPixels();
  
  int widthFFT = 1, heightFFT = 1;
  while(widthFFT * 4 < img.width) {
    widthFFT *= 2;
  }
  while(heightFFT * 4 < img.height) {
    heightFFT *= 2;
  }
  
  float[][] gsVals = new float[widthFFT * 4 + 1][heightFFT * 4 + 1];
  
  for(int y = 0; y < heightFFT * 4 + 1; y++) {
    for(int x = 0; x < widthFFT * 4 + 1; x++) {
      if(x < img.width && y < img.height) {
        gsVals[x][y] = brightness(img.pixels[y * img.width + x]);
      }
    }
  }
  
  

  for(int row = 0; row < heightFFT * 4 + 1; row++) {
    float[] temp = new float[widthFFT * 4 + 1];
    for(int i = 0; i < widthFFT * 4 + 1; i++) {
      temp[i] = gsVals[i][row];
    }
    four1(temp, widthFFT * 2, 1);
    for(int i = 0; i < widthFFT * 4 + 1; i++) {
      gsVals[i][row] = temp[i];
    }
  }
  for(int col = 0; col < widthFFT * 4 + 1; col++) {
    float[] temp = new float[heightFFT * 4 + 1];
    for(int i = 0; i < heightFFT * 4 + 1; i++) {
      temp[i] = gsVals[col][i];
    }
    four1(temp, heightFFT * 2, 1);
    for(int i = 0; i < heightFFT * 4 + 1; i++) {
      gsVals[col][i] = temp[i];
    }
  }
  
  for(int y = 0; y < heightFFT * 4 + 1; y++) {
    for(int x = 0; x < widthFFT * 4 + 1; x++) {
      if(dist(x, y, 0, 0) < radius || dist(x, y, widthFFT * 4, heightFFT * 4) < radius || dist(x, y, widthFFT * 4, 0) < radius || dist(x, y, 0, heightFFT * 4) < radius) {
        if(high) {
          gsVals[x][y] = 0;
        }
      }
      else if(!high) {
        gsVals[x][y] = 0;
      }
    }
  }
  
  for(int row = 0; row < heightFFT * 4 + 1; row++) {
    float[] temp = new float[widthFFT * 4 + 1];
    for(int i = 0; i < widthFFT * 4 + 1; i++) {
      temp[i] = gsVals[i][row];
    }
    four1(temp, widthFFT * 2, -1);
    for(int i = 0; i < widthFFT * 4 + 1; i++) {
      gsVals[i][row] = temp[i];
    }
  }
  for(int col = 0; col < widthFFT * 4 + 1; col++) {
    float[] temp = new float[heightFFT * 4 + 1];
    for(int i = 0; i < heightFFT * 4 + 1; i++) {
      temp[i] = gsVals[col][i];
    }
    four1(temp, heightFFT * 2, -1);
    for(int i = 0; i < heightFFT * 4 + 1; i++) {
      gsVals[col][i] = temp[i];
    }
  }

  
  PImage fourImg = createImage(img.width, img.height, ALPHA);

  fourImg.loadPixels();
  {
    float maxVal = 0;
    for(int y = 0; y < img.height; y++) {
      for(int x = 0; x < img.width; x++) {
        gsVals[x][y] /= (pow(widthFFT * heightFFT, 2));
        if(abs(gsVals[x][y]) > maxVal) {
          maxVal = abs(gsVals[x][y]);
        }
      }
    }
    int n = 0;
    for(int y = 0; y < img.height; y++) {
      for(int x = 0; x < img.width; x++) {
        float val;
        val = 255 * log(1 + abs(gsVals[x][y])) / log(1 + maxVal);
        fourImg.pixels[n] = color(val, val, val);
        n++;
      }
    }
    
  }
  fourImg.updatePixels();
  return fourImg;
}

public PImage fourImg(PImage img) {
  return fourImg(img, 1, 1, true);
}







public PImage fourImg(PImage img, int sign, int numTransforms, boolean log) {
  img.loadPixels();
  
  int widthFFT = 1, heightFFT = 1;
  while(widthFFT * 4 < img.width) {
    widthFFT *= 2;
  }
  while(heightFFT * 4 < img.height) {
    heightFFT *= 2;
  }
  
  float[][] gsVals = new float[img.width][img.height];
  
  for(int y = 0; y < heightFFT * 4 + 1; y++) {
    for(int x = 0; x < widthFFT * 4 + 1; x++) {
      if(x < img.width && y < img.height) {
        gsVals[x][y] = brightness(img.pixels[y * img.width + x]);
      }
    }
  }
  
  float gsVals2[][] = four2(gsVals, img.width, img.height, 1);
  for(int cycle = 0; cycle < numTransforms - 1; cycle++) {
    gsVals2 = four2(gsVals2, widthFFT * 4, heightFFT * 4, 1);
  }
  
  PImage fourImg = createImage(widthFFT * 4, heightFFT * 4, ALPHA);
  

  fourImg.loadPixels();
  {
    /*
    for(int y = 0; y < heightFFT * 2; y++) {
      for(int x = 0; x < widthFFT * 2; x++) {
        gsVals2[widthFFT * 2 + x][heightFFT * 2 + y] = gsVals[x][y];
        gsVals2[x][heightFFT * 2 + y] = gsVals[x + widthFFT * 2][y];
        gsVals2[widthFFT * 2 + x][y] = gsVals[x][y + heightFFT * 2];
        gsVals2[x][y] = gsVals[x + widthFFT * 2][y + heightFFT * 2];
      }
    }
    */
    float maxVal = 0;
    for(int y = 0; y < 4 * heightFFT; y++) {
      for(int x = 0; x < 4 * widthFFT; x++) {
        if(abs(gsVals2[x][y]) > maxVal) {
          maxVal = abs(gsVals2[x][y]);
        }
      }
    }
    int n = 0;
    for(int y = 0; y < heightFFT * 4; y++) {
      for(int x = 0; x < widthFFT * 4; x++) {
        float val;
        if(log) {
          val = 255 * log(1 + abs(gsVals2[x][y])) / log(1 + maxVal);
        }
        else {
          val = gsVals2[x][y];
        }
        fourImg.pixels[n] = color(val, val, val);
        n++;
      }
    }
    
  }
  fourImg.updatePixels();
  return fourImg;
}







public float[][] four2(float data[][], int dataWidth, int dataHeight, int sign) {
  int widthFFT = 1, heightFFT = 1;
  while(widthFFT * 4 < dataWidth) {
    widthFFT *= 2;
  }
  while(heightFFT * 4 < dataHeight) {
    heightFFT *= 2;
  }
  
  float[][] gsVals = new float[widthFFT * 4 + 1][heightFFT * 4 + 1];
  
  for(int y = 0; y < heightFFT * 4 + 1; y++) {
    for(int x = 0; x < widthFFT * 4 + 1; x++) {
      if(x < dataWidth && y < dataHeight) {
        gsVals[x][y] = data[x][y];
      }
    }
  }
  

  for(int row = 0; row < heightFFT * 4 + 1; row++) {
    float[] temp = new float[widthFFT * 4 + 1];
    for(int i = 0; i < widthFFT * 4 + 1; i++) {
      temp[i] = gsVals[i][row];
    }
    four1(temp, widthFFT * 2, sign);
    for(int i = 0; i < widthFFT * 4 + 1; i++) {
      gsVals[i][row] = temp[i];
    }
  }
  for(int col = 0; col < widthFFT * 4 + 1; col++) {
    float[] temp = new float[heightFFT * 4 + 1];
    for(int i = 0; i < heightFFT * 4 + 1; i++) {
      temp[i] = gsVals[col][i];
    }
    four1(temp, heightFFT * 2, sign);
    for(int i = 0; i < heightFFT * 4 + 1; i++) {
      gsVals[col][i] = temp[i];
    }
  }
  
  float[][] gsVals2 = new float[widthFFT * 4][heightFFT * 4];
  
  for(int y = 0; y < 4 * heightFFT; y++) {
    for(int x = 0; x < 4 * widthFFT; x++) {
      gsVals2[x][y] = gsVals[x][y] / ((widthFFT * heightFFT));
    }
  }
  return gsVals2;
}







public float[] four1(float data[], int nn, int isign) {
 
  int i, j, n, mmax, m, istep;
  float wtemp, wr, wpr, wpi, wi, theta, tempr, tempi;
 
  n = nn << 1;
  j = 1;
  for(i = 1; i < n; i += 2) {
    if(j > i) {
      float temp;
      temp = data[j];
      data[j] = data[i];
      data[i] = temp;
      temp = data[j+1];
      data[j+1] = data[i+1];
      data[i+1] = temp;
    }
    m = n >> 1;
    while(m >= 2 && j > m) {
      j -= m;
      m >>= 1;
    }
    j += m;
  }
  mmax = 2;
  while(n > mmax) {
    istep = (mmax << 1);
    theta = isign*(6.28318530717959/mmax);
    wtemp = sin(0.5*theta);
    wpr = -2.0*wtemp*wtemp;
    wpi = sin(theta);
    wr = 1.0;
    wi = 0.0;
    for(m = 1; m < mmax; m += 2) {
      for(i = m; i <= n; i += istep) {
        j = i+mmax;
        tempr = wr*data[j]-wi*data[j+1];
        tempi = wr*data[j+1]+wi*data[j];
        data[j] = data[i] - tempr;
        data[j+1] = data[i+1] - tempi;
        data[i] += tempr;
        data[i+1] += tempi;
      }
    wr = (wtemp=wr)*wpr-wi*wpi+wr;
    wi = wi*wpr+wtemp*wpi+wi;
    }
    mmax = istep;
  }
  return data;
}

