package lwjgl_test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import lwjgl_test.Exception.WrongLengthException;
import lwjgl_test.Exception.WrongTypeException;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.opengl.GL46.glBufferData;
import static org.lwjgl.glfw.GLFW.glfwGetProcAddress;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

public class LWJGL_Test {
    int frameCount;
    static int vao;
    static FloatBuffer vertexBuffer;
    int pid, vid, fid;
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
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        MemoryUtil.memFree(vertexBuffer);
        glDeleteProgram(pid);
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
        glEnableClientState(GL_VERTEX_ARRAY);
        glViewport(0, 0, windowWidth, windowHeight);
        glfwSwapInterval(1);
        glDisable(GL_CULL_FACE);
        pScalarW = 1/(float)Math.tan(fieldOfView/2);
        pScalarH = 21*pScalarW/9;
        System.out.println("Running openGL version "+glGetString(GL_VERSION));
        pid = glCreateProgram();
        if(pid == 0){
            System.out.println("SHADER. COULDN'T MAKE program");
            System.exit(0);
        }
        String vsCode = "";
        try {vsCode = new String(Files.readAllBytes(Paths.get("res/vertex.vs")));
        }catch(Exception ex){error(ex.getMessage());}
        System.out.println("");
        System.out.println("vsCode =");
        System.out.println(vsCode);
        vid = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vid, vsCode);
        glCompileShader(vid);
        if(glGetShaderi(vid, GL_COMPILE_STATUS) == GL_FALSE){
            System.out.println("vertex SHADER FAILURE");
            System.exit(0);
        }
        glAttachShader(pid, vid);
        String fsCode = "";
        try {fsCode = new String(Files.readAllBytes(Paths.get("res/fragment.fs")));
        }catch(Exception ex){error(ex.getMessage());}
        System.out.println("");
        System.out.println("fsCode =");
        System.out.println(fsCode);
        fid = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fid, fsCode);
        glCompileShader(fid);
        if(glGetShaderi(fid, GL_COMPILE_STATUS) == GL_FALSE){
            System.out.println("FRAGMENT SHADER FAILURE");
            System.exit(0);
        }
        glAttachShader(pid, fid);
        glLinkProgram(pid);
        if(glGetProgrami(pid, GL_LINK_STATUS) == GL_FALSE){
            error("failed to link program");
        }
        glValidateProgram(pid);
        if(glGetProgrami(pid, GL_VALIDATE_STATUS) == GL_FALSE){
            System.out.println("WARNING: INVALIDATED PGM");
        }
        /*int vboLength = 0;
        for(Model m: model)
            vboLength += m.point.length*3;
        vertexBuffer = MemoryUtil.memAllocFloat(vboLength);
        for(Model m: model){
            for(Point p: m.pointDisp){
                vertexBuffer.put(p.getX());
                vertexBuffer.put(p.getY());
                vertexBuffer.put(p.getZ());
            }
        }*/
        vertexBuffer = MemoryUtil.memAllocFloat(18);
        vertexBuffer.put(-0.5f);
        vertexBuffer.put(0.5f);
        vertexBuffer.put(0);
        
        vertexBuffer.put(-0.5f);
        vertexBuffer.put(-0.5f);
        vertexBuffer.put(0);
        
        vertexBuffer.put(0.5f);
        vertexBuffer.put(-0.5f);
        vertexBuffer.put(0);
        
        vertexBuffer.put(-0.5f);
        vertexBuffer.put(0.5f);
        vertexBuffer.put(0);
        
        vertexBuffer.put(0.5f);
        vertexBuffer.put(0.5f);
        vertexBuffer.put(0);
        
        vertexBuffer.put(0.5f);
        vertexBuffer.put(-0.5f);
        vertexBuffer.put(0);
        
        vertexBuffer.flip();
        while(vertexBuffer.hasRemaining()){
            System.out.println("    "+vertexBuffer.get());
        }
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_DYNAMIC_DRAW);
        glEnableVertexAttribArray(0);
        System.out.println("ERROR IN INIT = "+glGetError());
        memFree(vertexBuffer);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
    }
    private void error(String m){
        System.out.println(m);
        System.exit(0);
    }
    private void error(Exception e){
        System.out.println(e.getMessage());
        System.exit(0);
    }
    private void render(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glUseProgram(pid);
        glBindVertexArray(vao);
        glEnableVertexAttribArray(0);
        System.out.println("ERROR 0 = "+glGetError());
        
        /*
        glBegin(GL_TRIANGLES);
        glVertex3f(-1f, 1f, 0f);
        glVertex3f(-1f, -1f, 0f);
        glVertex3f(1f, -1f, 0f);
        glEnd();
        */
        
        glDrawArrays(GL_TRIANGLES, 0, 3);
        System.out.println("ERROR 1 = "+glGetError());
        
        //System.out.println(glGetError());
        
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
        
        
        glUseProgram(0);
        //keep at end of fxn!
        glfwSwapBuffers(window);
        frameCount++;
        System.out.println("frame "+frameCount);
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
        model = new Model[1];
        try {
            //init model loading on first model
            model[0] = readObjModelW(new File("res/teapot.obj"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LWJGL_Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        model[0].translate(new Vec(0, 0, -20));
        openglInit();
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