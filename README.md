# KorGe Tetris
This is the simple tetris game with built-in AI agent to play it automatically
### How to run the project?
 1. Clone the repository:
```bash
git clone https://github.com/KonstBeliakov/KorGe_Tetris.git
```
2. Build a project `./gradlew build`
3. Run `./gradlew runJvm`
4. Have fun :)
### Usage examples
Bot achieved 12800 points :)
![image](Screenshot%202025-01-21%20165216.png)
I'm trying to play tetris: https://youtu.be/xi-YsKHrKGc
### How to play?
- `←` - move tetramino to the left
- `→` - move tetramino to the right
- `R` - rotate tetramino
- `↓` - instantly drop tetramino to the bottom of the board
- `O` - make a move automatically (AI will instantly rotate, move and drop one tetramino)
- `N` - start a new game (if you finished the previous one)
---
##### How I created a project and what was hard in it
1. For some reason I can't make those imports to work:
```kotlin
import com.soywiz.korge.view.*
import com.soywiz.korge.input.*
import com.soywiz.korge.scene.*
import com.soywiz.korge.game.GameWindow
import com.soywiz.korge.time.*
import com.soywiz.korge.input.Key
import com.soywiz.korio.async.*
```
and ChatGpt didn't want to write code examples without them :(

2. My AI agent could not move pieces to the left (only to the right). I thought that this would not be critical, because the figures initially appear in the upper left corner, but it turns out that this is not enough):
Because of this, the first column was empty most of the time and the bot lost
