package com.comsuon.overlayinstruction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import androidx.core.view.setPadding
import androidx.fragment.app.DialogFragment
import screenRectPx

class OverlayInstructionDialog(
    private val dataArray: Array<InstructionData>,
    private val callbackListener: CallbackListener? = null,
    private val mTheme: Int = R.style.InstructionDialog
) :
    DialogFragment() {

    private var mView: View? = null
    private var mViewPort: ViewPort? = null
    private var mGuideContainer: ConstraintLayout? = null
    private var mDataIterator: Iterator<InstructionData>? = null
    private var mContent: TextView? = null
    private var mButton: TextView? = null
    private var mContentMargin = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false

        mView = inflater.inflate(R.layout.overlay_instruction_dialog_layout, container, false)
        return mView
    }

    override fun getTheme(): Int = mTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewPort = mView?.findViewById(R.id.viewPort)
        mGuideContainer = mView?.findViewById(R.id.cl_guideContainer)
        mContent = mGuideContainer?.findViewById(R.id.tv_content)
        mButton = mGuideContainer?.findViewById(R.id.tv_button)
        mButton?.setOnClickListener { showNextGuide() }
        setStyle()
        mDataIterator = dataArray.iterator()
        mDataIterator?.let {
            showNextGuide()
        }
    }

    private fun setStyle() {
        val theme = context?.theme
        theme?.let {

            val typedArray = it.obtainStyledAttributes(mTheme, R.styleable.OverlayInstruction)
            typedArray.use { ta ->
                val dimmedBackground =
                    ta.getColor(R.styleable.OverlayInstruction_oi_dimmedBackground, 0)
                val highlightPadding =
                    ta.getDimensionPixelSize(R.styleable.OverlayInstruction_oi_highlightPadding, 0)
                val highlightStrokeWidth = ta.getDimensionPixelSize(
                    R.styleable.OverlayInstruction_oi_highlightStrokeWidth,
                    0
                )
                val highlightStrokeColor =
                    ta.getColor(R.styleable.OverlayInstruction_oi_highlightStrokeColor, 0)
                val guideButtonStyle =
                    ta.getDrawable(R.styleable.OverlayInstruction_oi_guideBtnBG)
                val guideButtonTextStyle =
                    ta.getResourceId(R.styleable.OverlayInstruction_oi_guideBtnTextStyle, 0)
                val guideContentBG =
                    ta.getDrawable(R.styleable.OverlayInstruction_oi_guideContentBG)
                val guideTextStyle =
                    ta.getResourceId(R.styleable.OverlayInstruction_oi_guideContentTextStyle, 0)
                val guideContentMargin =
                    ta.getDimensionPixelSize(
                        R.styleable.OverlayInstruction_oi_guideContentMargin,
                        0
                    )
                val guideContentPadding =
                    ta.getDimensionPixelSize(
                        R.styleable.OverlayInstruction_oi_guideContentPadding,
                        0
                    )


                mViewPort?.apply {
                    dimmedBackgroundColor = dimmedBackground
                    framePadding = highlightPadding.toFloat()
                    frameColor = highlightStrokeColor
                    frameStrokeWidth = highlightStrokeWidth.toFloat()
                }
                mButton?.background = guideButtonStyle
                mButton?.setTextAppearance(context, guideButtonTextStyle)
                mContentMargin = guideContentMargin.toFloat()
                mGuideContainer?.background = guideContentBG
                mGuideContainer?.setPadding(guideContentPadding)
                mContent?.setTextAppearance(context, guideTextStyle)
            }

        }
    }

    private fun showNextGuide() {
        if (mDataIterator != null && mDataIterator?.hasNext() == true) {
            val data = mDataIterator!!.next()
            val (left, top) = data.viewPosition
            val (width, height) = data.viewSize

            mViewPort?.locateHighLight(left, top, width, height)
            setInstructionContent(
                data.instructionContent,
                data.buttonText,
                top,
                height
            )
        } else {
            callbackListener?.onInstructionFinished()
            dismiss()
        }
    }

    private fun setInstructionContent(
        content: String,
        buttonText: String,
        highlightViewTop: Int,
        highlightViewHeight: Int
    ) {
        mContent?.text = content
        mButton?.text = buttonText
        mGuideContainer?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mGuideContainer?.viewTreeObserver?.removeOnGlobalLayoutListener(this)

                val contentTop = when {
                    //below highlight
                    highlightViewTop + (mGuideContainer!!.measuredHeight + mContentMargin) < screenRectPx.height() -> {
                        highlightViewTop + highlightViewHeight + mContentMargin
                    }
                    //above the highlight
                    highlightViewTop - (mGuideContainer!!.measuredHeight + mContentMargin) > 0 -> {
                        highlightViewTop - (mGuideContainer!!.measuredHeight + mContentMargin + 44)
                    }
                    //inside highlight
                    else -> {
                        highlightViewTop
                    }
                }
                val layoutParams = (mGuideContainer?.layoutParams as FrameLayout.LayoutParams)
                layoutParams?.marginEnd = mContentMargin.toInt()
                layoutParams?.marginStart = mContentMargin.toInt()
                mGuideContainer?.layoutParams = layoutParams
                mGuideContainer!!.translationY = contentTop.toFloat()
            }
        })
    }

    interface CallbackListener {
        fun onInstructionFinished()
    }

    data class InstructionData(
        val viewPosition: Pair<Int, Int>,
        val viewSize: Pair<Int, Int>,
        val instructionContent: String = "",
        val buttonText: String = ""
    )
}
