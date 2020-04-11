import com.oocourse.TimableOutput;
import java.util.HashSet;

public class Elevator extends Thread {
    private String name;
    private int nowFloor;
    private final Floors floors;
    private Inside in;
    private int direction;
    private int arriveTime;
    private int maxMember;
    private HashSet<Integer> inout = new HashSet<>();
    private Elevators elevators;

    Elevator(Floors floors,String type,String name,Elevators elevators) {
        this.nowFloor = 4;
        this.floors = floors;
        in = new Inside(floors);
        this.direction = 1;
        this.name = name;
        this.elevators = elevators;
        switch (type) {
            case "A":
                arriveTime = 400;
                maxMember = 6;
                ASet();
                break;
            case "B":
                arriveTime = 500;
                maxMember = 8;
                BSet();
                break;
            default:
                arriveTime = 600;
                maxMember = 7;
                CSet();
        }
    }

    @Override
    public void run() {
        TimableOutput.initStartTimestamp();
        while (floors.getEmpty() == 0 || !floors.isEmpty() || !elevators.isEmpty()) {
            //System.out.println(ifStop()+name);
            if (ifStop()) {
                break;
            }
            //ifWait();

            while (!floors.isEmpty()) {
                if (ifStop()) {
                    break;
                }
                if (in.isEmpty()) {
                    InEmpty();
                }
                while (!in.isEmpty()) {
                    if (inout.contains(nowFloor)) {
                        if (in.ifStop(nowFloor, inout) ||
                                floors.ifStop(nowFloor, direction)) {
                            TimableOutput.println(String.format("OPEN-%d-%s",
                                    trueFloor(nowFloor), name));
                            inCloseSleep();
                            if (in.ifStop(nowFloor, inout)) {
                                in.getOut(nowFloor, name, inout);
                            }
                            if (in.isEmpty() && !floors.isThisEmpty(nowFloor)) {
                                in.addPerson(floors.getPerson(nowFloor, maxMember, inout),
                                        trueFloor(nowFloor), name);
                            } else if (floors.ifStop(nowFloor, direction) && in.getNum() < 7) {
                                in.addPerson(floors.inPass(nowFloor, direction,
                                        maxMember - in.getNum(), inout)
                                        , trueFloor(nowFloor), name);
                            }
                            inCloseSleep();
                            TimableOutput.println(String.format("CLOSE-%d-%s",
                                    trueFloor(nowFloor), name));
                        }
                    }
                    if (!in.isEmpty()) {
                        direction = in.getDirection();
                        arriveSleep();
                        nowFloor = nowFloor + direction;
                        TimableOutput.println(String.format("ARRIVE-%d-%s",
                                trueFloor(nowFloor),name));
                    }
                }
            }
        }
        synchronized (floors) {
            floors.notifyAll();
        }
        //System.out.println("Elevator" + name + "over");
    }

    private void arriveSleep() {
        try {
            Thread.sleep(arriveTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void inCloseSleep() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int trueFloor(int floor) {
        if (floor >= 4) {
            return floor - 3;
        }
        else {
            return floor - 4;
        }
    }

    private void ASet() {
        inout.add(1);
        inout.add(2);
        inout.add(3);
        inout.add(4);
        inout.add(18);
        inout.add(19);
        inout.add(20);
        inout.add(21);
        inout.add(22);
        inout.add(23);
    }

    private void BSet() {
        inout.add(2);
        inout.add(3);
        inout.add(4);
        inout.add(5);
        inout.add(7);
        inout.add(8);
        inout.add(9);
        inout.add(10);
        inout.add(11);
        inout.add(12);
        inout.add(13);
        inout.add(14);
        inout.add(15);
        inout.add(16);
        inout.add(17);
        inout.add(18);
    }

    private void CSet() {
        inout.add(4);
        inout.add(6);
        inout.add(8);
        inout.add(10);
        inout.add(12);
        inout.add(14);
        inout.add(16);
        inout.add(18);
    }

    private void InEmpty() {
        if (floors.NotStop(nowFloor,inout) && !floors.isEmpty()) {
            direction = floors.firstDir(nowFloor,inout);
        }
        while (floors.NotStop(nowFloor,inout) && !floors.isEmpty()) {
            //System.out.println(ifStop()+name);
            if (ifStop()) {
                break;
            }
            //ifWait();
            direction = floors.changeDir(direction,nowFloor,inout);

            arriveSleep();
            nowFloor = nowFloor + direction;
            //System.out.println(nowFloor);
            TimableOutput.println(String.format("ARRIVE-%d-%s",
                    trueFloor(nowFloor),name));
        }
        if (inout.contains(nowFloor)) {
            TimableOutput.println(String.format("OPEN-%d-%s",
                    trueFloor(nowFloor),name));
            inCloseSleep();
            in.addPerson(floors.getPerson(nowFloor,maxMember, inout),
                    trueFloor(nowFloor),name);
            inCloseSleep();
            TimableOutput.println(String.format("CLOSE-%d-%s",
                    trueFloor(nowFloor),name));
        }
    }

    private boolean ifStop() {

        if (!elevators.isEmpty() || floors.getEmpty() == 0) {
            //System.out.println(1);
            return false;
        }
        else if (floors.isEmpty()) {
            //System.out.println(2);
            return true;
        }
        else {
            //System.out.println("3" + floors.isEmpty());
            return  !floors.ifGo(inout) && !floors.CanWait(inout);
        }
    }

    public synchronized boolean isEmpty() {
        return in.isEmpty();
    }

    public void ifWait() {
        while ((!floors.isEmpty() && !floors.ifGo(inout)) ||
                !elevators.isEmpty() || (floors.isEmpty() && floors.getEmpty() == 0)) {
            //System.out.println((!floors.isEmpty() && !floors.ifGo(inout)));
            //System.out.println(!elevators.isEmpty());
            //System.out.println((floors.isEmpty() && floors.getEmpty() == 0));
            synchronized (floors) {
                try {
                    //System.out.println(Thread.currentThread() + "wait"+name);
                    floors.wait();
                    //System.out.println(Thread.currentThread() + "notify"+name);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        /*try{
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

    }

}
