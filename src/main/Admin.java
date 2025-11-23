package main;

import config.config;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Admin {

    static Scanner sc = new Scanner(System.in);
    static config db = new config();

    // Validate integer input
    public static int getValidNumber() {
        while (true) {
            if (sc.hasNextInt()) {
                int num = sc.nextInt();
                sc.nextLine();
                return num;
            } else {
                System.out.println("Invalid input! Numbers only.");
                System.out.print("Enter again: ");
                sc.nextLine();
            }
        }
    }

    // Check if record exists
    public static int getExistingID(String query) {
        while (true) {
            int id = getValidNumber();
            List<Map<String, Object>> result = db.fetchRecords(query, id);
            if (!result.isEmpty()) {
                return id;
            } else {
                System.out.println("ID not found. Try again.");
                System.out.print("Enter ID: ");
            }
        }
    }

    // View Agents, Properties, Transactions
    public static void viewAll() {
        String agentQuery = "SELECT * FROM tbl_agent";
        String[] agentHeaders = {"ID", "Name", "Email"};
        String[] agentCols = {"id", "name", "email"};
        db.viewRecords(agentQuery, agentHeaders, agentCols);

        String propQuery = "SELECT * FROM properties";
        String[] propHeaders = {"Property ID", "Address", "Type", "Price", "Agent ID"};
        String[] propCols = {"property_id", "address", "type", "price", "agent_id"};
        db.viewRecords(propQuery, propHeaders, propCols);

        String transQuery = "SELECT * FROM transactions";
        String[] transHeaders = {"Transaction ID", "Property ID", "Agent ID", "Date"};
        String[] transCols = {"transaction_id", "property_id", "agent_id", "transaction_date"};
        db.viewRecords(transQuery, transHeaders, transCols);
    }

    public void Admin() {
        char again;

        do {
            System.out.println("\n===== ADMIN DASHBOARD =====");
            System.out.println("1. View All Agents, Properties, Transactions");
            System.out.println("2. Approve Agent Account");
            System.out.println("3. Delete Agent / Property / Transaction");
            System.out.println("4. Exit to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = getValidNumber();

            switch (choice) {

                // View all
                case 1:
                    System.out.println("\n=== VIEW ALL DATA ===");
                    viewAll();
                    break;

                // Approve Agent
                case 2:
                    String pendingQuery = "SELECT * FROM tbl_user WHERE u_type = 'Agent' AND u_status = 'Pending'";
                    List<Map<String, Object>> pendingAgents = db.fetchRecords(pendingQuery);
                    if (pendingAgents.isEmpty()) {
                        System.out.println("No pending agents.");
                        break;
                    }
                    System.out.println("\n--- PENDING AGENTS ---");
                    db.viewRecords(pendingQuery, new String[]{"ID", "Name", "Email"}, new String[]{"u_id", "u_name", "u_email"});

                    System.out.print("Enter Agent ID to Approve: ");
                    int approveId = getExistingID("SELECT * FROM tbl_user WHERE u_id = ?");
                    String sqlApprove = "UPDATE tbl_user SET u_status = 'Approved' WHERE u_id = ?";
                    db.updateRecord(sqlApprove, approveId);
                    System.out.println("✅ Agent Approved Successfully!");
                    break;

                // Delete
                case 3:
                    System.out.println("\n--- DELETE MENU ---");
                    System.out.println("1. Delete Agent");
                    System.out.println("2. Delete Property");
                    System.out.println("3. Delete Transaction");
                    System.out.print("Enter choice: ");
                    int delChoice = getValidNumber();

                    switch (delChoice) {
                        case 1:
                            viewAll();
                            System.out.print("Enter Agent ID to delete: ");
                            int agentId = getExistingID("SELECT * FROM tbl_agent WHERE id = ?");
                            db.deleteRecord("DELETE FROM tbl_agent WHERE id = ?", agentId);
                            System.out.println("✅ Agent Deleted Successfully!");
                            break;

                        case 2:
                            viewAll();
                            System.out.print("Enter Property ID to delete: ");
                            int propId = getExistingID("SELECT * FROM properties WHERE property_id = ?");
                            db.deleteRecord("DELETE FROM properties WHERE property_id = ?", propId);
                            System.out.println("✅ Property Deleted Successfully!");
                            break;

                        case 3:
                            viewAll();
                            System.out.print("Enter Transaction ID to delete: ");
                            int transId = getExistingID("SELECT * FROM transactions WHERE transaction_id = ?");
                            db.deleteRecord("DELETE FROM transactions WHERE transaction_id = ?", transId);
                            System.out.println("✅ Transaction Deleted Successfully!");
                            break;

                        default:
                            System.out.println("❌ Invalid choice!");
                    }
                    break;

                case 4:
                    System.out.println("Returning to Main Menu...");
                    return;

                default:
                    System.out.println("Invalid option. Try again.");
            }

            System.out.print("\nDo you want to continue in ADMIN DASHBOARD? (Y/N): ");
            again = sc.next().charAt(0);
            sc.nextLine();

        } while (again == 'Y' || again == 'y');

        System.out.println("Exiting Admin Dashboard... Goodbye!");
    }
}
