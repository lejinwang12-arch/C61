package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
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
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        int N = 1000;
        AList<Integer> Ns = new AList<>();
        while(N <= 128000){
            Ns.addLast(N);
            N *= 2;
        }

        AList<Double> times = timeAListHelp(Ns);
        AList<Integer> opCounts = new AList<>();

        // 填充操作计数
        for (int i = 0; i < Ns.size(); i++) {
            opCounts.addLast(Ns.get(i)); // 每个 N 的操作计数就是 N
        }
        printTimingTable(Ns, times, opCounts);

    }

    private static AList<Double> timeAListHelp(AList<Integer> Ns) {
        AList<Double> op = new AList<>();
        for (int i = 0; i < Ns.size(); i++) {
            AList<Integer> temp = new AList<>();
            int N = Ns.get(i);
            Stopwatch sw = new Stopwatch();
            for(int j = 0; j < N; j++){
                temp.addLast(j);
            }
            double timeInSeconds = sw.elapsedTime();
            op.addLast(timeInSeconds);
        }

        return op;
    }

}
