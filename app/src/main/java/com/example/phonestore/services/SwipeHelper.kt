package com.example.phonestore.services

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.math.abs
import kotlin.math.max


@SuppressLint("ClickableViewAccessibility")
abstract class SwipeHelper(private val recyclerView: RecyclerView): ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.ACTION_STATE_IDLE,
        ItemTouchHelper.LEFT
) {
    private var swipedPosition = -1
    private val buttonBuffer: MutableMap<Int, List<UnderlayButton>> = mutableMapOf()
    private val recoverQueue = object : LinkedList<Int>(){
        override fun add(element: Int): Boolean {
            if(contains(element)) return false
            return super.add(element)
        }
    }
    private val touchListener = View.OnTouchListener { _, _ ->

        if(swipedPosition < 0) {
            return@OnTouchListener false
        }


        recoverQueue.add(swipedPosition)
        swipedPosition = -1
        recoverSwipeItem()
        true
    }
    init {
        recyclerView.setOnTouchListener(touchListener)
    }
    private fun recoverSwipeItem(){
        while (!recoverQueue.isEmpty()){
            val position = recoverQueue.poll() ?: return
            recyclerView.adapter?.notifyItemChanged(position)
        }
    }

    private fun drawButtons(
            canvas: Canvas,
            buttons: List<UnderlayButton>,
            itemView: View,
            dX: Float
    ){
        var right = itemView.right
        buttons.forEach{ button ->
            val width = button.intrinsicWidth / buttons.intrinsicWidth() *abs(dX)
            val left = right - width
            button.draw(
                    canvas,
                    RectF(left, itemView.top.toFloat(), right.toFloat(), itemView.bottom.toFloat())
            )
            right = left.toInt()
        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val position = viewHolder.adapterPosition
        val maxDX: Float
        val itemView = viewHolder.itemView
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            if(dX < 0){
                if(!buttonBuffer.containsKey(position)){
                    buttonBuffer[position] = instantiateUnderlayButton(position)
                }
                val buttons = buttonBuffer[position] ?: return
                if(buttons.isEmpty()) return
                maxDX = max(-buttons.intrinsicWidth(),dX)
                drawButtons(c, buttons, itemView, maxDX)
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
       val position = viewHolder.adapterPosition
        if(swipedPosition != position) recoverQueue.add(swipedPosition)
        swipedPosition = position
        buttonBuffer[swipedPosition]?.forEach { it.handle(position) }
        recoverSwipeItem()

    }
    abstract fun instantiateUnderlayButton(position: Int): List<UnderlayButton>
    class UnderlayButton(
            private val context: Context,
            private val title: String,
            textSize: Float,
            private val colorRes: Int,
            private val clickListener: (Int)->Unit,

    ){
        private var clickableRegion: RectF? = null
        private val textSizeInPixel: Float = textSize * context.resources.displayMetrics.density // convert dp to px
        private val horizontalPadding = 500.0f
        var intrinsicWidth: Float = 0.0f
        init {
            val paint = Paint()
            paint.textSize = textSizeInPixel
            paint.typeface = Typeface.DEFAULT_BOLD
            paint.textAlign = Paint.Align.LEFT
            val titleBounds = Rect()
            paint.getTextBounds(title, 0, title.length, titleBounds)
            intrinsicWidth = titleBounds.width() +2 * horizontalPadding
        }
        fun draw(canvas: Canvas, rect: RectF){
            val paint = Paint()
            paint.color = ContextCompat.getColor(context, colorRes)
            canvas.drawRect(rect, paint)
            paint.color = ContextCompat.getColor(context, android.R.color.white)
            paint.textSize = textSizeInPixel
            paint.typeface = Typeface.DEFAULT_BOLD
            paint.textAlign = Paint.Align.LEFT
            val titleBounds = Rect()
            paint.getTextBounds(title, 0, title.length, titleBounds)
            val y = rect.height() / 2 + titleBounds.height() / 2 - titleBounds.bottom
            canvas.drawText(title, rect.left + horizontalPadding, rect.top + y, paint)
            clickableRegion = rect

        }
        fun handle(position: Int){
                    clickListener.invoke(position)
        }

    }
    private fun List<UnderlayButton>.intrinsicWidth(): Float {
        if (isEmpty()) return 0.0f
        return map { it.intrinsicWidth }.reduce { acc, fl -> acc + fl }
    }
}