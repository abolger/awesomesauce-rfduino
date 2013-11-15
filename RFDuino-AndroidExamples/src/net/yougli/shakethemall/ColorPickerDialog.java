package net.yougli.shakethemall;

/** This contribution comes from the public domain as detailed here: 
 * http://www.yougli.net/android/a-photoshop-like-color-picker-for-your-android-application/ 
 * 
 * and modified so that the widget view could be scaled instead of staying at a fixed size.
 * 
 */


import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.rfduino.R;

public class ColorPickerDialog extends Dialog {
    public interface OnColorChangedListener {
        void colorChanged(String key, int color);
    }

    private OnColorChangedListener mListener;
    private int mInitialColor, mDefaultColor;
    private String mKey;

    
	private static class ColorPickerView extends View {
		private Paint mPaint;
		private float mCurrentHue = 0;
		private int mCurrentX = 0, mCurrentY = 0;
		private int mCurrentColor, mDefaultColor;
		private final int[] mHueBarColors = new int[258];
		private OnColorChangedListener mListener;

		
		private Rect hueBar;
		private Rect shadeBar;
		private Rect customColorButton;
		private Rect defaultColorButton;
		
		ColorPickerView(Context c, OnColorChangedListener l, int color, int defaultColor) {
			super(c);
			mListener = l;
			mDefaultColor = defaultColor;

			// Get the current hue from the current color and update the main color field
			float[] hsv = new float[3];
			Color.colorToHSV(color, hsv);
			mCurrentHue = hsv[0];
			
			mCurrentColor = color;

			// Initialize the colors of the hue slider bar
			int index = 0;
			for (float i=0; i<256; i += 256/42) // Red (#f00) to pink (#f0f)
			{
				mHueBarColors[index] = Color.rgb(255, 0, (int) i);
				index++;
			}
			for (float i=0; i<256; i += 256/42) // Pink (#f0f) to blue (#00f)
			{
				mHueBarColors[index] = Color.rgb(255-(int) i, 0, 255);
				index++;
			}
			for (float i=0; i<256; i += 256/42) // Blue (#00f) to light blue (#0ff)
			{
				mHueBarColors[index] = Color.rgb(0, (int) i, 255);
				index++;
			}
			for (float i=0; i<256; i += 256/42) // Light blue (#0ff) to green (#0f0)
			{
				mHueBarColors[index] = Color.rgb(0, 255, 255-(int) i);
				index++;
			}
			for (float i=0; i<256; i += 256/42) // Green (#0f0) to yellow (#ff0)
			{
				mHueBarColors[index] = Color.rgb((int) i, 255, 0);
				index++;
			}
			for (float i=0; i<256; i += 256/42) // Yellow (#ff0) to red (#f00)
			{
				mHueBarColors[index] = Color.rgb(255, 255-(int) i, 0);
				index++;
			}

			// Initializes the Paint that will draw the View
			mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			mPaint.setTextAlign(Paint.Align.CENTER);
			mPaint.setTextSize(12);
		}

