package com.example.swipemenulayout

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.LinearLayout
import android.widget.Scroller

class SwipeMenuLayout : LinearLayout {
    private var attrs: AttributeSet? = null
    private val contentViewId: Int
    private val rightMenuId: Int
    private lateinit var rightMenuView: View
    private lateinit var contentView: View
    private val mScroller: Scroller = Scroller(context)
    private var mScaledTouchSlop: Int = 0
    private var lastX = 0f
    private var lastY = 0f
    //是否正在横向滑动
    private var isHorizontalScroll = false

    companion object {
        @SuppressLint("StaticFieldLeak")
        //用单例保证只打开一个侧滑菜单
        var mViewCache: SwipeMenuLayout? = null
    }


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.attrs = attrs
        isClickable = true
        val typedArray = context!!.obtainStyledAttributes(attrs, R.styleable.SwipeMenuLayout)
        contentViewId = typedArray.getResourceId(R.styleable.SwipeMenuLayout_contentView, 0)
        rightMenuId = typedArray.getResourceId(R.styleable.SwipeMenuLayout_rightMenu, 0)
        typedArray.recycle()
    }


    init {
        mScaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        rightMenuView = findViewById(rightMenuId)
        contentView = findViewById(contentViewId)
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = ev.rawX
                lastY = ev.rawY
                mViewCache?.let {
                    if (it != this) {
                        it.animClose()
                    } else {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val distanceX = ev.rawX - lastX
                val distanceY = ev.rawY - lastY
                //当y滑动距离更长时，不拦截
                if (Math.abs(distanceY) > Math.abs(distanceX) && Math.abs(distanceY) > mScaledTouchSlop) {
                    mViewCache?.animClose()
                    return super.dispatchTouchEvent(ev)
                }

                //当x滑动距离更长时，拦截
                if (Math.abs(distanceX) > mScaledTouchSlop || isHorizontalScroll) {
                    isHorizontalScroll = true
                    //关闭其他菜单
                    mViewCache?.let {
                        if (it != this) {
                            it.animClose()
                        }
                    }
                    //拦截
                    parent.requestDisallowInterceptTouchEvent(true);
                    //滑动
                    scrollBy(-distanceX.toInt(), 0)

                    //防止越界
                    if (scrollX <= 0) {
                        scrollTo(0, 0)
                    }
                    if (scrollX > rightMenuView.right - contentView.right) {
                        scrollTo(rightMenuView.right - contentView.right, 0)
                    }

                    lastX = ev.rawX
                    lastY = ev.rawY

                }

            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                //停止滑动后，打开或关闭菜单的动画
                isHorizontalScroll = false
                if (scrollX > (rightMenuView.right - contentView.right) / 2) {//滑动距离超过菜单一半宽度则打开，否则关闭
                    //打开
                    animOpen()
                } else {
                    //关闭
                    animClose()
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    override fun computeScroll() {
        super.computeScroll()
        //判断Scroller是否执行完毕：
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            //通知View重绘-invalidate()->onDraw()->computeScroll()
            invalidate()
        }
    }

    //动画打开菜单
    private fun animOpen() {
        mViewCache = this
        mScroller.startScroll(scrollX, 0, rightMenuView.right - contentView.right - scrollX, 0)
        invalidate()
    }

    //动画关闭菜单
    private fun animClose() {
        mViewCache = null
        mScroller.startScroll(scrollX, 0, -scrollX, 0)
        invalidate()
    }

    //立即关闭菜单
    private fun close() {
        mViewCache = null
        scrollTo(0, 0)
    }

    //销毁时停止动画
    override fun onDetachedFromWindow() {
        mScroller.abortAnimation()
        close()
        super.onDetachedFromWindow()
    }
}