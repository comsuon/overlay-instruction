package com.example.myapplication

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.comsuon.overlayinstruction.OverlayInstructionDialog
import com.comsuon.overlayinstruction.setUpInstruction

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootView = findViewById<ViewGroup>(android.R.id.content).rootView as ViewGroup

        this.setUpInstruction(
            parentLayout = rootView,
            viewToHighlight = arrayOf(R.id.textView, R.id.nextTextView, R.id.finishTextView),
            instructionContent = arrayOf("Test", "Test 2", "Test 3"),
            btnContent = arrayOf("Next", "Next", "Done"),
            onFinishInstructionListener = object : OverlayInstructionDialog.CallbackListener {
                override fun onInstructionFinished() {
                    Toast.makeText(this@MainActivity, "Congrats! Welcome onboard.", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}