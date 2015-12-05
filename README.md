<!-- saved from url=(0063)http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html -->
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<!--  ADD STATIC FINAL VARIABLE TO SOLVER AND USE AS ARGUMENT TO SELECT PRIORITY FUNCTION ?
-->

<title>
8-Puzzle Programming Assignment
</title><style type="text/css"></style></head>

<body>
<h3>COS 226 Programming Assignment</h3>
<h2>8 Puzzle</h2>

<p>

Write a program to solve the 8-puzzle problem
(and its natural generalizations) using the 
A* search algorithm.

</p><p><b>The problem.</b>
The <a href="http://en.wikipedia.org/wiki/Fifteen_puzzle">8-puzzle problem</a>
is a puzzle invented and popularized by Noyes Palmer Chapman in the 1870s. It is 
played on a 3-by-3 grid with 8 square blocks labeled 1 through 8 and a blank 
square. Your goal is to rearrange the blocks so that they are in order, using
as few moves as possible. 
You are permitted to slide blocks horizontally or vertically
into the blank square. 
The following 
shows a sequence of legal moves from an <em>initial board</em> (left)
to the <em>goal board</em> (right).
</p><pre><blockquote>
    1  3        1     3        1  2  3        1  2  3        1  2  3
 4  2  5   =&gt;   4  2  5   =&gt;   4     5   =&gt;   4  5      =&gt;   4  5  6
 7  8  6        7  8  6        7  8  6        7  8  6        7  8 

 initial        1 left          2 up          5 left          goal</blockquote></pre>

<p>
<b>Best-first search.</b>
Now, we describe a solution to the problem that illustrates a 
general artificial intelligence methodology known as the
<a href="http://en.wikipedia.org/wiki/A*_search_algorithm">A* search algorithm</a>.
We define a <em>search node</em> of the game to be a board, the number
of moves made to reach the board, and the previous search node.
First, insert the initial search node
(the initial board, 0 moves, and a null previous search node) 
into a priority queue. Then,
delete from the priority queue the search node with the minimum priority,
and insert onto the priority queue all neighboring search nodes
(those that can be reached in one move from the dequeued search node).
Repeat this procedure until the search node dequeued corresponds to a goal board.
The success of this approach
hinges on the choice of <em>priority function</em> for a search node. We 
consider two priority functions:

</p><ul>

<li><em>Hamming priority function.</em>
The number of blocks in the wrong position,
plus the number of moves made so far to get to the search node.
Intuitively, a search node with a small number of blocks in the wrong position
is close to the goal, and we prefer a search node that
have been reached using a small number of moves.

<p></p></li><li><em>Manhattan priority function.</em>
The sum of the Manhattan distances (sum of the vertical and horizontal distance)
from the blocks to their goal positions,
plus the number of moves made so far to get to the search node.

</li></ul>

For example, the Hamming and Manhattan priorities of the initial search node
below are 5 and 10, respectively.

<pre><blockquote>
 8  1  3        1  2  3     1  2  3  4  5  6  7  8    1  2  3  4  5  6  7  8
 4     2        4  5  6     ----------------------    ----------------------
 7  6  5        7  8        1  1  0  0  1  1  0  1    1  2  0  0  2  2  0  3

 initial          goal         Hamming = 5 + 0          Manhattan = 10 + 0</blockquote></pre>


<p>
We make a key observation: To solve the puzzle from
a given search node on the priority queue, the total number of moves we
need to make (including those already made) is at least its priority,
using either the Hamming or Manhattan priority function.
(For Hamming priority, this is true because each block that is out of place
must move at least once to reach its goal position.
For Manhattan priority, this is true because each block must move
its Manhattan distance from its goal position.
Note that we do not count the blank square when computing the
Hamming or Manhattan priorities.)
Consequently, when the goal board is dequeued, we
have discovered not only a sequence of moves from the
initial board to the goal board, but one that makes the fewest number of moves. 
(Challenge for the mathematically inclined: prove this fact.)

