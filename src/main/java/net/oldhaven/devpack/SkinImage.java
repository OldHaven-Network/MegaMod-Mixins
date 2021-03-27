package net.oldhaven.devpack;

import java.awt.image.BufferedImage;

public class SkinImage {
    public int skinType = 0;
    public BufferedImage image = null;
    public String imageUrl = null;
    public Boolean failed = null;

    public void setAll(int skinType, boolean failed, Object img) {
        this.skinType = skinType;
        this.failed = failed;
        if(img.getClass().isInstance(BufferedImage.class))
            this.image = (BufferedImage) img;
        else if(img.getClass().isInstance(String.class))
            this.imageUrl = (String) img;
    }

    public void setSkinType(int skinType) {
        this.skinType = skinType;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setFailed(Boolean failed) {
        this.failed = failed;
    }

    public Boolean hasFailed() {
        return failed != null && failed;
    }
}
