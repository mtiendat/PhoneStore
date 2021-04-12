package com.example.phonestore.services

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import com.example.phonestore.R

class BadgeDrawable(val context: Context): Drawable() {
    private var badgePaint : Paint = Paint()
    private var badgePaint1 : Paint = Paint()
    private var textPaint : Paint = Paint()
    private var textRect : Rect = Rect()
    private var count: String =""
    private var willDraw: Boolean = false
    init {
        val textSize : Float = context.resources.getDimension(R.dimen.badge_text_size)
        badgePaint.color = Color.RED
        badgePaint.isAntiAlias = true //làm mịn
        badgePaint.style = Paint.Style.FILL
        badgePaint1.color = Color.DKGRAY
        badgePaint1.isAntiAlias = true //làm mịn
        badgePaint1.style = Paint.Style.FILL
        textPaint.color = Color.WHITE
        textPaint.typeface = Typeface.DEFAULT
        textPaint.textSize = textSize
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER

    }
    override fun draw(canvas: Canvas) {
        if(!willDraw){
            return
        }
        val width = bounds.right - bounds.left
        val height = bounds.bottom - bounds.top
        //Vị trí của huy hiệu trên cùng bên phải
        //Sử dụng Math.min
        val radius = ((width.coerceAtLeast(height))/2)/2
        val centerX = (width - radius - 1) + 5
        val centerY = radius - 5
        if(count.length <=2){
            canvas.drawCircle(centerX.toFloat(), centerY.toFloat(),(radius + 7.5).toFloat(), badgePaint1)
            canvas.drawCircle(centerX.toFloat(), centerY.toFloat(),(radius + 5.5).toFloat(), badgePaint)
        }else{
            canvas.drawCircle(centerX.toFloat(), centerY.toFloat(),(radius + 8.5).toFloat(), badgePaint1)
            canvas.drawCircle(centerX.toFloat(), centerY.toFloat(),(radius + 6.5).toFloat(), badgePaint)
        }
        textPaint.getTextBounds(count, 0, count.length, textRect)
        val textHeight = textRect.bottom - textRect.top
        val textY = centerY + (textHeight/2f)
        if(count.length > 2){
            canvas.drawText("99+", centerX.toFloat(), textY, textPaint)
        }else
            canvas.drawText(count, centerX.toFloat(), textY, textPaint)


    }
    fun setCount(count: String){
        this.count = count
        willDraw = count != "0"
        invalidateSelf()
    }
    override fun setAlpha(alpha: Int) {

    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    override fun getOpacity(): Int {
        return  PixelFormat.UNKNOWN
    }

}