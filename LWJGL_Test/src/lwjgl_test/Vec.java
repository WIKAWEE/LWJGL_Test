package lwjgl_test;

public class Vec {
    float x;
    float y;
    float z;
    public Vec(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public float getZ(){
        return z;
    }
    public static Vec add(Vec v1, Vec v2){
        return new Vec(v1.x+v2.x, v1.y+v2.y, v1.z+v2.z);
    }
    public static Vec subtract(Vec v1, Vec v2){
        return new Vec(v1.x-v2.x, v1.y-v2.y, v1.z-v2.z);
    }
    public static Vec scale(Vec v, float power){
        return new Vec(v.x*power, v.y*power, v.z*power);
    }
    public static Vec scale(Vec v, Vec o, float power){
        return new Vec((v.x-o.x)*power+o.x, (v.y-o.y)*power+o.y, (v.z-o.z)*power+o.z);
    }
    public static Vec scale(Vec v, Point o, float power){
        return new Vec((v.x-o.getX())*power+o.getX(), (v.y-o.getY())*power+o.getY(), (v.z-o.getZ())*power+o.getZ());
    }
    public static Vec scale(Vec v, float powerX, float powerY, float powerZ){
        return new Vec(v.x*powerX, v.y*powerY, v.z*powerZ);
    }
    public static Vec scale(Vec v, Vec o, float powerX, float powerY, float powerZ){
        return new Vec((v.x-o.x)*powerX+o.x, (v.y-o.y)*powerY+o.y, (v.z-o.z)*powerZ+o.z);
    }
    public static Vec scale(Vec v, Point o, float powerX, float powerY, float powerZ){
        return new Vec((v.x-o.getX())*powerX+o.getX(), (v.y-o.getY())*powerY+o.getY(), (v.z-o.getZ())*powerZ+o.getZ());
    }
}