package seedu.address.model.subject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class UniqueSubjectListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueSubjectList uniqueSubjectList = new UniqueSubjectList();
        thrown.expect(UnsupportedOperationException.class);
        uniqueSubjectList.asObservableList().remove(0);
    }

}
