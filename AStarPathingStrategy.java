
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy {


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

//        WorldNode { estimatedDistanceToEnd, distanceFromStart, position, prev } //write a WorldNode class
//
//        initialise the Open List: a PriorityQueue<WorldNode> (give it a comparator)
        PriorityQueue<WorldNode> OpenList = new PriorityQueue<>(new WorldNodeComparator());
//        initialise the Closed List: HashSet<Point>
        HashSet<Point> ClosedList = new HashSet<>();
//        * add the start to the open list
        //start: a point -> turn the point into a worldnode
        //write a method in worldnode that takes in a point and turns it into a worldnode
        WorldNode startnode = WorldNode.pointtoworldnode(start, start, end, null);
        OpenList.add(startnode);
//        * as long as the open list is not empty
        while (OpenList.size() != 0) {
//            - grab the first node from the open list
            WorldNode currentnode = OpenList.poll();
//            - if it's within reach of the target
            if (currentnode.position.adjacent(end)) {
                break;
            }
//            - if it is in the closed list
            if (!ClosedList.contains(currentnode.position)) {
//                * ignore it
//            - add it to the closed list
                ClosedList.add(currentnode.position);
            }

                List<WorldNode> potentialworldnodeneighbors =
                        potentialNeighbors.apply(currentnode.position)
                                .filter(canPassThrough)
                                .filter(point -> !ClosedList.contains(point)).map(point -> WorldNode.pointtoworldnode(start, point, end, currentnode)).toList();
//
                for (WorldNode node : potentialworldnodeneighbors) {
                    if (!OpenList.contains(node)) {
                        OpenList.add(node);
                    } else if (node.h < currentnode.h) {
                        OpenList.add(node);
                    }
                }
//             - for each world node
//                * if it's not in the open list, add it
//                * if it is in the open list, AND the new estimate is better than the old one
//                    - add it to the open list

//
//         Backtrack and construct a path


            }
            ArrayList<Point> path = new ArrayList<>();
            for (Point point : ClosedList) {
                path.add(0, point);
            }

            return path;
        }

    }

