float g = 9.8;
float thetaDeg = 22.0;
float theta;
float sinTheta;
float cosTheta;
float dt = 0.035;

float startX = 80;
float startY = 70;
float rampLengthM = 7.5;
float visualScale = 62.0;
float radiusPx = 16.0;
float radiusM;

boolean isRunning = true;
int finishCount = 0;

class Racer {
  String name;
  float c;
  int r, gCol, b;
  float s;
  float v;
  float thetaRot;
  int rank;

  Racer(String name, float c, int r, int gCol, int b) {
    this.name = name;
    this.c = c;
    this.r = r;
    this.gCol = gCol;
    this.b = b;
    this.reset();
  }

  void reset() {
    this.s = 0.0;
    this.v = 0.0;
    this.thetaRot = 0.0;
    this.rank = 0;
  }

  void move() {
    if (this.s < rampLengthM) {
      float a = (g * sinTheta) / (1.0 + this.c);
      this.v = this.v + (a * dt);
      float ds = this.v * dt;
      this.s = this.s + ds;

      this.thetaRot = this.thetaRot + (ds / radiusM);

      if (this.s >= rampLengthM) {
        this.s = rampLengthM;
        finishCount = finishCount + 1;
        this.rank = finishCount;
      }
    }
  }

  void show() {
    fill(this.r, this.gCol, this.b);
    stroke(255);
    strokeWeight(1.5);

    float posAlong = this.s * visualScale;
    float cx = startX + (posAlong * cosTheta);
    float cy = startY + (posAlong * sinTheta);

    ellipse(cx, cy, radiusPx * 2, radiusPx * 2);

    stroke(20);
    strokeWeight(2);
    float ang = this.thetaRot;
    float dx1 = radiusPx * cos(ang);
    float dy1 = radiusPx * sin(ang);
    float dx2 = radiusPx * cos(ang + HALF_PI);
    float dy2 = radiusPx * sin(ang + HALF_PI);
    line(cx - dx1, cy - dy1, cx + dx1, cy + dy1);
    line(cx - dx2, cy - dy2, cx + dx2, cy + dy2);

    if (this.rank > 0) {
      fill(255, 230, 80);
      textSize(12);
      text("Rank " + this.rank, cx - 18, cy - 22);
    }
  }
}

Racer[] racers;

void setup() {
  size(600, 420);

  theta = radians(thetaDeg);
  sinTheta = sin(theta);
  cosTheta = cos(theta);
  radiusM = radiusPx / visualScale;

  racers = new Racer[3];
  racers[0] = new Racer("1. Solid Sphere (c = 0.4)", 0.4, 255, 100, 100);
  racers[1] = new Racer("2. Solid Cylinder (c = 0.5)", 0.5, 100, 200, 255);
  racers[2] = new Racer("3. Hollow Cylinder (c = 1.0)", 1.0, 120, 240, 150);
}

void draw() {
  background(25, 28, 36);

  if (isRunning) {
    boolean allFinished = true;
    for (int i = 0; i < racers.length; i = i + 1) {
      if (racers[i].s < rampLengthM) {
        allFinished = false;
      }
      racers[i].move();
    }
    if (allFinished) {
      isRunning = false;
    }
  }

  float endX = startX + (rampLengthM * visualScale * cosTheta);
  float endY = startY + (rampLengthM * visualScale * sinTheta);

  stroke(180);
  strokeWeight(3);
  line(startX - 10, startY + radiusPx, endX + 30, endY + radiusPx);

  stroke(255, 60, 60);
  strokeWeight(2);
  line(endX, endY - 25, endX, endY + radiusPx + 10);

  fill(255, 80, 80);
  textSize(11);
  text("FINISH", endX - 15, endY - 30);

  for (int i = 0; i < racers.length; i = i + 1) {
    racers[i].show();
  }

  drawHud();
}

void drawHud() {
  fill(18, 20, 26);
  stroke(60);
  strokeWeight(1);
  rectMode(CORNER);
  rect(30, 250, 540, 150);

  fill(255);
  textSize(13);
  text("Rotational Inertia Race (Rolling without Slipping)", 45, 272);

  textSize(11);
  fill(170);
  text("Formula: a = (g * sin) / (1 + c)  |  Incline angle = 22 deg", 45, 292);

  for (int i = 0; i < racers.length; i = i + 1) {
    fill(racers[i].r, racers[i].gCol, racers[i].b);
    float rowY = 318 + (i * 24);

    float roundedV = ((int)(racers[i].v * 100)) / 100.0;
    String status = "v = " + roundedV + " m/s";

    if (racers[i].rank > 0) {
      status = status + " (FINISHED #" + racers[i].rank + ")";
    }
    text(racers[i].name + " -> " + status, 45, rowY);
  }

  fill(200, 200, 100);
  text("Press [SPACE] or [R] to Restart Race", 320, 272);
}

void resetRace() {
  finishCount = 0;
  isRunning = true;
  for (int i = 0; i < racers.length; i = i + 1) {
    racers[i].reset();
  }
}

void keyPressed() {
  if (key == ' ' || key == 'r' || key == 'R') {
    resetRace();
  }
}