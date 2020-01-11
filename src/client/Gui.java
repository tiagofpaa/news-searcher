package client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import struct.Article;
import struct.Data;


public class Gui extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JTextField expression;
	private JButton search;
	private JList<String> list;
	private JTextPane textPane;
	private JScrollPane scrollpane1;
	private JScrollPane scrollpane2;
	private Client client;
	private HashMap<String,Data> current_prev;
	
	public Gui(Client cl) {
		this.client = cl;
		setSize(new Dimension(1100, 500));
		setTitle("ISCTE Searcher...");
		setResizable(false);
		mainPanel();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void mainPanel() {
		panel = new JPanel();
		panel.setLayout(null);
		
		    
		list = new JList();
		scrollpane1 = new JScrollPane(list);
		
		textPane = new JTextPane();
		scrollpane2 = new JScrollPane(textPane);
		
		search = new JButton("Search");
		expression = new JTextField();
		
		this.scrollpane2.setBounds(500, 40, 585,423);
		this.search.setBounds(500, 5, 80, 25);
		this.expression.setBounds(300, 5, 200, 25);
		this.scrollpane1.setBounds(5, 40, 485, 423);
		
		panel.add(expression);
		panel.add(search);
		panel.add(scrollpane2);
		panel.add(scrollpane1);
		
		add(panel);
		
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(expression.getText()!=""){
					client.makeRequest(expression.getText());
					search.setEnabled(false);
					expression.setEnabled(false);
				}
			}
		});
		
		
		list.addListSelectionListener(new ListSelectionListener(){
			
		  public void valueChanged(ListSelectionEvent ev){
			  if (!ev.getValueIsAdjusting()) {
				  try {
					 if(list.getSelectedValue()!= null){
						 loadSelectedArticle(list.getSelectedValue());
					 }
				} catch (IOException | BadLocationException  e) {
					e.printStackTrace();
				}
			  }
		  }
		});
	}
	
	
	private void loadSelectedArticle(String selectedValue) throws IOException, BadLocationException {
		String[] a = selectedValue.split(" - ");
		if(this.current_prev.containsKey(a[1])){
			textPane.setText("");
			Data dat = current_prev.get(a[1]);
			Article art = dat.getArticle();
			textPane.setContentType("text/html");
			textPane.setText("<html><b>" +art.getHeader() + "</b></html>");
			appendString("\n");
			appendString("\n");
			
			for(String s : art.getArticle()){
				appendString(s + "\n");
			}
		}
		textPane.setCaretPosition(0);
	}
	
	public void appendString(String str) throws BadLocationException{
	     StyledDocument document = (StyledDocument) textPane.getDocument();
	     document.insertString(document.getLength(), str, null);
	}
	
	public void setPreview(HashMap<String,Data> prev){
		this.current_prev = prev;
		int cat_size = prev.size();
		String[] cat_array = new String[cat_size];
		cat_array = sortCatalog(cat_size, prev);
		list.setListData(cat_array);
	}
	
	private String[] sortCatalog(int num, HashMap<String,Data> prev){
		String[] cat_array = new String[num];
		ArrayList<String> to_sort = new ArrayList<>();
		for(String key: prev.keySet()){
			String a = Integer.toString(current_prev.get(key).getOcurr_number());
			a+= " - ";
			a+= key;
			to_sort.add(a);
		}
		
		Collections.sort(to_sort, new Comparator<String>() {
		    @Override
		    public int compare(String o1, String o2) {
		    	int a = Integer.parseInt(o1.split(" - ")[0]);
		    	int b = Integer.parseInt(o2.split(" - ")[0]);
		        return b - a;
		    }
		});
		
		int aux = 0;
		for(String s : to_sort){
			cat_array[aux] = s;
			aux++;
		}
		return cat_array;
	}
	
	public void enable(){
		this.search.setEnabled(true);
		this.expression.setEnabled(true);
	}
}