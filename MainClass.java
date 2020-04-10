import com.oocourse.elevator3.ElevatorInput;

public class MainClass {

    public static void main(String [] args) {
        Floors floors = new Floors();
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        Elevators elevator = new Elevators(floors);
        Input input = new Input(floors,elevatorInput,elevator);
        input.start();
        elevator.start();

    }
}
