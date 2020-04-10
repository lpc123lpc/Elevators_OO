import java.util.ArrayList;
import java.util.HashSet;

public class Floor {
    private ArrayList<Person> people = new ArrayList<>();

    Floor() {
    }

    public void addPerson(Person person) {
        people.add(person);
    }

    public  ArrayList<Person> getPeople(int nowFloor,int num,HashSet<Integer> Inout) {
        int min = 19;
        Person temp = people.get(0);
        for (Person person : people) {
            int distance = Math.abs(person.getToFloor() - nowFloor);
            if (distance < min) {
                min = distance;
                temp = person;
            }
        }
        int dir = temp.getDirection();
        return inPass(dir,num,nowFloor,Inout);
    }

    public boolean isEmpty() {
        return people.size() == 0;
    }

    public synchronized ArrayList<Person> inPass(int direction,int num,int nowFloor,HashSet<Integer> Inout) {
        int count = 0;
        ArrayList<Person> in = new ArrayList<>();
        for (Person person: people) {
            if (person.getDirection() == direction) {
                if (count >= num) {
                    break;
                }
                else {
                    if (nowFloor == 4 ||nowFloor == 18) {
                        if (Inout.contains(person.getToFloor())) {
                            in.add(person);
                            count ++;
                        }
                    }
                    else {
                        in.add(person);
                        count++;
                    }
                }
            }
        }
        people.removeAll(in);
        return in;
    }

    public synchronized boolean ifInPass(int direction) {
        for (Person person: people) {
            if (person.getDirection() == direction) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean ifGo(HashSet<Integer> Inout) {
        for (Person person : people) {
            int from;
            int to;
            from = person.getFromFloor();
            if (Inout.contains(from)){
                if (from == 4 || from == 18) {
                    to = person.getToFloor();
                    if (Inout.contains(to)) {
                        return true;
                    }
                }
                else {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean CanWait (HashSet<Integer> Inout) {
        for (Person person :people) {
            if (Inout.contains(person.getToFloor())){
                return true;
            }
        }
        return false;
    }
}
