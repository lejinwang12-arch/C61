package timingtest;
import antlr.collections.impl.LList;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        //创建Ns
        AList<Integer> Ns = new AList<>();
        int N = 1000;
        while(N <= 128000){
            Ns.addLast(N);
            N *= 2;
        }

        //创建ops
        AList<Integer> opCounts = new AList<>();
        for(int i = 0; i < Ns.size(); i += 1){
            opCounts.addLast(10000);
        }


        //创建times
        AList<Double> times = timeAddLastHelp(Ns, opCounts);

        //打印
        printTimingTable(Ns, times, opCounts);

    }

    private static AList<Double> timeAddLastHelp(AList<Integer> Ns, AList<Integer> opCounts) {
        AList<Double> times = new AList<>();
        for (int i = 0; i < Ns.size(); i += 1) {
            //创建SLList
            int N = Ns.get(i);
            SLList<Integer> temp = new SLList<>();
            for (int j = 0; j < N; j += 1) {
                temp.addLast(j);
            }
            //计时
            int opCount = opCounts.get(i);
            Stopwatch sw = new Stopwatch();
            for (int j = 0; j < opCount; j += 1) {
                temp.getLast();
            }
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
        }
        return times;
    }

}
