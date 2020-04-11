import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.Request;

import java.io.IOException;

public class Input extends Thread {
    private final Floors floors;
    private ElevatorInput elevatorInput;
    private Elevators elevators;

    Input(Floors floors,ElevatorInput elevatorInput,Elevators elevators) {
        this.floors = floors;
        this.elevatorInput = elevatorInput;
        this.elevators = elevators;
    }

    @Override
    public void run() {
        /*int[] x = {1000,1300,1000,300,800};
        int i =0;*/
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                break;
            }
            else {
                /*try{
                    Thread.sleep(x[i]);
                    i++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
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
        synchronized (floors) {
            floors.notifyAll();
        }
    }

}
