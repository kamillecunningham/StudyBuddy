import java.util.*;
import java.io.*;
/**
 * This program is meant to help me study for my programming exam!
 * 
 * @author Kamille Cunningham
 * @since October 21, 2025
 */

public class StudyBuddy {
    /**
     * Main method runs StudyBuddy program by reading file's questions,
     * putting them into a new random order, and then quizzing the user.
     * User enters numbers to answer questions and when finsihed is told how many questions they answered
     * correctly on the first try,
     * 
     * File's questions must be formatted as such:
     * Prompt
     * Answer 
     * Answer
     * Answer
     * Answer
     * 
     * Correct answer denoted with a astristk at the start. 
     * Must have four answer choices following prompt
     */
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter the file name: ");
        String fileName = keyboard.nextLine().trim();

        List<Question> questions = new ArrayList<>();

        try (Scanner fileScanner = new Scanner(new File(fileName))) {
            while (fileScanner.hasNextLine()) {
                String prompt = fileScanner.nextLine().trim();
                // Skips if line is empty or the number heading
                if (prompt.isEmpty() || prompt.matches("\\d+"))
                    continue;

                List<String> options = new ArrayList<>();
                int correctIndex = -1;
                for (int i = 0; i < 4; i++) {
                    if (fileScanner.hasNextLine()) {
                        String option = fileScanner.nextLine().trim();
                        // Finds index of correct answer and removes denoting prefix
                        if (option.startsWith("*")) {
                            correctIndex = i;
                            option = option.substring(1).trim();
                        }
                        // Adds all four options
                        options.add(option);
                    }
                    // If question does not have four answers --> Error
                    else {
                        System.out.println("ERROR: Question doesnt have four options " + prompt);
                        return;
                    }
                }
                // No option had the denoting prefix
                if (correctIndex == -1) {
                    System.out.println("ERROR: No answer marked as correct with * " + prompt);
                    return;
                }

                questions.add(new Question(prompt, options, correctIndex));
            }
        }
        // If file does not exist:
        catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found");
            return;
        }
        if (questions.isEmpty()) {
            System.out.println("ERROR: File is empty");
            return;
        }

        // Shuffles questions into a random order
        Collections.shuffle(questions);
        int totalQuestions = questions.size();
        int questionsRemaining = totalQuestions;
        int questionsAnswered = 0; // Sets counter of question answered to 0
        int correctAnswers = 0; // Sets counter of correct answers to 0

        // Test the user!
        for (Question q : questions) {
            System.out.println("\nQuestions remaining: " + questionsRemaining + " (Or enter 0 to exit)");
            System.out.println(q.prompt);
            for (int i = 0; i < q.options.size(); i++) { // Fixed the loop condition
                System.out.println((i + 1) + ") " + q.options.get(i));
            }

            boolean answeredCorrectly = false;
            boolean firstAttempt = true; // Track if this is the first attempt
            while (!answeredCorrectly) {
                System.out.print("Correct answer:  ");
                String userAnswer = keyboard.nextLine().trim();
                if (userAnswer.isEmpty()) {
                    System.out
                            .println("Enter a number!");
                    continue;
                }
                // Exit program early if user enters "0"
                if (userAnswer.equals("0")) {
                    System.out
                            .println("Study session ended early. Score: " + correctAnswers + " / " + questionsAnswered);
                    return;
                }
                // String to integer
                int userIndex = Integer.parseInt(userAnswer) - 1;
                questionsAnswered++;
                if (userIndex == q.correctIndex) {
                    System.out.println("Correct!");
                    if (firstAttempt) {
                        correctAnswers++; // Increment only on first attempt
                    }
                    answeredCorrectly = true; // Break out of while loop
                } else {
                    System.out.println("Nope! Try again.");
                    firstAttempt = false; // Mark that the first attempt has passed
                }
            }
            questionsRemaining--;
        }
        // Tell user study session is over, and tell them how many they got correctly
        System.out.println("You are done! Your score was " + correctAnswers + " / " + questionsAnswered);
    }
}
/**
 * This class represents one multiple-choice question (prompt, options, index of correct option)
 * 
 * @author Kamille Cunningham
 * @since October 21, 2025
 */
class Question {
    String prompt;
    List<String> options;
    int correctIndex;
    /**
     * Question constructor 
     *
     * @param prompt prompt
     * @param options list of possible answers
     * @param correctIndex index of correct option
     */
    public Question(String prompt, List<String> options, int correctIndex) {
        this.prompt = prompt;
        this.options = options;
        this.correctIndex = correctIndex;
    }
}