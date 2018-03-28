package seedu.address.model.util;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nric;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.subject.Subject;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Nric("S8743880X"),
                getTagSet("friends"), getSubjectSet("English A2"), new Remark("Class Rep")),
            new Person(new Name("Bernice Yu"), new Nric("S9927275Z"),
                getTagSet("colleagues", "friends"), getSubjectSet("Mathematics A1"), new Remark("Math Rep")),
            new Person(new Name("Charlotte Oliveiro"), new Nric("S9321028H"),
                getTagSet("neighbours"), getSubjectSet("Chemistry B3"), new Remark("Chemistry Rep")),
            new Person(new Name("David Li"), new Nric("S9103128J"),
                getTagSet("family"), getSubjectSet("Physics B3"), new Remark("Physics Rep")),
            new Person(new Name("Irfan Ibrahim"), new Nric("S9249202K"),
                getTagSet("classmates"), getSubjectSet("Humanities B4"), new Remark("Humanities Rep")),
            new Person(new Name("Roy Balakrishnan"), new Nric("S9262441U"),
                getTagSet("colleagues"), getSubjectSet("Chinese C5"), new Remark("Chinese Rep"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        try {
            AddressBook sampleAb = new AddressBook();
            for (Person samplePerson : getSamplePersons()) {
                sampleAb.addPerson(samplePerson);
            }
            return sampleAb;
        } catch (DuplicatePersonException e) {
            throw new AssertionError("sample data cannot contain duplicate persons", e);
        }
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        HashSet<Tag> tags = new HashSet<>();
        for (String s : strings) {
            tags.add(new Tag(s));
        }

        return tags;
    }

    /**
     * Returns a subject set containing the list of strings given.
     */
    public static Set<Subject> getSubjectSet(String... strings) {
        HashSet<Subject> subjects = new HashSet<>();
        for (String s : strings) {
            subjects.add(new Subject(s));
        }

        return subjects;
    }

}
