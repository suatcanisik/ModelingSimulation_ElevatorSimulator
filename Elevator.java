import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Elevator {
    int id;
    int currentFloor; //holds passangers that are in the elevator currently
    static int lock = 0;
    ElevatorStatus status; //direction

    List<Passenger> costumers = new ArrayList<>();
    Elevator(int i){
        id = i;
        currentFloor =0;
        status = ElevatorStatus.IDLE;
    }

    //add or remove passenger from costumer array
    void addPassenger(Passenger p){
        costumers.add(p);
    }
    void removePassenger(Passenger p){
        costumers.remove(p);
        System.out.println("yo removed");
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    boolean isIdle(){
        return status == ElevatorStatus.IDLE;
    }

    //functions to pick up people along the way
    void pickGoingUP(int cf,int df){
        for(int i =cf; i<=df;i++){
            for(int j =0; j<Main.floors.get(i).size();j++){
                Passenger p = Main.floors.get(i).get(j);
                if(p.direction == ElevatorStatus.GOING_UP){
                    costumers.add(p);
                    Main.floors.get(i).remove(p);
                    System.out.println("Elevator "+id+" picked passenger from floor"+i);
                }
            }
        }
    }
    void pickGoingDOWN(int cf,int df){
        for(int i =cf; i>=df;i--){
            for(int j =0; j<Main.floors.get(i).size();j++){
                Passenger p = Main.floors.get(i).get(j);
                if(p.direction == ElevatorStatus.GOING_DOWN){
                    costumers.add(p);
                    Main.floors.get(i).remove(p);
                    System.out.println("Elevator "+id+" picked passenger from floor"+i);
                }
            }
        }
    }

    //move function that picks up passangers from the event list
    void goToFloor(int floor) {
        lock = 1;

        //assign the direction
        if(currentFloor < floor) {
            status = ElevatorStatus.GOING_UP;
        }else if(currentFloor > floor){
            status = ElevatorStatus.GOING_DOWN;
        }
        System.out.println("[Elevator #" + id + "] Going" + status+ ", " + currentFloor+"->"+floor);


        pickGoingUP(currentFloor,floor);
        pickGoingDOWN(currentFloor,floor);



        //slow it down
        try {
            int floors = 0;
            if(currentFloor > floor){
                floors = currentFloor - floor;
            }else floors =  floor - currentFloor;

            Thread.sleep(floors * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        currentFloor = floor;
        status = ElevatorStatus.IDLE;
        lock = 0;
    }
}