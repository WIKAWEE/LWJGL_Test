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
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.glfw.GLFW.glfwGetProcAddress;
import org.lwjgl.opengl.GL;

public class LWJGL_Test {
    int programId, vertexShaderId, fragmentShaderId;
    //70degrees init
    double fieldOfView = 1.221730476;
    //translate away from cam for preview objs
    float distance = 0;
    
    float pScalarW = 1;
    float pScalarH = 1;
    double frameStart;
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
        for(Model m : model)
            m.updateCoords();
    }
    private void openglInit(){
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glViewport(0, 0, windowWidth, windowHeight);
        glfwSwapInterval(1);
        pScalarW = 1/(float)Math.tan(fieldOfView/2);
        pScalarH = 21*pScalarW/9;
        System.out.println("Running openGL version "+glGetString(GL_VERSION));
        programId = glCreateProgram();
        if(programId == 0){
            System.out.println("SHADER. COULDN'T MAKE SHADER.");
            System.exit(0);
        }
        vertexShaderId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderId);
        glCompileShader(vertexShaderId);
        if(glGetShaderi(vertexShaderId, GL_COMPILE_STATUS) == 0){
            System.out.println("VERTEX SHADER FAILURE");
            System.exit(0);
        }
        fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderId, Utils.loadResource("/vertex.vs"));
        glCompileShader(fragmentShaderId);
        if(glGetShaderi(fragmentShaderId, GL_COMPILE_STATUS) == 0){
            System.out.println("FRAGMENT SHADER FAILURE");
            System.exit(0);
        }
                
    }
    private void render(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        
        
        
        //keep at end of fxn!
        glfwSwapBuffers(window);
    }
    private void renderOld(){
        glClear(GL_COLOR_BUFFER_BIT);
        glBegin(GL_LINES);
        for(int i = 0; i < model.length; i++){
            for(Line l : model[i].line){
                Vec a = model[i].pointDisp[l.aPointer].cords;
                Vec b = model[i].pointDisp[l.bPointer].cords;
                Color c = model[i].color[l.cPointer];
                glColor3f(c.r, c.g, c.b);
                glVertex3f(-pScalarW*a.x/a.z, -pScalarH*a.y/a.z, 0);
                glVertex3f(-pScalarW*b.x/b.z, -pScalarH*b.y/b.z, 0);
            }
        }
        glEnd();
        //keep at end of fxn!
        glfwSwapBuffers(window);
    }
    private void init() {
        fieldOfView = 0.6;
        windowInit();
        openglInit();
        model = new Model[1];
        //init model loading on first model
        model[0] = getModel(0, "car.apw", Model.MODELW);
        model[0].translate(new Vec(0, -2, -15));
        model[0].displayData(ModelW.DISP_APW);
    }
    public ModelW readObjModelW(File objFile) throws FileNotFoundException{
        Point[] p = new Point[0];
        Color[] c = {new Color(0, 1, 0)};
        Line[] l = new Line[0];
        String currLine = null;
        BufferedReader reader = new BufferedReader(new FileReader(objFile));
        System.out.println("reader init successful");
        boolean going = true;
        while(going){
            try{
                currLine = reader.readLine();
                if(currLine != null){
                    String[] temp = currLine.split(" ", 0);
                    if("v".equals(temp[0])){
                        Point[] pTemp = new Point[p.length+1];
                        for(int i = 0; i < p.length; i++)
                            pTemp[i] = p[i];
                        pTemp[p.length] = new Point(Float.parseFloat(temp[1]), Float.parseFloat(temp[2]), Float.parseFloat(temp[3]));
                        p = pTemp;
                    }
                    if("f".equals(temp[0])){
                        l = addLineIfNoDuplicate(l, Integer.parseInt(temp[1])-1, Integer.parseInt(temp[2])-1);
                        l = addLineIfNoDuplicate(l, Integer.parseInt(temp[2])-1, Integer.parseInt(temp[3])-1);
                        l = addLineIfNoDuplicate(l, Integer.parseInt(temp[3])-1, Integer.parseInt(temp[1])-1);
                    }
                }else{
                    going = false;
                    
                }
            }catch(IOException e){
                System.out.println("IOEXCEPTION");
                going = false;
            }
        }
        return new ModelW(p, c, l);
    }
    private Line[] addLineIfNoDuplicate(Line[] l, int aPoint, int bPoint){
        boolean doIt = true;
        for(Line ln : l){
            if(ln.aPointer == aPoint && ln.bPointer == bPoint)
                doIt = false;
            if(ln.bPointer == aPoint && ln.aPointer == bPoint)
                doIt = false;
        }
        if(doIt){
            Line[] lTemp = new Line[l.length+1];
            for(int i = 0; i < l.length; i++)
                lTemp[i] = l[i];
            lTemp[l.length] = new Line(aPoint, bPoint, 0);
            return lTemp;
        }
        else{
            return l;
        }
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
            frameStart = glfwGetTime();
            glfwPollEvents();
            cycle();
            render();
            while(glfwGetTime() < frameStart+0.01666666666667){}
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