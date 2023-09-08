import org.junit.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.StringConcatFactory;

public class StringConcatFactoryTest {

    @Test
    public void makeConcat() throws Throwable {
        String arg1 = "Hello";
        char arg2 = ' ';
        String arg3 = "StringConcatFactory";

        MethodHandle mh = StringConcatFactory.makeConcat(
                MethodHandles.lookup(), // normally provided by the JVM
                "foobar",  // normally provided by javac, but meaningless here
                // method type is normally provided by the JVM and matches the invocation
                MethodType.methodType(String.class, String.class, char.class, String.class)
                // signature of custom concat method, arguments and return type
                )
                .getTarget();

        String result = (String) mh.invokeExact(arg1, arg2, arg3);
        System.out.println(result);
    }

    @Test
    public void makeConcatWithConstants() throws Throwable {
        MethodHandle mh = StringConcatFactory.makeConcatWithConstants(
                MethodHandles.lookup(), // normally provided by the JVM
                "foobar", // normally provided by javac, but meaningless here
                // method type is normally provided by the JVM and matches the invocation
                MethodType.methodType(String.class, String.class),
                "Hello \2, good \1!", // recipe, \1 binds a parameter, \2 a constant
                System.getProperty("user.name") // the first constant to bind
        ).getTarget();

        String result = (String)mh.invokeExact("night");
        System.out.println(result);
    }
}
