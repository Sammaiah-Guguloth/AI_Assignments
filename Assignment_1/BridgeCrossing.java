import java.util.*;

/*
 * 
 * ================================= STATE CLASS ===========================
 * 
 */
class State {

    ArrayList<String> leftSide;
    ArrayList<String> rightSide;
    char umbrella;
    int time;

    static Map<String, Integer> personTimeMap = new HashMap<>();
    static {
        personTimeMap.put("amogh", 5);
        personTimeMap.put("amegya", 10);
        personTimeMap.put("grandMother", 20);
        personTimeMap.put("grandFather", 25);
    }

    public State(ArrayList<String> leftSide, ArrayList<String> rightSide, char umbrella, int time) {
        this.leftSide = new ArrayList<>(leftSide);
        this.rightSide = new ArrayList<>(rightSide);
        this.umbrella = umbrella;
        this.time = time;
    }

    public boolean goalTest() {
        return leftSide.size() == 0 && rightSide.size() == 4 && umbrella == 'R';
    }

    public ArrayList<State> moveGen() {
        ArrayList<State> children = new ArrayList<>();

        if (umbrella == 'L') {
            // pick two persons from left and go to right
            for (int i = 0; i < leftSide.size(); i++) {
                for (int j = i + 1; j < leftSide.size(); j++) {
                    String p1 = leftSide.get(i);
                    String p2 = leftSide.get(j);

                    // as cost should be max of both i.e slow will be taken
                    int cost = Math.max(personTimeMap.get(p1), personTimeMap.get(p2));
                    if (time + cost > 60)
                        continue;

                    ArrayList<String> newLeft = new ArrayList<>(leftSide);
                    ArrayList<String> newRight = new ArrayList<>(rightSide);

                    newLeft.remove(p1);
                    newLeft.remove(p2);
                    newRight.add(p1);
                    newRight.add(p2);

                    children.add(new State(newLeft, newRight, 'R', time + cost));
                }
            }

        } else {
            // as we want minimum time we'll take one person from right and go to left and
            // no two persons ****
            for (String p : rightSide) {
                int cost = personTimeMap.get(p);
                if (time + cost > 60)
                    continue;

                ArrayList<String> newLeft = new ArrayList<>(leftSide);
                ArrayList<String> newRight = new ArrayList<>(rightSide);

                newRight.remove(p);
                newLeft.add(p);

                children.add(new State(newLeft, newRight, 'L', time + cost));
            }
        }

        return children;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Left  : ");
        for (String person : leftSide) {
            sb.append(person).append(" ");
        }
        if (umbrella == 'L') {
            sb.append("[umbrella]");
        }
        sb.append("\n");

        sb.append("Right : ");
        for (String person : rightSide) {
            sb.append(person).append(" ");
        }
        if (umbrella == 'R') {
            sb.append("[umbrella]");
        }
        sb.append("\n");

        sb.append("Time  : ").append(time).append(" minutes");
        sb.append("\n");
        return sb.toString();
    }

}

/*
 * 
 * ================================= SEARCH CLASS ===========================
 * 
 */
class Search {

    public void bfs(State start) {
        Queue<State> open = new LinkedList<>();
        Map<State, State> parentMap = new HashMap<>(); // acts as closed and also for reconstructing the path

        open.add(start);
        parentMap.put(start, null);

        while (!open.isEmpty()) {
            State current = open.poll();

            if (current.goalTest()) {
                System.out.println("Goal found using BFS!\n");
                reconstructPath(current, parentMap);
                return;
            }

            for (State child : current.moveGen()) {
                if (!parentMap.containsKey(child)) {
                    open.add(child);
                    parentMap.put(child, current);
                }
            }
        }

        System.out.println("Goal not found within 60 minutes , using BFs");
    }

    public void dfs(State start) {
        Stack<State> open = new Stack<>();
        Map<State, State> parentMap = new HashMap<>();

        open.push(start);
        parentMap.put(start, null);

        while (!open.isEmpty()) {
            State current = open.pop();

            if (current.goalTest()) {
                System.out.println("Goal found using DFS!\n");
                reconstructPath(current, parentMap);
                return;
            }

            for (State child : current.moveGen()) {
                if (!parentMap.containsKey(child)) {
                    open.push(child);
                    parentMap.put(child, current);
                }
            }
        }

        System.out.println("Goal not found using DFS.");
    }

    public void reconstructPath(State goal, Map<State, State> parentMap) {
        List<State> path = new ArrayList<>();
        State current = goal;
        while (current != null) {
            path.add(0, current);
            current = parentMap.get(current);
        }

        int step = 0;
        for (State s : path) {
            System.out.println("Step " + (step++) + ":");
            System.out.println(s);
            System.out.println("-----------------------------");
        }
    }
}

/*
 * 
 * ================================= MAIN CLASS ===========================
 * 
 */
class Main {
    public static void main(String[] args) {
        ArrayList<String> left = new ArrayList<>(Arrays.asList("amogh", "amegya", "grandMother", "grandFather"));
        ArrayList<String> right = new ArrayList<>();
        char umbrella = 'L';
        int time = 0;

        State start = new State(left, right, umbrella, time);
        Search search = new Search();

        search.bfs(start);
    }
}

/*
 * 
 * ================================= OUTPUT ===========================
 * 
 */

/*
 * Goal found using BFS!
 * 
 * Step 0:
 * Left : amogh amegya grandMother grandFather [umbrella]
 * Right :
 * Time : 0 minutes
 * 
 * -----------------------------
 * Step 1:
 * Left : grandMother grandFather
 * Right : amogh amegya [umbrella]
 * Time : 10 minutes
 * 
 * -----------------------------
 * Step 2:
 * Left : grandMother grandFather amogh [umbrella]
 * Right : amegya
 * Time : 15 minutes
 * 
 * -----------------------------
 * Step 3:
 * Left : amogh
 * Right : amegya grandMother grandFather [umbrella]
 * Time : 40 minutes
 * 
 * -----------------------------
 * Step 4:
 * Left : amogh amegya [umbrella]
 * Right : grandMother grandFather
 * Time : 50 minutes
 * 
 * -----------------------------
 * Step 5:
 * Left :
 * Right : grandMother grandFather amogh amegya [umbrella]
 * Time : 60 minutes
 * 
 * -----------------------------
 */