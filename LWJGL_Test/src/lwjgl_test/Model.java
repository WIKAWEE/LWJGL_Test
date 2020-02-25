package lwjgl_test;

import static lwjgl_test.Vec.add;
import static lwjgl_test.Vec.subtract;

public abstract class Model {
    //effects applied in this order on each frame.
    //apply effects normally, but every rand.nextInt(60, 180) seconds it will reset to mathematically close one.
    public Vec scale = new Vec(1, 1, 1);
    public Vec rotationCenter = new Vec(0, 0, 0);
    public Vec rotation = new Vec(0, 0, 0);
    public Vec position = new Vec(0, 0, 0f);
    public Color[] color;
    public Line[] line;
    public Point[] point;
    public Point[] pointDisp;
    //int statics for creation/initialization of models with different specific types
    public static final int MODELW = 0;   //wireframe model with points, colors, and lines
    public static final int MODELC = 1;   //tris on points. tris have color but no texture
    public static final int MODELT = 2;   //textures applied to tris on points
    public abstract void displayData(int DataType);
    //these next three rotate about the object origin.
    public void rotateX(double angle){
        rotation.x += angle;
    }
    public void rotateY(double angle){
        rotation.y += angle;
    }
    public void rotateZ(double angle){
        rotation.z += angle;
    }
    public void translate(Vec v){
        position = add(v, position);
    }
    public void rotate(Vec v){
        rotation = add(v, rotation);
    }
    public void scale(Vec v){
        scale = add(v, scale);
    }
    public void translate(float x, float y, float z){
        position = new Vec(position.x+x, position.y+y, position.z+z);
    }
    public void rotate(float x, float y, float z){
        rotation = new Vec(rotation.x+x, rotation.y+y, rotation.z+z);
    }
    public void rotate(float x, float y, float z, Vec center){
        rotation = new Vec(rotation.x+x, rotation.y+y, rotation.z+z);
        rotationCenter = new Vec(center.x, center.y, center.z);
    }
    public void setRotCenter(float x, float y, float z){
        rotationCenter = new Vec(x, y, z);
    }
    public void moveRotCenter(float x, float y, float z){
        rotationCenter = new Vec(rotationCenter.x+x, rotationCenter.y+y, rotationCenter.z+z);
    }
    void updateCoords() {
        System.out.println("scale "+scale.x+" "+scale.y+" "+scale.z);
        System.out.println("rotation "+rotation.x+" "+rotation.y+" "+rotation.z);
        System.out.println("position "+position.x+" "+position.y+" "+position.z);
        for(int i = 0; i < point.length; i++){
            Vec c = new Vec(point[i].cords.x, point[i].cords.y, point[i].cords.z);
            Vec pTemp = new Vec(point[i].cords.x, point[i].cords.y, point[i].cords.z);
            Vec rotC = new Vec(rotationCenter.x, rotationCenter.y, rotationCenter.z);
            System.out.println("point "+i+" "+c.x+" "+c.y+" "+c.z);
            //scale
            pTemp.x = scale.x*c.x;
            pTemp.y = scale.y*c.y;
            pTemp.z = scale.z*c.z;
            //rotation pre-move
            pTemp = subtract(pTemp, rotationCenter);
            //x rot
            //pTemp.y = c.y*(float)Math.cos(rotation.x) - c.z*(float)Math.sin(rotation.x);
            //pTemp.z = c.y*(float)Math.sin(rotation.x) + c.z*(float)Math.cos(rotation.x);
            c.y = pTemp.y;
            c.z = pTemp.z;
            //y rot
            pTemp.z = c.z*(float)Math.cos(rotation.y) - c.x*(float)Math.sin(rotation.y);
            pTemp.x = c.z*(float)Math.sin(rotation.y) + c.x*(float)Math.cos(rotation.y);
            c.x = pTemp.x;
            c.z = pTemp.z;
            //z rot
            //pTemp.x = c.x*(float)Math.cos(rotation.z) - c.y*(float)Math.sin(rotation.z);
            //pTemp.y = c.x*(float)Math.sin(rotation.z) + c.y*(float)Math.cos(rotation.z);
            //re-center after rot
            pTemp = add(pTemp, rotationCenter);
            //translate
            pTemp = add(pTemp, position);
            this.pointDisp[i].cords = pTemp;
        }
        System.out.println();
    }
    /*old-timey translation methods. Bad.
    public void scale(float power){
        for(int i = 0; i < point.length; i++)
            point[i] = new Point(Vec.scale(point[i].cords, power));
    }
    public void scale(Vec o, float power){
        for(int i = 0; i < point.length; i++)
            point[i] = new Point(Vec.scale(point[i].cords, o, power));
    }
    public void scale(Point o, float power){
        for(int i = 0; i < point.length; i++)
            point[i] = new Point(Vec.scale(point[i].cords, o, power));
    }
    public void scale(Vec o, float powerX, float powerY, float powerZ){
        for(int i = 0; i < point.length; i++)
            point[i] = new Point(Vec.scale(point[i].cords, o, powerX, powerY, powerZ));
    }
    public void scale(float powerX, float powerY, float powerZ){
        for(int i = 0; i < point.length; i++)
            point[i] = new Point(Vec.scale(point[i].cords, powerX, powerY, powerZ));
    }
    public void scale(Point o, float powerX, float powerY, float powerZ){
        for(int i = 0; i < point.length; i++)
            point[i] = new Point(Vec.scale(point[i].cords, o, powerX, powerY, powerZ));
    }
    public void translate(Vec v){
        for(int i = 0; i < point.length; i++)
            point[i] = new Point(Vec.add(point[i].cords, v));
    }
    public void translate(Point v){
        for(int i = 0; i < point.length; i++)
            point[i] = new Point(Vec.add(point[i].cords, v.cords));
    }
    */
}