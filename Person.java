public class Person {
    private int id;
    private int fromFloor;
    private int toFloor;
    private int direction;

    Person(int id,int fromFloor,int toFloor) {
        this.id = id;
        if (fromFloor < 0) {
            this.fromFloor = fromFloor + 4;
        }
        else {
            this.fromFloor = fromFloor + 3;
        }
        if (toFloor < 0) {
            this.toFloor = toFloor + 4;
        }
        else {
            this.toFloor = toFloor + 3;
        }
        this.direction = Direction();
    }

    public int getId() {
        return id;
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public int getToFloor() {
        return toFloor;
    }

    public int getTime() {
        return Math.abs(fromFloor - toFloor);
    }

    public void setFromFloor(int fromFloor) {
        this.fromFloor = fromFloor;
        direction = Direction();
        
    }

    public int getDirection() {
        return direction;
    }

    private int Direction() {
        if (fromFloor > toFloor) {
            return -1;
        }
        else {
            return 1;
        }
    }

}
