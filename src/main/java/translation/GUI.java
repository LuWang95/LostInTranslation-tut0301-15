package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;


// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
//            JPanel countryPanel = new JPanel();
//            JTextField countryField = new JTextField(10);
//            countryPanel.add(new JLabel("Country:"));
//            countryPanel.add(countryField);

            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));

            Translator translator = new JSONTranslator();
            CountryCodeConverter cconverter = new CountryCodeConverter();
            LanguageCodeConverter lconverter = new LanguageCodeConverter();

            // create combobox, add language codes into it, and add it to our panel
            JComboBox<String> languageComboBox = new JComboBox<>();
            for(String languageCode : translator.getLanguageCodes()) {
                languageComboBox.addItem(lconverter.fromLanguageCode(languageCode));
            }
            languagePanel.add(languageComboBox);

            JPanel buttonPanel = new JPanel();
            JButton submit = new JButton("Submit");
            buttonPanel.add(submit);

            JLabel resultLabelText = new JLabel("Translation:");
            buttonPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            buttonPanel.add(resultLabel);

            //country stuff
            JPanel countryPanel = new JPanel();
            countryPanel.setLayout(new GridLayout(0, 2));
            countryPanel.add(new JLabel("Country:"), 0);

            String[] items = new String[translator.getCountryCodes().size()];

            JComboBox<String> countryComboBox = new JComboBox<>();
            int i = 0;
            for(String countryCode : translator.getCountryCodes()) {
                items[i++] = cconverter.fromCountryCode(countryCode);
            }

            // create the JList with the array of strings and set it to allow multiple
            // items to be selected at once.
            JList<String> list = new JList<>(items);
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            // place the JList in a scroll pane so that it is scrollable in the UI
            JScrollPane scrollPane = new JScrollPane(list);
            countryPanel.add(scrollPane, 1);

            list.addListSelectionListener(new ListSelectionListener() {

                /**
                 * Called whenever the value of the selection changes.
                 *
                 * @param e the event that characterizes the change.
                 */
                @Override
                public void valueChanged(ListSelectionEvent e) {

                    int[] indices = list.getSelectedIndices();
                    String[] items = new String[indices.length];
                    for (int i = 0; i < indices.length; i++) {
                        items[i] = list.getModel().getElementAt(indices[i]);
                    }

                }
            });

            // adding listener for when the user clicks the submit button
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String language = lconverter.fromLanguage(languageComboBox.getSelectedItem().toString());
                    int selected_index = list.getSelectedIndex();
                    String country = cconverter.fromCountry(list.getModel().getElementAt(selected_index));


                    // for now, just using our simple translator, but
                    // we'll need to use the real JSON version later.
                    Translator translator = new JSONTranslator();

                    String result = translator.translate(country, language);
                    if (result == null) {
                        result = "no translation found!";
                    }
                    resultLabel.setText(result);

                }

            });

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(buttonPanel);
            mainPanel.add(countryPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);


        });
    }
}
