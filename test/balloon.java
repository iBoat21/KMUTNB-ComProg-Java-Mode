class Balloon {
  int x;
  int y;
  int r;

  Balloon(int x, int y, int r) {
    this.x = x;
    this.y = y;
    this.r = r;
  }

  void show() {
    line(this.x, this.y, this.x, this.y + 100);
    ellipse(this.x, this.y, this.r, this.r);
  }

  void move() {
    this.y = this.y - 2; 

    if (this.y < -100) {
      this.y = height + 50;
    }
  }
}

Balloon t;

void setup() {
  size(500, 500); 
  t = new Balloon(250, 400, 70); 
}

void draw() {
  background(255); 
  t.show();       
  t.move();       
}