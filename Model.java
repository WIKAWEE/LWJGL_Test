package lwjgl_test;

public interface Model {
    //int statics for creation/initialization of models with different specific types
    public static final int MODELW = 0;   //wireframe model with points, colors, and lines
    public static final int MODELC = 1;   //model constructed of tris on points. tris have color but no texture
    public static final int MODELT = 2;   //textures applied to tris on points
    public void displayData(int DataType);
}