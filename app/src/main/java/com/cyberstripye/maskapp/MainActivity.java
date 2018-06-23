package com.cyberstripye.maskapp;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;


public class MainActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.iv);
        Resources resources = getResources();
        BitmapDrawable original = new BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.slika));
        BitmapDrawable mask = new BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.mask));
        BitmapDrawable maskingImage = compositeDrawableWithMask(resources, original, mask);
        imageView.setImageDrawable(maskingImage);
    }

    public static BitmapDrawable compositeDrawableWithMask(Resources resources,
                                                           BitmapDrawable rgbDrawable, BitmapDrawable alphaDrawable) {

        Bitmap rgbBitmap = rgbDrawable.getBitmap();
        Bitmap alphaBitmap = Bitmap.createScaledBitmap(alphaDrawable.getBitmap(), rgbBitmap.getWidth(), rgbBitmap.getHeight(),false);
        int width = rgbBitmap.getWidth();
        int height = rgbBitmap.getHeight();
        if (width != alphaBitmap.getWidth() || height != alphaBitmap.getHeight()) {
            throw new IllegalStateException("image size mismatch!");
        }
        Bitmap destBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        int[] pixels = new int[width];
        int[] alpha = new int[width];
        for (int y = 0; y < height; y++) {
            rgbBitmap.getPixels(pixels, 0, width, 0, y, width, 1);
            alphaBitmap.getPixels(alpha, 0, width, 0, y, width, 1);
            for (int x = 0; x < width; x++) {
                // Replace the alpha channel with the r value from the bitmap.
                pixels[x] = (alpha[x] & 0x00FFFFFF) == 0 ? pixels[x] : 0xFFFFFFFF;
            }
            destBitmap.setPixels(pixels, 0, width, 0, y, width, 1);
        }
        return new BitmapDrawable(resources, destBitmap);
    }
}
