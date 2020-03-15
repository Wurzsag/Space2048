package fi.tuni.space2048;

import android.widget.ImageView;

public class GameCell {

    private int value;
    private ImageView img;

    public GameCell(int value, ImageView img) {
        this.value = value;
        this.img = img;
        initializeImageView();
    }

    public void initializeImageView() {
        img.setImageResource(R.drawable.cell_empty);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        if (value == 2) {
            img.setImageResource(R.drawable.cell_2);
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
