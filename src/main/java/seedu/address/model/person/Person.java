package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.subject.Subject;
import seedu.address.model.subject.UniqueSubjectList;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    private final Name name;
    private final Nric nric;

    private final UniqueTagList tags;
    private final UniqueSubjectList subjects;
    private final Remark remark;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Nric nric, Set<Tag> tags, Set<Subject> subjects, Remark remark) {
        requireAllNonNull(name, nric, tags, subjects);
        this.name = name;
        this.nric = nric;
        // protect internal tags from changes in the arg list
        this.tags = new UniqueTagList(tags);
        this.subjects = new UniqueSubjectList(subjects);
        this.remark = remark;
    }

    public Name getName() {
        return name;
    }

    public Nric getNric() {
        return nric;
    }

    public Remark getRemark() {
        return remark;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags.toSet());
    }

    public Set <Subject> getSubjects() {
        return Collections.unmodifiableSet(subjects.toSet());
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return otherPerson.getName().equals(this.getName())
                && otherPerson.getNric().equals(this.getNric());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, nric, tags, subjects, remark);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Nric: ")
                .append(getNric())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        builder.append(" Subjects: ");
        getSubjects().forEach(builder::append);
        builder.append(" Remarks: ")
               .append(getRemark());
        return builder.toString();
    }

}
