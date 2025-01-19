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

suspend fun main() = Korge(windowSize = Size(512, 512), backgroundColor = Colors["#2b2b2b"]) {
    val sceneContainer = sceneContainer()

    sceneContainer.changeTo { MyScene() }
}

class Figure(container: Container){
    var x = 0
    var y = 0
    var block_position = listOf(
        listOf(1, 1, 1, 1),
        listOf(0, 0, 0, 0),
        listOf(0, 0, 0, 0),
        listOf(0, 0, 0, 0)
    )
    val blocks = List(4){i ->
        container.solidRect(50.0, 50.0, Colors.BLUE) {
            position(50.0 + i * 60.0, 50.0 + 0 * 60.0)
        }
    }

    fun fall(){
        for(block in this.blocks){
            block.y += 60
        }
    }
}

class Board(container: Container) {
    var grid: List<List<SolidRect>> = List(5) { i ->
        List(5) { j ->
            container.solidRect(50, 50, Colors.WHITE) {
                position(50 + i * 60, 50 + j * 60)
            }
        }
    }
    var figure = Figure(container)
    var last_updated = Instant.now()

    fun update(){
        if(Duration.between(last_updated, Instant.now()).seconds > 1) {
            this.figure.fall()
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
