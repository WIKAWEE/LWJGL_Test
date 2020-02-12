package lwjgl_test.Exception;

public class WrongTypeException extends Exception{
    public WrongTypeException(String shouldBe, String is){
        super("Trying to load element with header "+shouldBe+" and encountered header "+is);
    }
}
