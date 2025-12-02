package deque;

import edu.princeton.cs.algs4.Stopwatch;

import java.lang.reflect.Array;

public class TimeArrayDeque {
    private static void printTimingTable(ArrayDeque<Integer> Ns, ArrayDeque<Double> times) {
        System.out.printf("%12s %12s %12s\n", "N", "time (s)", "microsecond/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            double timePerOp = time / (N/4*3-1) * 1e7;
            System.out.printf("%12d %12.2f %12.2f\n", N, time, timePerOp);
        }
    }


    public static void timeAListConstruction() {
        int N = 128;
        ArrayDeque<Integer> Ns = new ArrayDeque<>();//[128, 512, 2048, 8192, 32768, 131072]
        while(N <= 131072){
            Ns.addLast(N);
            N *= 4;
        }

        ArrayDeque<Double> times = timeAListHelp(Ns);
        //打印表格
        printTimingTable(Ns, times);

    }

    private static ArrayDeque<Double> timeAListHelp(ArrayDeque<Integer> Ns) {
        ArrayDeque<Double> times = new ArrayDeque<>();
        ArrayDeque<Integer> temp = new ArrayDeque<>();
        //初始化一个items.length == 32
        for (int i = 0; i < Ns.get(0)/4; i += 1) {
            temp.addFirst(i);
        }
        //
        for (int i = 0; i < Ns.size(); i += 1) {
            temp.addFirst(1);
            int N = Ns.get(i);
            int startIndex = temp.size()-1;
            //开始计时
            Stopwatch sw = new Stopwatch();
            for (int j = startIndex; j < N; j += 1) {
                temp.addFirst(1);
            }
            double timeInSeconds = sw.elapsedTime();
            //计时结束
            times.addLast(timeInSeconds);
        }
        return times;
    }
}
