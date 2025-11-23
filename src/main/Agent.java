package main;

import config.config;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Agent{

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

    // Check if Agent exists
    public static int getExistingAgentID() {
        while (true) {
            int id = getValidNumber();
            String check = "SELECT * FROM tbl_agent WHERE id = ?";
            List<Map<String, Object>> result = db.fetchRecords(check, id);
            if (!result.isEmpty()) {
                return id;
            } else {
                System.out.println("Agent ID not found. Try again.");
                System.out.print("Enter Agent ID: ");
            }
        }
    }

    // Check if Property exists
    public static int getExistingPropertyID() {
        while (true) {
            int id = getValidNumber();
            String check = "SELECT * FROM properties WHERE property_id = ?";
            List<Map<String, Object>> result = db.fetchRecords(check, id);
            if (!result.isEmpty()) {
                return id;
            } else {
                System.out.println("Property ID not found. Try again.");
                System.out.print("Enter Property ID: ");
            }
        }
    }

    // View all Agents, Properties, Transactions
    public static void agentPropertyTransaction() {

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

    // Staff Dashboard
    public void Agent() {
        char again;

        do {
            System.out.println("\n===== Agent DASHBOARD =====");
            System.out.println("1. Add Agent");
            System.out.println("2. Add Property");
            System.out.println("3. Add Transaction");
            System.out.println("4. View Agents, Properties, Transactions");
            System.out.println("5. Update Agent");
            System.out.println("6. Update Property");
            System.out.println("7. Exit to Main Menu");
            System.out.print("Enter your choice: ");

            int resp = getValidNumber();

            switch (resp) {

                // Add Agent
                case 1:
                    System.out.print("Enter agent name: ");
                    String agentName = sc.nextLine();
                    System.out.print("Enter agent email: ");
                    String agentEmail = sc.nextLine();

                    String agentSql = "INSERT INTO tbl_agent(name, email) VALUES(?, ?)";
                    db.addRecord(agentSql, agentName, agentEmail);
                    System.out.println(" Agent added successfully!");
                    break;

                // Add Property
                case 2:
                    System.out.println("\n=== ADD PROPERTY ===");

                    db.viewRecords(
                        "SELECT * FROM tbl_agent ORDER BY id ASC",
                        new String[]{"ID", "Name", "Email"},
                        new String[]{"id", "name", "email"}
                    );

                    System.out.print("\nEnter Agent ID (Property Owner): ");
                    int agentId = getExistingAgentID();

                    System.out.print("Enter Property Address: ");
                    String address = sc.nextLine();
                    System.out.print("Enter Property Type (House, Lot, Condo, etc.): ");
                    String type = sc.nextLine();
                    System.out.print("Enter Property Price: ");
                    double price = sc.nextDouble();
                    sc.nextLine();

                    String propSql = "INSERT INTO properties(address, type, price, agent_id) VALUES(?, ?, ?, ?)";
                    db.addRecord(propSql, address, type, price, agentId);
                    System.out.println(" Property added successfully!");
                    break;

                // Add Transaction
                case 3:
                    agentPropertyTransaction();
                    System.out.println("\n=== ADD TRANSACTION ===");

                    System.out.print("Enter Property ID: ");
                    int propId = getExistingPropertyID();
                    System.out.print("Enter Agent ID: ");
                    int tranAgentId = getExistingAgentID();
                    System.out.print("Enter Transaction Date (YYYY-MM-DD): ");
                    String tranDate = sc.nextLine();

                    String tranSql = "INSERT INTO transactions(property_id, agent_id, transaction_date) VALUES(?, ?, ?)";
                    db.addRecord(tranSql, propId, tranAgentId, tranDate);
                    System.out.println(" Transaction created successfully!");
                    break;

                // View all records
                case 4:
                    System.out.println("\n=== VIEW AGENTS, PROPERTIES, TRANSACTIONS ===");
                    agentPropertyTransaction();
                    break;

                // Update Agent
                case 5:
                    agentPropertyTransaction();
                    System.out.println("\n--- UPDATE AGENT ---");
                    System.out.print("Enter Agent ID to Update: ");
                    int agentUpdateId = getExistingAgentID();

                    System.out.print("Enter New Agent Name: ");
                    String newAgentName = sc.nextLine();
                    System.out.print("Enter New Email: ");
                    String newAgentEmail = sc.nextLine();

                    String sqlUpdateAgent = "UPDATE tbl_agent SET name = ?, email = ? WHERE id = ?";
                    db.updateRecord(sqlUpdateAgent, newAgentName, newAgentEmail, agentUpdateId);
                    System.out.println(" Agent Updated Successfully!");
                    break;

                // Update Property
                case 6:
                    agentPropertyTransaction();
                    System.out.println("\n--- UPDATE PROPERTY ---");
                    System.out.print("Enter Property ID to Update: ");
                    int propUpdateId = getExistingPropertyID();

                    System.out.print("Enter New Address: ");
                    String newAddress = sc.nextLine();
                    System.out.print("Enter New Type: ");
                    String newType = sc.nextLine();
                    System.out.print("Enter New Price: ");
                    double newPropPrice = sc.nextDouble();
                    sc.nextLine();

                    String sqlUpdateProp = "UPDATE properties SET address = ?, type = ?, price = ? WHERE property_id = ?";
                    db.updateRecord(sqlUpdateProp, newAddress, newType, newPropPrice, propUpdateId);
                    System.out.println(" Property Updated Successfully!");
                    break;

                case 7:
                    System.out.println(" Exiting Staff Dashboard... Returning to Main Menu!");
                    return;

                default:
                    System.out.println(" Invalid option. Please try again.");
            }

            System.out.print("\nDo you want to continue in Agent DASHBOARD? (Y/N): ");
            again = sc.next().charAt(0);
            sc.nextLine();

        } while (again == 'Y' || again == 'y');

        System.out.println(" Exiting Staff Dashboard... Goodbye!");
    }

}
