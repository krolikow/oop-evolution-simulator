//import java.util.ArrayList;
//
//public class OptionsParser {
//
//    public static ArrayList<MoveDirection> parse(String[] tab) {
//        ArrayList<MoveDirection> res = new ArrayList<>();
//        for (String elem : tab) {
//            switch (elem) {
//                case "f", "forward" -> res.add(MoveDirection.FORWARD);
//                case "b", "backward" -> res.add(MoveDirection.BACKWARD);
//                case "r", "right" -> res.add(MoveDirection.RIGHT);
//                case "l", "left" -> res.add(MoveDirection.LEFT);
//                default -> throw new IllegalArgumentException(elem + " is not legal move specification");
//            }
//        }
//        return res;
//    }
//}
