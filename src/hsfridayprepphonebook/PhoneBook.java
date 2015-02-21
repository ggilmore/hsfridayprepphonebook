package hsfridayprepphonebook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class PhoneBook implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7960224380621023914L;

    private final BiMap<Name, PhoneNumber> fullPhoneBook;

    private final Map<String, Set<String>> firstToLastGroup;

    private final Map<String, Set<String>> lastToFirstGroup;

    private static final transient String currentWorkingPath = PhoneBook.class
            .getProtectionDomain().getCodeSource().getLocation().getPath();

    public PhoneBook() {

        this.fullPhoneBook = HashBiMap.create();
        this.firstToLastGroup = new HashMap<String, Set<String>>();
        this.lastToFirstGroup = new HashMap<String, Set<String>>();
    }

    private PhoneBook(BiMap<Name, PhoneNumber> fullPhoneBook,
            Map<String, Set<String>> firstToLastGroup,
            Map<String, Set<String>> lastToFirstGroup) {
        this.fullPhoneBook = fullPhoneBook;
        this.firstToLastGroup = firstToLastGroup;
        this.lastToFirstGroup = lastToFirstGroup;
    }

    public boolean remove(String nameToRemove) {
        String[] splitName = this.splitName(nameToRemove);

        if (splitName.length == 2) {
            Name name = new Name(splitName[0], splitName[1]);
            

            if (this.fullPhoneBook.containsKey(name)) {
                this.fullPhoneBook.remove(name);
                return true;
            }
            return false;
        }
        return false;
    }

    public static void createFile(String fileName) throws IOException {
        PhoneBook pb = new PhoneBook();
        // File file = new File(PhoneBook.currentWorkingPath + fileName);
        // file.mkdir();
        // file.createNewFile();
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
                fileName));
        oos.writeObject(new PhoneBook());
        oos.flush();
        oos.close();
    }

    public void saveToFile(String fileName) throws FileNotFoundException,
            IOException {
        File file = new File(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
                file));
        oos.writeObject(this);
        oos.flush();
        oos.close();
    }

    public static PhoneBook buildPhoneBookFromFile(String fileName)
            throws FileNotFoundException, IOException, ClassNotFoundException {

        File file = new File(fileName);
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        PhoneBook pb = (PhoneBook) ois.readObject();
        ois.close();
        return pb;
    }

    public boolean change(String nameToChange, Long newNumber) {
        String[] splitName = this.splitName(nameToChange);

        if (splitName.length == 2) {
            Name name = new Name(splitName[0], splitName[1]);

            if (this.fullPhoneBook.containsKey(name)) {
                PhoneNumber number = new PhoneNumber(newNumber);
                this.fullPhoneBook.put(name, number);
                return true;

            }
            return false;
        }
        return false;
    }

    private String[] splitName(String nameToSplit) {
        String trimName = nameToSplit.trim();
        String[] firstLast = trimName.split(" ");
        for (int i = 0; i < firstLast.length; i++) {
            firstLast[i] = firstLast[i].trim();
        }

        return firstLast;
    }

    public Set<Listing> lookup(String nameToLookUp)
            throws BadlyFormattedNameException {
        Map<Listing, Integer> entries = new HashMap<Listing, Integer>();
        String[] splitName = this.splitName(nameToLookUp);
        System.out.println(this.fullPhoneBook);
        // full name
        if (splitName.length == 2) {
            Name name = new Name(splitName[0], splitName[1]);
            if (fullPhoneBook.containsKey(name)) {
                entries.put(new Listing(name, fullPhoneBook.get(name)), 1);
            }
        }

        // partial name, don't know if first or last
        else if (splitName.length == 1) {
            String partialName = splitName[0];
            if (firstToLastGroup.containsKey(partialName)) {
                for (String lastName : firstToLastGroup.get(partialName)) {
                    Name name = new Name(partialName, lastName);
                    PhoneNumber number = fullPhoneBook.get(name);
                    entries.put(new Listing(name, number), 1);
                }
            }

            if (lastToFirstGroup.containsKey(partialName)) {
                for (String firstName : lastToFirstGroup.get(partialName)) {
                    Name name = new Name(firstName, partialName);
                    PhoneNumber number = fullPhoneBook.get(name);
                    entries.put(new Listing(name, number), 1);
                }
            }

        }

        else {
            throw new BadlyFormattedNameException();
        }

        return entries.keySet();
    }

    public boolean addName(String nameToAdd, long numberToadd) {
        String[] splitName = this.splitName(nameToAdd);

        if (splitName.length == 2) {
            // full name
            Name name = new Name(splitName[0], splitName[1]);
            if (fullPhoneBook.containsKey(name)) {
                return false;
            }

            else {
                fullPhoneBook.put(name, new PhoneNumber(numberToadd));
                if (firstToLastGroup.containsKey(name.getFirstName())) {
                    firstToLastGroup.get(name.getFirstName()).add(
                            name.getLastName());
                } else {
                    Set<String> lastNameSet = new HashSet<String>();
                    lastNameSet.add(name.getLastName());
                    firstToLastGroup.put(name.getFirstName(), lastNameSet);
                }

                if (lastToFirstGroup.containsKey(name.getLastName())) {
                    lastToFirstGroup.get(name.getLastName()).add(
                            name.getFirstName());
                } else {
                    Set<String> firstNameSet = new HashSet<String>();
                    firstNameSet.add(name.getFirstName());
                    lastToFirstGroup.put(name.getLastName(), firstNameSet);
                }
                return true;

            }

        } else {
            return false;
        }
    }

    public Set<Listing> reverseLookup(Long numberToLookUp) {
        Set<Listing> entries = new HashSet<Listing>();
        try {
            
            PhoneNumber number = new PhoneNumber(numberToLookUp);
            if (this.fullPhoneBook.inverse().containsKey(number)) {
                Name name = this.fullPhoneBook.inverse().get(number);
                entries.add(new Listing(name, number));
            }
            return entries;

        } catch (NumberFormatException e) {
            // TODO: handle exception
            return entries;

        }
    }
}
