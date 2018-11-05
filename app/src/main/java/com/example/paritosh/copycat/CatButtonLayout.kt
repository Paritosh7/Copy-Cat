package com.example.paritosh.copycat

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageButton

class CatButtonLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var isExpanded: Boolean = false

    private val RADIUS: Float

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CatButtonLayout)
        RADIUS = a.getDimension(R.styleable.CatButtonLayout_radius, 0f)
        a.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        addView(
            ImageButton(context).apply {
                layoutParams =
                        LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                            .apply { gravity = Gravity.CENTER }
                setImageResource(R.drawable.cat)
                setBackgroundResource(R.drawable.cat_button_bg_selector)
                setOnClickListener { if (isExpanded) collapse() else expand() }
            }
        )
        iterateOverChildButtons { i ->
            (getChildAt(i).layoutParams as LayoutParams).gravity = Gravity.CENTER
        }
        showChildButtons(false)
    }

    private fun expand() {
        isExpanded = true
        iterateOverChildButtons { i ->
            getChildAt(i).animate().apply {
                duration = 500
                interpolator = OvershootInterpolator()
                setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {}

                    override fun onAnimationEnd(animation: Animator?) {}

                    override fun onAnimationCancel(animation: Animator?) {}

                    override fun onAnimationStart(animation: Animator?) {
                        getChildAt(i).visibility = View.VISIBLE
                    }
                })
                val angle = (getChildAt(i).layoutParams as LayoutParams).angle
                translationX(RADIUS * Math.cos(Math.toRadians(angle.toDouble())).toFloat())
                translationY(RADIUS * Math.sin(Math.toRadians(angle.toDouble())).toFloat())
            }.start()
        }
    }

    private fun collapse() {
        isExpanded = false
        iterateOverChildButtons { i ->
            getChildAt(i).animate().apply {
                duration = 200
                interpolator = AccelerateInterpolator()
                setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {}

                    override fun onAnimationEnd(animation: Animator?) {
                        getChildAt(i).visibility = View.GONE
                    }

                    override fun onAnimationCancel(animation: Animator?) {}

                    override fun onAnimationStart(animation: Animator?) {}

                })
                translationX(0f)
                translationY(0f)
            }.start()
        }
    }

    private fun showChildButtons(show: Boolean) = iterateOverChildButtons { i ->
        getChildAt(i).visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun iterateOverChildButtons(block: (i: Int) -> Unit) = repeat(childCount - 1) { i -> block(i) }

    fun setOnChildButtonClickListener(clickListener: (i: Int, view: View) -> Unit) =
        iterateOverChildButtons { i ->
            getChildAt(i).setOnClickListener { clickListener.invoke(i, it) }
        }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(context, null)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        return p is LayoutParams
    }

    class LayoutParams : FrameLayout.LayoutParams {
        var angle: Int = 0

        constructor(width: Int, height: Int) : super(width, height)

        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
            attrs?.let {
                val a = context.obtainStyledAttributes(it, R.styleable.CatButtonLayout_Layout)
                angle = a.getInteger(R.styleable.CatButtonLayout_Layout_angle, 0)
                a.recycle()
            }
        }
    }
}