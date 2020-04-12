package com.toothless.head2head.viewmodels.savedscore

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

class SavedRoundsDivider : RecyclerView.ItemDecoration {
    private companion object
    {
        private val attrs = intArrayOf(android.R.attr.listDivider)
    }

    private val divider : Drawable

    /**
     * Default divider
     */
    constructor(ctx : Context)
    {
        val styledAttributes = ctx.obtainStyledAttributes(attrs)
        divider = styledAttributes.getDrawable(0)!!
        styledAttributes.recycle()
    }

    /**
     * Custom divider
     */
    constructor(ctx : Context, resId : Int)
    {
        divider = ctx.getDrawable(resId)!!
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

//        val childCount = parent.childCount

        for(i in parent.children)
        {
//            val child = parent.getChildAt(i)
            val params = i.layoutParams as RecyclerView.LayoutParams

            val top = i.bottom + params.bottomMargin
            val bottom = top  + divider.intrinsicHeight

            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }
}