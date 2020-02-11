package lwjgl_test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LWJGL_Test {
    public static Model[] model;
    public void run() {
            init();
            //init model loading on first model
            model[0] = getModel(0, "model.apw", Model.MODELW);
            model[0].displayData(ModelW.DISP_APW);
            loop();
    }
    private void init() {
        model = new ModelW[1];
    }
    private void loop() {
        //loop code here
    }
    public static void main(String[] args) {
            new LWJGL_Test().run();
    }
    //use static modelType vars from Model class to generate certain types.
    private Model getModel(int mod, String fileName, int modelType) {
        if(modelType == Model.MODELW){
            System.out.println("Model "+mod+" initializing as ModelW object (wireframe)");
            try{
                //initialize matrices as null. Throws errors if file invalid
                Point[] point = null;
                Color[] color = null;
                Line[] line = null;
                //loading file into bufferedreader to read information.
                File modelTxt = new File("res/"+fileName);
                BufferedReader reader = new BufferedReader(new FileReader(modelTxt));
                System.out.println("reader init successful");
                //read points (start with check for point header)
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
                //read lines (start with check for line header)
                currLine = reader.readLine();
                if(currLine.substring(0, 1).equals("C")){
                    int cCt = Integer.parseInt(currLine.substring(2));
                    color = new Color[cCt];
                    for(int i = 0; i < color.length; i++){
                        currLine = reader.readLine();
                        String[] rgbTemp = currLine.split(" ", 0);
                        if(rgbTemp.length != 3){
                            System.out.println("Color "+i+" in model "+mod+" needs exactly 3 values, it has "+rgbTemp.length+". Please check model file.");
                            System.exit(0);
                        }
                        for(int a = 0; a < 3; a++){
                            color[i] = new Color(Float.parseFloat(rgbTemp[0]), Float.parseFloat(rgbTemp[1]), Float.parseFloat(rgbTemp[2]));
                        }
                    }
                }else{
                    System.out.println("Format bad, exiting program");
                    System.exit(0);
                }
                //read lines (start with check for line header)
                currLine = reader.readLine();
                if(currLine.substring(0, 1).equals("L")){
                    int lCt = Integer.parseInt(currLine.substring(2));
                    line = new Line[lCt];
                    for(int i = 0; i < line.length; i++){
                        currLine = reader.readLine();
                        String[] abcTemp = currLine.split(" ", 0);
                        if(abcTemp.length != 3){
                            System.out.println("Line "+i+" in model "+mod+" needs exactly 3 elements, it has "+abcTemp.length+". Please check model file.");
                            System.exit(0);
                        }
                        for(int a = 0; a < 3; a++){
                            line[i] = new Line(Integer.parseInt(abcTemp[0]), Integer.parseInt(abcTemp[1]), Integer.parseInt(abcTemp[2]));
                        }
                    }
                    System.out.println("Done loading model "+mod+" as ModelW (wireframe)");
                }else{
                    System.out.println("Format bad, exiting program");
                    System.exit(0);
                }
                return new ModelW(point, color, line);
            }catch(FileNotFoundException e){
                System.out.println("Error: file "+fileName+" not found in folder res");
                return(null);
            }catch(IOException e){
                System.out.println("Error: IOException thrown when attempting to read file.");
                return(null);
            }
        }else{
            return null;
        }
    }
}