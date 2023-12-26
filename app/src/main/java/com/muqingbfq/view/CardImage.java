package com.muqingbfq.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.muqingbfq.R;

public class CardImage extends MaterialCardView {
    private ImageView imageView;

    public CardImage(Context context) {
        super(context);
        start();
    }
    public CardImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        start();
    }

    public CardImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        start();
    }

    public void start() {
        imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(imageView);
        setImage(R.drawable.icon);
    }


    public void setImage(Object bitmap) {
        Glide.with(getContext())
                .load(bitmap)
                .into(imageView);
    }
}
