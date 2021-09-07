import javax.swing.*;
import javax.swing.table.*;
import java.awt.Dimension;
class JTableObstacles extends JFrame
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DefaultTableModel tableModel = new DefaultTableModel();
	JTable table = new JTable(tableModel);
	
	public JTableObstacles() 
	{


		//Adding Columns
		tableModel.addColumn("Number");
		tableModel.addColumn("Type");
		tableModel.addColumn("Attinuation");

		


		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(200, 100));
		this.getContentPane().add(scrollpane);
		this.pack();
		this.setVisible(true);
	}
}