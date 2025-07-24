import java.util.*;

/*

================================= STATE CLASS ===========================

 */

class State {

    StringBuilder positions;

    public State(StringBuilder positions) {
        this.positions = positions;
    }

    public boolean goalTest() {
        return this.positions.toString().equals("RRR_LLL");
    }

    public ArrayList<State> moveGen() {
        ArrayList<State> children = new ArrayList<>();

        for (int i = 0; i < positions.length(); i++) {
            if (positions.charAt(i) == 'L') {

                // if the left rabbit can move right
                if (i + 1 < positions.length() && positions.charAt(i + 1) == '_') {
                    StringBuilder newPositions = new StringBuilder(positions);
                    newPositions.setCharAt(i, '_');
                    newPositions.setCharAt(i + 1, 'L');
                    children.add(new State(newPositions));
                }

                // if the left rabbit can jump over a the rabbit in the space to the right
                if (i + 2 < positions.length() && positions.charAt(i + 2) == '_') {
                    StringBuilder newPositions = new StringBuilder(positions);
                    newPositions.setCharAt(i, '_');
                    newPositions.setCharAt(i + 2, 'L');
                    children.add(new State(newPositions));
                }
            } else if (positions.charAt(i) == 'R') {
                // if the right rabbit can move left
                if (i - 1 >= 0 && positions.charAt(i - 1) == '_') {
                    StringBuilder newPositions = new StringBuilder(positions);
                    newPositions.setCharAt(i, '_');
                    newPositions.setCharAt(i - 1, 'R');
                    children.add(new State(newPositions));
                }

                // if the right rabbit can jump over and take the space
                if (i - 2 >= 0 && positions.charAt(i - 2) == '_') {
                    StringBuilder newPositions = new StringBuilder(positions);
                    newPositions.setCharAt(i, '_');
                    newPositions.setCharAt(i - 2, 'R');
                    children.add(new State(newPositions));
                }
            }
        }

        return children;
    }

    public String toString() {
        return positions.toString();
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof State))
            return false;
        State state = (State) o;
        return positions.toString().equals(state.positions.toString());
    }

    public int hashCode() {
        return Objects.hash(this.positions.toString());
    }
}

/*
 * 
 * ================================= SEARCH CLASS ===========================
 * 
 */

class Search {

    // search class with bfs and dfs

    // Reconstructing the path
    public void reconstructPath(State goal, Map<State, State> parentMap) {
        List<State> path = new ArrayList<>();
        State current = goal;
        while (current != null) {
            path.add(0, current);
            current = parentMap.get(current);
        }

        for (State s : path) {
            System.out.println(s);
        }
    }

    public void bfs(State start) {
        Queue<State> open = new LinkedList<>();
        Map<State, State> parentMap = new HashMap<>();

        open.add(start);
        parentMap.put(start, null);

        while (!open.isEmpty()) {
            State current = open.poll();

            if (current.goalTest()) {
                System.out.println("Goal found using BFS!");
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

        System.out.println("Goal not found using BFS.");
    }

    public void dfs(State start) {
        Stack<State> open = new Stack<>();
        Map<State, State> parentMap = new HashMap<>();

        open.push(start);
        parentMap.put(start, null);

        while (!open.isEmpty()) {
            State current = open.pop();

            if (current.goalTest()) {
                System.out.println("Goal found using DFS!");
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
}

/*
 * 
 * ================================= MAIN CLASS ===========================
 * 
 */

class Main {
    public static void main(String[] args) {
        StringBuilder startPositions = new StringBuilder("LLL_RRR");
        State startState = new State(startPositions);

        Search search = new Search();

        System.out.println("Running BFS:");
        search.bfs(startState);

        System.out.println("\nRunning DFS:");
        search.dfs(startState);
    }
}

/*
 * ============================ OUTPUT
 * 
 * {
 * 
 * Running BFS:
 * Goal found using BFS!
 * LLL_RRR
 * LL_LRRR
 * LLRL_RR
 * LLRLR_R
 * LLR_RLR
 * L_RLRLR
 * _LRLRLR
 * RL_LRLR
 * RLRL_LR
 * RLRLRL_
 * RLRLR_L
 * RLR_RLL
 * R_RLRLL
 * RR_LRLL
 * RRRL_LL
 * RRR_LLL
 * 
 * Running DFS:
 * Goal found using DFS!
 * LLL_RRR
 * LLLR_RR
 * LL_RLRR
 * L_LRLRR
 * LRL_LRR
 * LRLRL_R
 * LRLRLR_
 * LRLR_RL
 * LR_RLRL
 * _RLRLRL
 * R_LRLRL
 * RRL_LRL
 * RRLRL_L
 * RRLR_LL
 * RR_RLLL
 * RRR_LLL
 * }
 * 
 */
