package fanta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class TaskListTest {

    @Test
    void markAndUnmark_toggleStatus() throws Exception {
        TaskList list = new TaskList();
        list.add(new Todo("read book"));

        Task marked = list.mark(0);
        assertEquals("[T][X] read book", marked.toString());

        Task unmarked = list.unmark(0);
        assertEquals("[T][ ] read book", unmarked.toString());
    }

    @Test
    void remove_invalidIndex_throwsFantaException() {
        TaskList list = new TaskList();
        list.add(new Todo("one"));
        assertThrows(FantaException.class, () -> list.remove(5));
    }
}
