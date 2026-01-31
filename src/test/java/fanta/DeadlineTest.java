package fanta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DeadlineTest {

    @Test
    void toString_withDateTime_formatsNicely() throws Exception {
        Deadline d = new Deadline("return book", "2019-12-02 1800");
        assertEquals("[D][ ] return book (by: Dec 02 2019 1800)", d.toString());
        assertEquals("2019-12-02T18:00:00", d.getByStorageString());
    }

    @Test
    void constructor_invalidDate_throwsFantaException() {
        assertThrows(FantaException.class, () -> new Deadline("bad date", "yesterday"));
    }
}
