package fi.tuni.space2048;

import android.widget.ImageView;

public class GameCell {

    private int value;
    private ImageView img;
    private ImageView animImg;

    public GameCell(int value, ImageView img, ImageView animImg) {
        this.img = img;
        this.animImg = animImg;
        setValue(value);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        if (value == 2) {
            img.setImageResource(R.drawable.cell_2);
        }
        else if (value == 4) {
            img.setImageResource(R.drawable.cell_4);
        }
        else if (value == 8) {
            img.setImageResource(R.drawable.cell_8);
        }
        else if (value == 16) {
            img.setImageResource(R.drawable.cell_16);
        }
        else if (value == 32) {
            img.setImageResource(R.drawable.cell_32);
        }
        else if (value == 64) {
            img.setImageResource(R.drawable.cell_64);
        }
        else if (value == 128) {
            img.setImageResource(R.drawable.cell_128);
        }
        else if (value == 256) {
            img.setImageResource(R.drawable.cell_256);
        }
        else if (value == 512) {
            img.setImageResource(R.drawable.cell_512);
        }
        else if (value == 1024) {
            img.setImageResource(R.drawable.cell_1024);
        }
        else if (value == 2048) {
            img.setImageResource(R.drawable.cell_2048);
        }
        else {
            img.setImageResource(R.drawable.cell_0);
        }
    }

    public ImageView getImg() {
        return img;
    }
    public void setImg(ImageView img) {
        this.img = img;
    }
    public ImageView getAnimImg() {
        return animImg;
    }
    public void setAnimImg(ImageView animImg) {
        this.animImg = animImg;
    }
}
