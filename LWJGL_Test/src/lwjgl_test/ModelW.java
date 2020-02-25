package lwjgl_test;

public class ModelW extends Model{
    public static final int DISP_APW = 0;
    public static final int DISP_EASYREAD = 1;
    int pointCount;
    int colorCount;
    int lineCount;
    public ModelW(Point[] p, Color[] c, Line[] l){
        point = new Point[p.length];
        pointDisp = new Point[p.length];
        for(int i = 0; i < p.length; i++){
            point[i] = new Point(p[i].cords.x, p[i].cords.y, p[i].cords.z);
            pointDisp[i] = new Point(p[i].cords.x, p[i].cords.y, p[i].cords.z);
        }
        color = new Color[c.length];
        for(int i = 0; i < c.length; i++){
            color[i] = new Color(c[i].r, c[i].g, c[i].b);
        }
        line = new Line[l.length];
        for(int i = 0; i < l.length; i++){
            line[i] = new Line(l[i].aPointer, l[i].bPointer, l[i].cPointer);
        }
        pointCount = point.length;
        colorCount = color.length;
        lineCount = line.length;
    }
    
    @Override
    public void displayData(int dataType){
        if(dataType == DISP_APW){
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
        }
        if(dataType == DISP_EASYREAD){
            
        }
    }
}