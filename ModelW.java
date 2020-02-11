package lwjgl_test;

public class ModelW implements Model{
    public static final int DISP_APW = 0;
    public static final int DISP_EASYREAD = 1;
    int pointCount;
    int colorCount;
    int lineCount;
    Point[] point;
    Color[] color;
    Line[] line;
    public ModelW(Point[] p, Color[] c, Line[] l){
        this.point = p;
        this.color = c;
        this.line = l;
        this.pointCount = point.length;
        this.colorCount = color.length;
        this.lineCount = line.length;
    }
    
    @Override
    public void displayData(int displayType){
        if(displayType == DISP_APW){
            System.out.println();
            System.out.print("P ");
            System.out.println(this.pointCount);
            for(Point p: point){
                System.out.println(p.getX()+" "+p.getY()+" "+p.getZ());
            }
            System.out.print("C ");
            System.out.println(this.colorCount);
            for(Color c: color){
                System.out.println(c.r+" "+c.g+" "+c.b);
            }
            System.out.print("L ");
            System.out.println(this.lineCount);
            for(Line l: line){
                System.out.println(l.aPointer+" "+l.bPointer+" "+l.cPointer);
            }
        }else if(displayType == DISP_EASYREAD){
            
        }
    }
}