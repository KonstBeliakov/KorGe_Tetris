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

import settings.*
import board.Board

suspend fun main() = Korge(windowSize = Size(512, 512), backgroundColor = Colors["#2b2b2b"]) {
    val sceneContainer = sceneContainer()

    sceneContainer.changeTo { MyScene() }
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
