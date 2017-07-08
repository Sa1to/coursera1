package week4;

public class Counter {
    private long c;
    private static Counter ourInstance = new Counter();

    public static Counter getInstance() {
        return ourInstance;
    }

    private Counter() {
        c = 0;
    }

    public long getC() {
        return c;
    }

    public void setC(long c) {
        this.c = c;
    }
}
