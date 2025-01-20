package figure

import korlibs.image.color.*
import korlibs.korge.view.*
import settings.*

class Figure(container: Container) {
    var x = 0
    var y = 0
    val color = Colors.BLUE
    var blockPosition = listOf(
        listOf(1, 0, 0, 0),
        listOf(1, 0, 0, 0),
        listOf(1, 0, 0, 0),
        listOf(1, 0, 0, 0)
    )
    private val blocks = List(4) { i ->
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

    fun left() {
        for (block in this.blocks) {
            block.x -= BLOCK_SIZE + SEPARATION
        }
        this.x -= 1
    }

    fun right() {
        for (block in this.blocks) {
            block.x += BLOCK_SIZE + SEPARATION
        }
        this.x += 1
    }

    fun hide() {
        for (i in 0..<4) {
            this.blocks[i].removeFromParent()
        }
    }
}
