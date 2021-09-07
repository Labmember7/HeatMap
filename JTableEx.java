import javax.swing.*;
import javax.swing.table.*;
import java.awt.Dimension;
class JTableEx extends JFrame
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DefaultTableModel tableModel = new DefaultTableModel();
	JTable table = new JTable(tableModel);
	
	public JTableEx() 
	{


		//Adding Columns
		tableModel.addColumn("User");
		tableModel.addColumn("Status");
		tableModel.addColumn("Access Point");
		tableModel.addColumn("Signal Intensity");

		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(500, 300));
		this.getContentPane().add(scrollpane);
		this.pack();
		this.setVisible(true);
	}
}