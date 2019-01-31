package com.example.eafor.socialnetwork.support;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.example.eafor.socialnetwork.R;

public class AvatarAdapter {
    public static void setImg(int id, Context context, ImageView imageView){
        switch (id){
            case 1: imageView.setImageDrawable(context.getDrawable(R.drawable.m1)); break;
            case 2:imageView.setImageDrawable(context.getDrawable(R.drawable.m2)); break;
            case 3:imageView.setImageDrawable(context.getDrawable(R.drawable.m3)); break;
            case 4:imageView.setImageDrawable(context.getDrawable(R.drawable.m4)); break;
            case 5:imageView.setImageDrawable(context.getDrawable(R.drawable.m5)); break;
            case 6:imageView.setImageDrawable(context.getDrawable(R.drawable.m6)); break;
            case 7:imageView.setImageDrawable(context.getDrawable(R.drawable.m7)); break;
            case 8:imageView.setImageDrawable(context.getDrawable(R.drawable.m8)); break;
            case 9:imageView.setImageDrawable(context.getDrawable(R.drawable.m9)); break;
            case 10: imageView.setImageDrawable(context.getDrawable(R.drawable.m10)); break;
            case 11: imageView.setImageDrawable(context.getDrawable(R.drawable.w1)); break;
            case 12: imageView.setImageDrawable(context.getDrawable(R.drawable.w2)); break;
            case 13: imageView.setImageDrawable(context.getDrawable(R.drawable.w3)); break;
            case 14: imageView.setImageDrawable(context.getDrawable(R.drawable.w4)); break;
            case 15: imageView.setImageDrawable(context.getDrawable(R.drawable.w5)); break;
                default: imageView.setImageDrawable(context.getDrawable(R.drawable.m1)); break;
        }
    }
}
