package translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ArrayList<String> countries = new ArrayList<>();
            ArrayList<String> languages = new ArrayList<>();

            try {
                List<String> lines = Files.readAllLines(
                        Paths.get(GUI.class.getResource("/country-codes.txt").toURI()));
                lines.remove(0);;
                for (String line : lines) {
                    String[] parts = line.split("\t");
                    countries.add(parts[0]);
                }
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }

            try {
                List<String> lines = Files.readAllLines(
                        Paths.get(GUI.class.getResource("/language-codes.txt").toURI()));
                lines.remove(0);
                for (String line : lines) {
                    String[] parts = line.split("\t");
                    languages.add(parts[0]);
                }
            } catch (IOException | URISyntaxException e) {
                JOptionPane.showMessageDialog(null,
                        "Failed to read language or country code files:\n" + e.getMessage(),
                        "File Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JList<String> countriesList = new JList<>(countries.toArray(new String[0]));
            countriesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            countriesList.setVisibleRowCount(5);

            JList<String> languageList = new JList<>(languages.toArray(new String[0]));
            languageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            languageList.setVisibleRowCount(5);

            JScrollPane countriesScrollPane = new JScrollPane(countriesList);
            countriesScrollPane.setPreferredSize(new Dimension(150,100));

            JScrollPane languageScrollPane = new JScrollPane(languageList);
            languageScrollPane.setPreferredSize(new Dimension(150,100));

            JPanel countryPanel = new JPanel();
            countryPanel.add(new JLabel("Country:"));
            countryPanel.add(countriesScrollPane);


            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));
            languagePanel.add(languageScrollPane);

            JPanel buttonPanel = new JPanel();
            JButton submit = new JButton("Submit");
            buttonPanel.add(submit);

            JLabel resultLabelText = new JLabel("Translation:");
            buttonPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            buttonPanel.add(resultLabel);


            // adding listener for when the user clicks the submit button
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String country_selected = countriesList.getSelectedValue();
                    String language_selected = languageList.getSelectedValue();
                    if (country_selected == null) {
                        JOptionPane.showMessageDialog(null, "Please select a country.");
                        return;
                    }
                    if (language_selected == null) {
                        JOptionPane.showMessageDialog(null, "Please select a language.");
                        return;
                    }

                    Translator translator = new JSONTranslator();
                    LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();
                    CountryCodeConverter countryCodeConverter = new CountryCodeConverter();

                    String result = translator.translate(countryCodeConverter.fromCountry(country_selected),
                            languageCodeConverter.fromLanguage(language_selected));
                    if (result == null) {
                        result = "no translation found!";
                    }
                    resultLabel.setText(result);

                }

            });

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(countryPanel);
            mainPanel.add(languagePanel);
            mainPanel.add(buttonPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);


        });
    }
}
