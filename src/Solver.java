
import java.util.Comparator;
import java.util.Iterator;

public class Solver {

    private Board initial;
    private MinPQ<BoardNode> searchQueue;
    private MinPQ<BoardNode> searchQueueTwin;
    private Stack<Board> solution;
    private boolean solvable;
    private int nrMoves;

    public Solver(Board initial) {
        this.initial = initial;
        this.solvable = false;

        this.searchQueue = new MinPQ<BoardNode>(new Comparator<BoardNode>() {
            @Override
            public int compare(BoardNode t1, BoardNode t2) {
                return (t1.manhattanPri() - t2.manhattanPri());
            }
        });

        this.searchQueueTwin = new MinPQ<BoardNode>(new Comparator<BoardNode>() {
            @Override
            public int compare(BoardNode t1, BoardNode t2) {
                return (t1.manhattanPri() - t2.manhattanPri());
            }
        });

        this.solution = new Stack<Board>();

        this.solve();
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        if (isSolvable()) {
            return nrMoves;
        }
        return -1;
    }

    private void solve() {
        /*
         * put the intial board in queue
         * while solution not found and queue not empty
         *     remove head of queue
         *     if is goal stop
         *     else find its neighbors
         *     enque each neighbor different than previos (parrent board) with moves + 1
         */

        searchQueue.insert(new BoardNode(initial, 0, null));
        searchQueueTwin.insert(new BoardNode(initial.twin(), 0, null));

        while (!searchQueue.isEmpty() && !searchQueueTwin.isEmpty()) {
            BoardNode current = searchQueue.delMin();
            BoardNode currentTwin = searchQueueTwin.delMin();

            if (current.board.isGoal()) {
                solvable = true;
                nrMoves = current.moves;

                while (current != null) {
                    solution.push(current.board);
                    current = current.prev;
                }
                break;
            }
            if (currentTwin.board.isGoal()) {
                break;
            }

            enqueueNeighbors(searchQueue, current);
            enqueueNeighbors(searchQueueTwin, currentTwin);
        }
    }

    private void enqueueNeighbors(MinPQ<BoardNode> queue, BoardNode current) {
        Iterable<Board> curNeighbors = current.board.neighbors();
        Iterator<Board> it = curNeighbors.iterator();

        while (it.hasNext()) {
            Board n = it.next();
            if ((current.prev != null) && (n.equals(current.prev.board))) {
                continue;
            }
            queue.insert(new BoardNode(n, current.moves + 1, current));
        }
    }

    public Iterable<Board> solution() {
        if (isSolvable()) {
            return solution;
        }
        return null;
    }

    private class BoardNode {

        public Board board;
        public int moves;
        public BoardNode prev;
        public int pri;

        public BoardNode(Board board, int moves, BoardNode previous) {
            this.board = board;
            this.moves = moves;
            this.prev = previous;
            this.pri = moves + board.manhattan();
        }

        public int manhattanPri() {
            return pri;
        }
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);
        StdOut.println(initial);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }
}
