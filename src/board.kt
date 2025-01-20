package board

import figure.*
import korlibs.event.*
import korlibs.korge.view.*
import settings.*

import java.time.Instant
import java.time.Duration

class Board(val container: Container) {
    var grid: List<List<SolidRect>> = List(BOARD_SIZE_X) { x ->
        List(BOARD_SIZE_Y) { y ->
            container.solidRect(BLOCK_SIZE, BLOCK_SIZE, BOARD_COLOR) {
                position(50 + x * (BLOCK_SIZE + SEPARATION), 50 + y * (BLOCK_SIZE + SEPARATION))
            }
        }
    }
    private var figure = Figure(container)
    private var lastUpdated = System.currentTimeMillis()
    private var lastUpdatedHorizontal = System.currentTimeMillis()
    var score = 0
    var gameOver = false

    private fun mayFall(): Boolean {
        for (i in 0..<4) {
            for (j in 0..<4) {
                if (
                    this.figure.blockPosition[i][j] == 1 &&
                    (j + this.figure.y >= BOARD_SIZE_Y - 1 || this.grid[this.figure.x + i][this.figure.y + j + 1].color != BOARD_COLOR)
                ) {
                    return false
                }

            }
        }
        return true
    }

    fun update(keys: List<Key>) {
        if (System.currentTimeMillis() - this.lastUpdated > UPDATE_TIME) {
            if (this.mayFall()) {
                this.figure.fall()
                this.lastUpdated = System.currentTimeMillis()
            } else {
                for (i in 0..<4) {
                    for (j in 0..<4) {
                        if (this.figure.blockPosition[i][j] == 1) {
                            this.grid[i + this.figure.x][j + this.figure.y].color = figure.color
                        }
                    }
                }

                this.figure.hide()
                this.figure = Figure(this.container)
            }

            this.checkFullRows()
            this.checkGameOver()
        }
        if(System.currentTimeMillis() - this.lastUpdatedHorizontal > HORISONTAL_MOVEMENT_UPATE_TIME){
            this.lastUpdatedHorizontal = System.currentTimeMillis()

            if (Key.LEFT in keys) this.figure.left()
            if (Key.RIGHT in keys) this.figure.right()
            if (Key.R in keys) this.figure.rotate()
        }
    }

    private fun removeRow(rowNumber: Int){
        for(y in rowNumber downTo 1){
            for(x in 0..<BOARD_SIZE_X){
                this.grid[x][y].color = this.grid[x][y-1].color
            }
        }

        for(x in 0..<BOARD_SIZE_X)
            this.grid[0][x].color = BOARD_COLOR
    }

    private fun checkFullRows() {
        var rowsRemoved = 0
        for(y in 0..<BOARD_SIZE_Y){
            // if the row is full
            if (List(BOARD_SIZE_X){ x -> this.grid[x][y]}.all{ it.color != BOARD_COLOR }){
                this.removeRow(y)
                rowsRemoved ++
            }
        }

        this.score += when(rowsRemoved){
            1 ->  100
            2 ->  200
            3 ->  400
            4 ->  800
            else -> 0
        }
    }

    private fun checkGameOver(){
        if(List(BOARD_SIZE_X){ x -> this.grid[x][0]}.any{ it.color != BOARD_COLOR})
            this.gameOver = true
    }
}
