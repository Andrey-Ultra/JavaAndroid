package com.example.birdgame

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import kotlin.math.abs


class Sprite {
    private var bitmap: Bitmap? = null
    private var frames: ArrayList<Rect>? = null
    private var frameWidth = 0
    private var frameHeight = 0
    private var currentFrame = 0
    private var frameTime = 0.0
    private var timeForCurrentFrame = 0.0
    private var x = 0.0
    private var y = 0.0
    private var velocityX = 0.0
    private var velocityY = 0.0
    private var padding = 0

    constructor(
        x: Double, y: Double, velocityX: Double, velocityY: Double,
        initialFrame: Rect, bitmap: Bitmap?
    ) {
        this.x = x
        this.y = y
        this.velocityX = velocityX
        this.velocityY = velocityY
        this.bitmap = bitmap
        this.frames = ArrayList()
        this.frames!!.add(initialFrame)
        this.bitmap = bitmap
        this.timeForCurrentFrame = 0.0
        this.frameTime = 0.1
        this.currentFrame = 0
        this.frameWidth = initialFrame.width()
        this.frameHeight = initialFrame.height()
        this.padding = 20
    }

    fun getX(): Double {
        return x
    }

    fun setX(x: Double) {
        this.x = x
    }

    fun getY(): Double {
        return y
    }

    fun setY(y: Double) {
        this.y = y
    }

    fun getFrameWidth(): Int {
        return frameWidth
    }

    fun setFrameWidth(frameWidth: Int) {
        this.frameWidth = frameWidth
    }

    fun getFrameHeight(): Int {
        return frameHeight
    }

    fun setFrameHeight(frameHeight: Int) {
        this.frameHeight = frameHeight
    }

    fun getVx(): Double {
        return velocityX
    }

    fun setVx(velocityX: Double) {
        this.velocityX = velocityX
    }

    fun getVy(): Double {
        return velocityY
    }

    fun setVy(velocityY: Double) {
        this.velocityY = velocityY
    }

    fun getCurrentFrame(): Int {
        return currentFrame
    }


    fun setCurrentFrame(currentFrame: Int) {
        this.currentFrame = currentFrame % frames!!.size
    }

    fun getFrameTime(): Double {
        return frameTime
    }

    fun setFrameTime(frameTime: Double) {
        this.frameTime = abs(frameTime)
    }

    fun getTimeForCurrentFrame(): Double {
        return timeForCurrentFrame
    }

    fun setTimeForCurrentFrame(timeForCurrentFrame: Double) {
        this.timeForCurrentFrame = abs(timeForCurrentFrame)
    }

    fun getFramesCount(): Int {
        return frames!!.size
    }

    fun addFrame(frame: Rect?) {
        frames!!.add(frame!!)
    }

    fun update(ms: Int) {
        timeForCurrentFrame += ms.toDouble()
        if (timeForCurrentFrame >= frameTime) {
            currentFrame = (currentFrame + 1) % frames!!.size
            timeForCurrentFrame = timeForCurrentFrame - frameTime
        }
        x = x + velocityX * ms / 1000.0
        y = y + velocityY * ms / 1000.0
    }

    fun draw(canvas: Canvas) {
        val p = Paint()
        val destination = Rect(
            x.toInt(), y.toInt(), (x + frameWidth).toInt(),
            (y + frameHeight).toInt()
        )
        canvas.drawBitmap(bitmap!!, frames!![currentFrame], destination, p)
    }


    fun getBoundingBoxRect(): Rect {
        return Rect(
            x.toInt() + padding,
            y.toInt() + padding,
            (x + frameWidth - 2 * padding).toInt(),
            (y + frameHeight - 2 * padding).toInt()
        )
    }

    fun intersect(s: Sprite): Boolean {
        return getBoundingBoxRect().intersect(s.getBoundingBoxRect())
    }
}