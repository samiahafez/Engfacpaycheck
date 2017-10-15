package org.alexu.engfaculty.gui;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.alexu.engfaculty.ExcelFileReader;
import org.alexu.engfaculty.JasperReportsGenerator;
import org.alexu.engfaculty.database.DBStream;
import org.jdesktop.swingx.JXDatePicker;

public class GUI extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7824923993707808158L;
	public final int WIDTH = 800;
	public final int HEIGHT = 400;
	private JButton buttons[];
	private JTextField texts[];
	private JLabel labels[];
	private JComboBox<String> lists[];
	private JXDatePicker datePicker[];

	private JFrame frame = new JFrame();
	private JPanel panel;
	private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	private File dataFile;
	private File empNames;
	boolean secondPanel = false;

	public void showAllStuff() {
		secondPanel = true;
		addButtons();
		addTextBoxes();
		addLabels();
		addLists();
		addPickers();
		putAllInPlace(true, true);
		// the panel with the button and text
		panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		panel.setLayout(new GridLayout(8, 4, 5, 20));
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.setSize(WIDTH, HEIGHT);
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("برنامج ادخال و طباعة بيانات مرتبات الموظفين");
		frame.pack();
		frame.setVisible(true);
		// Set default Language of text field to be arabic
		texts[0].getInputContext().selectInputMethod(new Locale("ar", "EG"));
		texts[0].setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

	}

	public void initPanel() {
		panel = this;
		final JLabel enterPass = new JLabel("Enter Password");
		enterPass.setHorizontalAlignment(SwingConstants.CENTER);
		final JPasswordField pass = new JPasswordField();
		final JButton OK = new JButton("OK");
		OK.requestFocusInWindow();
		OK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Read entered password
				char[] input = pass.getPassword();
				if (isPasswordCorrect(input)) {
					panel.remove(OK);
					panel.remove(pass);
					panel.remove(enterPass);
					showAllStuff();
				} else {
					JOptionPane.showMessageDialog(panel, "Invalid password. Try again.", "Error Message",
							JOptionPane.ERROR_MESSAGE);
					pass.setText("");
					pass.requestFocus();
				}
			}

			private boolean isPasswordCorrect(char[] input) {
				// TODO Auto-generated method stub
				char[] correctPass = { 'a', 'r', 's', 'a' };
				return Arrays.equals(input, correctPass);
			}
		});
		panel.add(enterPass);
		panel.add(pass);
		panel.add(OK);
		// the panel with the button and text
		panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		panel.setLayout(new GridLayout(3, 1, 20, 20));
		panel.setPreferredSize(new Dimension(300, 150));

		// set up the frame and display it
		frame.setSize(WIDTH, HEIGHT);
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("برنامج ادخال و طباعة بيانات مرتبات الموظفين");
		frame.pack();
		frame.setVisible(true);

	}

	private void addPickers() {
		datePicker = new JXDatePicker[3];
		for (int i = 0; i < datePicker.length; i++) {
			datePicker[i] = new JXDatePicker();
			datePicker[i].setDate(Calendar.getInstance().getTime());
			datePicker[i].setFormats(new SimpleDateFormat("dd-MM-yyyy"));
		}
	}

	final String[] pacycheckTypes = { "مرتب", "امتحانات", "شفوى", "تصحيح", "ساعات زائدة", "مجالس كلية", "مجالس أقسام",
			"مشاريع", "ماجستير", "رسائل", "إشراف", "اضافى", "مدفوعات اخرى" };;
	String[] months = new String[12];
	String[] years = new String[100];

	@SuppressWarnings("unchecked")
	private void addLists() {
		lists = new JComboBox[8];
		// initialize options of other drop down list
		for (int i = 0; i < months.length; i++) {
			months[i] = "" + (i + 1);
		}
		for (int i = 0; i < years.length; i++) {
			years[i] = "" + (i + 2010);
		}
		lists[0] = new JComboBox<String>(pacycheckTypes);
		lists[1] = new JComboBox<String>(months);
		lists[3] = new JComboBox<String>(years);
		lists[2] = new JComboBox<String>(months);
		lists[4] = new JComboBox<String>(years);
		lists[5] = new JComboBox<String>();
		lists[6] = new JComboBox<String>(getDepartments());
		lists[7] = new JComboBox<String>();

		// add a trigger for lists[5], convert to text again if an item
		// is chosen
		lists[5].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				texts[1].setText((String) lists[5].getSelectedItem());
				putAllInPlace(true, true);
			}
		});
		lists[7].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				texts[2].setText((String) lists[7].getSelectedItem());
				putAllInPlace(true, true);
			}
		});
	}

	private String[] findAllContains(String[] s, String sub) {
		ArrayList<String> resA = new ArrayList<String>();
		for (String str : s) {
			if (str.contains(sub))
				resA.add(str);
		}
		String[] result = new String[resA.size()];
		int count = 0;
		for (String str : resA) {
			result[count] = str;
			count++;
		}
		return result;
	}

	private void putAllInPlace(boolean empText, boolean fileText) {
		panel.removeAll();
		panel.add(labels[13]);
		panel.add(labels[11]);
		panel.add(labels[14]);
		panel.add(labels[12]);

		panel.add(buttons[0]);
		panel.add(new JLabel(""));
		if (empText)
			panel.add(texts[1]); // we will give user the ability to write a
									// name for search
		else
			panel.add(lists[5]); // the old implementation
		panel.add(labels[0]);

		panel.add(labels[1]);
		panel.add(new JLabel(""));
		panel.add(labels[6]);
		panel.add(buttons[3]);

		panel.add(lists[0]);
		panel.add(labels[2]);
		panel.add(lists[6]);
		panel.add(labels[9]);

		panel.add(texts[0]);
		panel.add(labels[8]);
		panel.add(datePicker[1]);
		panel.add(labels[3]);

		panel.add(datePicker[0]);
		panel.add(labels[4]);
		panel.add(datePicker[2]);
		panel.add(labels[5]);

		panel.add(buttons[1]);
		panel.add(labels[7]);
		panel.add(buttons[2]);
		panel.add(buttons[4]);

		panel.add(new JLabel(""));
		panel.add(buttons[5]);
		if (fileText) {
			panel.add(texts[2]); // we will give user the ability to write a
									// file name for search
		} else
			panel.add(lists[7]); // the old implementation
		panel.add(labels[10]);

		// Draw a line in the middle of the page
		repaint();
	}

	private String[] getDepartments() {
		String[] depts = new String[] { "قسم الرياضيات والفيزياء الهندسيه", "قسم الهندسة الانشائية",
				"قسم الهندسة الكهربية", " قسم هند سة الرى والهيدروليكا", " قسم هندسة المواصلات",
				"قسم الهندسة المعمارية", "قسم الهندسة الميكانيكية", "قسم هندسة الأنتاج",
				"قسم الهندسة البحرية وعمارة السفن", "قسم الهندسة الكيميائية", "قسم هندسة الغزل والنسيج",
				" قسم الهندسة النووية والاشعاعية", "قسم هندسة الحاسب والنظم" };

		return depts;

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (secondPanel) {
			g.drawLine(WIDTH / 2, 55, WIDTH / 2, HEIGHT - 50);
			g.drawLine(20, 55, WIDTH - 20, 55);
		}
	}

	private void addLabels() {
		labels = new JLabel[15];
		for (int i = 0; i < labels.length; i++) {
			labels[i] = new JLabel(textStrings[i]);
		}
		labels[0].setHorizontalAlignment(SwingConstants.LEFT);
		labels[1].setHorizontalAlignment(SwingConstants.LEFT);
		labels[3].setHorizontalAlignment(SwingConstants.LEFT);
		labels[4].setHorizontalAlignment(SwingConstants.LEFT);
		labels[5].setHorizontalAlignment(SwingConstants.LEFT);
		labels[6].setHorizontalAlignment(SwingConstants.RIGHT);
		labels[7].setHorizontalAlignment(SwingConstants.CENTER);
		labels[13].setHorizontalAlignment(SwingConstants.RIGHT);
		labels[14].setHorizontalAlignment(SwingConstants.RIGHT);

		for (int i = 11; i < 15; i++) {
			labels[i].setFont(new Font(labels[i].getFont().getName(), Font.PLAIN, 16));
			labels[i].setVerticalAlignment(SwingConstants.TOP);
		}
	}

	String fromDate = "من";
	String paymentDate = "تاريخ الصرف";
	String[] textStrings = { "اسم الموظف", "", "نوع المدفوعة", fromDate, paymentDate, "إلى", "", "", "الوصف", "القسم",
			"اختر ملف لمسحه", "ادخال", "طباعة", "البيانات", "التقارير" };

	private void addTextBoxes() {
		texts = new JTextField[3];
		for (int i = 0; i < texts.length; i++) {
			texts[i] = new JTextField("");
		}
		// we should add here a listener for texts[1]
		texts[1].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// In this method, I should convert the name to list
				System.out.println(texts[1].getText());
				String[] names = findAllContains(getAllNames(), texts[1].getText());
				System.out.println(Arrays.toString(names));
				lists[5].removeAllItems();
				for (String s : names)
					lists[5].addItem(s);
				// We should replace texts[1] with lists[5]
				if (names.length != 0)
					putAllInPlace(false, true);
			}
		});
		texts[2].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// In this method, I should convert the file name to a list
				String[] names = findAllContains(retrieveFilename(), texts[2].getText());
				lists[7].removeAllItems();
				for (String s : names)
					lists[7].addItem(s);
				// We should replace texts[2] with lists[7]
				if (names.length != 0)
					putAllInPlace(true, false);

			}
		});
	}

	private void addButtons() {
		buttons = new JButton[6];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton();
		}
		buttons[0].setText("اختر الملف");
		buttons[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files", "xls", "xlsx");
				fc.setFileFilter(filter);
				int result = fc.showOpenDialog(panel);
				if (result == JFileChooser.APPROVE_OPTION) {
					dataFile = fc.getSelectedFile();
					labels[1].setText(dataFile.getName());
				} else {
					JOptionPane.showMessageDialog(panel, "لم يتم اختيار ملف");
				}
			}
		});
		buttons[1].setText("ادخال الملف");
		buttons[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String payCheckType = pacycheckTypes[lists[0].getSelectedIndex()];
				String payDate = df.format(datePicker[0].getDate());
				// int month = 0, year = 0;
				// int month = lists[1].getSelectedIndex() + 1;
				// int year = lists[3].getSelectedIndex() + 2010;
				if (labels[1].getText().equals(""))
					JOptionPane.showMessageDialog(panel, "لم يتم اختيار ملف");
				else if (Arrays.asList(retrieveFilename()).contains(labels[1].getText()))
					JOptionPane.showMessageDialog(panel, "لقد  تم إدخال هذا الملف من قبل");
				else {
					labels[7].setText("Loading...");
					labels[7].paintImmediately(labels[7].getVisibleRect());
					String desc = texts[0].getText();
					enterToDatabase(dataFile, payCheckType, payDate, desc);
					//lists[7].removeAllItems();
					//String[] allFiles = retrieveFilename();
					//for (String s : allFiles)
					//	lists[7].addItem(s);
					labels[7].setText("");
				}
			}
		});
		buttons[2].setText("اظهار بيان الموظف");
		buttons[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// String empName =
				// lists[5].getItemAt(lists[5].getSelectedIndex());
				String empName = texts[1].getText();
				System.out.println("Employee Name is: " + empName);
				String from = df.format(datePicker[1].getDate());
				String to = df.format(datePicker[2].getDate());
				// int month = lists[2].getSelectedIndex() + 1;
				// int year = lists[4].getSelectedIndex() + 2010;
				if (empName.equals(""))
					JOptionPane.showMessageDialog(panel, "لم يتم ادخال اسم الموظف");
				else if (datePicker[1].getDate().after(datePicker[2].getDate()))
					JOptionPane.showMessageDialog(panel, "تأكد من ترتيب التواريخ");
				else {
					labels[7].setText("Loading...");
					labels[7].paintImmediately(labels[7].getVisibleRect());
					String dept = lists[6].getItemAt(lists[6].getSelectedIndex());
					showReport(empName, from, to);
					labels[7].setText("");
				}
			}
		});
		buttons[3].setText("ادخال أسماء الموظفين");
		buttons[3].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int result = fc.showOpenDialog(panel);
				if (result == JFileChooser.APPROVE_OPTION) {
					empNames = fc.getSelectedFile();
					labels[6].setText(empNames.getName());
				} else {
					JOptionPane.showMessageDialog(panel, "لم يتم اختيار ملف");
				}
			}
		});
		buttons[4].setText("اظهار بيانات الموظفين");
		buttons[4].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String from = df.format(datePicker[1].getDate());
				String to = df.format(datePicker[2].getDate());
				// int month = lists[2].getSelectedIndex() + 1;
				// int year = lists[4].getSelectedIndex() + 2010;
				if (labels[6].getText().equals(""))
					JOptionPane.showMessageDialog(panel, "لم يتم ادخال الملف");
				else if (datePicker[1].getDate().after(datePicker[2].getDate()))
					JOptionPane.showMessageDialog(panel, "تأكد من ترتيب التواريخ");
				else {
					labels[7].setText("Loading...");
					labels[7].paintImmediately(labels[7].getVisibleRect());
					showReports(empNames, from, to);
					labels[7].setText("");
				}
			}
		});
		buttons[5].setText("مسح من قاعدة البيانات");
		buttons[5].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String filename = lists[7].getItemAt(lists[7].getSelectedIndex());
				if (filename == null || filename.length() == 0)
					JOptionPane.showMessageDialog(panel, "لم  يتم إدخال اسم ملف للمسح");
				else {
					labels[7].setText("Loading...");
					labels[7].paintImmediately(labels[7].getVisibleRect());
					deleteRowsofFile(filename);
					//lists[7].removeAllItems();
					//String[] allFiles = retrieveFilename();
					//for (String s : allFiles)
					//	lists[7].addItem(s);
					labels[7].setText("");
				}
			}
		});
	}

	private String[] getAllNames() {
		ArrayList<String> emps = retrieveEmployees();
		int size = emps.size();
		String[] names = new String[size];
		for (int i = 0; i < size; i++) {
			names[i] = emps.get(i);
		}
		Arrays.sort(names);
		return names;
	}

	private void enterToDatabase(File dataFile, String payCheckType, String payDate, String desc) {
		ExcelFileReader efr = new ExcelFileReader();
		efr.setInputFile(dataFile);
		try {
			String result = efr.importData(payDate, payCheckType, desc);
			// JOptionPane.showMessageDialog(panel, result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// change the empName to empID in lookup in the first place
	private void showReport(String empName,  String from, String to) {

		DBStream dbs = new DBStream();
		long ID = dbs.retrieveEmployeeID(empName);
		String dept =  dbs.retrieveEmployeeDept(ID);
		double sum = dbs.retrieveSum(ID,from,to);
		JasperReportsGenerator jrg = new JasperReportsGenerator();
		// we want to use thre ID for a better design we are going to deprecate this once we use the ID
		String result = jrg.generateReportwithName(ID ,empName, from, to, dept, sum);
		JOptionPane.showMessageDialog(panel, result);
	}
	private void linkDepts(File emplNames, String from, String to) {
		ExcelFileReader efr = new ExcelFileReader();
		efr.setInputFile(emplNames);
		try {
			efr.prepareReports(emplNames, from, to);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private void showReports(File emplNames, String from, String to) {
		ExcelFileReader efr = new ExcelFileReader();
		efr.setInputFile(emplNames);
		try {
			efr.prepareReports(emplNames, from, to);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		new GUI().initPanel();
	}

	private String[] retrieveFilename() {
		DBStream dbs = new DBStream();
		ArrayList<String> fileNames = dbs.retrieveFileNames();
		int size = fileNames.size();
		String[] filenames = new String[size];
		for (int i = 0; i < size; i++) {
			filenames[i] = fileNames.get(i);
		}
		return filenames;
	}

	private void deleteRowsofFile(String filename) {
		DBStream dbs = new DBStream();
		dbs.deleteRowsofFile(filename);
	}

	private ArrayList<String> retrieveEmployees() {
		DBStream dbs = new DBStream();
		return dbs.retrieveEmployeeNames();
	}

}
