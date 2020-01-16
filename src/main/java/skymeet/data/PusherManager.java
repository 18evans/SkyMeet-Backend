package skymeet.data;

import com.pusher.rest.Pusher;

import java.util.List;

import skymeet.model.Flight;

public class PusherManager {

    private static Pusher pusher = new Pusher(
            System.getenv("PUSHER_APP_ID"),
            System.getenv("PUSHER_APP_KEY"),
            System.getenv("PUSHER_APP_SECRET")
    );

    static {
        pusher.setCluster("eu");
    }


    public static void triggerPushFlightsMoved(List<Flight> flightList) {
        pusher.trigger("private-channel", "new-location", flightList);
    }

//    private static class LocationItemPusher extends Location {
//        private double heading = 360;
//
//        private LocationItemPusher(double latitude, double longitude){
//            super(latitude, longitude);
//        }
//        private LocationItemPusher(double latitude, double longitude, double heading){
//            super(latitude, longitude);
//            this.heading = heading;
//        }
//
//        @Override
//        public String toString() {
//            return new Gson().toJson(this);
//        }
//    }

//    private static void testTriggerPusherMessage() {
////        LocationItemPusher arrLocation = new LocationItemPusher(new Location(51.441643, 5.469722), "360");
//        LocationItemPusher[] arrLocation = new LocationItemPusher[]{
//                new LocationItemPusher(51.441643, 5.469722),
//                new LocationItemPusher(51.440318, 5.468905),
//                new LocationItemPusher(51.439435, 5.468983),
//                new LocationItemPusher(51.438713, 5.468983),
//                new LocationItemPusher(51.438579, 5.470186),
//                new LocationItemPusher(51.438525, 5.471818),
//                new LocationItemPusher(51.437937, 5.471646),
//                new LocationItemPusher(51.437520, 5.470796),
//                new LocationItemPusher(51.437373, 5.470839),
//                new LocationItemPusher(51.437224, 5.471026),
//                new LocationItemPusher(51.437097, 5.471162),
//                new LocationItemPusher(51.437010, 5.471318),
//                new LocationItemPusher(51.436927, 5.471418),
//                new LocationItemPusher(51.436840, 5.471526)
//        };
//
//        final AtomicInteger index = new AtomicInteger(0);
//        Timer timer = new Timer(); // creating timer
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                int i = index.get();
//                System.out.println("Triggered " + index + " " + arrLocation[i].toString());
//                pusher.trigger("private-channel", "new-location", arrLocation[i]);
//                if (i == arrLocation.length - 1) {
//                    timer.cancel();
//                }
//                index.incrementAndGet();
//            }
//        }; // creating timer task
//        // scheduling the task for repeated fixed-delay execution, beginning after the specified delay
//        timer.schedule(task, 0, 2000);

//    }

}
