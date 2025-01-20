package figure

import kotlin.random.Random
import korlibs.image.color.*
import korlibs.korge.view.*
import settings.*

val blockPositions = listOf(
    listOf(
        listOf(
            listOf(0, 0, 0, 0),
            listOf(1, 1, 1, 1),
            listOf(0, 0, 0, 0),
            listOf(0, 0, 0, 0)
        ),
        listOf(
            listOf(0, 1, 0, 0),
            listOf(0, 1, 0, 0),
            listOf(0, 1, 0, 0),
            listOf(0, 1, 0, 0)
        ),
        listOf(
            listOf(0, 0, 0, 0),
            listOf(0, 0, 0, 0),
            listOf(1, 1, 1, 1),
            listOf(0, 0, 0, 0)
        ),
        listOf(
            listOf(0, 0, 1, 0),
            listOf(0, 0, 1, 0),
            listOf(0, 0, 1, 0),
            listOf(0, 0, 1, 0)
        )
    ),

    listOf(
        listOf(
            listOf(0, 0, 1, 0),
            listOf(1, 1, 1, 0),
            listOf(0, 0, 0, 0),
            listOf(0, 0, 0, 0)
        ),
        listOf(
            listOf(1, 1, 0, 0),
            listOf(0, 1, 0, 0),
            listOf(0, 1, 0, 0),
            listOf(0, 0, 0, 0)
        ),
        listOf(
            listOf(0, 0, 0, 0),
            listOf(1, 1, 1, 0),
            listOf(1, 0, 0, 0),
            listOf(0, 0, 0, 0)
        ),
        listOf(
            listOf(0, 1, 0, 0),
            listOf(0, 1, 0, 0),
            listOf(0, 1, 1, 0),
            listOf(0, 0, 0, 0)
        )
    ),

    listOf(
        listOf(
            listOf(0, 0, 0, 0),
            listOf(1, 1, 1, 0),
            listOf(0, 0, 1, 0),
            listOf(0, 0, 0, 0)
        ),
        listOf(
            listOf(0, 1, 1, 0),
            listOf(0, 1, 0, 0),
            listOf(0, 1, 0, 0),
            listOf(0, 0, 0, 0)
        ),
        listOf(
            listOf(1, 0, 0, 0),
            listOf(1, 1, 1, 0),
            listOf(0, 0, 0, 0),
            listOf(0, 0, 0, 0)
        ),
        listOf(
            listOf(0, 1, 0, 0),
            listOf(0, 1, 0, 0),
            listOf(1, 1, 0, 0),
            listOf(0, 0, 0, 0)
        )
    ),

    listOf(
        listOf(
            listOf(1, 1, 0, 0),
            listOf(1, 1, 0, 0),
            listOf(0, 0, 0, 0),
            listOf(0, 0, 0, 0)
        ),
        listOf(
            listOf(1, 1, 0, 0),
            listOf(1, 1, 0, 0),
            listOf(0, 0, 0, 0),
            listOf(0, 0, 0, 0)
        ),
        listOf(
            listOf(1, 1, 0, 0),
            listOf(1, 1, 0, 0),
            listOf(0, 0, 0, 0),
            listOf(0, 0, 0, 0)
        ),
        listOf(
            listOf(1, 1, 0, 0),
            listOf(1, 1, 0, 0),
            listOf(0, 0, 0, 0),
            listOf(0, 0, 0, 0)
        )
    ),

    listOf(
        listOf(
            listOf(0, 0, 1, 0),
            listOf(0, 1, 1, 0),
            listOf(0, 1, 0, 0),
            listOf(0, 0, 0, 0)
        ),
        listOf(
            listOf(1, 1, 0, 0),
            listOf(0, 1, 1, 0),
            listOf(0, 0, 0, 0),
            listOf(0, 0, 0, 0)
        ),
        listOf(
            listOf(0, 1, 0, 0),
            listOf(1, 1, 0, 0),
            listOf(1, 0, 0, 0),
            listOf(0, 0, 0, 0)
        ),
        listOf(
            listOf(0, 0, 0, 0),
            listOf(1, 1, 0, 0),
            listOf(0, 1, 1, 0),
            listOf(0, 0, 0, 0)
        )
    ),

    listOf(
        listOf(
            listOf(0, 0, 1, 0),
            listOf(0, 1, 1, 0),
            listOf(0, 0, 1, 0),
            listOf(0, 0, 0, 0)
        ),
        listOf(
            listOf(0, 1, 0, 0),
            listOf(1, 1, 1, 0),
            listOf(0, 0, 0, 0),
            listOf(0, 0, 0, 0)
        ),
        listOf(
            listOf(0, 1, 0, 0),
            listOf(1, 1, 0, 0),
            listOf(0, 1, 0, 0),
            listOf(0, 0, 0, 0)
        ),
        listOf(
            listOf(0, 0, 0, 0),
            listOf(1, 1, 1, 0),
            listOf(0, 1, 0, 0),
            listOf(0, 0, 0, 0)
        )
    ),

    listOf(
        listOf(
            listOf(0, 1, 0, 0),
            listOf(0, 1, 1, 0),
            listOf(0, 0, 1, 0),
            listOf(0, 0, 0, 0)
        ),
        listOf(
            listOf(0, 1, 1, 0),
            listOf(1, 1, 0, 0),
            listOf(0, 0, 0, 0),
            listOf(0, 0, 0, 0)
        ),
        listOf(
            listOf(1, 0, 0, 0),
            listOf(1, 1, 0, 0),
            listOf(0, 1, 0, 0),
            listOf(0, 0, 0, 0)
        ),
        listOf(
            listOf(0, 0, 0, 0),
            listOf(0, 1, 1, 0),
            listOf(1, 1, 0, 0),
            listOf(0, 0, 0, 0)
        )
    ),
)

val figureColors =
    listOf(Colors.GREEN, Colors.ORANGE, Colors.YELLOW, Colors.PINK, Colors.BLUE, Colors.GREEN, Colors.ORANGE)

class Figure(container: Container) {
    var x = 0
    var y = 0
    val blockType = Random.nextInt(0, blockPositions.size)
    val color = figureColors[this.blockType]
    var rotation = 0
    var blockPosition = blockPositions[this.blockType][this.rotation]
    private val blocks = List(4) { i ->
        container.solidRect(BLOCK_SIZE, BLOCK_SIZE, this.color) {
            position(50.0 + i * (BLOCK_SIZE + SEPARATION), 50.0 + 0 * (BLOCK_SIZE + SEPARATION))
        }
    }

    init {
        this.setBlocksPosition()
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

    fun rotate() {
        this.rotation = (this.rotation + 1) % 4
        this.blockPosition = blockPositions[this.blockType][this.rotation]
        this.setBlocksPosition()
    }

    private fun setBlocksPosition() {
        val pos: MutableList<List<Int>> = mutableListOf()
        for (i in 0..<4) {
            for (j in 0..<4) {
                if (this.blockPosition[i][j] == 1)
                    pos.add(listOf(i, j))
            }
        }
        for (i in 0..<4) {
            blocks[i].x = 50.0 + (this.x + pos[i][0]) * (BLOCK_SIZE + SEPARATION)
            blocks[i].y = 50.0 + (this.y + pos[i][1]) * (BLOCK_SIZE + SEPARATION)
        }
    }
}
