package com.comsuon.overlayinstruction

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ViewPort @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var topPos = 100.0f
    var leftPos = 100.0f
    var maskHeight = 300.0f
    var maskWidth = 600.0f

    private val eraserPaint: Paint = Paint()
    private val framePaint: Paint = Paint()

    var framePadding = 0f
    var frameStrokeWidth = 4f
        set(value) {
            framePaint.strokeWidth = value
        }
    var frameColor = Color.WHITE
        set(value) {
            framePaint.color = value
        }

    var frameCornerRadius = 8f

    var dimmedBackgroundColor = Color.parseColor("#B3000000")


    init {
        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_HARDWARE, null)
        eraserPaint.isAntiAlias = true
        eraserPaint.color = Color.TRANSPARENT
        eraserPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        framePaint.strokeWidth = frameStrokeWidth
        framePaint.color = frameColor
        framePaint.style = Paint.Style.STROKE
        framePaint.pathEffect = DashPathEffect(floatArrayOf(20f, 10f), 15f)
        framePaint.isAntiAlias = true
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(dimmedBackgroundColor)
        val hlLeft = leftPos - framePadding
        val hlTop = topPos - framePadding
        val hlRight = leftPos + maskWidth + framePadding
        val hlBottom = topPos + maskHeight + framePadding

        val rect = RectF(hlLeft, hlTop, hlRight, hlBottom)

        val frameRect =
            RectF(
                hlLeft - frameStrokeWidth,
                hlTop - frameStrokeWidth,
                hlRight + frameStrokeWidth,
                hlBottom + frameStrokeWidth
            )
        val path = Path()
        path.addRoundRect(frameRect, frameCornerRadius, frameCornerRadius, Path.Direction.CW)
        canvas?.drawPath(path, framePaint)
        canvas?.drawRoundRect(rect, frameCornerRadius, frameCornerRadius, eraserPaint)
    }

    fun locateHighLight(left: Int, top: Int, width: Int, height: Int) {
        this.topPos = top.toFloat()
        this.leftPos = left.toFloat()
        this.maskWidth = width.toFloat()
        this.maskHeight = height.toFloat()
        invalidate()
    }

}