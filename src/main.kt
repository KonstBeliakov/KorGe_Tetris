import korlibs.event.*
import korlibs.time.*
import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.korge.tween.*
import korlibs.korge.view.*
import korlibs.image.color.*
import korlibs.image.format.*
import korlibs.io.file.std.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*

import java.time.Instant
import java.time.Duration

const val BOARD_SIZE_X = 10
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

    fun update() {
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

                this.figure = Figure(this.container)
            }
        }
    }
}

class MyScene : Scene() {
    override suspend fun SContainer.sceneMain() {
        val board = Board(this)

        //val square = solidRect(50, 50, Colors.RED) {
        //    position(256, 256)
        //}

        /*addUpdater { time ->
            // Скорость перемещения квадрата
            val speed = 200 * time.seconds
            if (views.input.keys[Key.LEFT]) square.x -= speed
            if (views.input.keys[Key.RIGHT]) square.x += speed
            if (views.input.keys[Key.UP]) square.y -= speed
            if (views.input.keys[Key.DOWN]) square.y += speed
        }*/

        addUpdater { time ->
            board.update()
        }
    }
}
