package seedu.address.model.subject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class SubjectTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Subject(null));
    }

    @Test
    public void constructorWithTwoStringParameters_invalidSubjectName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        String validSubjectGrade = "A2";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Subject(invalidTagName, validSubjectGrade));
    }

    @Test
    public void constructorWithTwoStringParameters_invalidSubjectGrade_throwsIllegalArgumentException() {
        String validTagName = "English";
        String invalidSubjectGrade = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Subject(validTagName, invalidSubjectGrade));
    }

    @Test
    public void constructorWithOneStringPararmeter_validSubjectFormatEntered_success() {
        String validSubjectFormat = "English A1";
        Subject testing = new Subject(validSubjectFormat);
        assertEquals("English", testing.subjectName);
        assertEquals("A1", testing.subjectGrade);
    }

    @Test
    public void isValidSubjectName_invalidSubjectNameEntered_exceptionThrown() {
        // null subject name
        Assert.assertThrows(NullPointerException.class, () -> Subject.isValidSubjectName(null));
        // invalid subject name
        assertFalse(Subject.isValidSubjectName("")); // empty string
        assertFalse(Subject.isValidSubjectName(" ")); // spaces only
        assertFalse(Subject.isValidSubjectName("^")); // only non-alphanumeric characters
        assertFalse(Subject.isValidSubjectName("English*")); // contains non-alphanumeric characters
        assertFalse(Subject.isValidSubjectName("2djs22")); //contains alphanumeric characters

        // valid name
        assertTrue(Subject.isValidSubjectName("English")); // alphabets only
        assertTrue(Subject.isValidSubjectName("Mathematics")); // numbers only
    }

    @Test
    public void isValidSubjectGrade_invalidSubjectGradeEntered_exceptionThrown() {
        // null subject grade
        Assert.assertThrows(NullPointerException.class, () -> Subject.isValidSubjectGrade(null));
        // invalid name
        assertFalse(Subject.isValidSubjectGrade("")); // empty string
        assertFalse(Subject.isValidSubjectGrade(" ")); // spaces only
        assertFalse(Subject.isValidSubjectGrade("^")); // only non-alphanumeric characters
        assertFalse(Subject.isValidSubjectGrade("121*")); // contains non-alphanumeric characters

        // valid name
        assertTrue(Subject.isValidSubjectGrade("A1")); // alphabets only
        assertTrue(Subject.isValidSubjectGrade("B3")); // numbers only

    }

    @Test
    public void equals_compareTwoDifferentConstructors_success() {
        Subject subject1 = new Subject("English A1");
        Subject subject2 = new Subject("English", "A1");
        Subject subject3  = new Subject("Mathematics A2");

        //Test if the subject object created by two different constructors produces object with the same parameters
        assertEquals(subject1, subject2);
        //Compare two different subjects
        assertNotEquals(subject1, subject3);
    }

}
