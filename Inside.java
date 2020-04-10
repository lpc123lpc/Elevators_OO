import com.oocourse.TimableOutput;

import java.util.ArrayList;
import java.util.HashSet;

public class Inside {

    private ArrayList<Person> insidePeople = new ArrayList<>();
    private int num = 0;
    private Floors floors;

    Inside(Floors floors) {
        this.floors = floors;
    }

    public void addPerson(ArrayList<Person> people,int nowFloor,String name) {
        try {
            for (Person person : people) {
                TimableOutput.println(String.format("IN-%d-%d-%s",person.getId(),nowFloor,name));
                insidePeople.add(person);
                num++;
            }
        } catch (NullPointerException n) {
            //没有拉到人，好气。
        }

    }

    public int getDirection() {
        return insidePeople.get(0).getDirection();
    }

    public boolean isEmpty() {
        return insidePeople.size() == 0;
    }

    public void getOut(int nowFloor,String name,HashSet<Integer> inout) {
        ArrayList<Person> out = new ArrayList<>();
        for (Person person : insidePeople) {
            if (person.getToFloor() == nowFloor) {
                out.add(person);
                TimableOutput.println(String.format("OUT-%d-%d-%s",person.getId(),
                        trueFloor(nowFloor),name));
                num--;
            }
        }
        insidePeople.removeAll(out);
        if (nowFloor == 4 || nowFloor == 18) {
            changeEle(inout,nowFloor,name);
        }
    }

    public boolean ifStop(int nowFloor,HashSet<Integer> inout) {
        for (Person person : insidePeople) {
            if (person.getToFloor() == nowFloor) {
                return true;
            }
            if (nowFloor == 4 || nowFloor == 18) {
                if (!inout.contains(person.getToFloor())) {
                    return true;
                }
            }
        }
        return false;
    }

    private int trueFloor(int nowFloor) {
        if (nowFloor >= 4) {
            return nowFloor - 3;
        }
        else {
            return nowFloor - 4;
        }
    }

    public int getNum() {
        return num;
    }

    public void changeEle(HashSet<Integer> inout,int nowFloor,String name) {
        ArrayList<Person> out = new ArrayList<>();
        for (Person person :insidePeople) {
            if (!inout.contains(person.getToFloor())) {
                person.setFromFloor(nowFloor);
                out.add(person);
            }
        }
        insidePeople.removeAll(out);
        for (Person person : out) {
            TimableOutput.println(String.format("OUT-%d-%d-%s",person.getId(),
                    trueFloor(nowFloor),name));
            floors.addPerson(person);
        }
    }
}
