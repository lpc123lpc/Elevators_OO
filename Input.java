import com.oocourse.elevator3.*;

import java.io.IOException;

public class Input extends Thread {
    private Floors floors;
    private ElevatorInput elevatorInput;
    private Elevators elevators;

    Input(Floors floors,ElevatorInput elevatorInput,Elevators elevators) {
        this.floors = floors;
        this.elevatorInput = elevatorInput;
        this.elevators = elevators;
    }

    @Override
    public void run() {
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                break;
            }
            else {
                if (request instanceof PersonRequest) {
                    PersonRequest personRequest = (PersonRequest)request;
                    Person person = new Person(personRequest.getPersonId(),
                            personRequest.getFromFloor(),personRequest.getToFloor());
                    floors.addPerson(person);
                }
                else if (request instanceof ElevatorRequest) {
                    ElevatorRequest elevatorRequest = (ElevatorRequest)request;
                    elevators.addElevator(elevatorRequest,floors);
                }
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        floors.setEmpty(1);
    }

}
