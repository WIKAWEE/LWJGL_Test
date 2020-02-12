package lwjgl_test.Exception;

public class WrongLengthException extends Exception{
    public WrongLengthException(int shouldBe, int is){
        super("Trying to load element with header "+shouldBe+" and encountered header "+is);
    }
}
