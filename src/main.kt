import board.*
import board_evaluator.*
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

suspend fun main() = Korge(windowSize = Size(512, 512), backgroundColor = Colors["#2b2b2b"]) {
    val sceneContainer = sceneContainer()

    sceneContainer.changeTo { MyScene() }
}

class MyScene : Scene() {
    override suspend fun SContainer.sceneMain() {
        val evaluator = BoardEvaluator(listOf(1.0, -5.0))
        val board = Board(this, evaluator)

        var keys: MutableList<Key> = mutableListOf()

        addUpdater { time ->
            keys.clear()
            if (views.input.keys[Key.LEFT]) keys.add(Key.LEFT)
            if (views.input.keys[Key.RIGHT]) keys.add(Key.RIGHT)
            if (views.input.keys[Key.R]) keys.add(Key.R)
            if (views.input.keys[Key.DOWN]) keys.add(Key.DOWN)
            if (views.input.keys[Key.O]) keys.add(Key.O)
        }

        val score = text("Score: 0", textSize = 32.0).apply {
            x = 10.0
            y = 10.0
        }

        val gameOverText = text("Game Over", textSize = 72.0).apply {
            x = (views.virtualWidth - width) / 2
            y = (views.virtualHeight - height) / 2
            visible = false
            color = Colors.RED
        }

        val newGameText = text("Press N to start a new game", textSize = 18.0).apply {
            x = (views.virtualWidth - width) / 2
            y = (views.virtualHeight - height) / 2 + 50
            visible = false
            color = Colors.RED
        }

        var sumScore = 0
        var games = 0

        val avgScoreText = text("Average score: ${if (games == 0) 0 else sumScore / games} ($games games)", textSize = 24.0).apply{
            x = 200.0
            y = 0.0
        }

        addUpdater { time ->
            if (board.gameOver) {
                gameOverText.visible = true
                newGameText.visible = true
                if (views.input.keys[Key.N]){
                    sumScore += board.score
                    games ++
                    board.newGame()
                    avgScoreText.text = "Average score: ${if (games == 0) 0 else sumScore / games} ($games games)"
                }
            } else {
                gameOverText.visible = false
                newGameText.visible = false
                board.update(keys)
            }
            score.text = "Score: ${board.score}"
        }
    }
}
