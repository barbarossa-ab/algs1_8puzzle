
public class Board {

    private int[][] blocks;
    private int N;
    private int iBlank, jBlank;

    public Board(int[][] blocks) {
        this(blocks.length);
        initBlocks(blocks);
    }

    private Board(int[][] blocks, Board goal) {
        this(blocks.length);
        initBlocks(blocks);
    }

    private Board(int N) {
        this.N = N;
        this.blocks = new int[N][N];
    }

    private void initBlocks(int[][] blocks) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.blocks[i][j] = blocks[i][j];
                if (blocks[i][j] == 0) {
                    this.iBlank = i;
                    this.jBlank = j;
                }
            }
        }
    }

    public int dimension() {
        return N;
    }

    public int hamming() {
        int dist = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if ((blocks[i][j] != 0) && (blocks[i][j]
                        != //                        goal.blocks[i][j]
                        i * N + j + 1)) {
                    dist++;
                }
            }
        }

        return dist;
    }

    public int manhattan() {
        int dist = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if ((blocks[i][j] != 0) && (blocks[i][j] != (i * N + j + 1))) {
                    int iGoal = (blocks[i][j] - 1) / N;
                    int jGoal = (blocks[i][j] - 1) % N;
                    int iDist = (iGoal > i) ? (iGoal - i) : (i - iGoal);
                    int jDist = (jGoal > j) ? (jGoal - j) : (j - jGoal);

                    dist += (iDist + jDist);
                }
            }
        }

        return dist;
    }

    public boolean isGoal() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if ((i == (N - 1)) && (j == (N - 1))) {
                    continue;
                }
                if (blocks[i][j] == 0) {
                    return false;
                }
                if (blocks[i][j] != (i * N + j + 1)) {
                    return false;
                }
            }
        }
        if (blocks[N - 1][N - 1] != 0) {
            return false;
        }
        return true;
    }

    
    public Board twin() {
        Board twin = new Board(blocks);
        boolean exchanged = false;

        while (!exchanged) {
            int i = StdRandom.uniform(N);
            int j = StdRandom.uniform(N);
            int jExch = (j < (N - 1)) ? (j + 1) : (j - 1);
            
            if((i == iBlank) && ((j == jBlank) ||(jExch == jBlank))) {
                continue;
            }
            
            twin.exchangeBlocks(i, j, i, jExch);
            exchanged = true;
        }

        return twin;
    }

    
    @Override
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }

        Board that = (Board) y;

        if (this == that) {
            return true;
        }

        if (this.N != that.N) {
            return false;
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (this.blocks[i][j] != that.blocks[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {

        Queue<Board> neighbors = new Queue<Board>() {
            @Override
            public String toString() {
                StringBuilder s = new StringBuilder();
                for (Board item : this) {
                    s.append(item).append("\n");
                }
                return s.toString();
            }
        };

        Board genNeighbor;

        /* Moving the blank up*/
        if (iBlank > 0) {
            genNeighbor = new Board(this.blocks);
            genNeighbor.exchangeBlocks(iBlank, jBlank, iBlank - 1, jBlank);
            neighbors.enqueue(genNeighbor);
        }

        /* Moving the blank down*/
        if (iBlank < (N - 1)) {
            genNeighbor = new Board(this.blocks);
            genNeighbor.exchangeBlocks(iBlank, jBlank, iBlank + 1, jBlank);
            neighbors.enqueue(genNeighbor);
        }

        /* Moving the blank left*/
        if (jBlank > 0) {
            genNeighbor = new Board(this.blocks);
            genNeighbor.exchangeBlocks(iBlank, jBlank, iBlank, jBlank - 1);
            neighbors.enqueue(genNeighbor);
        }

        /* Moving the blank right*/
        if (jBlank < (N - 1)) {
            genNeighbor = new Board(this.blocks);
            genNeighbor.exchangeBlocks(iBlank, jBlank, iBlank, jBlank + 1);
            neighbors.enqueue(genNeighbor);
        }

        return neighbors;
    }

    private void exchangeBlocks(int i1, int j1, int i2, int j2) {
        if (blocks[i1][j1] == 0) {
            iBlank = i2;
            jBlank = j2;
        }

        int aux = blocks[i1][j1];
        blocks[i1][j1] = blocks[i2][j2];
        blocks[i2][j2] = aux;
    }

    @Override
    public String toString() {
        StringBuffer text = new StringBuffer();

        text.append(N).append("\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                text.append(blocks[i][j]).append(" ");
            }
            text.append("\n");
        }

        return new String(text);
    }
}
