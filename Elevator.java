import com.oocourse.TimableOutput;

import java.util.Collection;
import java.util.HashSet;

public class Elevator extends Thread {
    private String name;
    private int nowFloor;
    private final Floors floors;
    private Inside in;
    private int direction;
    private int arriveTime;
    private int maxMember;
    private HashSet<Integer> Inout = new HashSet<>();
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

        while (floors.getEmpty() == 0 || !floors.isEmpty() || !elevators.isEmpty())  {
            if (ifStop()) {
                break;
            }
            ifWait();

            while (!floors.isEmpty()) {
                if (ifStop()) {
                    break;
                }
                if (in.isEmpty()) {
                    /*if (floors.isThisEmpty(nowFloor)) {
                       direction = floors.changeDir(direction,nowFloor);
                    }*/
                    while (floors.isThisEmpty(nowFloor) && !floors.isEmpty()) {
                        direction = floors.changeDir(direction,nowFloor);
                        arriveSleep();
                        nowFloor = nowFloor + direction;
                        TimableOutput.println(String.format("ARRIVE-%d-%s",
                                trueFloor(nowFloor),name));
                    }
                    if (Inout.contains(nowFloor)){
                        TimableOutput.println(String.format("OPEN-%d-%s",
                                trueFloor(nowFloor),name));
                        inCloseSleep();
                        in.addPerson(floors.getPerson(nowFloor,maxMember,Inout),trueFloor(nowFloor),name);
                        inCloseSleep();
                        TimableOutput.println(String.format("CLOSE-%d-%s",trueFloor(nowFloor),name));
                    }
                }
                while (!in.isEmpty()) {
                    if (Inout.contains(nowFloor)) {
                        if (in.ifStop(nowFloor,Inout) || floors.ifStop(nowFloor, direction)) {
                            TimableOutput.println(String.format("OPEN-%d-%s", trueFloor(nowFloor), name));
                            inCloseSleep();
                            if (in.ifStop(nowFloor,Inout)) {
                                in.getOut(nowFloor, name,Inout);
                            }
                            if (in.isEmpty() && !floors.isThisEmpty(nowFloor)) {
                                in.addPerson(floors.getPerson(nowFloor, maxMember,Inout), trueFloor(nowFloor), name);
                            } else if (floors.ifStop(nowFloor, direction) && in.getNum() < 7) {
                                in.addPerson(floors.inPass(nowFloor, direction, maxMember - in.getNum(),Inout)
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
        Inout.add(1);
        Inout.add(2);
        Inout.add(3);
        Inout.add(4);
        Inout.add(18);
        Inout.add(19);
        Inout.add(20);
        Inout.add(21);
        Inout.add(22);
        Inout.add(23);
    }

    private void BSet() {
        Inout.add(2);
        Inout.add(3);
        Inout.add(4);
        Inout.add(5);
        Inout.add(7);
        Inout.add(8);
        Inout.add(9);
        Inout.add(10);
        Inout.add(11);
        Inout.add(12);
        Inout.add(13);
        Inout.add(14);
        Inout.add(15);
        Inout.add(16);
        Inout.add(17);
        Inout.add(18);
    }

    private void CSet() {
        Inout.add(4);
        Inout.add(6);
        Inout.add(8);
        Inout.add(10);
        Inout.add(12);
        Inout.add(14);
        Inout.add(16);
        Inout.add(18);
    }

    private boolean ifStop() {
        if (!elevators.isEmpty() || floors.getEmpty() == 0) {
            return false;
        }
        else if (floors.isEmpty()) {
            return true;
        }
        else return  !floors.ifGo(Inout) && !floors.CanWait(Inout);
    }

    public boolean isEmpty() {
        return in.isEmpty();
    }

    public void ifWait() {
        //System.out.println((!floors.isEmpty() && !floors.ifGo(Inout)));
        while ((!floors.isEmpty() && !floors.ifGo(Inout)) || !elevators.isEmpty() ||(floors.isEmpty() && floors.getEmpty() == 0)) {
            synchronized (floors){
                try {
                    //System.out.println(Thread.currentThread() + "wait"+name);
                    floors.wait();
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
        //System.out.println(Thread.currentThread() + "notify"+name);
    }

}