</p><p><b>A critical optimization.</b>
Best-first search has one annoying feature:
search nodes corresponding to the same board
are enqueued on the priority queue many times.
To reduce unnecessary exploration of useless search nodes,
when considering the neighbors of a search node, don't enqueue
a neighbor if its board is the same as the board of the
previous search node.

</p><pre><blockquote>
 8  1  3       8  1  3       8  1       8  1  3     8  1  3
 4     2       4  2          4  2  3    4     2     4  2  5
 7  6  5       7  6  5       7  6  5    7  6  5     7  6

 previous    search node    neighbor   neighbor    neighbor
                                      (disallow)
</blockquote></pre>

<!--
<p><b>Your task.</b>
Write a program <tt>Solver.java</tt> that takes the name of a file as a command-line
argument, reads the initial board from the file, and prints to standard output a sequence of
boards that solves the puzzle in the fewest number of moves.
Also print out the total number of moves.
-->


<p><b>Game tree.</b>
One way to view the computation is as a <em>game tree</em>, where each search node
is a node in the game tree and the children of a node correspond to its
neighboring search nodes. The root of the game tree is the initial search node;
the internal nodes have already been processed; the leaf nodes are maintained
in a priority queue; at each step, the A* algorithm removes the node with the smallest
priority from the priority queue and processes it (by adding its children
to both the game tree and the priority queue).


</p><center>
<img src="./README_files/8puzzle-game-tree.png" width="715" alt="8puzzle game tree">
</center>


<p><b>Detecting unsolvable puzzles.</b>
Not all initial boards can lead to the goal board by a sequence of legal moves,
including the two below:

</p><pre><blockquote> 1  2  3         1  2  3  4
 4  5  6         5  6  7  8
 8  7            9 10 11 12
                13 15 14 
unsolvable
                unsolvable
</blockquote></pre>

To detect such situations,
use the fact that boards
are divided into two equivalence classes with respect to reachability:
(i) those that lead to the goal board and (ii) those that
lead to the goal board if we modify the initial board by
swapping any pair of blocks (the blank square is not a block).
(Difficult challenge for the mathematically inclined: prove this fact.)
To apply the fact, 
run the A* algorithm on <em>two</em> puzzle instances—one with the
initial board and one with the initial board modified by
swapping a pair of blocks—in lockstep
(alternating back and forth between exploring search nodes in each of
the two game trees).
Exactly one of the two will lead to the goal board.

<!--
<p><li> Derive a mathematical formula that tells you whether a board is 
solvable or not.

</ul>
-->


<p><b>Board and Solver data types.</b>
Organize your program by creating an immutable data type <tt>Board</tt> 
with the following API:

</p><p>
</p><blockquote><pre><b>public class Board {</b>
<b>    public Board(int[][] blocks)           </b><font color="gray">// construct a board from an N-by-N array of blocks</font>
                                           <font color="gray">// (where blocks[i][j] = block in row i, column j)</font>
<b>    public int dimension()                 </b><font color="gray">// board dimension N</font>
<b>    public int hamming()                   </b><font color="gray">// number of blocks out of place</font>
<b>    public int manhattan()                 </b><font color="gray">// sum of Manhattan distances between blocks and goal</font>
<b>    public boolean isGoal()                </b><font color="gray">// is this board the goal board?</font>
<b>    public Board twin()                    </b><font color="gray">// a board that is obtained by exchanging any pair of blocks</font>
<b>    public boolean equals(Object y)        </b><font color="gray">// does this board equal y?</font>
<b>    public Iterable&lt;Board&gt; neighbors()     </b><font color="gray">// all neighboring boards</font>
<b>    public String toString()               </b><font color="gray">// string representation of this board (in the output format specified below)</font>

<b>    public static void main(String[] args) </b><font color="gray">// unit tests (not graded)</font>
}</pre></blockquote>

<p><em>Corner cases.&nbsp;</em>
You may assume that the constructor receives an <em>N</em>-by-<em>N</em> array
containing the <em>N</em><sup>2</sup> integers between 0 and <em>N</em><sup>2</sup> − 1,
where 0 represents the blank square.