		// Get the current selected color from the hue bar
		private int getCurrentMainColor()
		{
			int translatedHue = 255-(int)(mCurrentHue*255/360);
			int index = 0;
			for (float i=0; i<256; i += 256/42)
			{
				if (index == translatedHue)
					return Color.rgb(255, 0, (int) i);
				index++;
			}
			for (float i=0; i<256; i += 256/42)
			{
				if (index == translatedHue)
					return Color.rgb(255-(int) i, 0, 255);
				index++;
			}
			for (float i=0; i<256; i += 256/42)
			{
				if (index == translatedHue)
					return Color.rgb(0, (int) i, 255);
				index++;
			}
			for (float i=0; i<256; i += 256/42)
			{
				if (index == translatedHue)
					return Color.rgb(0, 255, 255-(int) i);
				index++;
			}
			for (float i=0; i<256; i += 256/42)
			{
				if (index == translatedHue)
					return Color.rgb((int) i, 255, 0);
				index++;
			}
			for (float i=0; i<256; i += 256/42)
			{
				if (index == translatedHue)
					return Color.rgb(255, 255-(int) i, 0);
				index++;
			}
			return Color.RED;
		}

				
		@Override
		protected void onDraw(Canvas canvas) {
			int bar_width = (canvas.getWidth() - 20)/256; 
			int bar_height = (int) Math.round(canvas.getHeight()*.3 - 20); 
			
			int translatedHue = 255-(int)(mCurrentHue*255/360);
			// Display all the colors of the hue bar with lines
			for (int x=0; x<256; x++)
			{
				// If this is not the current selected hue, display the actual color
				if (translatedHue != x)
				{
					mPaint.setColor(mHueBarColors[x]);
					mPaint.setStrokeWidth(bar_width/256 + 2);
				}
				else // else display a slightly larger black line
				{
					mPaint.setColor(Color.BLACK);
					mPaint.setStrokeWidth(bar_width+3);
				}
				canvas.drawLine(x*bar_width+10, 10, x*bar_width+10, 10+bar_height, mPaint);
			}
			
			//Use this as reference point for touching colors:
			if (hueBar == null) hueBar = new Rect(10, 10, 255*bar_width+10, 10+bar_height); 
			
			int main_color_box_height = (int) Math.round(canvas.getHeight()*.5 - 20); 
			int main_color_box_top_y = (int) Math.round(canvas.getHeight()*.3+10); 
			int main_color_box_bottom_y = (int) Math.round(canvas.getHeight()*.8 - 10); 
						
			int button_row_height = (int) Math.round(canvas.getHeight()*.2 - 20);
			int button_row_top_y = main_color_box_bottom_y + 20;
			int button_row_bottom_y = button_row_top_y + button_row_height;
			
			
			
			// Display all main field colors using LinearGradient
			for (int x=0; x<256; x++)
			{
				int[] colors = new int[2];
				colors[0] = Color.HSVToColor(new float[]{mCurrentHue, 0, x/256.0f});
				colors[1] = Color.HSVToColor(new float[]{mCurrentHue, 1, x/256.0f});
				Shader shader = new LinearGradient(x*bar_width+10, main_color_box_top_y, x*bar_width+10, main_color_box_bottom_y, colors, null, Shader.TileMode.REPEAT);
				mPaint.setShader(shader);
				canvas.drawLine(x*bar_width+10, main_color_box_top_y, x*bar_width+10, main_color_box_bottom_y,  mPaint);
			}
			
			//Use this as reference point for touching colors.
			if (shadeBar == null) shadeBar = new Rect(10, main_color_box_top_y, 255*bar_width+10, main_color_box_bottom_y); 
			
			
			mPaint.setShader(null);

			// Display the circle around the currently selected color in the main field
			if (mCurrentX != 0 && mCurrentY != 0)
			{
				mPaint.setStyle(Paint.Style.STROKE);
				mPaint.setColor(Color.BLACK);
				canvas.drawCircle(mCurrentX, mCurrentY, 10, mPaint);
			}

			// Draw a 'button' with the currently selected color
			mPaint.setStyle(Paint.Style.FILL);
			mPaint.setColor(mCurrentColor);
			customColorButton = new Rect(10, button_row_top_y, canvas.getWidth()/2 -10 , button_row_bottom_y);
			canvas.drawRect(customColorButton, mPaint);

			// Set the text color according to the brightness of the color
			if (Color.red(mCurrentColor)+Color.green(mCurrentColor)+Color.blue(mCurrentColor) < 384)
				mPaint.setColor(Color.WHITE);
			else
				mPaint.setColor(Color.BLACK);
			
			int custom_buttom_center = canvas.getWidth()/4;
			canvas.drawText(getResources().getString(R.string.settings_bg_color_confirm), custom_buttom_center, (button_row_top_y + button_row_bottom_y)/2, mPaint);

			// Draw a 'button' with the default color
			mPaint.setStyle(Paint.Style.FILL);
			mPaint.setColor(mDefaultColor);
			defaultColorButton = new Rect(canvas.getWidth()/2 + 10, button_row_top_y, canvas.getWidth() - 10, button_row_bottom_y);
			canvas.drawRect(defaultColorButton, mPaint);
			int default_button_center = (canvas.getWidth()/2 + canvas.getWidth() - 10)/2;
			
			// Set the text color according to the brightness of the color
			if (Color.red(mDefaultColor)+Color.green(mDefaultColor)+Color.blue(mDefaultColor) < 384)
				mPaint.setColor(Color.WHITE);
			else
				mPaint.setColor(Color.BLACK);
			canvas.drawText(getResources().getString(R.string.settings_default_color_confirm), default_button_center,  (button_row_top_y + button_row_bottom_y)/2, mPaint);
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			
			setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (event.getAction() != MotionEvent.ACTION_DOWN) return true;
			int x = (int) event.getX();
			int y = (int) event.getY();

			// If the touch event is located in the hue bar
			if (hueBar.contains(x, y))
			{
				int h = Math.round((x - hueBar.left)*255.0f/hueBar.width()); //figure out where on the bar we are color-wise
				// Update the main field colors
				mCurrentHue = (255-h)*360/255;
				float [] hsv = {0, 0,0};
				Color.colorToHSV(mCurrentColor, hsv);
				hsv[0] = mCurrentHue; //override with the newly selected hue
				mCurrentColor = Color.HSVToColor(hsv);
				
				// Force the redraw of the dialog
				invalidate();
			}

			// If the touch event is located in the main field
			if (shadeBar.contains(x,y))
			{
				mCurrentX = (int) x;
				mCurrentY = (int) y;
				// Update the current selected color using HSV of existing color to get it right:
				float xValue = 256* Math.round(((float)(mCurrentX - shadeBar.left))/shadeBar.width());
				float ySaturation = ((float) (mCurrentY - shadeBar.top))/shadeBar.height();
				mCurrentColor = Color.HSVToColor(new float[]{mCurrentHue, ySaturation, xValue});
				
				// Force the redraw of the dialog
				invalidate();
				
			}

			// If the touch event is located in the left button, notify the listener with the current color
			if (customColorButton.contains(x,y))
				mListener.colorChanged("", mCurrentColor);

			// If the touch event is located in the right button, notify the listener with the default color
			if (defaultColorButton.contains(x,y))
					mListener.colorChanged("", mDefaultColor);

			return true;
		}
	}

    public ColorPickerDialog(Context context, OnColorChangedListener listener, String key, int initialColor, int defaultColor) {
        super(context);

        mListener = listener;
        mKey = key;
        mInitialColor = initialColor;
        mDefaultColor = defaultColor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnColorChangedListener l = new OnColorChangedListener() {
            public void colorChanged(String key, int color) {
                mListener.colorChanged(mKey, color);
                dismiss();
            }
        };

        setContentView(new ColorPickerView(getContext(), l, mInitialColor, mDefaultColor));
        setTitle(R.string.settings_bg_color_dialog);
    }
}
