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

suspend fun main() = Korge(windowSize = Size(512, 512), backgroundColor = Colors["#2b2b2b"]) {
    val sceneContainer = sceneContainer()

    sceneContainer.changeTo { MyScene() }
}

class Figure(container: Container){
    var x = 0
    var y = 0
    var block_position = listOf(
        listOf(1, 0, 0, 0),
        listOf(1, 0, 0, 0),
        listOf(1, 0, 0, 0),
        listOf(1, 0, 0, 0)
    )
    val blocks = List(4){i ->
        container.solidRect(BLOCK_SIZE, BLOCK_SIZE, Colors.BLUE) {
            position(50.0 + i * (BLOCK_SIZE + SEPARATION), 50.0 + 0 * (BLOCK_SIZE + SEPARATION))
        }
    }

    fun fall(grid: List<List<SolidRect>>){
        var mayFall = true
        for(i in 0..<4){
            for(j in 0..<4){
                if(j + this.y >= BOARD_SIZE_Y - 1 && block_position[i][j] == 1) mayFall = false
            }
        }

        if (mayFall) {
            for (block in this.blocks) {
                block.y += BLOCK_SIZE + SEPARATION
            }
            this.y += 1
        }
    }
}

class Board(container: Container) {
    var grid: List<List<SolidRect>> = List(BOARD_SIZE_X) { x ->
        List(BOARD_SIZE_Y) { y ->
            container.solidRect(BLOCK_SIZE, BLOCK_SIZE, Colors.WHITE) {
                position(50 + x * (BLOCK_SIZE + SEPARATION), 50 + y * (BLOCK_SIZE + SEPARATION))
            }
        }
    }
    var figure = Figure(container)
    var last_updated = Instant.now()

    fun update(){
        if(Duration.between(last_updated, Instant.now()).seconds > UPDATE_TIME) {
            this.figure.fall(this.grid)
            this.last_updated = Instant.now()
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
