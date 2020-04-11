import com.oocourse.elevator3.ElevatorRequest;

import java.util.ArrayList;

public class Elevators {
    private ArrayList<Elevator> elevators = new ArrayList<>();

    Elevators(Floors floors) {
        for (int i = 0;i < 3;i++) {
            elevators.add(new Elevator(floors,String.valueOf((char)('A' + i)),
                    String.valueOf((char)('A' + i)),this));
        }
    }

    public void start() {
        for (Elevator elevator : elevators) {
            elevator.start();
        }
    }

    public synchronized void addElevator(ElevatorRequest elevatorRequest,Floors floors) {
        elevators.add(new Elevator(floors,elevatorRequest.getElevatorType()
                ,elevatorRequest.getElevatorId(),this));
        elevators.get(elevators.size() - 1).start();
    }

    public synchronized boolean isEmpty() {
        for (Elevator elevator : elevators) {
            if (!elevator.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
