package me.aknyzor;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final DecimalFormat percentFormat = new DecimalFormat("#.#%");
    private static final DecimalFormat ordinalFormat = new DecimalFormat("#");

    static List<StatPanel> mainStatPanels = new ArrayList<>();
    static List<StatPanel> subStatPanels = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatDarkLaf.setup();

            JFrame frame = new JFrame("Artifact Rater");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1300, 700);
            frame.setLayout(new BorderLayout(10, 10));
            frame.setLocationRelativeTo(null);

            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
            inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
            controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

            JPanel resultPanel = new JPanel();
            resultPanel.setLayout(new BorderLayout());
            resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JButton calculateButton = new JButton("Calculate");
            JButton themeButton = new JButton("");
            JComboBox<StatContainerType> statContainerTypeComboBox = new JComboBox<>(StatContainerType.values());
            statContainerTypeComboBox.setRenderer(new StatContainerTypeRenderer());
            JComboBox<ArtifactSetType> artifactSetTypeComboBox = new JComboBox<>(getFilteredArtifactSetTypes(RarityType.FIVE));
            artifactSetTypeComboBox.setRenderer(new ArtifactSetTypeRenderer());
            JLabel resultLabel = new JLabel("Score: ", SwingConstants.CENTER);
            resultLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            JComboBox<RarityType> rarityTypeComboBox = new JComboBox<>(RarityType.values());
            rarityTypeComboBox.setSelectedItem(RarityType.FIVE);

            JComboBox<Integer> levelComboBox = new JComboBox<>();
            for (int i = 0; i <= 20; i++) {
                levelComboBox.addItem(i);
            }

            calculateButton.addActionListener(e -> {
                int level = (Integer) levelComboBox.getSelectedItem();
                if (level == 0) {
                    JOptionPane.showMessageDialog(frame, "At least 1 level is required to calculate!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    Artifact artifact = new Artifact();
                    artifact.setStatContainerType((StatContainerType) statContainerTypeComboBox.getSelectedItem());
                    artifact.setArtifactSetType((ArtifactSetType) artifactSetTypeComboBox.getSelectedItem());
                    artifact.setRarityType(RarityType.FIVE);
                    int selectedLevel = (Integer) levelComboBox.getSelectedItem();
                    artifact.setLevel(selectedLevel);

                    for (StatPanel statPanel : mainStatPanels) {
                        StatType statType = (StatType) statPanel.getStatTypeComboBox().getSelectedItem();
                        double statValue = Double.parseDouble(statPanel.getStatValueField().getText());
                        artifact.addStat(new Stat(statType).setStatValue(statValue));
                    }

                    for (StatPanel statPanel : subStatPanels) {
                        StatType statType = (StatType) statPanel.getStatTypeComboBox().getSelectedItem();
                        double statValue = Double.parseDouble(statPanel.getStatValueField().getText());
                        artifact.addStat(new Stat(statType).setStatValue(statValue));
                    }

                    ArtifactScore artifactScore = new ArtifactScore();
                    ArtifactScoreResult result = artifactScore.score(artifact);

                    double overallScore = result.getSubScorePercent() * 100;
                    String feedback = null;

                    if (overallScore <= 25) {
                        feedback = "<font color='red'>Disgusting</font>";
                    } else if (overallScore > 25 && overallScore <= 40) {
                        feedback = "<font color='red'>Bad</font>";
                    } else if (overallScore > 40 && overallScore <= 75) {
                        feedback = "<font color='green'>Good</font>";
                    } else if (overallScore > 75 && overallScore <= 90) {
                        feedback = "<font color='green'>Very Good</font>";
                    } else if (overallScore > 90) {
                        feedback = "<font color='yellow'>Legendary</font>";
                    }

                    String resultText = String.format(
                            "<html>Score: %s (%s)<br>Feedback: %s</html>",
                            ordinal(result.getSubScore() * 100), percent(result.getSubScorePercent()),
                            feedback
                    );

                    resultLabel.setText(resultText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            themeButton.setIcon(createIcon("/light-mode.png"));
            themeButton.setPreferredSize(new Dimension(50, 50));
            themeButton.setContentAreaFilled(false);
            themeButton.setBorderPainted(false);
            themeButton.setFocusPainted(false);

            boolean[] darkTheme = {true};
            themeButton.addActionListener(e -> {
                darkTheme[0] = !darkTheme[0];
                if (darkTheme[0]) {
                    FlatDarkLaf.setup();
                } else {
                    FlatLightLaf.setup();
                }
                SwingUtilities.updateComponentTreeUI(frame);
            });

            controlPanel.add(new JLabel("Stat Container Type:"));
            controlPanel.add(statContainerTypeComboBox);

            controlPanel.add(new JLabel("Rarity Type:"));
            controlPanel.add(rarityTypeComboBox);
            rarityTypeComboBox.addActionListener(e -> {
                RarityType selectedRarity = (RarityType) rarityTypeComboBox.getSelectedItem();
                List<ArtifactSetType> options = new ArrayList<>();
                for (ArtifactSetType type : ArtifactSetType.values()) {
                    if (selectedRarity == RarityType.FIVE && type.getMaxRarity().ordinal() >= RarityType.FOUR.ordinal()) {
                        options.add(type);
                    } else if (selectedRarity == RarityType.FOUR) {
                        options.add(type);
                    }
                }
                artifactSetTypeComboBox.setModel(new DefaultComboBoxModel<>(options.toArray(new ArtifactSetType[0])));
            });
            rarityTypeComboBox.setSelectedItem(RarityType.FIVE);

            controlPanel.add(new JLabel("Artifact Set Type:"));
            controlPanel.add(artifactSetTypeComboBox);

            controlPanel.add(new JLabel("Artifact Level:"));
            controlPanel.add(levelComboBox);
            controlPanel.add(calculateButton);
            controlPanel.add(themeButton);

            resultPanel.add(resultLabel, BorderLayout.CENTER);

            frame.add(controlPanel, BorderLayout.NORTH);
            frame.add(new JScrollPane(inputPanel), BorderLayout.CENTER);
            frame.add(resultPanel, BorderLayout.SOUTH);

            for (int i = 0; i < 5; i++) {
                StatPanel statPanel;
                if (i == 0) {
                    statPanel = new StatPanel("Main Stats");
                    mainStatPanels.add(statPanel);
                } else {
                    statPanel = new StatPanel("Sub Stats");
                    subStatPanels.add(statPanel);
                }
                inputPanel.add(statPanel);
            }

            frame.setVisible(true);
        });
    }

    public static String ordinal(double value) {
        return ordinalFormat.format(value);
    }

    public static String percent(double value) {
        return percentFormat.format(value);
    }

    static class StatPanel extends JPanel {
        private final JComboBox<StatType> statTypeComboBox;
        private final JTextField statValueField;

        public StatPanel(String label) {
            this.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
            this.add(new JLabel(label + ":"));
            statTypeComboBox = new JComboBox<>(label.equals("Main Stats") ? StatType.values() : StatType.valuesWithoutExcluded());
            statValueField = new JTextField(5);
            this.add(statTypeComboBox);
            this.add(statValueField);
            this.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        }

        public JComboBox<StatType> getStatTypeComboBox() {
            return statTypeComboBox;
        }

        public JTextField getStatValueField() {
            return statValueField;
        }
    }

    private static ImageIcon createIcon(String path) {
        URL imgURL = Main.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    static class ArtifactSetTypeRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof ArtifactSetType) {
                ArtifactSetType setType = (ArtifactSetType) value;
                label.setText(setType.toString());
                ImageIcon icon = createIcon(setType);
                label.setIcon(icon);
            }
            return label;
        }

        private ImageIcon createIcon(ArtifactSetType setType) {
            String iconName = setType.name().toLowerCase();
            String iconPath = "/artifact_type/" + iconName + ".png";
            URL url = getClass().getResource(iconPath);
            if (url != null) {
                return new ImageIcon(url);
            }
            return null;
        }
    }

    static class StatContainerTypeRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof StatContainerType) {
                StatContainerType setType = (StatContainerType) value;
                label.setText(setType.toString());
                ImageIcon icon = createIcon(setType);
                label.setIcon(icon);
            }
            return label;
        }

        private ImageIcon createIcon(StatContainerType setType) {
            String iconName = setType.name().toLowerCase();
            String iconPath = "/stat_type/" + iconName + ".png";
            URL url = getClass().getResource(iconPath);
            if (url != null) {
                return new ImageIcon(url);
            }
            return null;
        }
    }

    static ArtifactSetType[] getFilteredArtifactSetTypes(RarityType rarityType) {
        List<ArtifactSetType> filteredTypes = new ArrayList<>();
        for (ArtifactSetType setType : ArtifactSetType.values()) {
            if (setType.getMaxRarity() == rarityType) {
                filteredTypes.add(setType);
            }
        }
        return filteredTypes.toArray(new ArtifactSetType[0]);
    }
}