import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class EmployeeDash extends JFrame implements ActionListener {
    JLabel lName, lSalary, lPhoneNumber;
    JTextField txtName, txtSalary, txtPhoneNumber;
    JButton btnSave, btnUpdate, btnDelete, btnSearch,btnDepartment;
    JTable table;

    private Connection conn;
    private Statement stmt;

    public EmployeeDash() {
        // Initialize database connection





        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/lbs","root","root");
            stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS employees (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(255), salary DOUBLE, phone_number VARCHAR(20))");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create GUI components
        setTitle("Employee Portal");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        lName = new JLabel("Name");
        lName.setBounds(20, 20, 100, 25);
        add(lName);

        txtName = new JTextField();
        txtName.setBounds(130, 20, 200, 25);
        add(txtName);

        lSalary = new JLabel("Salary");
        lSalary.setBounds(20, 60, 100, 25);
        add(lSalary);

        txtSalary = new JTextField();
        txtSalary.setBounds(130, 60, 200, 25);
        add(txtSalary);

        lPhoneNumber = new JLabel("Phone Number");
        lPhoneNumber.setBounds(20, 100, 100, 25);
        add(lPhoneNumber);

        txtPhoneNumber = new JTextField();
        txtPhoneNumber.setBounds(130, 100, 200, 25);
        add(txtPhoneNumber);

        btnSave = new JButton("Save");
        btnSave.setBounds(20, 140, 100, 25);
        btnSave.addActionListener(this);
        add(btnSave);

        btnUpdate = new JButton("Update");
        btnUpdate.setBounds(130, 140, 100, 25);
        btnUpdate.addActionListener(this);
        add(btnUpdate);

        btnDelete = new JButton("Delete");
        btnDelete.setBounds(240, 140, 100, 25);
        btnDelete.addActionListener(this);
        add(btnDelete);

        btnSearch = new JButton("Search");
        btnSearch.setBounds(350, 140, 100, 25);
        btnSearch.addActionListener(this);
        add(btnSearch);

        btnDepartment = new JButton("Department Details");
        btnDepartment.setBounds(460, 140, 150, 25);
        btnDepartment.addActionListener(this);
        add(btnDepartment);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Salary");
        model.addColumn("Phone Number");

        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM employees");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double salary = rs.getDouble("salary");
                String phoneNumber = rs.getString("phone_number");
                model.addRow(new Object[]{id, name, salary, phoneNumber});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 180, 660, 270);
        add(scrollPane);

        setVisible(true);


        // Add components to JFrame



    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            String name = txtName.getText().trim();
            String salaryStr = txtSalary.getText().trim();
            String phoneNumber = txtPhoneNumber.getText().trim();
            if (name.isEmpty() || salaryStr.isEmpty() || phoneNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter all fields.");
                return;
            }
            try {
                double salary = Double.parseDouble(salaryStr);
                String sql = "INSERT INTO employees (name, salary, phone_number) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, name);
                pstmt.setDouble(2, salary);
                pstmt.setString(3, phoneNumber);
                pstmt.executeUpdate();
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.addRow(new Object[]{null, name, salary, phoneNumber});
                txtName.setText("");
                txtSalary.setText("");
                txtPhoneNumber.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for salary.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving data to database.");
            }
        }


        else if (e.getSource() == btnUpdate) {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to update.");
                return;
            }
            int id = (int) table.getValueAt(row, 0);
            String name = txtName.getText().trim();
            String salaryStr = txtSalary.getText().trim();
            String phoneNumber = txtPhoneNumber.getText().trim();
            if (name.isEmpty() || salaryStr.isEmpty() || phoneNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter all fields.");
                return;
            }
            try {
                double salary = Double.parseDouble(salaryStr);
                String sql = "UPDATE employees SET name=?, salary=?, phone_number=? WHERE id=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, name);
                pstmt.setDouble(2, salary);
                pstmt.setString(3, phoneNumber);
                pstmt.setInt(4, id);
                pstmt.executeUpdate();
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setValueAt(name, row, 1);
                model.setValueAt(salary, row, 2);
                model.setValueAt(phoneNumber, row, 3);
                txtName.setText("");
                txtSalary.setText("");
                txtPhoneNumber.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for salary.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating data in database.");
            }
        } else if (e.getSource() == btnDelete) {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.");
                return;
            }
            int id = (int) table.getValueAt(row, 0);
            try {
                String sql = "DELETE FROM employees WHERE id=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.removeRow(row);
                txtName.setText("");
                txtSalary.setText("");
                txtPhoneNumber.setText("");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting data from database.");
            }
        }


        else if (e.getSource() == btnSearch) {
            String name = JOptionPane.showInputDialog(this, "Enter name to search:");
            if (name != null && !name.isEmpty()) {
                try {
                    String sql = "SELECT * FROM employees WHERE name LIKE ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, "%" + name + "%");
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        int id = rs.getInt("id");
                        String foundName = rs.getString("name");
                        double salary = rs.getDouble("salary");
                        String phoneNumber = rs.getString("phone_number");
                        JOptionPane.showMessageDialog(this, "ID: " + id + "\nName: " + foundName + "\nSalary: " + salary + "\nPhone Number: " + phoneNumber);
                    } else {
                        JOptionPane.showMessageDialog(this, "No record found for name: " + name);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error searching data in database.");
                }
            }
        }

    }

    public static void main(String[] args) {
        new EmployeeDash();
    }
}