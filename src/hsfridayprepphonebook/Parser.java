package hsfridayprepphonebook;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Parser {

    private final static Map<String, ParserHelper> commandMapping = new HashMap<String, ParserHelper>() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        {
            // TODO: WTF, If I name this function "remove", there is a syntax
            // error...
            put("remove", x -> removeWTF(x));
            put("create", x -> create(x));
            put("lookup", x -> lookup(x));
            put("reverse-lookup", x -> reverseLookup(x));
            put("change", x -> change(x));
            put("add", x -> add(x));

        }
    };

    public static void main(String[] args) {
        Parser p = new Parser();
        if ((args.length > 0) && commandMapping.containsKey(args[0])) {
            System.out.println(commandMapping.get(args[0]).run(
                    Arrays.copyOfRange(args, 1, args.length)));
        } else {
            System.out.println("Invalid Command.");
        }

    }

    public Parser() {

    }

    /**
     * lookup, change, remove, reverse-lookup, create
     */

    private static Long stripNumber(String numToStrip)
            throws NumberFormatException {
        String strippedNum = numToStrip.replace("'", "").replace("\"", "");
        strippedNum = strippedNum.replace(" ", "").replace("-", "");
        Long l = Long.parseLong(strippedNum, 10);
        return l;
    }

    @ParserFunctionTagger("lookup")
    public static String lookup(String[] args) {
        if (!(args.length == 2)) {
            return "Badly Formatted Lookup";
        }

        String name = args[0];
        String fileName = args[1];

        try {
            PhoneBook pb = PhoneBook.buildPhoneBookFromFile(fileName);
            Set<Listing> entries = pb.lookup(name);
            StringBuilder sb = new StringBuilder();
            for (Listing listing : entries) {
                sb.append(listing.toString() + "\n");

            }
            return sb.toString();
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Corrupted File or file not found";
        } catch (BadlyFormattedNameException e) {
            return "Badly formatted name";
        }
    }

    @ParserFunctionTagger("remove")
    public static String removeWTF(String[] args) {
        if (!(args.length == 2)) {
            return "Badly Formatted remove command";
        }

        String name = args[0];

        String fileName = args[1];

        try {
            PhoneBook pb = PhoneBook.buildPhoneBookFromFile(fileName);
            boolean successfull = pb.remove(name);

            if (successfull) {
                pb.saveToFile(fileName);
                return "OK";
            }
            return "name not in phonebook";
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Corrupted File or file not found";
        }

    }

    @ParserFunctionTagger("reverse-lookup")
    public static String reverseLookup(String[] args) {
        if (!(args.length == 2)) {
            return "Badly Formatted reverse-lookup";
        }

        String number = args[0];

        String fileName = args[1];

        try {
            PhoneBook pb = PhoneBook.buildPhoneBookFromFile(fileName);
            Set<Listing> names = pb.reverseLookup(stripNumber(number));
            StringBuilder sb = new StringBuilder();
            for (Listing name : names) {
                sb.append(name + "\n");
            }
            return sb.toString();
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Corrupted File or file not found";
        } catch (NumberFormatException e) {
            return "Badly formatted number";
        }
    }

    @ParserFunctionTagger("change")
    public static String change(String[] args) {
        if (!(args.length == 3)) {
            return "Badly formatted change command";
        }

        String name = args[0];
        String number = args[1];
        String fileName = args[2];

        try {
            PhoneBook pb = PhoneBook.buildPhoneBookFromFile(fileName);

            boolean successfull = pb.change(name, Long.parseLong(number, 10));
            if (successfull) {
                pb.saveToFile(fileName);
                return "OK";
            }
            return "Name badly formatted or not found";
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Corrupted File or file not found";
        }

    }

    @ParserFunctionTagger("create")
    public static String create(String[] args) {
        if (args.length != 1) {
            return "Bad create command format";
        }

        try {
            PhoneBook.createFile(args[0]);
            return "OK";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "some issue happend";
        }

    }

    @ParserFunctionTagger("add")
    public static String add(String[] args) {
        if (args.length != 3) {
            return "Badly Formatted Add Command";
        }
        String name = args[0];
        String number = args[1];
        String fileName = args[2];

        try {
            PhoneBook pb = PhoneBook.buildPhoneBookFromFile(fileName);
            Long numberToAdd = stripNumber(number);
            boolean succesfull = pb.addName(name, numberToAdd);
            if (succesfull) {
                pb.saveToFile(fileName);
                return "OK";
            }
            return "already present";
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "File badly formatted or not found";
        } catch (NumberFormatException e) {
            return "Number badly formatted";
        }

    }

}
