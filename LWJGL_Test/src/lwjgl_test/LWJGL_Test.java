package lwjgl_test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import lwjgl_test.Exception.WrongLengthException;
import lwjgl_test.Exception.WrongTypeException;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.glfwGetProcAddress;
import org.lwjgl.opengl.GL;

public class LWJGL_Test {
    double fieldOfView = 1.221730476;
    float pScalarW = 1;
    float pScalarH = 1;
    double time;
    boolean screenRunning = false;
    int windowWidth = 1575;
    int windowHeight = 675;
    long window;
    long monitor;
    public static Model[] model;
    public void run(){
        init();
        loop();
        glfwDestroyWindow(window);
        glfwTerminate();
    }
    private void cycle(){
        glfwPollEvents();
        time = glfwGetTime();
        model[0].translate(new Vec(0.01f, 0, 0));
    }
    private void render(){
        glClear(GL_COLOR_BUFFER_BIT);
        glBegin(GL_TRIANGLES);
        glColor3f(0f, 0.25f, 0.16f);
        glVertex3f(1f, 1f, 0f);
        glColor3f(0f, 0f, 0f);
        glVertex3f(-1, 1f, 0f);
        glVertex3f(1f, -1f, 0f);
        glVertex3f(-1, 1f, 0f);
        glVertex3f(1f, -1f, 0f);
        glColor3f(0.2f, 0f, 0.125f);
        glVertex3f(-1f, -1f, 0f);
        glEnd();
        glBegin(GL_LINES);
        for(Line l : model[0].line){
            Vec a = model[0].point[l.aPointer].cords;
            Vec b = model[0].point[l.bPointer].cords;
            Color c = model[0].color[l.cPointer];
            glColor3f(c.r, c.g, c.b);
            glVertex3f(-pScalarW*a.x/a.z, -pScalarH*a.y/a.z, 0);
            glVertex3f(-pScalarW*b.x/b.z, -pScalarH*b.y/b.z, 0);
        }
        glEnd();
        //keep at end of fxn!
        glfwSwapBuffers(window);
    }
    private void init() {
        windowInit();
        openglInit();
        model = new Model[1];
        //init model loading on first model
        model[0] = getModel(0, "pyramid.apw", Model.MODELW);
        model[0].translate(new Vec(-5, 0, -6));
        model[0].displayData(ModelW.DISP_APW);
    }
    private void openglInit(){
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glViewport(0, 0, windowWidth, windowHeight);
        time = glfwGetTime();
        glfwSwapInterval(1);
        pScalarW = (float)Math.atan(fieldOfView/2);
        pScalarH = 21*pScalarW/9;
    }
    private void windowInit(){
        //if (!glfwInit()){ line must be first!!!
        if(!glfwInit()){
            System.out.println("Error: GLFW Initialization has failed. Exiting with error.");
            System.exit(0);
        }else{
            System.out.println("GLFW init successful. Running version "+glfwGetVersionString());
        }
        
        monitor = glfwGetPrimaryMonitor();
        if(monitor == NULL){
            System.out.println("NO MONITOR FOUND");
        }
        int mWidth = (int)(glfwGetVideoMode(monitor).width()*0.82);
        int mHeight = (int)(glfwGetVideoMode(monitor).height()*0.82);
        if(21*mHeight/9 < mWidth){
            windowHeight = mHeight;
            windowWidth = mHeight*21/9;
        }else{
            windowWidth = mWidth;
            windowHeight = mWidth*9/21;
        }
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.out));
        glfwWindowHint(GLFW_DECORATED, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API);
        window = glfwCreateWindow(windowWidth, windowHeight, "Render test window", NULL, NULL);
        glfwSetWindowCloseCallback(window, (long window) -> {
            myWindowCallback();
        });
        glfwSetKeyCallback(window, (long window, int key, int scancode, int action, int mods)->{
            if(key == GLFW_KEY_ESCAPE && action == GLFW_PRESS){
                myWindowCallback();
            }
        });
        screenRunning = true;
    }
    private void loop() {
        while(screenRunning){
            cycle();
            render();
        }
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
    void recieveKey(long window, int key, int code, int action, int modifiers){
        if(key == GLFW_KEY_A && action == GLFW_RELEASE)
            glfwSetWindowShouldClose(window, true);
    }
    void myWindowCallback(){
            screenRunning = false;
            System.out.println("EXIT");
    }
}