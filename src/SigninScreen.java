import javax.servlet.http.HttpServlet;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;






public class SigninScreen extends HttpServlet implements ActionListener {
    JFrame jFrame;
    JLabel lUsername, lPassword;
    JTextField txtUsername, txtPassword;
    JButton btnLogin,btnSignUp;

    private Connection conn;
    private Statement stmt;


    private static final String DB_URL = "jdbc:mysql://localhost/lbs"; // replace mydatabase with the name of your MySQL database
    private static final String DB_USER = "root"; // replace root with your MySQL username
    private static final String DB_PASSWORD = "root"; // replace password with your MySQL password





    public SigninScreen() {
        // Create GUI components
        jFrame = new JFrame();
        jFrame.setTitle("Login Signup");
        jFrame.setSize(300, 300);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);
        jFrame.setLayout(null);

        lUsername = new JLabel("Username");
        lUsername.setBounds(20, 20, 100, 25);
        jFrame.add(lUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(130, 20, 150, 25);
        jFrame.add(txtUsername);

        lPassword = new JLabel("Password");
        lPassword.setBounds(20, 60, 100, 25);
        jFrame.add(lPassword);

        txtPassword = new JTextField();
        txtPassword.setBounds(130, 60, 150, 25);
        jFrame.add(txtPassword);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(30, 100, 100, 25);
        btnLogin.addActionListener(this);
        jFrame.add(btnLogin);

        btnSignUp = new JButton("Signup");
        btnSignUp.setBounds(170, 100, 100, 25);
        btnSignUp.addActionListener(this);
        jFrame.add(btnSignUp);

        jFrame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (e.getSource() == btnLogin) {
            try {
                // Open a connection to the MySQL database
                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                // Create a statement to execute SQL queries
                stmt = conn.createStatement();

                // Execute a SELECT query to check if the username and password are correct
                String sql = "SELECT * FROM users WHERE username='" + username + "' AND password='" + password + "'";
                ResultSet rs = stmt.executeQuery(sql);

                // Check if the query returned a row (i.e., the username and password are correct)
                if (rs.next()) {
                    // Close the login screen and open the EmployeeDash window
// get the current session or create a new one if it doesn't exist





                    jFrame.dispose();
                    new EmployeeDash();
                } else {
                    JOptionPane.showMessageDialog(jFrame, "Invalid username or password.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(jFrame, "Error connecting to the database.");
                ex.printStackTrace();
            } finally {
                // Close the database connection and statement
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == btnSignUp) {
            try {
                // Open a connection to the MySQL database
                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                // Create a statement to execute SQL queries
                stmt = conn.createStatement();
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (username VARCHAR(255) PRIMARY KEY,password VARCHAR(255))");


                // Execute an INSERT query to add a new user to the table
                String sql = "INSERT INTO users (username, password) VALUES ('" + username + "', '" + password + "')";
                int rowsAffected = stmt.executeUpdate(sql);

                // Check if the query inserted a row successfully
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(jFrame, "User added successfully.");
                } else {
                    JOptionPane.showMessageDialog(jFrame, "Error adding user.");
                }
            } catch (SQLException ex) {

                if(ex.getErrorCode()==1062){
                    JOptionPane.showMessageDialog(jFrame, "username already exists " + ex.getErrorCode());
                }

                else {

                    JOptionPane.showMessageDialog(jFrame, "Error connecting to the database." + ex.getErrorCode());
                    ex.printStackTrace();
                }

            } finally {
                // Close the database connection and statement
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {
      new SigninScreen();
    }
}

