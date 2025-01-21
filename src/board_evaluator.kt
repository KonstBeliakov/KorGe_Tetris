package board_evaluator

import figure.*
import korlibs.korge.view.*
import settings.*

class BoardEvaluator(val coeffecients: List<Double>){
    private fun removeRow(grid: MutableList<MutableList<Boolean>>, rowNumber: Int) {
        for (y in rowNumber downTo 1) {
            for (x in 0..<BOARD_SIZE_X) {
                grid[x][y] = grid[x][y - 1]
            }
        }

        for (x in 0..<BOARD_SIZE_X)
            grid[0][x] = false
    }

    private fun removeFullRows(grid: MutableList<MutableList<Boolean>>) {
        for (y in 0..<BOARD_SIZE_Y) {
            if (List(BOARD_SIZE_X) { x -> grid[x][y] }.all { it })
                this.removeRow(grid, y)
        }
    }

    fun moveEvalute(grid: List<List<SolidRect>>, x: Int, y: Int, figreType: Int, rotation: Int, log:Boolean = false): Double {
        val grid1 = MutableList(BOARD_SIZE_X){x ->
            MutableList(BOARD_SIZE_Y){y ->
                grid[x][y].color != BOARD_COLOR
            }
        }
        for(i in 0..<4){
            for(j in 0..<4){
                if(blockPositions[figreType][rotation][i][j] == 1)
                    grid1[x + i][j + y] = true
            }
        }
        this.removeFullRows(grid1)

        var s1 = 0
        for(i in 0..<BOARD_SIZE_X){
            for(j in 0..<BOARD_SIZE_Y){
                if (grid1[i][j]) s1 -= (BOARD_SIZE_Y - j)
            }
        }

        var bad_cells = 0
        for(i in 0..<BOARD_SIZE_X){
            for(j in 1..<BOARD_SIZE_Y){
                if(!grid1[i][j] && grid1[i][j - 1]) bad_cells ++
            }
        }

        if(log){
            println()
            println("height coeffecient: $s1 (x${coeffecients[0]})")
            println("bad cells: $bad_cells (x${coeffecients[1]})")
        }

        return s1 * this.coeffecients[0] + bad_cells * this.coeffecients[1]
    }
}
