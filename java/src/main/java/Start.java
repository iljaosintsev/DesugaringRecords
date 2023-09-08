public class Start {

    public static void main(String[] args) {
        String arg;
        if (args.length > 0) {
            arg = args[0];
        } else {
            arg = "Start";
        }
        String str = "Hello" + ", " + arg;
        System.out.println(str);
    }
}