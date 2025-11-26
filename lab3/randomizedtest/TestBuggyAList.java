package randomizedtest;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import timingtest.AList;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        //creat
        AListNoResizing<Integer> list1 = new AListNoResizing<>();
        BuggyAList<Integer> list2 = new BuggyAList<>();
        //add
        list1.addLast(1);
        list1.addLast(2);
        list1.addLast(3);
        list2.addLast(1);
        list2.addLast(2);
        list2.addLast(3);
        //removal and test
        assertEquals(list1.size(), list2.size());

        assertEquals(list1.removeLast(), list2.removeLast());
        assertEquals(list1.removeLast(), list2.removeLast());
        assertEquals(list1.removeLast(), list2.removeLast());

    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        BuggyAList<Integer> broken = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            switch (operationNumber) {
                case 0:{
                    // addLast
                    int randVal = StdRandom.uniform(0, 100);
                    correct.addLast(randVal);
                    broken.addLast(randVal);
                    //System.out.println("addLast(" + randVal + ")");
                    break;}
                case 1: {
                    // size
                    int size1 = correct.size();
                    int size2 = broken.size();
                    //System.out.println("size1: " + size1);
                    //System.out.println("size2: " + size2);
                    break;}
                case 2:{
                    //getlast
                    if (correct.size() == 0 || broken.size() == 0) {break;}
                    correct.getLast();
                    broken.getLast();
                    //System.out.println("getLast(" + correct.getLast() + ")");
                    //System.out.println("getLast(" + broken.getLast() + ")");
                    break;
                }
                case 3: {
                    //removelast
                    if (correct.size() == 0 || broken.size() == 0) {break;}
                    correct.removeLast();
                    broken.removeLast();
                    //System.out.println("removeLast(" + last + ")");
                    break;
                }
                default: {
                    System.out.println("operation number: " + operationNumber);
                    break;
                }
            }
            assertEquals(broken.size(), correct.size());
            if(broken.size() == 0) {break;}
            assertEquals(broken.getLast(), correct.getLast());
        }
    }
}
