import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class QuizApp extends JFrame implements ActionListener {

    private JLabel questionLabel, timerLabel;
    private JRadioButton[] options = new JRadioButton[4];
    private ButtonGroup optionGroup;
    private JButton nextButton;
    private javax.swing.Timer timer;
    private int timeLeft = 15;
    private int currentQuestion = 0;
    private int score = 0;

   
	private String[][] questions = {
        {"What comes next in the series: 2, 4, 6, 8, ?", "9", "10", "11", "12", "10"},
        {"Which number is the smallest?", "0.01", "0.001", "0.1", "0.0001", "0.0001"},
        {"If 5x = 20, what is x?", "2", "4", "5", "6", "4"},
        {"What is the opposite of 'profit'?", "Gain", "Loss", "Bonus", "Surplus", "Loss"},
        {"Which shape has 4 equal sides and angles?", "Rectangle", "Triangle", "Square", "Circle", "Square"}
    };
       
    private java.util.List<String> userAnswers = new ArrayList<>();

    public QuizApp() {
        setTitle("Modern Quiz App");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Main panel setup
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 255));
        add(mainPanel, BorderLayout.CENTER);

        // Timer and question panel
        timerLabel = new JLabel("Time Left: 15");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setForeground(Color.RED);
        timerLabel.setHorizontalAlignment(JLabel.RIGHT);

        questionLabel = new JLabel("Question");
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 255));
        topPanel.add(questionLabel, BorderLayout.WEST);
        topPanel.add(timerLabel, BorderLayout.EAST);

        // Options panel
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBackground(new Color(245, 245, 255));
        optionGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setFont(new Font("Arial", Font.PLAIN, 16));
            options[i].setBackground(new Color(245, 245, 255));
            optionGroup.add(options[i]);
            optionsPanel.add(Box.createVerticalStrut(10));
            optionsPanel.add(options[i]);
        }

        // Next button
        nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", Font.BOLD, 16));
        nextButton.setFocusPainted(false);
        nextButton.setBackground(new Color(0, 120, 215));
        nextButton.setForeground(Color.WHITE);
        nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextButton.addActionListener(this);

        // Add components
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(optionsPanel, BorderLayout.CENTER);
        mainPanel.add(nextButton, BorderLayout.SOUTH);

        loadQuestion();
        startTimer();

        setVisible(true);
    }

    private void loadQuestion() {
        if (currentQuestion < questions.length) {
            questionLabel.setText("Q" + (currentQuestion + 1) + ": " + questions[currentQuestion][0]);
            for (int i = 0; i < 4; i++) {
                options[i].setText(questions[currentQuestion][i + 1]);
            }
            optionGroup.clearSelection();
            timeLeft = 15;
            timerLabel.setText("Time Left: " + timeLeft);
        } else {
            showResult();
        }
    }

    private void startTimer() {
        timer = new javax.swing.Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time Left: " + timeLeft);
            if (timeLeft == 0) {
                timer.stop();
                evaluateAnswer();
            }
        });
        timer.start();
    }

    private void evaluateAnswer() {
        String selected = null;
        for (JRadioButton rb : options) {
            if (rb.isSelected()) {
                selected = rb.getText();
                break;
            }
        }
        if (selected != null && selected.equals(questions[currentQuestion][5])) {
            score++;
            userAnswers.add("Q" + (currentQuestion + 1) + ": Correct");
        } else {
            userAnswers.add("Q" + (currentQuestion + 1) + ": Incorrect (Correct: " + questions[currentQuestion][5] + ")");
        }

        currentQuestion++;
        if (currentQuestion < questions.length) {
            loadQuestion();
            timer.restart();
        } else {
            timer.stop();
            showResult();
        }
    }

    private void showResult() {
        StringBuilder result = new StringBuilder("<html><h2>Final Score: " + score + "/" + questions.length + "</h2><hr><br>");
        for (String ans : userAnswers) {
            result.append(ans).append("<br>");
        }
        result.append("</html>");

        JLabel resultLabel = new JLabel(result.toString());
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(resultLabel);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Quiz Results", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.stop();
        evaluateAnswer();
    }

    public static void main(String[] args) {
        new QuizApp();
    }
}