package fr.pssoftware.scoretarot;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * TODO: document your custom view class.
 */
public class TableDonneCell extends FrameLayout {
	private String mExampleString; // TODO: use a default from R.string...
	private int mExampleColor = Color.RED; // TODO: use a default from
											// R.color...
	private float mExampleDimension = 0; // TODO: use a default from R.dimen...
	private Drawable mExampleDrawable;

	private TextPaint mTextPaint;
	private float mTextWidth;
	private float mTextHeight;

	public TableDonneCell(Context context) {
		super(context);
		init(context,null, 0);
	}

	public TableDonneCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs, 0);
	}

	public TableDonneCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context,attrs, defStyle);
	}

	private void init(Context ctx,AttributeSet attrs, int defStyle) {
		// Load attributes
		final TypedArray a = getContext().obtainStyledAttributes(attrs,
				R.styleable.TableDonneCell, defStyle, 0);
		LayoutInflater.from(ctx).inflate(R.layout.table_donne_cell, this, true);

		mExampleString = a.getString(R.styleable.TableDonneCell_exampleString);
		mExampleColor = a.getColor(R.styleable.TableDonneCell_exampleColor,
				mExampleColor);
		// Use getDimensionPixelSize or getDimensionPixelOffset when dealing
		// with
		// values that should fall on pixel boundaries.
		mExampleDimension = a.getDimension(
				R.styleable.TableDonneCell_exampleDimension, mExampleDimension);

		if (a.hasValue(R.styleable.TableDonneCell_exampleDrawable)) {
			mExampleDrawable = a
					.getDrawable(R.styleable.TableDonneCell_exampleDrawable);
			mExampleDrawable.setCallback(this);
		}

		a.recycle();

		// Set up a default TextPaint object
		mTextPaint = new TextPaint();
		mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setTextAlign(Paint.Align.LEFT);

	}


	public String getExampleString() {
		return mExampleString;
	}

	public void setExampleString(String exampleString) {
		mExampleString = exampleString;
	}

	public int getExampleColor() {
		return mExampleColor;
	}

	public void setExampleColor(int exampleColor) {
		mExampleColor = exampleColor;
	}

	public float getExampleDimension() {
		return mExampleDimension;
	}

	public void setExampleDimension(float exampleDimension) {
		mExampleDimension = exampleDimension;
	}

	public Drawable getExampleDrawable() {
		return mExampleDrawable;
	}

	public void setExampleDrawable(Drawable exampleDrawable) {
		mExampleDrawable = exampleDrawable;
	}
}
