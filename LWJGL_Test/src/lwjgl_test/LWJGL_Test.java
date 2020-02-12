package lwjgl_test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lwjgl_test.Exception.WrongLengthException;
import lwjgl_test.Exception.WrongTypeException;

public class LWJGL_Test {
    public static Model[] model;
    public void run(){
        init();
        //init model loading on first model
        model[0] = getModel(0, "pyramid.apw", Model.MODELW);
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
    private Model getModel(int mod, String fileName, int modelType){
        if(modelType == Model.MODELW){
            System.out.println("Model "+mod+" initializing as ModelW object (wireframe)");
            try{
                //loading file into bufferedreader to read information.
                File modelTxt = new File("res/"+fileName);
                BufferedReader reader = new BufferedReader(new FileReader(modelTxt));
                System.out.println("reader init successful");
                return new ModelW(Element.readPoint(reader), Element.readColor(reader), Element.readLine(reader));
            }catch(WrongTypeException e){
                System.out.println("Wrong type exception!");
                return null;
            }catch (WrongLengthException e){
                System.out.println("Wrong length exception!");
                return null;
            }catch(FileNotFoundException e){
                System.out.println("Error: file "+fileName+" not found in folder res");
                return(null);
            }catch(IOException e){
                System.out.println("Error: IOException thrown when attempting to read file.");
                return(null);
            }
        }
        return null;
    }
}