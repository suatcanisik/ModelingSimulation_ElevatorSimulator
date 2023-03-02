import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Controller {
    ConcurrentLinkedQueue eventList = new ConcurrentLinkedQueue<Passenger>() ;
    List<Elevator> elevatorList = new ArrayList<Elevator>();

    List<Double> wait_time = new ArrayList<Double>();  //list for recorded waiting times

    void output(){
        String e5,e4,e3,e2,e1,e0;
        e5=e4=e3=e2=e1=e0="";
        for(int i=0; i<4;i++){
            if(elevatorList.get(i).currentFloor ==5 ){e5 +="["+i+"]";}
            if(elevatorList.get(i).currentFloor ==4 ){e4 +="["+i+"]";}
            if(elevatorList.get(i).currentFloor ==3 ){e3 +="["+i+"]";}
            if(elevatorList.get(i).currentFloor ==2 ){e2 +="["+i+"]";}
            if(elevatorList.get(i).currentFloor ==1 ){e1 +="["+i+"]";}
            if(elevatorList.get(i).currentFloor ==0 ){e0 +="["+i+"]";}
        }
        System.out.println("5:  "+Main.floors.get(5).size()+e5);
        System.out.println("4:  "+Main.floors.get(4).size()+e4);
        System.out.println("3:  "+Main.floors.get(3).size()+e3);
        System.out.println("2:  "+Main.floors.get(2).size()+e2);
        System.out.println("1:  "+Main.floors.get(1).size()+e1);
        System.out.println("0:  "+Main.floors.get(0).size()+e0);

        for (int i =0;i<elevatorList.size();i++){
            System.out.println("Elevator "+i+": "+elevatorList.get(i).costumers.size());
        }
        System.out.println("=====================================");
        System.out.println(" "+wait_time.size() + " number of people arrived their destination");
        System.out.print(" Average waiting time: ");
        double sum = 0;
        for (int i =0;i<wait_time.size();i++){
            sum += wait_time.get(i);
        }
        System.out.println(sum/(float)wait_time.size() +" seconds");
        System.out.println("=====================================");

    }


    void registerElevator(Elevator e){
        //System.out.println("Registering elevator #" + e.id);
        //System.out.println("Current elevator list size " + elevatorList.size());
        Elevator.lock = 1;
        elevatorList.add(e);
        //System.out.println("New elevator list size " + elevatorList.size());

    }

    void request(Passenger p ) throws InterruptedException {
        output();
        eventList.add(p);
        //System.out.println("[Controller] New passenger at floor #" + p.current_floor);
    }

    Elevator getIdleElevator(){
        for (int i = 0; i < elevatorList.size(); i++) {
            if(elevatorList.get(i).isIdle())

                return elevatorList.get(i);
        }

        return null;
    }

    void run(){

        while(true){
            if(!eventList.isEmpty()){

                Passenger p  = (Passenger) eventList.poll();
                Thread et = new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        while(Elevator.lock == 1); // wait for lock
                        Elevator e = getIdleElevator();
                        if(e != null){

                            System.out.println("[Elevator #" + e.id + "] picking up passenger from floor #" + p.current_floor);
                            e.addPassenger(p);
                            Main.floors.get(p.current_floor).remove(p);
                            if(e.getCurrentFloor() == p.current_floor){
                                System.out.println("[Controller] Elevator is already at passenger floor");

                                e.goToFloor(p.dest_floor);


                            }else{

                                e.goToFloor(p.current_floor);
                                e.goToFloor(p.dest_floor);

                                System.out.println("[Elevator #" + e.id +  "] Dropped passenger at floor " + e.currentFloor);
                                double waitingT = Main.current_time - p.spawnTime;
                                wait_time.add(waitingT);
                                e.removePassenger(p);

                            }
                        }

                    }
                };
                et.start();
                Elevator.lock = 0;

            }
        }
    }

}