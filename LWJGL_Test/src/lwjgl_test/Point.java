package lwjgl_test;

public class Point extends Element{
    Vec cords;
    public Point(Vec vec){
        this.cords = vec;
    }
    public Point(float x, float y, float z){
        this.cords = new Vec(x, y, z);
    }
    public float getX(){
        return this.cords.getX();
    }
    public float getY(){
        return this.cords.getY();
    }
    public float getZ(){
        return this.cords.getZ();
    }
}