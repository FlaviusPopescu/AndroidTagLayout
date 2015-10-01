package com.flavpopescu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by flavius on 8/30/15.
 */
public class TagLayout extends LinearLayout {
    private boolean mShuffleRows; //todo #shuffle
    private boolean mSmarterRows; //todo #binpacking
    private int mTextColor;
    private int mTextColorSelected;
    private float mTextSize;
    private String mTagTextFont;
    private float mTagTextPadding;
    private float mVerticalSpacing;
    private Context mContext;
    private String[] mTags;
    private float mHorizontalSpacing;
    private TextView[] mTagViews;
    private int[] mTagWidths;
    private int mAvailableWidth;
    private Set<Integer> mSelectedTags;
    private int mTagBackgroundId;
    private int mTagBackgroundSelectedId;

    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TagLayout, 0, 0);
        try {
            int tagsArrayId = a.getResourceId(R.styleable.TagLayout_com_flavpopescu_tags, 0);
            if (tagsArrayId != 0) {
                mTags = context.getResources().getStringArray(tagsArrayId);
            }

            mHorizontalSpacing = a.getDimension(R.styleable.TagLayout_com_flavpopescu_tagHorizontalSpacing,
                    (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, mContext.getResources().getDisplayMetrics())));

            mVerticalSpacing = a.getDimension(R.styleable.TagLayout_com_flavpopescu_tagVerticalSpacing,
                    (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, mContext.getResources().getDisplayMetrics())));

            mTagBackgroundId = a.getResourceId(R.styleable.TagLayout_com_flavpopescu_tagBackground, 0);
            mTagBackgroundSelectedId = a.getResourceId(R.styleable.TagLayout_com_flavpopescu_tagBackgroundSelected, 0);

            mTagTextPadding = a.getDimension(R.styleable.TagLayout_com_flavpopescu_tagTextPadding,
                    (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, mContext.getResources().getDisplayMetrics())));
            mTagTextFont = a.getString(R.styleable.TagLayout_com_flavpopescu_tagTextFont);
            mTextSize = a.getDimensionPixelSize(R.styleable.TagLayout_com_flavpopescu_tagTextSize,
                    (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, mContext.getResources().getDisplayMetrics())));
            mTextColor = a.getColor(R.styleable.TagLayout_com_flavpopescu_tagTextColor, 0xff000000);
            mTextColorSelected = a.getColor(R.styleable.TagLayout_com_flavpopescu_tagTextColorSelected, 0xff393939);
            mSmarterRows = a.getBoolean(R.styleable.TagLayout_com_flavpopescu_smarterRows, true);
            mShuffleRows = a.getBoolean(R.styleable.TagLayout_com_flavpopescu_shuffleRows, true);
        } finally {
            a.recycle();
        }

        mSelectedTags = new HashSet<>();
    }

    public Set<Integer> getSelectedTags() {
        return mSelectedTags;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setOrientation(VERTICAL);
        mAvailableWidth = w;
        int i = 0;
        mTagViews = new TextView[mTags.length];
        mTagWidths = new int[mTags.length];
        while (i < mTags.length) {
            String currentTag = mTags[i];

            final TextView textView = new TextView(mContext);
            textView.setTextColor(mTextColor);

            final Drawable drawableNormal;
            final Drawable drawableSelected;
            if (mTagBackgroundId > 0) {
                drawableNormal = getResources().getDrawable(mTagBackgroundId).getConstantState().newDrawable();
                textView.setBackground(drawableNormal);
            } else {
                drawableNormal = null;
            }

            if (mTagBackgroundSelectedId > 0) {
                drawableSelected = getResources().getDrawable(mTagBackgroundSelectedId).getConstantState().newDrawable();
            } else {
                drawableSelected = null;
            }

            textView.setText(currentTag);

            Paint paint = new Paint();
            if (!TextUtils.isEmpty(mTagTextFont)) {
                Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), mTagTextFont);
                paint.setTypeface(typeface);
                textView.setTypeface(typeface);
            }

            paint.setTextSize(mTextSize);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            float textWidth = paint.measureText(currentTag);

            LayoutParams textViewParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int horizontalSpacing = (int) mHorizontalSpacing;
            textViewParams.setMargins(horizontalSpacing, 0, horizontalSpacing, 0);
            textView.setLayoutParams(textViewParams);

            final int tagIndex = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectedTags.contains(tagIndex)) {
                        mSelectedTags.remove(tagIndex);
                        textView.setTextColor(mTextColor);
                        if (drawableNormal != null) {
                            textView.setBackground(drawableNormal);
                        }
                    } else {
                        mSelectedTags.add(tagIndex);
                        textView.setTextColor(mTextColorSelected);
                        if (drawableSelected != null) {
                            textView.setBackground(drawableSelected);
                        }
                    }
                }
            });

            int spaceNeeded = (int) textWidth + 2 * horizontalSpacing + 2 * (int) mTagTextPadding;

            mTagWidths[i] = spaceNeeded;
            mTagViews[i] = textView;
            i++;
        }
        addTagViews();
    }

    private void addTagViews() {
        LinearLayout row = new LinearLayout(mContext);
        row.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        row.setOrientation(HORIZONTAL);
        row.setGravity(Gravity.CENTER_HORIZONTAL);

        int spaceLeft = mAvailableWidth;

        for (int i = 0; i < mTagViews.length; i++) {
            int spaceNeeded = mTagWidths[i];
            TextView textView = mTagViews[i];

            if (spaceNeeded < spaceLeft) {
                row.addView(textView);
                spaceLeft -= spaceNeeded;
            } else {
                //row finished
                addView(row);
                row = new LinearLayout(mContext);
                LayoutParams params = new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, (int) mVerticalSpacing, 0, 0);
                row.setLayoutParams(params);
                row.setOrientation(HORIZONTAL);
                row.setGravity(Gravity.CENTER_HORIZONTAL);
                row.addView(textView);
                spaceLeft = mAvailableWidth - spaceNeeded;
            }
        }
    }
}