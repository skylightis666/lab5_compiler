import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Recognizer {
    Map<Character, Map<Character, Integer>> lookupTable = Map.of(
            'T', Map.of(
                    'a', 1,
                    'b', 1,
                    'c', 1,
                    'd', 1,
                    '$', -1),
            'S', Map.of(
                    'a', 4,
                    'b', 3,
                    'c', 2,
                    'd', 4,
                    '$', -1),
            'X', Map.of(
                    'a', 5,
                    'b', -1,
                    'c', -1,
                    'd', 6,
                    '$', 6),
            'Y', Map.of(
                    'a', 7,
                    'b', -1,
                    'c', -1,
                    'd', 8,
                    '$', 8));
    Map<Integer, String> productions = Map.of(
            1, "S$",
            2, "cS",
            3, "bX",
            4, "Xd",
            5, "aY",
            6, "",
            7, "a",
            8, "");

    Map<Integer, String> semantics = Map.of(
            1, "North ",
            2, "North-East ",
            3, "East ",
            4, "South-East ",
            5, "South ",
            6, "South-West ",
            7, "West ",
            8, "North-West ");

    Set<Character> terminals = Set.of('a', 'b', 'c', 'd', '$');
    Set<Character> notTerminals = Set.of('T', 'S', 'X', 'Y');

    int nextState(String symbolsSequence) {
        int answer = 1;
        StringBuilder productionSequence = new StringBuilder();
        Character[] characters = new Character[symbolsSequence.length()];
        for (int i = 0; i < symbolsSequence.length(); i++) {
            characters[i] = symbolsSequence.charAt(i);
        }
        Stack<Character> stack = new Stack<>();
        stack.push('T');
        int currentInTextNumber = 0;
        while (currentInTextNumber < characters.length) {
            Character currentInText = characters[currentInTextNumber];
            if (!terminals.contains(currentInText)) {
                return -1;
            }
            if (stack.isEmpty()) {
                answer = 0;
                break;
            }
            Character currentInStack = stack.pop();
            if (terminals.contains(currentInStack)) {
                if (currentInStack == currentInText) {
                    currentInTextNumber++;
                    continue;
                } else {
                    answer = 0;
                    break;
                }
            }
            if (notTerminals.contains(currentInStack)) {
                Integer production = lookupTable.get(currentInStack).get(currentInText);
                if (production == -1) {
                    answer = 0;
                    break;
                }
                productionSequence.append(semantics.get(production));
                String line = productions.get(production);
                for (int i = line.length() - 1; i >= 0; i--) {
                    stack.push(line.charAt(i));
                }
            }
        }
        if (answer == 0) {
            for (int i = 0; i < symbolsSequence.length(); i++) {
                if (!terminals.contains(symbolsSequence.charAt(i))) {
                    return -1;
                }
            }
            return 0;
        }
        if (stack.isEmpty()) {
            System.out.println("Semantic was: " + productionSequence);
            return 1;
        } else {
            return 0;
        }
    }
}