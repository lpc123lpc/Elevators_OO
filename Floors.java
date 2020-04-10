import java.util.ArrayList;
import java.util.HashSet;

public class Floors {
    private ArrayList<Floor> floors = new ArrayList<>();
    private int empty = 0;

    Floors() {
        for (int i = 1; i <= 23;i++) {
            floors.add(new Floor());
        }
    }

    public ArrayList<Floor> getFloors() {
        return floors;
    }

    public synchronized void  addPerson(Person person) {
        floors.get(person.getFromFloor() - 1).addPerson(person);
        //System.out.println("notifyAll");
        notifyAll();
    }

    public synchronized boolean isEmpty() {
        int flag = 0;
        for (Floor floor : floors) {
            //System.out.print(floor.isEmpty());
            if (!floor.isEmpty()) {
                flag = 1;
            }
        }
        return flag != 1;
    }

    public synchronized ArrayList<Person> getPerson(int nowFloor,int num,HashSet<Integer> inout) {
        if (floors.get(nowFloor - 1).isEmpty()) {
            return null;
        }
        else {
            return floors.get(nowFloor - 1).getPeople(nowFloor,num,inout);
        }
    }

    public synchronized int getHighPer(HashSet<Integer> inout) {
        int high = 1;
        for (Floor floor : floors) {
            if (!floor.isEmpty() && inout.contains(floors.indexOf(floor) + 1)) {
                high = floors.indexOf(floor) + 1;
            }
        }

        return high;
    }

    public synchronized int getLowPer(HashSet<Integer> inout) {
        int low = 23;
        for (Floor floor :floors) {
            if (!floor.isEmpty() && inout.contains(floors.indexOf(floor) + 1)) {
                return floors.indexOf(floor) + 1;
            }
        }

        return low;
    }

    public synchronized int firstDir(int nowFloor,HashSet<Integer> inout) {
        /*if (direction == 1) {
            if (nowFloor < getHighPer()) {
                return 1;
            } else {
                return -1;
            }
        } else {
            if (nowFloor > getLowPer()) {
                return -1;
            } else {
                return 1;
            }
        }*/
        int high = getHighPer(inout);
        int low = getLowPer(inout);
        if (nowFloor < low) {
            return 1;
        }
        else if (nowFloor > high) {
            return -1;
        }
        else {
            if (nowFloor - low > high - nowFloor) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }

    public synchronized boolean isThisEmpty(int nowFloor) {
        return floors.get(nowFloor - 1).isEmpty();
    }

    public synchronized boolean ifStop(int nowFloor,int direction) {
        Floor floor = floors.get(nowFloor - 1);
        return floor.ifInPass(direction);
    }

    public synchronized ArrayList<Person> inPass(int nowFloor,
                                                 int direction,int num,HashSet<Integer> inout) {
        return floors.get(nowFloor - 1).inPass(direction,num,nowFloor,inout);
    }

    public void setEmpty(int empty) {
        this.empty = empty;
    }

    public synchronized int getEmpty() {
        return empty;
    }

    public synchronized void Wait(boolean flag) {
        //System.out.println(Thread.currentThread().getId() + "wait");
        if (flag) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized boolean ifGo(HashSet<Integer> inout) {
        for (Floor floor : floors) {
            if (floor.ifGo(inout)) {
                //System.out.println(i);
                return true;
            }
        }
        return false;
    }

    public synchronized boolean CanWait(HashSet<Integer> inout) {
        for (Floor floor :floors) {
            if (floor.CanWait(inout)) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean NotStop(int nowFloor,HashSet<Integer> inout) {
        if (isThisEmpty(nowFloor) || !inout.contains(nowFloor)) {
            return true;
        }
        else if (nowFloor == 4 || nowFloor == 18) {
            return !floors.get(nowFloor - 1).CanWait(inout);
        }
        else {
            return false;
        }
    }

    public synchronized int changeDir(int direction,int nowFloor,HashSet<Integer> inout) {
        if (direction == 1) {
            if (nowFloor < getHighPer(inout)) {
                return 1;
            } else {
                return -1;
            }
        } else {
            if (nowFloor > getLowPer(inout)) {
                return -1;
            } else {
                return 1;
            }
        }
    }

}
