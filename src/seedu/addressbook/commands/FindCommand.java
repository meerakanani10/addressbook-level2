package seedu.addressbook.commands;

import java.util.*;

import seedu.addressbook.data.person.ReadOnlyPerson;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final Set<String> keywords;

    public FindCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    /**
     * Returns a copy of keywords in this command.
     */
    public Set<String> getKeywords() {
        return new HashSet<>(keywords);
    }

    @Override
    public CommandResult execute() {
        final List<ReadOnlyPerson> personsFound = getPersonsWithNameContainingAnyKeyword(keywords);
        return new CommandResult(getMessageForPersonListShownSummary(personsFound), personsFound);
    }

    /**
     * Retrieves all persons in the address book whose names contain some of the specified keywords.
     *
     * @param keywords for searching
     * @return list of persons found
     */
    private List<ReadOnlyPerson> getPersonsWithNameContainingAnyKeyword(Set<String> keywords) {
        final List<ReadOnlyPerson> matchedPersons = new ArrayList<>();
        List<String> nameSplitByWhiteSpace = new ArrayList<>();
        ArrayList<String> lowerCaseNames = new ArrayList<>();
        Set<String> loweredKeywords = new HashSet<>();
        for (ReadOnlyPerson person : addressBook.getAllPersons()) {
            nameSplitByWhiteSpace = person.getName().getWordsInName();
            for (String split : nameSplitByWhiteSpace) {
                lowerCaseNames.add(makeLowerCase(split));
            }
            final Set<String> wordsInName = new HashSet<>(lowerCaseNames);
            for (String keyword : keywords) {
                loweredKeywords.add(makeLowerCase(keyword));
            }
            if (!Collections.disjoint(lowerCaseNames, loweredKeywords)) {
                matchedPersons.add(person);
                lowerCaseNames.clear();
            }
        }
        return matchedPersons;
    }

    private static ArrayList<String> splitByWhitespace(String toSplit) {
        return new ArrayList<>(Arrays.asList(toSplit.trim().split("\\s+")));
    }

    private static String makeLowerCase(String partOfName) {
        return partOfName.toLowerCase();
    }
}
