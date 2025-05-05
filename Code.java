package com.mycompany.brainfizzlergame2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BrainFizzlerGame2 {
    private int score = 0;
    private int timeLimit = 15;
    private int currentQuestionIndex = 0;
    private boolean lifelineUsed = false;

    private JFrame frame;
    private JLabel questionLabel, timerLabel, scoreLabel;
    private JButton[] optionButtons = new JButton[4];
    private JButton hintButton, lifelineButton, skipButton, themeButton;
    private Timer gameTimer;

    private List<Integer> questionOrder;
    private String difficultyLevel = "Easy"; // Default difficulty level

    private String[] questions = {
        "Who painted the Mona Lisa?", "Who discovered gravity?",
        "Which of the following is the correct way to instantiate a class in Java?",
        "What is the core principle of OOP?", "What does UI stand for?",
        "What is 2+2?", "Who wrote Hamlet?", "What is the capital of France?",
        "What does CPU stand for?", "Which planet is known as the Red Planet?",
        "What is the time complexity of binary search?", "Who developed C language?",
        "Which gas do plants absorb?", "What is the square root of 64?",
        "What is the output of 2^3 in C?",
        "What is the largest mammal?", "What is the chemical symbol for gold?",
        "Who invented the telephone?", "What is the largest ocean?",
        "What is the smallest prime number?", "What is the boiling point of water?",
        "What is the speed of light?", "What is the main ingredient in guacamole?",
        "What is the currency of Japan?", "What is the largest continent?",
        "What is the process of converting sugar into energy called?",
        "What is the fear of spiders called?", "What is the longest river?",
        "What is the hardest natural substance?", "What is the SI unit of force?"
    };

    private String[][] options = {
        {"Van Gogh", "Picasso", "Michelangelo", "Da Vinci"},
        {"Einstein", "Galileo", "Newton", "Tesla"},
        {"ClassName obj = new ClassName();", "new ClassName() = obj;", "ClassName = new obj();", "ClassName obj;"},
        {"Modularity", "Encapsulation", "Reusability", "None"},
        {"User Interface", "Unique Interface", "Utility Igern", "None"},
        {"3", "4", "5", "6"}, {"Shakespeare", "Newton", "Einstein", "Hawking"},
        {"Berlin", "Madrid", "Rome", "Paris"},
        {"Central Processing Unit", "Computer Power Unit", "Core Processing Unit", "None"},
        {"Mars", "Venus", "Saturn", "Jupiter"},
        {"O(n)", "O(log n)", "O(n^2)", "O(1)"},
        {"Dennis Ritchie", "James Gosling", "Linus Torvalds", "Bjarne Stroustrup"},
        {"Oxygen", "Carbon Dioxide", "Nitrogen", "Hydrogen"},
        {"6", "8", "10", "12"}, {"6", "8", "10", "16"},
        {"Elephant", "Blue Whale", "Giraffe", "Lion"}, {"Fe", "Au", "Ag", "Cu"},
        {"Alexander Graham Bell", "Thomas Edison", "Nikola Tesla", "Albert Einstein"},
        {"Atlantic Ocean", "Indian Ocean", "Pacific Ocean", "Arctic Ocean"},
        {"0", "1", "2", "3"}, {"90°C", "100°C", "110°C", "120°C"},
        {"300,000 km/s", "200,000 km/s", "150,000 km/s", "250,000 km/s"},
        {"Tomato", "Avocado", "Lime", "Onion"},
        {"Yen", "Dollar", "Euro", "Pound"}, {"Asia", "Africa", "Europe", "Australia"},
        {"Photosynthesis", "Respiration", "Fermentation", "Digestion"},
        {"Arachnophobia", "Claustrophobia", "Acrophobia", "Agoraphobia"},
        {"Amazon River", "Yangtze River", "Mississippi River", "Nile River"},
        {"Gold", "Graphite", "Quartz", "Diamond"}, {"Newton", "Joule", "Watt", "Pascal"}
    };

    private String[] correctAnswers = {
        "Da Vinci", "Newton", "ClassName obj = new ClassName();", "Encapsulation", "User Interface",
        "4", "Shakespeare", "Paris", "Central Processing Unit", "Mars",
        "O(log n)", "Dennis Ritchie", "Carbon Dioxide", "8", "8",
        "Blue Whale", "Au", "Alexander Graham Bell", "Pacific Ocean",
        "2", "100°C", "300,000 km/s", "Avocado",
        "Yen", "Asia", "Photosynthesis", "Arachnophobia",
        "Nile River", "Diamond", "Newton"
    };

    public void startGame() {
        frame = new JFrame("Brain Fizzler 2.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920 , 1040);
        frame.getContentPane().setBackground(new Color(230, 30, 110));
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(250, 150, 50));
        timerLabel = new JLabel("Time Left: " + timeLimit);
        timerLabel.setForeground(Color.WHITE);
        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setForeground(Color.WHITE);
        topPanel.add(timerLabel);
        topPanel.add(scoreLabel);
        frame.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(5, 1));
        centerPanel.setBackground(new Color(0, 130, 30));

        questionLabel = new JLabel("Question appears here", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 25));
        questionLabel.setForeground(Color.YELLOW);
        centerPanel.add(questionLabel);

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JButton();
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 20));
            optionButtons[i].setBackground(new Color(170, 70, 90));
            optionButtons[i].setForeground(Color.WHITE);
            optionButtons[i].addActionListener(new AnswerButtonListener());
            addHoverEffectLightTheme(optionButtons[i]);
            centerPanel.add(optionButtons[i]);
        }
        frame.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        hintButton = new JButton("Hint");
        lifelineButton = new JButton("50-50 Lifeline");
        skipButton = new JButton("Skip Question");
        themeButton = new JButton("Toggle Theme");

        hintButton.addActionListener(e -> useHint());
        lifelineButton.addActionListener(e -> useLifeline());
        skipButton.addActionListener(e -> skipQuestion());
        themeButton.addActionListener(e -> toggleTheme());

        bottomPanel.add(hintButton);
        bottomPanel.add(lifelineButton);
        bottomPanel.add(skipButton);
        bottomPanel.add(themeButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        selectDifficulty();
        generateRandomQuestions();
        loadNextQuestion();
        startTimer();

        frame.setVisible(true);
    }

    private void selectDifficulty() {
        Object[] options = {"Easy", "Medium", "Hard"};
        String selected = (String) JOptionPane.showInputDialog(
            frame,
            "Select Difficulty Level:",
            "Difficulty",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            "Easy"
        );
        if (selected != null) {
            difficultyLevel = selected;
            switch (difficultyLevel) {
                case "Easy":
                    timeLimit = 15;
                    break;
                case "Medium":
                    timeLimit = 10;
                    break;
                case "Hard":
                    timeLimit = 5;
                    break;
            }
        } else {
            difficultyLevel = "Easy";
            timeLimit = 15;
        }
    }

    private void generateRandomQuestions() {
        questionOrder = new ArrayList<>();
        List<Integer> allQuestions = new ArrayList<>();
        for (int i = 0; i < questions.length; i++) {
            allQuestions.add(i);
        }
        Collections.shuffle(allQuestions);
        for (int i = 0; i < 10; i++) { // Select only 10 random questions
            questionOrder.add(allQuestions.get(i));
        }
    }

    private void loadNextQuestion() {
        if (currentQuestionIndex >= questionOrder.size()) {
            endGame();
            return;
        }

        // Reset the timer based on the difficulty level
        timeLimit = (difficultyLevel.equals("Easy")) ? 15 : (difficultyLevel.equals("Medium") ? 10 : 5);
        timerLabel.setText("Time Left: " + timeLimit);

        // Stop the current timer and start a new one
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        startTimer();

        // Load the next question
        int questionIndex = questionOrder.get(currentQuestionIndex);
        questionLabel.setText(questions[questionIndex]);

        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(options[questionIndex][i]);
            optionButtons[i].setEnabled(true); // Re-enable buttons
        }
        currentQuestionIndex++;
    }

    private void toggleTheme() {
        Color background = frame.getContentPane().getBackground();
        if (background.equals(new Color(230, 30, 110))) { // Light theme
            // Switch to dark theme
            frame.getContentPane().setBackground(new Color(40, 40, 40));
            questionLabel.setForeground(Color.WHITE);
            themeButton.setText("Light Theme");

            // Update button colors for dark theme
            for (JButton button : optionButtons) {
                button.setBackground(new Color(70, 70, 70));
                button.setForeground(Color.WHITE);
                addHoverEffectDarkTheme(button);
            }
            hintButton.setBackground(new Color(255, 255, 255));
            lifelineButton.setBackground(new Color(255, 255, 255));
            skipButton.setBackground(new Color(255, 255, 255));
            themeButton.setBackground(new Color(255, 255, 255));
        } else { // Dark theme
            // Switch back to light theme
            frame.getContentPane().setBackground(new Color(230, 30, 110));
            questionLabel.setForeground(Color.YELLOW);
            themeButton.setText("Dark Theme");

            // Update button colors for light theme
            for (JButton button : optionButtons) {
                button.setBackground(new Color(170, 70, 90));
                button.setForeground(Color.WHITE);
                addHoverEffectLightTheme(button);
            }
            hintButton.setBackground(new Color(170, 70, 90));
            lifelineButton.setBackground(new Color(170, 70, 90));
            skipButton.setBackground(new Color(170, 70, 90));
            themeButton.setBackground(new Color(170, 70, 90));
        }
        frame.repaint(); // Force the frame to repaint
    }

    private void useHint() {
        JOptionPane.showMessageDialog(frame, "Think logically about the answer!");
    }

    private void useLifeline() {
        if (!lifelineUsed) {
            int questionIndex = questionOrder.get(currentQuestionIndex - 1);
            List<JButton> wrongOptions = new ArrayList<>();
            for (JButton button : optionButtons) {
                if (!button.getText().equals(correctAnswers[questionIndex]) && button.isEnabled()) {
                    wrongOptions.add(button);
                }
            }
            if (wrongOptions.size() >= 2) {
                Collections.shuffle(wrongOptions);
                wrongOptions.get(0).setEnabled(false);
                wrongOptions.get(1).setEnabled(false);
                lifelineButton.setEnabled(false);
                lifelineUsed = true;
            } else {
                JOptionPane.showMessageDialog(frame, "Not enough incorrect options to apply lifeline!");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Lifeline already used!");
        }
    }

    private void skipQuestion() {
        score = Math.max(score - 5, 0); // Deduct points for skipping
        scoreLabel.setText("Score: " + score);

        // Stop the current timer and load the next question
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        loadNextQuestion();
    }

    private void endGame() {
        gameTimer.stop();
        String message = "";
        if (score >= 80) {
            message = "Congratulations!! You did a great job!!";
        } else if (score >= 40) {
            message = "Not bad.. Keep trying!";
        } else {
            message = "It's an average score, need to improve a lot.";
        }
        JOptionPane.showMessageDialog(frame, "Game Over! Your final score is: " + score + " out of 100" + "\n" + message);
        frame.dispose();
    }

    private void startTimer() {
        // Stop any existing timer to prevent conflicts
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }

        // Start a new timer
        gameTimer = new Timer(1000, e -> {
            if (timeLimit > 0) {
                timeLimit--;
                timerLabel.setText("Time Left: " + timeLimit);
            } else {
                gameTimer.stop(); // Stop the timer when it reaches 0
                JOptionPane.showMessageDialog(frame, "Time's up! Moving to the next question.");
                loadNextQuestion();
            }
        });
        gameTimer.start();
    }

    private void addHoverEffectLightTheme(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(Color.RED);
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(170, 70, 90));
            }
        });
    }

    private void addHoverEffectDarkTheme(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(120, 120, 120)); // Brighter gray
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(70, 70, 70));
            }
        });
    }

    private class AnswerButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Stop the timer when a button is clicked
            if (gameTimer != null && gameTimer.isRunning()) {
                gameTimer.stop();
            }

            JButton clickedButton = (JButton) e.getSource();
            int questionIndex = questionOrder.get(currentQuestionIndex - 1);
            if (clickedButton.getText().equals(correctAnswers[questionIndex])) {
                score += 10;
                JOptionPane.showMessageDialog(frame, "✅ Correct! +10 Points");
            } else {
                score = Math.max(score - 5, 0);
                JOptionPane.showMessageDialog(frame, "❌ Incorrect! -5 Points");
            }
            scoreLabel.setText("Score: " + score);

            // Load the next question and restart the timer
            loadNextQuestion();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BrainFizzlerGame2().startGame());
    }
}
