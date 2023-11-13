package com.example.cryptomarket.view.customs

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.LinkedList


interface ActionClickListener {
    fun onClick(pos: Int)
}

@SuppressLint("ClickableViewAccessibility")
abstract class RecyclerViewSwipeHelper(
    private val context: Context?,
    private val recyclerView: RecyclerView,
    private val buttonWidth: Int
) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private var buttonList = mutableListOf<ActionButton>()
    private val gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            for (button in buttonList) {
                if (button.onClick(e.x, e.y)) break
            }
            return true
        }
    })
    private var swipePosition = -1
    private var swipeThreshold = 0.5f
    private val buttonBuffer: MutableMap<Int, MutableList<ActionButton>> = HashMap()
    private val removerQueue = object : LinkedList<Int>() {
        override fun add(element: Int): Boolean {
            return if (contains(element)) false else super.add(element)
        }
    }
    private val onTouchListener = object: View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(p0: View, event: MotionEvent): Boolean {

            try {
                if (swipePosition < 0) return false
                val point = Point(event.rawX.toInt(), event.rawY.toInt())
                val swipeViewHolder = recyclerView.findViewHolderForAdapterPosition(swipePosition)
                val swipedItem = swipeViewHolder!!.itemView
                val rect = Rect()
                swipedItem.getGlobalVisibleRect(rect)
                if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_MOVE) {
                    if (rect.top < point.y && rect.bottom > point.y) {
                        gestureDetector.onTouchEvent(event)
                    } else {
                        removerQueue.add(swipePosition)
                        swipePosition = -1
                        recoverSwipedItem()
                    }
                }
            } catch (e: Exception) {
                Log.e("ISSUE", e.message!!)
            }
            return false
        }

    }

    init {
        recyclerView.setOnTouchListener(onTouchListener)
        attachSwipe()
    }

    private fun attachSwipe() {
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    @Synchronized
    private fun recoverSwipedItem() {
        while (!removerQueue.isEmpty()) {
            val pos = removerQueue.poll()!!
            if (pos > -1) {
                recyclerView.adapter!!.notifyItemChanged(pos)
            }
        }
    }

    inner class ActionButton(
        private val context: Context,
        private val text: String,
        private val textSize: Int,
        private val imageResId: Int,
        private val color: Int,
        listener: ActionClickListener
    ) {
        private var pos = 0
        private var clickRegion: RectF? = null
        private val listener: ActionClickListener
        private val resources: Resources

        init {
            this.listener = listener
            resources = context.resources
        }

        fun onClick(x: Float, y: Float): Boolean {
            if (clickRegion != null && clickRegion!!.contains(x, y)) {
                listener.onClick(pos)
                return true
            }
            return false
        }

        fun onDraw(c: Canvas, rectF: RectF, pos: Int) {
            val p = Paint()
            p.color = color
            c.drawRect(rectF, p)
            //text
            p.color = Color.WHITE
            p.textSize = textSize.toFloat()
            val r = Rect()
            val cHeight = rectF.height()
            val cWidth = rectF.width()
            p.textAlign = Paint.Align.LEFT
            p.getTextBounds(text, 0, text.length, r)
            val x: Float = cWidth / 2f - r.width() / 2f - r.left
            val y: Float = cHeight / 2f + r.height() / 2f - r.bottom
            if (imageResId == 0) { // if only display text
                c.drawText(text, rectF.left + x, rectF.top + y, p)
            } else {
                val d = ContextCompat.getDrawable(
                    context,
                    imageResId
                )
                val bitmap = drawableToBitmap(d)
                c.drawBitmap(
                    bitmap,
                    rectF.left + x - bitmap.width / 2, rectF.top + y - bitmap.height / 2, p
                )
                //c.drawBitmap(bitmap, (rectF.left + rectF.right) / 2 , (rectF.top + rectF.bottom) /2 , p);
            }
            clickRegion = rectF
            this.pos = pos
        }
    }

    private fun drawableToBitmap(d: Drawable?): Bitmap {
        if (d is BitmapDrawable) {
            return d.bitmap
        }
        val bitmap =
            Bitmap.createBitmap(d!!.intrinsicWidth, d.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        d.setBounds(0, 0, canvas.width, canvas.height)
        d.draw(canvas)
        return bitmap
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        viewHolder1: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
        val pos = viewHolder.bindingAdapterPosition
        if (swipePosition != pos) {
            removerQueue.add(swipePosition)
        }
        swipePosition = pos
        if (buttonBuffer.containsKey(swipePosition)) {
            buttonList = buttonBuffer[swipePosition]!!
        } else {
            buttonList.clear()
        }
        buttonBuffer.clear()
        swipeThreshold = 0.5f * buttonList.size * buttonWidth
        recoverSwipedItem()
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return swipeThreshold
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 0.1f * defaultValue
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 5.0f * defaultValue
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val pos = viewHolder.bindingAdapterPosition
        var translations = dX
        val itemView = viewHolder.itemView
        if (pos < 0) {
            swipePosition = pos
            return
        }
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                var buffer: MutableList<ActionButton> = ArrayList()
                if (!buttonBuffer.containsKey(pos)) {
                    instantiateMyButton(viewHolder, buffer)
                    buttonBuffer[pos] = buffer
                } else {
                    buffer = buttonBuffer[pos]!!
                }
                translations = dX * buffer.size * buttonWidth / itemView.width
                drawButton(c, itemView, buffer, pos, translations)
            }
        }
        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            translations,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    private fun drawButton(
        c: Canvas,
        itemView: View,
        buffer: List<ActionButton>,
        pos: Int,
        translations: Float
    ) {
        var right = itemView.right.toFloat()
        val dButtonWidth = -1 * translations / buffer.size
        for (button in buffer) {
            val left = right - dButtonWidth
            button.onDraw(
                c,
                RectF(left, itemView.top.toFloat(), right, itemView.bottom.toFloat()),
                pos
            )
            right = left
        }
    }

    abstract fun instantiateMyButton(viewHolder: RecyclerView.ViewHolder?, buffer: MutableList<ActionButton>?)
}