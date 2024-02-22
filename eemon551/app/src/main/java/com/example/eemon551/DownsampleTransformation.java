package com.example.eemon551;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.Key;

import android.content.Context;
import android.graphics.Bitmap;

import java.security.MessageDigest;

public class DownsampleTransformation extends BitmapTransformation {
    private static final String ID = "com.example.DownsampleTransformation";
    private static final byte[] ID_BYTES = ID.getBytes(Key.CHARSET);

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        // 解像度を半分にする例
        int width = toTransform.getWidth() / 10;
        int height = toTransform.getHeight() / 10;
        return Bitmap.createScaledBitmap(toTransform, width, height, false);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof DownsampleTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
