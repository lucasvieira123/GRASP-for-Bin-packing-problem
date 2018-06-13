package custom.utils;

public class Trace {
    private String traceString = "";
    private static Trace instance = null;

    private Trace() {

    }

    public static Trace getInstance() {
        if(instance==null){
            instance = new Trace();
        }
            return instance;

    }

    public void add(String s){
        traceString = traceString.concat(s);
    }

    public String getTraceString() {
        return traceString;
    }
}
