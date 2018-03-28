package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.subject.Subject;
import seedu.address.model.subject.UniqueSubjectList;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.tag.exceptions.TagNotFoundException;


/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueTagList tags;
    private final UniqueSubjectList subjects;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        tags = new UniqueTagList();
        subjects = new UniqueSubjectList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons and Tags in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    public void setPersons(List<Person> persons) throws DuplicatePersonException {
        this.persons.setPersons(persons);
    }

    public void setTags(Set<Tag> tags) {
        this.tags.setTags(tags);
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects.setSubjects(subjects);
    }
    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setTags(new HashSet<>(newData.getTagList()));
        List<Person> syncedPersonList = newData.getPersonList().stream()
                .map(this::syncWithMasterTagSubjectList)
                .collect(Collectors.toList());

        try {
            setPersons(syncedPersonList);
        } catch (DuplicatePersonException e) {
            throw new AssertionError("AddressBooks should not have duplicate persons");
        }
    }

    //// person-level operations

    /**
     * Adds a person to the address book.
     * Also checks the new person's tags and subjects and updates {@link #tags #subjects} with any new
     * tags or subjects found, and updates the Tag objects and Subject objects in the person
     * to point to those in {@link #tags #subjects}.
     *
     * @throws DuplicatePersonException if an equivalent person already exists.
     */
    public void addPerson(Person p) throws DuplicatePersonException {
        Person person = syncWithMasterTagSubjectList(p);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the person list.
        persons.add(person);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code AddressBook}'s tag and subject list will be updated with the tags and subjects of {@code editedPerson}.
     *
     * @throws DuplicatePersonException if updating the person's details causes the person to be equivalent to
     *      another existing person in the list.
     * @throws PersonNotFoundException if {@code target} could not be found in the list.
     *
     * @see #syncWithMasterTagSubjectList(Person)
     */
    public void updatePerson(Person target, Person editedPerson)
            throws DuplicatePersonException, PersonNotFoundException {
        requireNonNull(editedPerson);

        Person syncedEditedPerson = syncWithMasterTagSubjectList(editedPerson);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the person list.
        persons.setPerson(target, syncedEditedPerson);
        removeUnusedTags();
    }

    /**
     * Removes all {@code Tag}s that are not used by any {@code Person} in this {@code AddressBook}.
     */
    private void removeUnusedTags() {
        Set<Tag> tagsInPersons = persons.asObservableList().stream()
                .map(Person::getTags)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        tags.setTags(tagsInPersons);
    }
    /**
     *  Updates the master tag list to include tags and subjects in {@code person} that are not in the list.
     *  @return a copy of this {@code person} such that every tag in this person points to a Tag object
     *  and a Subject Object in the master list.
     */
    private Person syncWithMasterTagSubjectList(Person person) {
        final UniqueTagList personTags = new UniqueTagList(person.getTags());
        tags.mergeFrom(personTags);

        final UniqueSubjectList personSubjects = new UniqueSubjectList((person.getSubjects()));
        subjects.mergeFrom(personSubjects);

        // Create map with values = tag object references in the master list
        // used for checking person tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Create map with values = subject object references in the master list
        // used for checking person subject references
        final Map<Subject, Subject> masterSubjectObjects = new HashMap<>();
        subjects.forEach(subject -> masterSubjectObjects.put(subject, subject));

        // Rebuild the list of person tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        personTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        // Rebuild the list of person subjects to point to the relevant subjects in the master subject list.
        final Set<Subject> correctSubjectReferences = new HashSet<>();
        personSubjects.forEach(subject -> correctSubjectReferences.add(masterSubjectObjects.get(subject)));
        return new Person(
                person.getName(), person.getNric(), correctTagReferences, correctSubjectReferences, person.getRemark());
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * @throws PersonNotFoundException if the {@code key} is not in this {@code AddressBook}.
     */
    public boolean removePerson(Person key) throws PersonNotFoundException {
        if (persons.remove(key)) {
            return true;
        } else {
            throw new PersonNotFoundException();
        }
    }

    /**
     * Chooses the sorting method based on the parameter given
     */
    public void sort(String parameter) {
        if (("name").equals(parameter)) {
            persons.sortNames();
        }
        if (("tag").equals(parameter)) {
            persons.sortTags();
        }
    }

    //// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

    public void addSubject(Subject s) throws UniqueSubjectList.DuplicateSubjectException {
        subjects.add(s);
    }

    /**
     * Calls removeTagFromPerson method when tag is found in tags.
     * @param t
     * @throws TagNotFoundException
     */
    public void removeTag(Tag t) throws TagNotFoundException {
        if (tags.contains(t)) {
            for (Person person : persons) {
                removeTagFromPerson(t, person);
            }
        } else {
            throw new TagNotFoundException("Specific tag is not used in the address book.");
        }
    }

    /**
     * Calls replaceTagForPerson method when tag is found in tags.
     * @param tagSet
     * @throws TagNotFoundException
     */
    public void replaceTag(List<Tag> tagSet) throws TagNotFoundException {
        Tag[] tagArray = new Tag[2];
        tagSet.toArray(tagArray);
        Tag tagToBeReplaced = tagArray[0];
        Tag tagToBePlaced = tagArray[1];
        if (tags.contains(tagToBeReplaced)) {
            for (Person person : persons) {
                replaceTagForPerson(tagToBeReplaced, tagToBePlaced, person);
            }
        } else {
            throw new TagNotFoundException("Specific tag is not used in the address book.");
        }
    }


    /**
     * Removes a specific tag from an individual person and updates the person's information.
     * Person needs to have the specific tag in his/her tag list.
     * @param tag
     * @param person
     */
    public void removeTagFromPerson(Tag tag, Person person) {
        Set<Tag> tagList = new HashSet<>(person.getTags());
        if (tagList.remove(tag)) {
            Person newPerson = new Person(person.getName(), person.getNric(), tagList, person.getSubjects(), person.getRemark());
            try {
                updatePerson(person, newPerson);
            } catch (DuplicatePersonException error1) {
                throw new AssertionError("Updating person after removing tag should not have duplicate persons.");
            } catch (PersonNotFoundException error2) {
                throw new AssertionError("Person should exist in the address book.");
            }
        }

    }

    /**
     * Removes a specific tag from an individual person and updates the person's information.
     * @param tagToBeReplaced
     * @param tagToBePlaced
     * @param person
     */

    public void replaceTagForPerson(Tag tagToBeReplaced, Tag tagToBePlaced, Person person) {
        Set<Tag> tagList = new HashSet<>(person.getTags());
        if (tagList.remove(tagToBeReplaced)) {
            tagList.add(tagToBePlaced);
            Person newPerson = new Person(person.getName(), person.getNric(),
                    tagList, person.getSubjects());

            try {
                updatePerson(person, newPerson);
            } catch (DuplicatePersonException error1) {
                throw new AssertionError("Updating person after removing tag should not have duplicate persons.");
            } catch (PersonNotFoundException error2) {
                throw new AssertionError("Person should exist in the address book.");
            }
        }
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asObservableList().size() + " persons, " + tags.asObservableList().size() +  " tags, "
                + subjects.asObservableList().size() + " subjects";
        // TODO: refine later
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asObservableList();
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return tags.asObservableList();
    }

    @Override
    public ObservableList<Subject> getSubjectList() {
        return subjects.asObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && this.persons.equals(((AddressBook) other).persons)
                && this.tags.equalsOrderInsensitive(((AddressBook) other).tags)
                && this.subjects.equalsOrderInsensitive(((AddressBook) other).subjects));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(persons, tags, subjects);
    }
}
