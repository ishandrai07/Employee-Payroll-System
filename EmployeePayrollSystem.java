import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class EmployeePayrollSystem extends JFrame {

    private static final String FILE_NAME = "employee_payroll.txt";
    private DefaultTableModel model;

    public EmployeePayrollSystem() {
        setTitle("Employee Payroll System");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(new String[]{
                "ID", "Name", "Basic Salary", "Bonus", "Deduction", "Net Salary"
        }, 0);

        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();

        JPanel panel = new JPanel();
        JButton addBtn = new JButton("Add Employee");
        JButton deleteBtn = new JButton("Delete Employee");
        JButton payslipBtn = new JButton("Generate Payslip");

        panel.add(addBtn);
        panel.add(deleteBtn);
        panel.add(payslipBtn);
        add(panel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addEmployee());
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                model.removeRow(row);
                saveData();
            } else {
                JOptionPane.showMessageDialog(this, "Select a record to delete!");
            }
        });

        payslipBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                showPayslip(row);
            } else {
                JOptionPane.showMessageDialog(this, "Select an employee first!");
            }
        });
    }

    private void addEmployee() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField salaryField = new JTextField();
        JTextField bonusField = new JTextField();
        JTextField deductField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Employee ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Basic Salary:"));
        panel.add(salaryField);
        panel.add(new JLabel("Bonus:"));
        panel.add(bonusField);
        panel.add(new JLabel("Deduction:"));
        panel.add(deductField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Add Employee", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String id = idField.getText();
                String name = nameField.getText();
                double salary = Double.parseDouble(salaryField.getText());
                double bonus = Double.parseDouble(bonusField.getText());
                double deduct = Double.parseDouble(deductField.getText());

                double net = salary + bonus - deduct;

                model.addRow(new Object[]{id, name, salary, bonus, deduct, net});
                saveData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        }
    }

    private void showPayslip(int row) {
        String id = model.getValueAt(row, 0).toString();
        String name = model.getValueAt(row, 1).toString();
        double salary = Double.parseDouble(model.getValueAt(row, 2).toString());
        double bonus = Double.parseDouble(model.getValueAt(row, 3).toString());
        double deduct = Double.parseDouble(model.getValueAt(row, 4).toString());
        double net = Double.parseDouble(model.getValueAt(row, 5).toString());

        String payslip = "PAYSLIP\n\n"
                + "Employee ID: " + id + "\n"
                + "Name: " + name + "\n"
                + "Basic Salary: ₹" + salary + "\n"
                + "Bonus: ₹" + bonus + "\n"
                + "Deduction: ₹" + deduct + "\n"
                + "------------------------------\n"
                + "Net Salary: ₹" + net;

        JOptionPane.showMessageDialog(this, payslip);
    }

    private void saveData() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                pw.println(
                        model.getValueAt(i, 0) + "," +
                        model.getValueAt(i, 1) + "," +
                        model.getValueAt(i, 2) + "," +
                        model.getValueAt(i, 3) + "," +
                        model.getValueAt(i, 4) + "," +
                        model.getValueAt(i, 5));
            }
        } catch (Exception ignored) {}
    }

    private void loadData() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                model.addRow(p);
            }
        } catch (Exception ignored) {}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeePayrollSystem().setVisible(true));
    }
}
