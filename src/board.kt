package board

import figure.*
import korlibs.event.*
import korlibs.korge.view.*
import settings.*

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
                if (blockPositions[this.figure.blockType][this.figure.rotation][i][j] == 1 &&
                    (j + this.figure.y >= BOARD_SIZE_Y - 1 ||
                            this.grid[this.figure.x + i][this.figure.y + j + 1].color != BOARD_COLOR)
                )
                    return false
            }
        }
        return true
    }

    fun fastFall() {
        while (mayFall()) {
            this.figure.fall()
        }
    }

    fun moveEvalute(x: Int, y: Int, figreType: Int, rotation: Int): Int {
        var s = 0
        for(i in 0..<4){
            for(j in 0..<4){
                if (blockPositions[figreType][rotation][i][j] == 1)
                    s += (y + j)
            }
        }
        return s
    }

    fun optimizedFastFall() {
        var maxMoveValue = -1000
        var mRotation = 0
        var mRight = 0
        var mDy = 0

        for (rotation in 0..<4) {
            if (!this.correctFigurePosition(
                    this.figure.x, this.figure.y, this.figure.blockType, (this.figure.rotation + rotation) % 4
                )
            )
                break
            for (right in 0..<BOARD_SIZE_X) {
                if (!this.correctFigurePosition(
                        this.figure.x + right, this.figure.y, this.figure.blockType, (this.figure.rotation + rotation) % 4
                    )
                )
                    break

                var dy = 0
                for(dy_ in 0..<BOARD_SIZE_Y){
                    if (!this.correctFigurePosition(
                        this.figure.x + right, this.figure.y + dy_, this.figure.blockType, (this.figure.rotation + rotation) % 4
                    )
                    ){
                        dy = dy_ - 1
                        break
                    }
                }

                val moveValue = this.moveEvalute(
                        this.figure.x + right, this.figure.y + dy, this.figure.blockType, (this.figure.rotation + rotation) % 4
                    )

                if (moveValue > maxMoveValue) {
                    maxMoveValue = moveValue
                    mRotation = rotation
                    mRight = right
                    mDy = dy
                }
            }
        }

        //this.figure.x += mRiht
        //this.figure.rotation = (this.figure.rotation + mRotation) % 4
        //this.figure.setBlocksPosition()

        repeat(mRotation) { this.figure.rotate() }
        repeat(mRight) { this.figure.right() }
        repeat(mDy) { this.figure.fall() }
    }

    private fun correctFigurePosition(x: Int, y: Int, figreType: Int, rotation: Int): Boolean {
        for (i in 0..<4) {
            for (j in 0..<4) {
                if (blockPositions[figreType][rotation][i][j] == 1) {
                    if (j + y >= BOARD_SIZE_Y || i + x >= BOARD_SIZE_X || i + x < 0 ||
                        this.grid[i + x][j + y].color != BOARD_COLOR
                    )
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
                        if (blockPositions[this.figure.blockType][this.figure.rotation][i][j] == 1) {
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
        if (System.currentTimeMillis() - this.lastUpdatedHorizontal > HORISONTAL_MOVEMENT_UPATE_TIME) {
            this.lastUpdatedHorizontal = System.currentTimeMillis()

            if (Key.LEFT in keys) {
                if (this.correctFigurePosition(
                        this.figure.x - 1,
                        this.figure.y,
                        this.figure.blockType,
                        this.figure.rotation
                    )
                )
                    this.figure.left()
            }
            if (Key.RIGHT in keys) {
                if (this.correctFigurePosition(
                        this.figure.x + 1,
                        this.figure.y,
                        this.figure.blockType,
                        this.figure.rotation
                    )
                )
                    this.figure.right()
            }
            if (Key.R in keys) {
                if (this.correctFigurePosition(
                        this.figure.x,
                        this.figure.y,
                        this.figure.blockType,
                        (this.figure.rotation + 1) % 4
                    )
                )
                    this.figure.rotate()
            }

            if (Key.DOWN in keys) this.fastFall()
            if (Key.O in keys) this.optimizedFastFall()
        }
    }

    private fun removeRow(rowNumber: Int) {
        for (y in rowNumber downTo 1) {
            for (x in 0..<BOARD_SIZE_X) {
                this.grid[x][y].color = this.grid[x][y - 1].color
            }
        }

        for (x in 0..<BOARD_SIZE_X)
            this.grid[0][x].color = BOARD_COLOR
    }

    private fun checkFullRows() {
        var rowsRemoved = 0
        for (y in 0..<BOARD_SIZE_Y) {
            // if the row is full
            if (List(BOARD_SIZE_X) { x -> this.grid[x][y] }.all { it.color != BOARD_COLOR }) {
                this.removeRow(y)
                rowsRemoved++
            }
        }

        this.score += when (rowsRemoved) {
            1 -> 100
            2 -> 200
            3 -> 400
            4 -> 800
            else -> 0
        }
    }

    private fun checkGameOver() {
        if (List(BOARD_SIZE_X) { x -> this.grid[x][0] }.any { it.color != BOARD_COLOR })
            this.gameOver = true
    }
}
