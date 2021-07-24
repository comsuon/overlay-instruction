
# Overlay Instruction Layout  
  
Hi, this is custom view to show an overlay view on your app screens, which is cut-off where you want to gain attention of users.  
This library is using DialogFragment to host the overlay layout. Along with the custom view, there is an helper function which helps to quickly create and show the layout on desired position.   
  
***Attention**: this library is still under development, please create an issue for improvements or bugs you have encountered. Thanks!*   
  
<img src="https://github.com/comsuon/overlay-instruction/blob/master/ezgif.com-gif-maker.gif" alt="Example" width="300"/>

# Usage  
  
## Add dependency  
   [![](https://jitpack.io/v/comsuon/overlay-instruction.svg)](https://jitpack.io/#comsuon/overlay-instruction) 
- **Step 1:**  
  Add Jitpack repository to your root build.gradle at the end of repositories.  

		allprojects { 
		     repositories { 	
		     ... maven { url 'https://jitpack.io' 
		         } 
		     } 
		}

 - **Step 2:**  
 Add the dependency to your project build.gradle dependency.  
 

	    dependencies { implementation 'com.github.comsuon:overlay-instruction:Tag' } 
  
## Setup and show   
In your Fragment activity class, call the helper extension function ***setupInstruction***.  
 E.g:  
  

     val rootView = findViewById<ViewGroup>(android.R.id.content).rootView as ViewGroup
     this.setUpInstruction(    
           parentLayout = rootView,   
           viewToHighlight = arrayOf(R.id.textView, R.id.nextTextView, R.id.finishTextView),  
		   instructionContent = arrayOf("Test", "Test 2", "Test 3"),
		   btnContent = arrayOf("Next", "Next", "Done"),
		   onFinishInstructionListener = object : OverlayInstructionDialog.CallbackListener {           override fun onInstructionFinished() {    
                   Toast.makeText(this@MainActivity, "Congrats! Welcome onboard.", Toast.LENGTH_SHORT).show()    
               }    
           }  
     )  

**Parameters:**  
  
 - `parentLayout = rootView`: This is root view of the activity.  
 - `viewToHighlight = arrayOf(R.id.textView, R.id.nextTextView, R.id.finishTextView)` : Array of views that you want to highlight, these view will be shown under the layout at the cut-off position.  
 - `instructionContent = arrayOf("Test", "Test 2", "Test 3")` : Array of additional guidelines for each highlighted view above.  
 - `btnContent = arrayOf("Next", "Next", "Done"),`: Array of content for call to actions to navigate through the instruction.  
 - `onFinishInstructionListener` : A callback which will be invoked after the instruction finished  
   
## Customize the style  
  
The library exposes several styleable  attributes, to support you customizing the style of the Overlay layout  
  
 

    <declare-styleable name="OverlayInstruction"> 
          <attr name="oi_dimmedBackground" format="reference" />
          <attr name="oi_highlightStrokeWidth" format="dimension" />
          <attr name="oi_highlightStrokeColor" format="color" />
          <attr name="oi_highlightStrokeStyle" format="enum"> 
	          <enum name="line" value="0" /> 
	          <enum name="dashed" value="1" />
	          <enum name="dotted" value="2" /> 
          </attr> 
          <attr name="oi_highlightPadding" format="dimension" /> 
          <attr name="oi_guideContentMargin" format="dimension" />
          <attr name="oi_guideContentPadding" format="dimension" /> 
          <attr name="oi_guideContentBG" format="reference" /> 
          <attr name="oi_guideContentTextStyle" format="reference" /> 
          <attr name="oi_guideBtnBG" format="reference" /> 
          <attr name="oi_guideBtnTextStyle" format="reference" /> 
    </declare-styleable>  

The library is already styled through a style item `InstructionDialog`.  
To customize the style, create new style item with the parent as `InstructionDialog` , and define the attributes you desire to change.  
E.g:  
  

     <style name="CustomInstructionDialog" parent="InstructionDialog"> 
	     <item name="oi_dimmedBackground">@color/red</item> 
	     <item name="oi_highlightStrokeStyle">line</item> 
	     <item name="oi_guideBtnTextStyle">@style/btnStyle</item> 
     </style>
