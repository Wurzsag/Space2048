package fi.tuni.space2048;

import android.widget.ImageView;

public class GameCell {

    private int value;
    private ImageView img;

    public GameCell(int value, ImageView img) {
        this.img = img;
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
        else {
            img.setImageResource(R.drawable.cell_empty);
        }
    }

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }
}