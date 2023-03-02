public class Passenger {
    int current_floor;
    int dest_floor;
    double spawnTime; //time that the costumer arrived
    ElevatorStatus direction;

    Passenger(int cf, int df){
        System.out.println("New Passenger spawned at #" + cf  + ", destination floor: #" + df);
        current_floor = cf;
        dest_floor = df;
        if(cf < df) {
            direction = ElevatorStatus.GOING_UP;
        }else if(cf > df){
            direction = ElevatorStatus.GOING_DOWN;
        }
    }


}
