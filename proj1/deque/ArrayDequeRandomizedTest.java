package deque;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Random;

public class ArrayDequeRandomizedTest {

    @Test
    public void randomizedTest() {
        java.util.ArrayDeque<Integer> reference = new java.util.ArrayDeque<>();
        ArrayDeque<Integer> student = new ArrayDeque<>();
        LinkedListDeque<Integer> studentLL = new LinkedListDeque<>();

        Random rand = new Random();
        StringBuilder ops = new StringBuilder();

        int N = 100000;

        for (int i = 0; i < N; i++) {
            int operation = rand.nextInt(6);

            switch (operation) {
                case 0: // addFirst
                    int valAF = rand.nextInt(1000);
                    reference.addFirst(valAF);
                    student.addFirst(valAF);
                    studentLL.addFirst(valAF);
                    ops.append("addFirst(").append(valAF).append(")\n");
                    break;

                case 1: // addLast
                    int valAL = rand.nextInt(1000);
                    reference.addLast(valAL);
                    student.addLast(valAL);
                    studentLL.addLast(valAL);
                    ops.append("addLast(").append(valAL).append(")\n");
                    break;

                case 2: // removeFirst
                    Integer r1 = reference.pollFirst();
                    Integer s1 = student.removeFirst();
                    Integer l1 = studentLL.removeFirst();
                    ops.append("removeFirst()\n");
                    assertEquals(ops.toString(), r1, s1);
                    assertEquals(ops.toString(), r1, l1);
                    break;

                case 3: // removeLast
                    Integer r2 = reference.pollLast();
                    Integer s2 = student.removeLast();
                    Integer l2 = studentLL.removeLast();
                    ops.append("removeLast()\n");
                    assertEquals(ops.toString(), r2, s2);
                    assertEquals(ops.toString(), r2, l2);
                    break;

                case 4: // size
                    ops.append("size()\n");
                    assertEquals(ops.toString(), reference.size(), student.size());
                    assertEquals(ops.toString(), reference.size(), studentLL.size());
                    break;

                case 5: // get
                    if (reference.size() > 0) {
                        int index = rand.nextInt(reference.size());
                        ops.append("get(").append(index).append(")\n");
                        Integer refVal = reference.toArray(new Integer[0])[index];
                        assertEquals(ops.toString(), refVal, student.get(index));
                        assertEquals(ops.toString(), refVal, studentLL.get(index));
                    }
                    break;

                default:
                    break;
            }

            // equals check every round
            assertTrue(ops.toString(), student.equals(studentLL));
            assertTrue(ops.toString(), studentLL.equals(student));
        }
    }
}

