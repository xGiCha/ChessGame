package gr.repo.chessgame

import java.util.*

object KnightPath {
    private val chessboard = Array(8) { arrayOfNulls<Pos>(8) }
    private var q: Stack<Pos> = Stack()

    var hashMap: HashMap<Int, Point> = HashMap<Int, Point>()


    fun pathPoints(startX: Int, startY: Int, endX: Int, endY: Int): HashMap<Int, Point> {
        hashMap.clear()
        var index = 0

        //Populate the chessboard with position values as unreachable
        populateChessBoard()

        //Assume the position for simplicity. In real world, accept the values using Scanner.
        val start = Pos(startX, startY, 0) // Position 0, 1 on the chessboard
        val end = Pos(endX, endY, Int.MAX_VALUE)

        //Assign starting depth for the source as 0 (as this position is reachable in 0 moves)
        chessboard[0][1] = Pos(start.x, start.y, 0)

        //Add start position to queue
        q.add(start)
        while (q.size != 0) // While queue is not empty
        {
            val pos = q.pop() //read and remove element from the queue

            //If this position is same as the end position, you found the destination
            if (end.equals(pos!!) && pos.depth <= 3) {
                val random = Random()
                var color = String.format("#%06x", random.nextInt(256 * 256 * 256))

                // We found the Position. Now trace back from this position to get the actual shortest path
                val path: Iterable<Pos?> = getShortestPath(start, end)
                println("Minimum jumps required: " + pos.depth)
                println("Actual Path")
                println("(" + pos.x + " " + pos.y + ")")

                for (value in path.iterator()) {
                    println("(" + value?.x + " " + value?.y + ")")
                    if (value?.x != startX || value.y != startY) {
                        hashMap[index] = Point(value?.x!!, value.y, color)
                        index++
                    }
                }
            } else {
                // perform BFS on this Pos if it is not already visited
                bfs(pos, ++pos.depth)
            }
        }

        //This code is reached when the queue is empty and we still did not find the location.
        println("End position is not reachable for the knight")
        return hashMap
    }

    //Breadth First Search
    private fun bfs(current: Pos, depth: Int) {

        // Start from -2 to +2 range and start marking each location on the board
        for (i in -2..2) {
            for (j in -2..2) {
                val next = Pos(current.x + i, current.y + j, depth)
                if (inRange(next.x, next.y)) {
                    //Skip if next location is same as the location you came from in previous run
                    if (current.equals(next)) continue
                    if (isValid(current, next)) {
                        val position = chessboard[next.x][next.y]
                        /*
						 * Get the current position object at this location on chessboard.
						 * If this location was reachable with a costlier depth, this iteration has given a shorter way to reach
						 */if (position!!.depth >= depth) {
                            chessboard[current.x + i][current.y + j] = Pos(current.x, current.y, depth)
                            q.add(next)
                        }
                    }
                }
            }
        }
    }

    private fun inRange(x: Int, y: Int): Boolean {
        return 0 <= x && x < 8 && 0 <= y && y < 8
    }

    /*Check if this is a valid jump or position for Knight based on its current location */
    private fun isValid(current: Pos, next: Pos): Boolean {
        // Use Pythagoras theorem to ensure that a move makes a right-angled triangle with sides of 1 and 2. 1-squared + 2-squared is 5.
        val deltaR = next.x - current.x
        val deltaC = next.y - current.y
        return 5 == deltaR * deltaR + deltaC * deltaC
    }

    /*Populate initial chessboard values*/
    private fun populateChessBoard() {
        for (i in chessboard.indices) {
            for (j in 0 until chessboard[0].size) {
                chessboard[i][j] = Pos(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)
            }
        }
    }

    /*Get the shortest Path and return iterable object */
    private fun getShortestPath(start: Pos, end: Pos): Iterable<Pos?> {
        val path = Stack<Pos?>()
        var current = chessboard[end.x][end.y]
        while (!current!!.equals(start)) {
            path.add(current)
            current = chessboard[current.x][current.y]
        }
        path.add(Pos(start.x, start.y, 0))
        return path
    }
}

class Pos(var x: Int, var y: Int, var depth: Int) {
    fun equals(that: Pos): Boolean {
        return x == that.x && y == that.y
    }
}

class Point(var x: Int, var y: Int, var color: String)