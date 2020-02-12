package lwjgl_test;

import java.io.BufferedReader;
import java.io.IOException;
import lwjgl_test.Exception.WrongLengthException;
import lwjgl_test.Exception.WrongTypeException;

public abstract class Element {
    public static final int ELEMENT_POINT = 0;
    public static final int ELEMENT_LINE = 1;
    public static final int ELEMENT_TRI = 2;
    public static final int ELEMENT_TRI_TEXTURED = 3;
    public static final int ELEMENT_COLOR = 4;
    public static final int ELEMENT_TEXTURE = 5;
    public static final int ELEMENT_VU = 6;
    
    public static Point[] readPoint(BufferedReader reader) throws IOException, WrongTypeException, WrongLengthException{
        String currLine = reader.readLine();
        if(currLine.substring(0, 1).equals("P")){
            int c = Integer.parseInt(currLine.substring(2));
            Point[] point = new Point[c];
            for(int i = 0; i < point.length; i++){
                currLine = reader.readLine();
                String[] temp = currLine.split(" ", 0);
                if(temp.length != 3){
                    throw new WrongLengthException(3, temp.length);
                }
                point[i] = new Point(Float.parseFloat(temp[0]), Float.parseFloat(temp[1]), Float.parseFloat(temp[2]));
            }
            return point;
        }else{
            throw new WrongTypeException("P", currLine.substring(0, 1));
        }
    }
    public static Color[] readColor(BufferedReader reader) throws IOException, WrongTypeException, WrongLengthException{
        String currLine = reader.readLine();
        if(currLine.substring(0, 1).equals("C")){
            int c = Integer.parseInt(currLine.substring(2));
            Color[] color = new Color[c];
            for(int i = 0; i < color.length; i++){
                currLine = reader.readLine();
                String[] temp = currLine.split(" ", 0);
                if(temp.length != 3){
                    throw new WrongLengthException(3, temp.length);
                }
                color[i] = new Color(Float.parseFloat(temp[0]), Float.parseFloat(temp[1]), Float.parseFloat(temp[2]));
            }
            return color;
        }else{
            throw new WrongTypeException("P", currLine.substring(0, 1));
        }
    }
    public static Line[] readLine(BufferedReader reader) throws IOException, WrongTypeException, WrongLengthException{
        String currLine = reader.readLine();
        if(currLine.substring(0, 1).equals("L")){
            int c = Integer.parseInt(currLine.substring(2));
            Line[] line = new Line[c];
            for(int i = 0; i < line.length; i++){
                currLine = reader.readLine();
                String[] temp = currLine.split(" ", 0);
                if(temp.length != 3){
                    throw new WrongLengthException(3, temp.length);
                }
                line[i] = new Line(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
            }
            return line;
        }else{
            throw new WrongTypeException("P", currLine.substring(0, 1));
        }
    }
}