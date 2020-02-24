package lwjgl_test;

public abstract class Model {
    //int statics for creation/initialization of models with different specific types
    public Color[] color;
    public Line[] line;
    public Point[] point;
    public static final int MODELW = 0;   //wireframe model with points, colors, and lines
    public static final int MODELC = 1;   //tris on points. tris have color but no texture
    public static final int MODELT = 2;   //textures applied to tris on points
    public abstract void displayData(int DataType);
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
    public void rotateY(double angle){
        for(int i = 0; i < point.length; i++){
            float x = point[i].cords.x;
            float y = point[i].cords.y;
            float z = point[i].cords.z;
            point[i] = new Point(z*(float)Math.sin(angle) + x*(float)Math.cos(angle), y, z*(float)Math.cos(angle) - x*(float)Math.sin(angle));
        }
    }
    public void rotateY(Vec o, double angle){
        for(int i = 0; i < point.length; i++){
            float x = point[i].cords.x-o.x;
            float y = point[i].cords.y;
            float z = point[i].cords.z-o.z;
            point[i] = new Point(z*(float)Math.sin(angle) + x*(float)Math.cos(angle) + o.x, y, z*(float)Math.cos(angle) - x*(float)Math.sin(angle) + o.z);
        }
    }
}