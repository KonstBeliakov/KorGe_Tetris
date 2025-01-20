import korlibs.event.*
import korlibs.time.*
import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.korge.tween.*
import korlibs.korge.view.Text
import korlibs.korge.view.text
import korlibs.korge.view.*
import korlibs.image.color.*
import korlibs.image.format.*
import korlibs.io.file.std.*
import korlibs.io.lang.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*

import java.time.Instant
import java.time.Duration

const val BOARD_SIZE_X = 8
const val BOARD_SIZE_Y = 12
const val UPDATE_TIME = 0.1
const val BLOCK_SIZE = 30.0
const val SEPARATION = 5.0

val BOARD_COLOR = Colors.WHITE

suspend fun main() = Korge(windowSize = Size(512, 512), backgroundColor = Colors["#2b2b2b"]) {
    val sceneContainer = sceneContainer()

    sceneContainer.changeTo { MyScene() }
}

class Figure(container: Container) {
    var x = 0
    var y = 0
    val color = Colors.BLUE
    var block_position = listOf(
        listOf(1, 0, 0, 0),
        listOf(1, 0, 0, 0),
        listOf(1, 0, 0, 0),
        listOf(1, 0, 0, 0)
    )
    val blocks = List(4) { i ->
        container.solidRect(BLOCK_SIZE, BLOCK_SIZE, this.color) {
            position(50.0 + i * (BLOCK_SIZE + SEPARATION), 50.0 + 0 * (BLOCK_SIZE + SEPARATION))
        }
    }

    fun fall() {
        for (block in this.blocks) {
            block.y += BLOCK_SIZE + SEPARATION
        }
        this.y += 1
    }

    fun left(){
        for (block in this.blocks){
            block.x -= BLOCK_SIZE + SEPARATION
        }
        this.x -= 1
    }

    fun right(){
        for (block in this.blocks){
            block.x += BLOCK_SIZE + SEPARATION
        }
        this.x += 1
    }

    fun hide(){
        for(i in 0..<4){
            this.blocks[i].removeFromParent()
        }
    }
}

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
            if (List(BOARD_SIZE_X){x -> this.grid[x][y]}.all{ it.color != BOARD_COLOR }){
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

class MyScene : Scene() {
    override suspend fun SContainer.sceneMain() {
        val board = Board(this)

        var keys: MutableList<Key> = mutableListOf()

        addUpdater { time ->
            keys.clear()
            if (views.input.keys[Key.LEFT]) keys.add(Key.LEFT)
            if (views.input.keys[Key.RIGHT]) keys.add(Key.RIGHT)
        }

        val score = text("Score: 0", textSize = 32.0).apply {
            x = 10.0
            y = 10.0
        }

        addUpdater { time ->
            board.update(keys)
            score.text = "Score: ${board.score}"
        }
    }
}
