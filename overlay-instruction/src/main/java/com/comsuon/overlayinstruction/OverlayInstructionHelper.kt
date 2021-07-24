package com.comsuon.overlayinstruction

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.Window
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.setUpInstruction(
    parentLayout: ViewGroup,
    viewToHighlight: Array<Int>,
    instructionContent: Array<String>,
    btnContent: Array<String>,
    style: Int = R.style.InstructionDialog,
    onFinishInstructionListener: OverlayInstructionDialog.CallbackListener? = null
) {
    parentLayout.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            parentLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

            val rectangle = Rect()
            val window: Window = window
            window.decorView.getWindowVisibleDisplayFrame(rectangle)
            val statusBarHeight: Int = rectangle.top

            val instructionData = viewToHighlight.mapIndexed { index, viewId ->
                val location = IntArray(2)
                val childView = parentLayout.findViewById<View>(viewId)
                childView.getLocationInWindow(location)
                val (left, top) = location

                OverlayInstructionDialog.InstructionData(
                    viewPosition = Pair(left, top - statusBarHeight),
                    viewSize = Pair(childView.measuredWidth, childView.measuredHeight),
                    instructionContent = instructionContent[index],
                    buttonText = btnContent[index]
                )
            }.toTypedArray()

            val instructionDialog = OverlayInstructionDialog(instructionData, mTheme = style, callbackListener = onFinishInstructionListener)
            instructionDialog.show(
                this@setUpInstruction.supportFragmentManager,
                "instructionDialog"
            )
        }
    })
}