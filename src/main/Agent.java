package main;

import config.config;
import java.util.Map;
import java.util.Scanner;

public class Agent {
    Scanner sc = new Scanner(System.in);
    config db = new config();

    private void viewProperties() {
        String query = "SELECT * FROM properties";
        String[] headers = {"Property ID", "Address", "Type", "Price"};
        String[] cols = {"property_id", "address", "type", "price"};
        db.viewRecords(query, headers, cols);
    }

    private void addProperty(int agentId) {
        System.out.println("\n=== ADD NEW PROPERTY ===");
        System.out.print("Enter Address: ");
        String address = sc.nextLine();
        System.out.print("Enter Type (House, Lot, Condo, etc.): ");
        String type = sc.nextLine();
        System.out.print("Enter Price: ");
        double price = sc.nextDouble();
        sc.nextLine();

        String sql = "INSERT INTO properties (address, type, price, agent_id) VALUES (?, ?, ?, ?)";
        db.addRecord(sql, address, type, price, agentId);
        System.out.println("✅ Property Added Successfully!");
    }

    private void viewTransactions(int agentId) {
        String query = "SELECT * FROM transactions WHERE agent_id = " + agentId;
        String[] headers = {"Transaction ID", "Property ID", "Agent ID", "Date"};
        String[] cols = {"transaction_id", "property_id", "agent_id", "transaction_date"};
        db.viewRecords(query, headers, cols);
    }

    private void updateProperty(int agentId) {
        System.out.println("\n=== UPDATE PROPERTY ===");
        String query = "SELECT * FROM properties WHERE agent_id = " + agentId;
        String[] headers = {"Property ID", "Address", "Type", "Price"};
        String[] cols = {"property_id", "address", "type", "price"};
        db.viewRecords(query, headers, cols);

        System.out.print("Enter Property ID to Update: ");
        int propId = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter New Price: ");
        double newPrice = sc.nextDouble();
        sc.nextLine();

        String sql = "UPDATE properties SET price = ? WHERE property_id = ? AND agent_id = ?";
        db.updateRecord(sql, newPrice, propId, agentId);
        System.out.println("✅ Property Updated Successfully!");
    }

    public void AgentDashboard(Map<String, Object> user) {
        char again;
        int agentId = (int) user.get("u_id");

        do {
            System.out.println("\n===== AGENT DASHBOARD =====");
            System.out.println("Welcome Agent " + user.get("u_name"));
            System.out.println("[1] Add Property");
            System.out.println("[2] View My Properties");
            System.out.println("[3] Update Property");
            System.out.println("[4] View My Transactions");
            System.out.println("[5] Logout");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addProperty(agentId);
                    break;

                case 2:
                    System.out.println("\n=== MY PROPERTY LIST ===");
                    String query = "SELECT * FROM properties WHERE agent_id = " + agentId;
                    String[] headers = {"Property ID", "Address", "Type", "Price"};
                    String[] cols = {"property_id", "address", "type", "price"};
                    db.viewRecords(query, headers, cols);
                    break;

                case 3:
                    updateProperty(agentId);
                    break;

                case 4:
                    System.out.println("\n=== MY TRANSACTIONS ===");
                    viewTransactions(agentId);
                    break;

                case 5:
                    System.out.println("🔙 Logging out...");
                    return;

                default:
                    System.out.println("❌ Invalid option. Please try again.");
                    break;
            }

            System.out.print("\nDo you want to continue in AGENT DASHBOARD? (Y/N): ");
            again = sc.next().charAt(0);
            sc.nextLine();

        } while (again == 'Y' || again == 'y');

        System.out.println("👋 Exiting Agent Dashboard... Goodbye!");
    }
}
