package main;

import config.config;
import java.util.Map;
import java.util.Scanner;

public class Admin {
    Scanner sc = new Scanner(System.in);
    config db = new config();

    private void viewUsers() {
        String query = "SELECT * FROM tbl_user";
        String[] headers = {"ID", "Name", "Email", "Type", "Status"};
        String[] cols = {"u_id", "u_name", "u_email", "u_type", "u_status"};
        db.viewRecords(query, headers, cols);
    }

    private void viewProperties() {
        String query = "SELECT * FROM properties";
        String[] headers = {"Property ID", "Address", "Type", "Price"};
        String[] cols = {"property_id", "address", "type", "price"};
        db.viewRecords(query, headers, cols);
    }

    private void viewTransactions() {
        String query = "SELECT * FROM transactions";
        String[] headers = {"Transaction ID", "Property ID", "Agent ID", "Date"};
        String[] cols = {"transaction_id", "property_id", "agent_id", "transaction_date"};
        db.viewRecords(query, headers, cols);
    }

    public void AdminDashboard(Map<String, Object> user) {
        char again;

        do {
            System.out.println("\n===== ADMIN DASHBOARD =====");
            System.out.println("Welcome Admin " + user.get("u_name"));
            System.out.println("[1] Approve Accounts");
            System.out.println("[2] View All Users");
            System.out.println("[3] View Properties");
            System.out.println("[4] View Transactions");
            System.out.println("[5] Logout");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\n=== APPROVE PENDING ACCOUNTS ===");
                    String qry = "SELECT * FROM tbl_user WHERE u_status = 'Pending'";
                    String[] headers1 = {"ID", "Name", "Email", "Type", "Status"};
                    String[] cols1 = {"u_id", "u_name", "u_email", "u_type", "u_status"};
                    db.viewRecords(qry, headers1, cols1);

                    System.out.print("Enter User ID to Approve: ");
                    int id = sc.nextInt();
                    String sql = "UPDATE tbl_user SET u_status = ? WHERE u_id = ?";
                    db.updateRecord(sql, "Approved", id);
                    System.out.println("✅ User Approved!");
                    break;

                case 2:
                    System.out.println("\n=== ALL USER RECORDS ===");
                    viewUsers();
                    break;

                case 3:
                    System.out.println("\n=== PROPERTY LIST ===");
                    viewProperties();
                    break;

                case 4:
                    System.out.println("\n=== TRANSACTION LIST ===");
                    viewTransactions();
                    break;

                case 5:
                    System.out.println("🔙 Logging out...");
                    return;

                default:
                    System.out.println("❌ Invalid option. Please try again.");
                    break;
            }

            System.out.print("\nDo you want to continue in ADMIN DASHBOARD? (Y/N): ");
            again = sc.next().charAt(0);
            sc.nextLine();

        } while (again == 'Y' || again == 'y');

        System.out.println("👋 Exiting Admin Dashboard... Goodbye!");
    }
}
