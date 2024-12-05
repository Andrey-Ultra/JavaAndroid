package com.example.birdgame

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.CountDownTimer
import android.util.Log
import android.view.MotionEvent
import android.view.View


class GameView(context: Context?) : View(context) {
    private var viewWidth: Int = 0
    private var viewHeight: Int = 0
    private var points: Int = 0
    private var playerBird: Sprite? = null
    private val timerInterval = 30
    private var enemyBird: Sprite? = null
    private val backButtonRect = Rect()
    private val backButtonText = "Menu"



    private fun teleportEnemy() {
        enemyBird!!.setX(viewWidth!! + Math.random() * 500)
        enemyBird!!.setY((Math.random() * (viewHeight!! - enemyBird!!.getFrameHeight())).toInt().toDouble())
    }

    private fun update() {
        Log.i("update", "In update")
        playerBird!!.update(timerInterval)
        enemyBird!!.update(timerInterval)
        Log.i("update", "updated birds")

        Log.i("update", viewHeight!!.toString())
        if (playerBird!!.getY() + playerBird!!.getFrameHeight() > viewHeight!!) {
            Log.i("update", "In first if")
            playerBird!!.setY((viewHeight!! - playerBird!!.getFrameHeight().toDouble()))
            playerBird!!.setVy(-playerBird!!.getVy())
        }
        else if (playerBird!!.getY() < 0) {
            Log.i("update", "in else if")
            playerBird!!.setY(0.0)
            playerBird!!.setVy(-playerBird!!.getVy())
        }
        Log.i("update", "updated PlayerBird")

        if (enemyBird!!.getX() < - enemyBird!!.getFrameWidth()) {
            teleportEnemy ()
            points +=10
        }

        if (enemyBird!!.intersect(playerBird!!)) {
            teleportEnemy ()
            points -= 20
        }
        Log.i("update", "updated enemyBird")


        invalidate()
        Log.i("update", "Invalidated")

    }


    inner class Timer: CountDownTimer(Int.MAX_VALUE.toLong(), timerInterval.toLong()) {
        override fun onTick(millisUntilFinished: Long) {
            update()
        }

        override fun onFinish() {
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.i("sizeChanged", "In sizeChanged")
        super.onSizeChanged(w, h, oldw, oldh)
        Log.i("sizeChanged", "made super")
        viewWidth = w
        viewHeight = h
        Log.i("sizeChanged", "did viewWidth")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Заливка фона
        canvas.drawARGB(250, 127, 199, 255)

        // Настройка краски для текста
        val p = Paint()
        p.isAntiAlias = true
        p.textSize = 80.0f
        p.color = Color.WHITE

        // Отображение очков
        val text = points.toString()
        val textWidth = p.measureText(text)
        val textX = (viewWidth / 2).toFloat() - textWidth / 2
        val textY = 200f
        canvas.drawText(text, textX, textY, p)

        // Рисуем кнопку "Menu"
        p.textSize = 80.0f
        val buttonWidth = p.measureText("<--").toInt()
        val buttonHeight = 60

        val topOffset = 100
        backButtonRect.set(20, topOffset, 20 + buttonWidth + 40, topOffset + buttonHeight + 40) // Позиция кнопки

        p.color = Color.GRAY
        canvas.drawRect(backButtonRect, p)

        p.color = Color.WHITE
        canvas.drawText("<--", (backButtonRect.left + 20).toFloat(), (backButtonRect.bottom - 20).toFloat(), p)

        // Рисование птиц
        playerBird!!.draw(canvas)
        enemyBird!!.draw(canvas)
    }




    init {
        var b = BitmapFactory.decodeResource(context!!.resources, R.drawable.bird_drawing)
        var w = b.width / 5
        var h = b.height / 3
        Log.i("drawing", "w = $w h = $h")
        var firstFrame = Rect(0, 0, w, h)
        playerBird = Sprite(10.0, 0.0, 0.0, 250.0, firstFrame, b)
        Log.i("gameview", "made playerBird")

        val t = Timer()
        t.start()
        Log.i("gameview", "started timer")


        for (i in 0..2) {
            for (j in 0..4) {
                if (i == 0 && j == 0) {
                    continue
                }
                if (i == 2 && j == 4) {
                    continue
                }
                playerBird!!.addFrame(Rect(j * w, i * h, j * w + w, i * h + h))
                Log.i("drawing", "frame " + (j * w).toString() + " " + (i * h).toString() + " " + (j * w + w).toString() + " " + (i * h + h).toString())
            }
        }
        Log.i("gameview", "added frames")

        b = BitmapFactory.decodeResource(context.resources, R.drawable.enemy_drawing)
        w = b.width / 5
        h = b.height / 3
        firstFrame = Rect(4 * w, 0, 5 * w, h)
        enemyBird = Sprite(2000.0, 250.0, -300.0, 0.0, firstFrame, b)
        Log.i("gameview", "made enemyBird")

        for (i in 0..2) {
            for (j in 4 downTo 0) {
                if (i == 0 && j == 4) {
                    continue
                }
                if (i == 2 && j == 0) {
                    continue
                }
                enemyBird!!.addFrame(Rect(j * w, i * h, j * w + w, i * h + h))
            }
        }
        Log.i("gameview", "added enemy frames")
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val eventAction = event.action

        if (eventAction == MotionEvent.ACTION_DOWN) {

            if (backButtonRect.contains(event.x.toInt(), event.y.toInt())) {
                // Возврат в меню
                val intent = android.content.Intent(context, GameMenuActivity::class.java)
                context.startActivity(intent)
                (context as? android.app.Activity)?.finish() // Завершение текущей активности
                return true
            }

            // Логика управления птицей
            if (event.y < playerBird!!.getBoundingBoxRect().top) {
                playerBird!!.setVy(-250.0)
                points--
            } else if (event.y > (playerBird!!.getBoundingBoxRect().bottom)) {
                playerBird!!.setVy(250.0)
                points--
            }
        }

        return true
    }


}
