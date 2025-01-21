package board_evaluator

import figure.*
import korlibs.korge.view.*
import settings.*

class BoardEvaluator(val coeffecients: List<Double>){
    fun moveEvalute(grid: List<List<SolidRect>>, x: Int, y: Int, figreType: Int, rotation: Int): Double {
        var s1 = 0.0
        for(i in 0..<4){
            for(j in 0..<4){
                if (blockPositions[figreType][rotation][i][j] == 1)
                    s1 += (y + j)
            }
        }

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

        var bad_cells = 0
        for(i in 0..<BOARD_SIZE_X){
            for(j in 1..<BOARD_SIZE_Y){
                if(!grid1[i][j] && grid1[i][j - 1]) bad_cells ++
            }
        }

        return s1 * this.coeffecients[0] + bad_cells * this.coeffecients[1]
    }
}
