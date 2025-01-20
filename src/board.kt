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
    var figure = Figure(container)
    var last_updated = Instant.now()
    var score = 0

    fun mayFall(): Boolean {
        for (i in 0..<4) {
            for (j in 0..<4) {
                if (
                    this.figure.block_position[i][j] == 1 &&
                    (j + this.figure.y >= BOARD_SIZE_Y - 1 || this.grid[this.figure.x + i][this.figure.y + j + 1].color != BOARD_COLOR)
                ) {
                    return false
                }

            }
        }
        return true
    }

    fun update(keys: List<Key>) {
        if (Duration.between(last_updated, Instant.now()).seconds > UPDATE_TIME) {
            if (this.mayFall()) {
                println("figure fall")
                this.figure.fall()
                this.last_updated = Instant.now()
            } else {
                println("figure can't fall")
                for (i in 0..<4) {
                    for (j in 0..<4) {
                        if (this.figure.block_position[i][j] == 1) {
                            this.grid[i + this.figure.x][j + this.figure.y].color = figure.color
                        }
                    }
                }
                this.check_full_rows()

                this.figure.hide()
                this.figure = Figure(this.container)
            }

            if (Key.LEFT in keys) this.figure.left()
            if (Key.RIGHT in keys) this.figure.right()
        }
    }

    private fun remove_row(row_number: Int){
        for(y in row_number downTo 1){
            for(x in 0..<BOARD_SIZE_X){
                this.grid[x][y].color = this.grid[x][y-1].color
            }
        }

        for(x in 0..<BOARD_SIZE_X)
            this.grid[0][x].color = BOARD_COLOR
    }

    private fun check_full_rows() {
        var rows_removed = 0
        for(y in 0..<BOARD_SIZE_Y){
            // if the row is full
            if (List(BOARD_SIZE_X){ x -> this.grid[x][y]}.all{ it.color != BOARD_COLOR }){
                this.remove_row(y)
                rows_removed ++
            }
        }

        this.score += when(rows_removed){
            1 ->  100
            2 ->  200
            3 ->  400
            4 ->  800
            else -> 0
        }
    }
}
