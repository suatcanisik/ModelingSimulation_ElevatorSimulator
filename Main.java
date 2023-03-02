import java.util.*;

//simulation is 6 times faster than real time clock, so current time will increase 6 times faster ( 12 second = 2 second in simulation)
public class Main {
    static ArrayList<ArrayList<Passenger>> floors = new ArrayList<ArrayList<Passenger>>();
    static double current_time;
    static Passenger createRandomPassenger(int floor){
        Random rand = new Random();
        int dest_floor = rand.nextInt(6); // only 6 floors
        if(dest_floor == floor){
            // try again
            return createRandomPassenger(floor);
        }else{
            return new Passenger(floor, dest_floor);
        }

    }

    public static void main(String[] args) {
        System.out.println();
        //create controller
        Controller controller = new Controller();
        for(int i=0; i<4; i++)
            controller.registerElevator(new Elevator(i));

        // empty list of passengers for each floor
        for(int i=0; i<6; i++)
            floors.add(new ArrayList<>());

        //following two threads will work in parallel 1:create costumer 2:elevators

        //thread 1 for creating random passengers
        Thread t1 = new Thread(){
            @Override
            public void run() {
                super.run();
                while(true){

                    Random rand = new Random();
                    int randFloorIndex = rand.nextInt(5)+1;
                    ArrayList<Passenger> firstFloor = floors.get(0);
                    ArrayList<Passenger> randFloor = floors.get(randFloorIndex);

                    // add 2 passengers; 1 for floor 0 , 1 for floors 1-5
                    Passenger p1 =  createRandomPassenger(randFloorIndex);
                    Passenger p2 =  createRandomPassenger(0);

                    randFloor.add(p1);
                    firstFloor.add(p2);

                    //incerase the current time


                    // both passengers request elevator
                    try {
                        controller.request(p1);
                        controller.request(p2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //repeat every 12 seconds to simulate 5/min
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };


        t1.start();


        //thread 2 for elevators
        Thread t2 = new Thread() {
            @Override
            public void run() {
                super.run();
                controller.run();

            }
        };
        t2.start();
        Thread t3 = new Thread() {
            @Override
            public void run() {
                super.run();
                current_time += 0.36;
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        t3.start();


    }
}
