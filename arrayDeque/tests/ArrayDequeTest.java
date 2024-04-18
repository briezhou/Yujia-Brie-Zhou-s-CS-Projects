import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDequeTest {
    @Test
    public void addFirstAndAddLastTest1() {
        Deque<Integer> lld1 = new ArrayDeque<>();
        assertThat(lld1.toList()).containsExactly();
        lld1.addFirst(4);
        lld1.addFirst(3);
        lld1.addLast(5);
        lld1.addLast(6);
        lld1.addLast(7);
        lld1.addLast(0);
        lld1.addFirst(2);
        lld1.addFirst(1);
        lld1.addFirst(100);
    }
    @Test
    /** In this test, we have three different assert statements that verify that addFirst works correctly. */
    public void addFirstTestBasic() {
        Deque<String> lld1 = new ArrayDeque<>();
        assertThat(lld1.toList()).containsExactly();
        lld1.addFirst("back"); // after this call we expect: ["back"]
        assertThat(lld1.toList()).containsExactly("back").inOrder();

        lld1.addFirst("middle"); // after this call we expect: ["middle", "back"]
        assertThat(lld1.toList()).containsExactly("middle", "back").inOrder();

        lld1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
        assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();

         /* Note: The first two assertThat statements aren't really necessary. For example, it's hard
            to imagine a bug in your code that would lead to ["front"] and ["front", "middle"] failing,
            but not ["front", "middle", "back"].
          */
    }
    @Test
    /** In this test, we use only one assertThat statement. IMO this test is just as good as addFirstTestBasic.
     *  In other words, the tedious work of adding the extra assertThat statements isn't worth it. */
    public void addLastTestBasic() {
        Deque<String> lld1 = new ArrayDeque<>();

        lld1.addLast("front"); // after this call we expect: ["front"]
        lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
        lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
        assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();
    }
    @Test
    /** This test performs interspersed addFirst and addLast calls. */
    public void addFirstAndAddLastTest() {
        Deque<Integer> lld1 = new ArrayDeque<>();

         /* I've decided to add in comments the state after each call for the convenience of the
            person reading this test. Some programmers might consider this excessively verbose. */
        lld1.addLast(0);   // [0]
        lld1.addLast(1);   // [0, 1]
        lld1.addFirst(-1); // [-1, 0, 1]
        lld1.addLast(2);   // [-1, 0, 1, 2]
        lld1.addFirst(-2); // [-2, -1, 0, 1, 2]
        assertThat(lld1.toList()).containsExactly(-2, -1, 0, 1, 2).inOrder();
    }

    @Test
    public void isEmpty() {
        Deque<Integer> lld1 = new ArrayDeque<>();
        assertThat(lld1.isEmpty()).isTrue();
        lld1.addFirst(2);
        assertThat(lld1.isEmpty()).isFalse();
        lld1.addLast(3);
        assertThat(lld1.isEmpty()).isFalse();
    }

    @Test
    public void sizeTest() {
        Deque<Integer> lld1 = new ArrayDeque<>();
        assertThat(lld1.size()).isEqualTo(0);
        lld1.addFirst(11);
        lld1.addFirst(13);
        lld1.addLast(16);
        assertThat(lld1.size()).isEqualTo(3);
    }

    @Test
    public void getTest() {
        Deque<Integer> lld1 = new ArrayDeque<>();
        lld1.addFirst(11);
        lld1.addFirst(13);
        lld1.addLast(16);
        assertThat(lld1.get(0)).isEqualTo(13);
        assertThat(lld1.get(2)).isEqualTo(16);
        assertThat(lld1.get(10)).isEqualTo(null);
        assertThat(lld1.get(-10)).isEqualTo(null);
    }



    @Test
    public void removeFirstTest() {
        Deque<Integer> lld1 = new ArrayDeque<>();
        assertThat(lld1.removeFirst()).isEqualTo(null);
        lld1.addFirst(11);
        lld1.addFirst(13);
        lld1.addLast(16);
        assertThat(lld1.size()).isEqualTo(3);
        lld1.removeFirst();
        assertThat(lld1.size()).isEqualTo(2);
        assertThat(lld1.get(0)).isEqualTo(11);
        assertThat(lld1.get(1)).isEqualTo(16);
        lld1.removeFirst();
        lld1.removeFirst();
        assertThat(lld1.size()).isEqualTo(0);
        assertThat(lld1.removeFirst()).isEqualTo(null);
        lld1.addFirst(16);
    }

    @Test
    public void removeLastTest() {
        Deque<Integer> lld1 = new ArrayDeque<>();
        assertThat(lld1.size()).isEqualTo(0);
        assertThat(lld1.removeLast()).isEqualTo(null);
        lld1.addFirst(11);
        lld1.addFirst(13);
        lld1.addLast(16);
        assertThat(lld1.size()).isEqualTo(3);
        lld1.removeLast();
        assertThat(lld1.size()).isEqualTo(2);
        assertThat(lld1.get(0)).isEqualTo(13);
        assertThat(lld1.get(1)).isEqualTo(11);
        lld1.removeLast();
        lld1.removeLast();
        assertThat(lld1.size()).isEqualTo(0);
        assertThat(lld1.removeLast()).isEqualTo(null);
        lld1.addLast(16);
    }
    @Test
    public void remove_first_trigger_resize(){
        Deque<Integer> lld1 = new ArrayDeque<>();
        for (int i = 0; i < 20; i++) {
            lld1.addFirst(i);
        }
        for (int i = 0; i < 20; i++) {
            lld1.removeFirst();
        }
    }

    @Test
    public void remove_last_trigger_resize(){
        Deque<Integer> lld1 = new ArrayDeque<>();
        for (int i = 0; i < 30; i++) {
            lld1.addLast(i);
        }
        for (int i = 0; i < 30; i++) {
            lld1.removeLast();
        }
    }
}
