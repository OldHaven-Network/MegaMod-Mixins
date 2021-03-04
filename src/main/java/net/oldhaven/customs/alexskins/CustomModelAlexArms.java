package net.oldhaven.customs.alexskins;

public class CustomModelAlexArms {
    private final CustomModelRenderer alexArms;
    private final CustomModelRenderer steveArms;
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    public float rotateAngleX;
    public float rotateAngleY;
    public float rotateAngleZ;
    public boolean isAlex = false;
    public boolean mirror = false;
    private boolean outer;
    private final float f;

    public CustomModelAlexArms(int i, int i2, float scale, float f, boolean outer) {
        this.outer = outer;
        this.f = f;
        if(outer)
            scale = scale + 0.25F;
        this.alexArms = new CustomModelRenderer(i, i2);
        this.alexArms.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, scale, 64, 64);
        this.alexArms.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.alexArms.showModel = false;

        this.steveArms = new CustomModelRenderer(i, i2);
        this.steveArms.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, scale, 64, 64);
        this.steveArms.setRotationPoint(5.0F, 1.5F, 0.0F);
        this.steveArms.showModel = false;
    }

    public void setRotationPoint(float var1, float var2, float var3) {
        this.rotationPointX = var1;
        this.rotationPointY = var2;
        this.rotationPointZ = var3;
    }

    public void setIsAlex(boolean b) {
        this.isAlex = b;
    }

    public CustomModelRenderer getModel()  {
        return isAlex ? alexArms : steveArms;
    }

    public void render(float scale) {
        if(isAlex) {
            this.alexArms.showModel = true;
            if(!mirror) {
                this.rotationPointX -= 1F;
                if(this.outer)
                    this.rotationPointX += 1F;
            } else {
                if(this.outer)
                    this.rotationPointX -= 1F;
            }
            this.rotationPointY = 2.0F+f;
            doRotations(alexArms);
            alexArms.render(scale);
        } else {
            this.steveArms.showModel = true;
            if(mirror && !this.outer)
                this.rotationPointX += 2F;
            if(!mirror && this.outer)
                this.rotationPointX += 2F;
            //if(!mirror && this.outer)
                //this.rotationPointX += 0.5F;
            this.rotationPointY = 2.0F+f;
            doRotations(steveArms);
            steveArms.render(scale);
        }
    }

    public void postRender(float scale) {
        if (isAlex) {
            alexArms.postRender(scale);
        } else {
            steveArms.postRender(scale);
        }
    }

    private void doRotations(CustomModelRenderer arm) {
        arm.rotationPointX = this.rotationPointX;
        arm.rotationPointY = this.rotationPointY;
        arm.rotationPointZ = this.rotationPointZ;
        arm.rotateAngleX = this.rotateAngleX;
        arm.rotateAngleY = this.rotateAngleY;
        arm.rotateAngleZ = this.rotateAngleZ;
        arm.mirror = this.mirror;
    }
}