</p><p><em>Performance requirements.&nbsp;</em>
Your implementation should support all <tt>Board</tt> methods in time
proportional to <em>N</em><sup>2</sup> (or better) in the worst case.

</p><p>
Also, create an immutable data type <tt>Solver</tt> with the following API:


</p><blockquote><pre><b>public class Solver {</b>
<b>    public Solver(Board initial)           </b><font color="gray">// find a solution to the initial board (using the A* algorithm)</font>
<b>    public boolean isSolvable()            </b><font color="gray">// is the initial board solvable?</font>
<b>    public int moves()                     </b><font color="gray">// min number of moves to solve initial board; -1 if unsolvable</font>
<b>    public Iterable&lt;Board&gt; solution()      </b><font color="gray">// sequence of boards in a shortest solution; null if unsolvable</font>
<b>    public static void main(String[] args) </b><font color="gray">// solve a slider puzzle (given below)</font>
}
</pre>
</blockquote>

To implement the A* algorithm, <em>you must use 
<a href="http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/MinPQ.html"><tt>MinPQ</tt></a>
from algs4.jar for the priority queue(s)</em>.
<p>
</p><p><em>Corner cases.&nbsp;</em>
The constructor should throw a <tt>java.lang.NullPointerException</tt> if passed a null argument.

</p><p><b>Solver test client.</b>
Use the following test client to read a puzzle from a file
(specified as a command-line argument) and print the solution to standard output.

</p><blockquote><pre>public static void main(String[] args) {

    <font color="gray">// create initial board from file</font>
    In in = new In(args[0]);
    int N = in.readInt();
    int[][] blocks = new int[N][N];
    for (int i = 0; i &lt; N; i++)
        for (int j = 0; j &lt; N; j++)
            blocks[i][j] = in.readInt();
    Board initial = new Board(blocks);

    <font color="gray">// solve the puzzle</font>
    Solver solver = new Solver(initial);

    <font color="gray">// print solution to standard output</font>
    if (!solver.isSolvable())
        StdOut.println("No solution possible");
    else {
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (Board board : solver.solution())
            StdOut.println(board);
    }
}
</pre>
</blockquote>


<p><b>Input and output formats.</b>
The input and output format for a board is the board dimension <em>N</em> followed by
the <em>N</em>-by-<em>N</em>
initial board, using 0 to represent the blank square.
As an example,

</p><pre><blockquote><b>% more puzzle04.txt</b>
3
 0  1  3
 4  2  5
 7  8  6

% <b>java Solver puzzle04.txt</b>
Minimum number of moves = 4

3
 0  1  3 
 4  2  5 
 7  8  6 

3
 1  0  3 
 4  2  5 
 7  8  6 

3
 1  2  3 
 4  0  5 
 7  8  6 

3
 1  2  3 
 4  5  0   
 7  8  6 

3
 1  2  3 
 4  5  6 
 7  8  0
</blockquote></pre>

<pre><blockquote>% <b>more puzzle3x3-unsolvable.txt</b>
3
 1  2  3
 4  5  6
 8  7  0

% <b>java Solver puzzle3x3-unsolvable.txt</b>
No solution possible</blockquote></pre>

Your program should work correctly for arbitrary <em>N</em>-by-<em>N</em> boards
(for any 2 ≤ <em>N</em> &lt; 128), even if it is too slow to solve 
some of them in a reasonable amount of time.



<p>
</p><p><b>Deliverables.</b>
Submit only the files <tt>Board.java</tt> and <tt>Solver.java</tt> (with the Manhattan priority).
We will supply <tt>algs4.jar</tt>.
You may not call any library functions other those in
<tt>java.lang</tt>, <tt>java.util</tt>, and <tt>algs4.jar</tt>.
You must use 
<a href="http://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/MinPQ.html"><tt>MinPQ</tt></a>
for the priority queue(s).




</p></body></html>
