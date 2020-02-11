package lwjgl_test;

import java.io.BufferedReader;
import java.io.IOException;

public abstract class Element {
    public static final int ELEMENT_POINT = 0;
    public static final int ELEMENT_LINE = 1;
    public static final int ELEMENT_TRI = 2;
    public static final int ELEMENT_TRI_TEXTURED = 3;
    public static final int ELEMENT_COLOR = 4;
    public static final int ELEMENT_TEXTURE = 5;
    public static final int ELEMENT_VU = 6;
    
    
    public static Element[] readElement(BufferedReader reader, int type, int mod) throws IOException{
        if(type == ELEMENT_POINT){
            Point[] point = null;
            String currLine = reader.readLine();
                if(currLine.substring(0, 1).equals("P")){
                    int pCt = Integer.parseInt(currLine.substring(2));
                    point = new Point[pCt];
                    for(int i = 0; i < point.length; i++){
                        currLine = reader.readLine();
                        String[] cordTemp = currLine.split(" ", 0);
                        if(cordTemp.length != 3){
                            System.out.println("Point "+i+" in model "+mod+" needs exactly 3 co-ords, it has "+cordTemp.length+". Please check model file.");
                            System.exit(0);
                        }
                        for(int a = 0; a < 3; a++){
                            point[i] = new Point(Float.parseFloat(cordTemp[0]), Float.parseFloat(cordTemp[1]), Float.parseFloat(cordTemp[2]));
                        }
                    }
                }
            return point;
        }else if(type == ELEMENT_LINE){
            
        }
        if(type == ELEMENT_LINE){
            
        }
        if(type == ELEMENT_LINE){
            
        }
        if(type == ELEMENT_LINE){
            
        }
        if(type == ELEMENT_LINE){
            
        }
        if(type == ELEMENT_LINE){
            
        }
        return null;
    }
}