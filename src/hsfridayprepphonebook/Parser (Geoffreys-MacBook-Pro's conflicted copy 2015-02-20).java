package hsfridayprepphonebook;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Parser {
    
    private final static Map<String, ParserHelper> commandMapping = new HashMap<String, ParserHelper>();
    
    public static void main(String[] args) {
        
        
    }
    
    /**
     * lookup, change, remove, reverse-lookup, create 
     */


    @ParserFunctionTagger("lookup")
    public String lookup(String[] args){
        if (args.length > 2){
            return "Badly Formatted Lookup";
        }
        
        String name = args[0];
        String fileName = args[1];
        
        try {
            PhoneBook pb = PhoneBook.buildPhoneBookFromFile(fileName);
            Set<Listing> entries =  pb.lookup(name);            
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Corrupted File or file not found";
        } catch (BadlyFormattedNameException e){
            return "Badly formatted name";
        }
    }
    
    
}
