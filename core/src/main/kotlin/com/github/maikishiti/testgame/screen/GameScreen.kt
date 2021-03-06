package com.github.maikishiti.testgame.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.github.maikishiti.ecs.component.GraphicComponent
import com.github.maikishiti.ecs.component.TransformComponent
import com.github.maikishiti.testgame.Main
import com.github.maikishiti.testgame.UNIT_SCALE
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.log.logger
import kotlin.math.abs

private val LOG = logger<GameScreen>()

fun moveUp(): Boolean {
    return  Gdx.input.isKeyPressed(Input.Keys.W) or
            Gdx.input.isKeyPressed(Input.Keys.UP)
}
fun moveDown(): Boolean {
    return  Gdx.input.isKeyPressed(Input.Keys.S) or
            Gdx.input.isKeyPressed(Input.Keys.DOWN)
}
fun moveRight(): Boolean {
    return  Gdx.input.isKeyPressed(Input.Keys.D) or
            Gdx.input.isKeyPressed(Input.Keys.RIGHT)
}
fun moveLeft(): Boolean {
    return  Gdx.input.isKeyPressed(Input.Keys.A) or
            Gdx.input.isKeyPressed(Input.Keys.LEFT)
}




class GameScreen(game: Main) : InstanceScreen(game) {

    private val playerTexture = Texture(Gdx.files.internal("Graphics/adventurer.png"))

    private val player = engine.entity{
        with<TransformComponent>{
            position.set(1f,1f,0f)
        }
        with<GraphicComponent>{
            sprite.run {
                setRegion(playerTexture)
                setSize(texture.width * UNIT_SCALE,texture.height * UNIT_SCALE)
                setOriginCenter()
            }
        }
    }

    private val bgTexture = Texture(Gdx.files.internal("Graphics/tileSet.png"))
    private val bgSprite  = Sprite(bgTexture).apply { setSize(144f * UNIT_SCALE,80f * UNIT_SCALE) }

    private var posX = 0f
    private var posY = 0f
    private var dirX = 0f
    private var dirY = 0f

    override fun show() {
        bgSprite.setPosition(0f,0f)
        LOG.debug { "Game screen is shown" }
    }



    override fun render(delta: Float) {
        dirY = if (moveUp  ()) 1f
        else   if (moveDown())-1f
        else 0f

        dirX = if (moveRight()) 1f
        else   if (moveLeft ())-1f
        else 0f

        if (abs(dirX) == 1f &&
            abs(dirY) == 1f) {
            dirX *= 0.707f
            dirY *= 0.707f
        }

        posX += dirX * delta * 10
        posY += dirY * delta * 10
        player[TransformComponent.mapper]?.position?.set(posX, posY, 0f)


        engine.update(delta)
    }

    override fun dispose() {
        LOG.debug { "Disposed the texture $playerTexture" }
        playerTexture.dispose()

        LOG.debug { "Disposed the texture $bgTexture" }
        bgTexture.dispose()
    }
}