import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;

import java.text.DecimalFormat;

public class JoeA3 extends JFrame {

	// Swing components declaration
	private JButton processButton, resetButton;
	private JTextArea rawSeqTextArea, newSeqTextArea, dataDisplayTextArea;
	private JCheckBox lineNumBox, groupSeqBox;
	private JRadioButton upperCaseButton, lowerCaseButton;
	private JComboBox charPerLine;
	private JLabel rawSeqMsg, newSeqMsg, dataMsg, charPerLineMsg, lineNumMsg, groupSeqMsg, radioMsg, blank;
	private String[] chars = {"40", "50", "60", "70"};


	public JoeA3() {
		super("JoeA3");

		// Set up container
		Container c = getContentPane();
		c.setLayout(new GridLayout(3,2));

		// Setting up labels
		rawSeqMsg = new JLabel("Insert raw sequence: ");
		newSeqMsg = new JLabel("Formatted sequence: ");
		dataMsg = new JLabel("Analysis Results:         ");
		charPerLineMsg = new JLabel("Characters per line: ");
		lineNumMsg = new JLabel("Show line numbers: ");
		groupSeqMsg = new JLabel("Group sequence in 10's: ");
		radioMsg = new JLabel("Select Case: ");
		blank = new JLabel("");

		// Setting up raw sequence box
		rawSeqTextArea = new JTextArea(10,60);	// text area to accept user input
		Font monospaced12 = new Font("Monospaced", Font.PLAIN, 12);	// sets up font that will be used (fixed width)
		rawSeqTextArea.setColumns(85);	// set number of columns to appropriate size
		rawSeqTextArea.setFont(monospaced12);	// sets the font of the text area
		rawSeqTextArea.setWrapStyleWord(true);
		rawSeqTextArea.setLineWrap(true);		// gets rid of horizontal scroll bar
		JScrollPane rawScroll = new JScrollPane(rawSeqTextArea);	// creates a vertical scroll bar
		rawScroll.setVisible(true);		// makes scroll bar visible

		// Setting up new sequence box
		newSeqTextArea = new JTextArea(10,60);	// text area to present formatted sequence
		newSeqTextArea.setColumns(85);
		newSeqTextArea.setFont(monospaced12);
		newSeqTextArea.setEditable(false);
		newSeqTextArea.setWrapStyleWord(true);
		newSeqTextArea.setLineWrap(true);
		JScrollPane newScroll = new JScrollPane(newSeqTextArea);
		newScroll.setVisible(true);

		// Setting up data display box
		dataDisplayTextArea = new JTextArea(10,60);
		dataDisplayTextArea.setColumns(85);
		dataDisplayTextArea.setFont(monospaced12);
		dataDisplayTextArea.setEditable(false);		// user will not be able to edit text area
		dataDisplayTextArea.setWrapStyleWord(true);
		dataDisplayTextArea.setLineWrap(true);
		JScrollPane dataScroll = new JScrollPane(dataDisplayTextArea);
		dataScroll.setVisible(true);

		// Buttons
		processButton = new JButton("Process");
		resetButton = new JButton("  Reset  ");

		// Checkboxes
		lineNumBox = new JCheckBox();
		groupSeqBox = new JCheckBox();

		// Radio Buttons
		upperCaseButton = new JRadioButton("Upper Case", true);
		lowerCaseButton = new JRadioButton("Lower Case", false);

		// Group the radio buttons
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(upperCaseButton);
		radioGroup.add(lowerCaseButton);

		// Dropdown menu
		charPerLine = new JComboBox(chars);
		charPerLine.setSelectedIndex(2);	// default is 60

		// Panels
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel p4 = new JPanel();
		JPanel p5 = new JPanel();
		p4.setLayout(new BoxLayout(p4, BoxLayout.PAGE_AXIS));
		p5.setLayout(new GridLayout(5,2));

		// Fill in panels
		p1.add(rawSeqMsg);
		p1.add(rawScroll);
		p2.add(newSeqMsg);
		p2.add(newScroll);
		p4.add(processButton, BorderLayout.NORTH);
		p4.add(resetButton, BorderLayout.SOUTH);
		p2.add(p4);
		p3.add(dataMsg);
		p3.add(dataScroll);
		p5.add(charPerLineMsg);
		p5.add(charPerLine);
		p5.add(lineNumMsg);
		p5.add(lineNumBox);
		p5.add(groupSeqMsg);
		p5.add(groupSeqBox);
		p5.add(radioMsg);
		p5.add(upperCaseButton);
		p5.add(blank);
		p5.add(lowerCaseButton);
		p1.add(p5);

		// Apply panels to container
		c.add(p1);
		c.add(p2);
		c.add(p3);
		setSize(1100,600);
		setResizable(false);	// gives the window a fixed size
		setVisible(true);

		// When process button is clicked
		processButton.addActionListener (
			new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					newSeqTextArea.setText("");
					dataDisplayTextArea.setText("");
					Pattern validate = Pattern.compile("[^ACGTacgt\\p{Space}]");	// creates pattern consisting of only valid dna characters and whitespace(to allow for variable formatting)
					Matcher matchValidation = validate.matcher(rawSeqTextArea.getText());	// creates matcher object with given pattern
					int invalid = 0;

					while(matchValidation.find()) {   // looks for a match in the user inputted sequence
						invalid++;		// +1 for every match
					}

					if (invalid > 0) {
						JOptionPane.showMessageDialog(null, "The sequence contains invalid characters");	// if there are >0 matches popup window notifies user that the sequence contains invalid characters
					}

					else {		// if the sequence is valid
						int Index = charPerLine.getSelectedIndex();		// retrieve selected drop down menu item
						int cpl = 0;		// cpl will be set to the characters per line value
						int cplSpace = 0;	// cplSpace will later contain the characters per line value + (1 for every group of 10 bases per line)
						int groups = 0;		// groups will later contain how many groups of 10 are on a line
						int matchCount = 0;
						if (Index == 0) {		// if the user selects 40 characters per line
							cpl = 40;
							cplSpace = 44;
							groups = 4;
						}
						else if (Index == 1) {	// if the user selects 50 characters per line
							cpl = 50;
							cplSpace = 55;
							groups = 5;
						}
						else if (Index == 2) {	// if the user selects 60 characters per line
							cpl = 60;
							cplSpace = 66;
							groups = 6;
						}
						else if (Index == 3) {	// if the user selects 70 characters per line
							cpl = 70;
							cplSpace = 77;
							groups = 7;
						}

						// remove whitespace
						Pattern whiteSpace = Pattern.compile("[\\p{Space}]*");		// Pattern consists of whitespace
						Matcher matchWhiteSpace = whiteSpace.matcher(rawSeqTextArea.getText());
						matchWhiteSpace.find();	// searches for whitespace
						String s = "";
						String noSpace = matchWhiteSpace.replaceAll(s);		// replaces all whitespace with nothing; combines all the bases
						String caseFormat = "";		// will be used to store a string of a user defined case

						// convert string to user selected choice
						if (upperCaseButton.isSelected()) {
							caseFormat = noSpace.toUpperCase();
						}
						else {
							caseFormat = noSpace.toLowerCase();
						}

						Pattern basesPerLine = Pattern.compile("(.{"+cpl+"})");	// Pattern is user selected characters per line
						Matcher matchBasesPerLine = basesPerLine.matcher(caseFormat);
						String[] sections;
						// Strings that will be used during the formatted process
						String changeString = "";
						String formattedString = "";		// formattedString will be the final string that is sent to the new sequence text area
						String formattedString1 = "";

							// if the user selected line numbers
							if (lineNumBox.isSelected()) {

								int stringLength = caseFormat.length();	// retrives length of formatted string
								String stringLength1 = "";
								stringLength1 = stringLength1 + stringLength;		// stores the integer number as a string
								int stringLength2 = stringLength1.length();		// gets how many digits long the number is; will be used to format line numbers properly
								String decimalForm = "";

								// creates proper argument for DecimalFormat
								for(int x=0;x<stringLength2;x++) {
									if (x==stringLength2-1) {
									decimalForm = decimalForm + "0 ";		// leaves a space between the line number and the first base of the line
									}
									else {
										decimalForm = decimalForm + "0";
									}
								}

								DecimalFormat df = new DecimalFormat(decimalForm);

								// if the user selected group sequence in 10's
								if (groupSeqBox.isSelected()) {
								// code for both checked

									Pattern groupSeqs = Pattern.compile("(.{10})");	// creates pattern of 10 characters
									Matcher matchGroups = groupSeqs.matcher(caseFormat);
									matchGroups.find();		// gets pattern of 10 characters
									changeString = "$1|";   // a pipe is added to the end of each 10 base string
									formattedString1 = matchGroups.replaceAll(changeString);		// string now contains a pipe after every 10 bases
									Pattern pipeSplit = Pattern.compile("[|]");		// Pattern is a pipe
									sections = pipeSplit.split(formattedString1);		// string is split at the pipes
									int z=1;
									for (int i=0; i<sections.length; i++) {	// this loop allows for proper formatting of lines with line numbers and spaces between groups of 10
										matchCount++;	// matchCount will be used to refer to the groups of 10 on each line

										if(matchCount == 1) {	// for the first group of 10
											sections[i] = df.format(z) + sections[i] + " ";	// adds the line numbers with proper formatting, followed by the bases and then a space
											z=z+cpl;	// adds the characters per line value to the existing value
										}
										else if((matchCount > 1) && (matchCount < groups)) { 	// for all groups between the first and last
											sections[i] = sections[i] + " ";	// adds the group of 10 bases followed by a space
										}
										else {
											sections[i] = sections[i] + "\n";		// for the last group of the line; adds the bases followed by a newline character
											matchCount = 0;		// resets matchcount to 0
										}
										formattedString = formattedString + sections[i];	// creates the new formatted string one section at a time
									}
								}
								else {
								// code for only line numbers checked
									matchBasesPerLine.find();		// searches for patterns of number of bases per line
									changeString = "$1\n|";  // adds a newline character and a pipe to the end of each section
									formattedString1 = matchBasesPerLine.replaceAll(changeString);
									Pattern pipeSplit = Pattern.compile("[|]");
									sections = pipeSplit.split(formattedString1);	// splits string at the pipes

									// joins sections of formatted string together, including line numbers
									for (int i=0, z=1;i<sections.length;i++, z=z+cpl) {
										sections[i] = df.format(z) + sections[i];
										formattedString = formattedString + sections[i];
									}
								}
							}
							else {
								if (groupSeqBox.isSelected()) {
									// code for only grouped seq checked
									Pattern groupSeqs = Pattern.compile("(.{10})");	// pattern is 10 bases
									Matcher matchGroups = groupSeqs.matcher(caseFormat);
									matchGroups.find();
									changeString = "$1|";  // adds a pipe after every 10 bases
									formattedString1 = matchGroups.replaceAll(changeString);
									Pattern pipeSplit = Pattern.compile("[|]");
									sections = pipeSplit.split(formattedString1);	// splits string at the pipes

									for (int i=0; i<sections.length; i++) {		// formatting of lines
										matchCount++;
										if(matchCount < groups) {
											sections[i] = sections[i] + " ";
										}
										else {
											sections[i] = sections[i] + "\n";
											matchCount = 0;
										}
										formattedString = formattedString + sections[i];
									}
								}
								// if neither line numbers, of group by 10 are selected
								else {
									matchBasesPerLine.find();
									changeString = "$1\n";
									formattedString = matchBasesPerLine.replaceAll(changeString);
								}
							}

							// base number and percent calculations
							int numOfA = 0;
							int numOfT = 0;
							int numOfC = 0;
							int numOfG = 0;
							int seqLength = 0;

							String allUpper = noSpace.toUpperCase();
							// search for A's
							Pattern baseA = Pattern.compile("(A)");
							Matcher matchBaseA = baseA.matcher(allUpper);

							while (matchBaseA.find()) {
							numOfA++;
							}
							// search for T's
							Pattern baseT = Pattern.compile("(T)");
							Matcher matchBaseT = baseT.matcher(allUpper);

							while (matchBaseT.find()) {
								numOfT++;
							}
							// search for C's
							Pattern baseC = Pattern.compile("(C)");
							Matcher matchBaseC = baseC.matcher(allUpper);

							while (matchBaseC.find()) {
								numOfC++;
							}
							// search for G's
							Pattern baseG = Pattern.compile("(G)");
							Matcher matchBaseG = baseG.matcher(allUpper);

							while (matchBaseG.find()) {
								numOfG++;
							}
							// Get sequence length
							Pattern totalBases = Pattern.compile("(.)");
							Matcher matchTotalBases = totalBases.matcher(allUpper);

							while (matchTotalBases.find()) {
								seqLength++;
							}
							// base percentage calculations
							double percA = (double)numOfA/(double)seqLength * 100;
							double percT = (double)numOfT/(double)seqLength * 100;
							double percC = (double)numOfC/(double)seqLength * 100;
							double percG = (double)numOfG/(double)seqLength * 100;

							// analysis results message
							String displayMessage = "Sequence length: " + seqLength + "\nNumber of A's: " + numOfA + " ("+percA+"%)"+"\nNumber of T's: "+ numOfT + " ("+percT+"%)"+"\nNumber of C's: " + numOfC +" ("+percC+"%)"+ "\nNumber of G's: " + numOfG+" ("+percG+"%)";
							newSeqTextArea.append(formattedString); // adds formatted string to the new sequence text area
							dataDisplayTextArea.append(displayMessage);	// adds the display message to the analysis text area
					}
				}
			}
		);

		// when the user clicks reset button
		resetButton.addActionListener (
			new ActionListener() {
				public void actionPerformed(ActionEvent ae1) {
					int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset?");
					if (result == JOptionPane.YES_OPTION) {
						rawSeqTextArea.setText("");	// clears text area
						newSeqTextArea.setText("");	// clears text area
						dataDisplayTextArea.setText("");	// clears text area
						lineNumBox.setSelected(false);		// deselects line numbers if it was selected
						 groupSeqBox.setSelected(false);		// deselects group sequence box if it was selected
						 lowerCaseButton.setSelected(true);		// selects lower case button (default)
						 charPerLine.setSelectedIndex(2);		// sets back to defaul of 60 characters per line
					}
				}
			}
		);
	
	}
	public static void main(String[] args) {
		JoeA3 A3 = new JoeA3();
		A3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
