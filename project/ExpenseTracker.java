/**
 * Expense Tracker Application
 * A comprehensive console-based expense tracking system with Supabase integration
 *
 * @author Divyansh Srivastav
 * @version 2.0
 *
 * FEATURES:
 * 1. Add new expenses with details (description, amount, category, date)
 * 2. View all expenses in a formatted table
 * 3. Filter expenses by category
 * 4. Filter expenses by date range
 * 5. Edit existing expenses
 * 6. Delete expenses
 * 7. Calculate total expenses (overall and by category)
 * 8. Search expenses by description
 * 9. Data persistence using Supabase cloud database
 * 10. Category-based expense analysis
 * 11. Monthly expense reports
 * 12. Budget tracking and alerts
 * 13. Currency in Indian Rupees (₹)
 * 14. Demo database with sample expenses
 */

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.json.*;

import java.io.InputStreamReader;
import java.io.OutputStream;

public class ExpenseTracker {

    // Global data structures and constants
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat API_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static List<Expense> expenseList = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    // Supabase Configuration
    private static final String SUPABASE_URL = "https://0ec90b57d6e95fcbda19832f.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJib2x0IiwicmVmIjoiMGVjOTBiNTdkNmU5NWZjYmRhMTk4MzJmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTg4ODE1NzQsImV4cCI6MTc1ODg4MTU3NH0.9I8-U0x86Ak8t2DGaIk0HfvTSLsAyzdnz-Nw00mMkKw";
    private static final String EXPENSES_TABLE = "expenses";

    /**
     * Main method - Entry point of the application
     * Displays menu and handles user interactions
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   EXPENSE TRACKER APPLICATION");
        System.out.println("   Author: Divyansh Srivastav");
        System.out.println("========================================\n");

        loadExpensesFromDatabase();

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addExpense();
                    break;
                case 2:
                    viewAllExpenses();
                    break;
                case 3:
                    viewExpensesByCategory();
                    break;
                case 4:
                    viewExpensesByDateRange();
                    break;
                case 5:
                    searchExpensesByDescription();
                    break;
                case 6:
                    editExpense();
                    break;
                case 7:
                    deleteExpense();
                    break;
                case 8:
                    calculateTotalExpenses();
                    break;
                case 9:
                    calculateExpensesByCategory();
                    break;
                case 10:
                    generateMonthlyReport();
                    break;
                case 11:
                    setBudgetAlert();
                    break;
                case 12:
                    System.out.println("\nData is auto-synced with Supabase!");
                    break;
                case 13:
                    System.out.println("\nThank you for using Expense Tracker!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }

            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    /**
     * Displays the main menu with all available options
     */
    private static void displayMenu() {
        System.out.println("\n========================================");
        System.out.println("           MAIN MENU");
        System.out.println("========================================");
        System.out.println("1.  Add New Expense");
        System.out.println("2.  View All Expenses");
        System.out.println("3.  View Expenses by Category");
        System.out.println("4.  View Expenses by Date Range");
        System.out.println("5.  Search Expenses by Description");
        System.out.println("6.  Edit Expense");
        System.out.println("7.  Delete Expense");
        System.out.println("8.  Calculate Total Expenses");
        System.out.println("9.  Calculate Expenses by Category");
        System.out.println("10. Generate Monthly Report");
        System.out.println("11. Set Budget Alert");
        System.out.println("12. Save Data");
        System.out.println("13. Exit");
        System.out.println("========================================");
    }

    /**
     * Adds a new expense to the system
     * Prompts user for description, amount, category, and date
     */
    private static void addExpense() {
        System.out.println("\n--- Add New Expense ---");

        scanner.nextLine(); // Clear buffer

        System.out.print("Enter description: ");
        String description = scanner.nextLine().trim();

        double amount = getDoubleInput("Enter amount: ");

        System.out.println("\nAvailable Categories:");
        System.out.println("1. Food & Dining");
        System.out.println("2. Transportation");
        System.out.println("3. Shopping");
        System.out.println("4. Entertainment");
        System.out.println("5. Bills & Utilities");
        System.out.println("6. Healthcare");
        System.out.println("7. Education");
        System.out.println("8. Others");

        int categoryChoice = getIntInput("Select category (1-8): ");
        String category = getCategoryName(categoryChoice);

        System.out.print("Enter date (DD-MM-YYYY) or press Enter for today: ");
        String dateStr = scanner.nextLine().trim();
        Date date;

        if (dateStr.isEmpty()) {
            date = new Date();
        } else {
            try {
                date = DATE_FORMAT.parse(dateStr);
            } catch (ParseException e) {
                System.out.println("Invalid date format! Using today's date.");
                date = new Date();
            }
        }

        Expense expense = new Expense(UUID.randomUUID().toString(), description, amount, category, date);
        expenseList.add(expense);
        saveExpenseToDatabase(expense);

        System.out.println("\n✓ Expense added successfully!");
        System.out.println(expense);
    }

    /**
     * Displays all expenses in a formatted table
     */
    private static void viewAllExpenses() {
        System.out.println("\n--- All Expenses ---");

        if (expenseList.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        printExpenseTable(expenseList);
    }

    /**
     * Displays expenses filtered by a specific category
     */
    private static void viewExpensesByCategory() {
        System.out.println("\n--- View Expenses by Category ---");

        System.out.println("\nAvailable Categories:");
        System.out.println("1. Food & Dining");
        System.out.println("2. Transportation");
        System.out.println("3. Shopping");
        System.out.println("4. Entertainment");
        System.out.println("5. Bills & Utilities");
        System.out.println("6. Healthcare");
        System.out.println("7. Education");
        System.out.println("8. Others");

        int categoryChoice = getIntInput("Select category (1-8): ");
        String category = getCategoryName(categoryChoice);

        List<Expense> filtered = expenseList.stream()
                .filter(e -> e.getCategory().equals(category))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            System.out.println("No expenses found in category: " + category);
        } else {
            System.out.println("\nExpenses in category: " + category);
            printExpenseTable(filtered);
        }
    }

    /**
     * Displays expenses within a specified date range
     */
    private static void viewExpensesByDateRange() {
        System.out.println("\n--- View Expenses by Date Range ---");

        scanner.nextLine(); // Clear buffer

        System.out.print("Enter start date (DD-MM-YYYY): ");
        String startDateStr = scanner.nextLine().trim();

        System.out.print("Enter end date (DD-MM-YYYY): ");
        String endDateStr = scanner.nextLine().trim();

        try {
            Date startDate = DATE_FORMAT.parse(startDateStr);
            Date endDate = DATE_FORMAT.parse(endDateStr);

            List<Expense> filtered = expenseList.stream()
                    .filter(e -> !e.getDate().before(startDate) && !e.getDate().after(endDate))
                    .collect(Collectors.toList());

            if (filtered.isEmpty()) {
                System.out.println("No expenses found in this date range.");
            } else {
                System.out.println("\nExpenses from " + startDateStr + " to " + endDateStr);
                printExpenseTable(filtered);
            }
        } catch (ParseException e) {
            System.out.println("Invalid date format! Please use DD-MM-YYYY format.");
        }
    }

    /**
     * Searches and displays expenses matching a description keyword
     */
    private static void searchExpensesByDescription() {
        System.out.println("\n--- Search Expenses ---");

        scanner.nextLine(); // Clear buffer

        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine().trim().toLowerCase();

        List<Expense> results = expenseList.stream()
                .filter(e -> e.getDescription().toLowerCase().contains(keyword))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            System.out.println("No expenses found matching: " + keyword);
        } else {
            System.out.println("\nSearch results for: " + keyword);
            printExpenseTable(results);
        }
    }

    /**
     * Allows editing of an existing expense
     * User can modify description, amount, category, or date
     */
    private static void editExpense() {
        System.out.println("\n--- Edit Expense ---");

        if (expenseList.isEmpty()) {
            System.out.println("No expenses to edit.");
            return;
        }

        int id = getIntInput("Enter expense ID to edit: ");
        Expense expense = findExpenseById(id);

        if (expense == null) {
            System.out.println("Expense not found with ID: " + id);
            return;
        }

        System.out.println("\nCurrent expense details:");
        System.out.println(expense);

        System.out.println("\nWhat would you like to edit?");
        System.out.println("1. Description");
        System.out.println("2. Amount");
        System.out.println("3. Category");
        System.out.println("4. Date");
        System.out.println("5. Edit All");

        int choice = getIntInput("Enter your choice: ");
        scanner.nextLine(); // Clear buffer

        switch (choice) {
            case 1:
                System.out.print("Enter new description: ");
                expense.setDescription(scanner.nextLine().trim());
                break;
            case 2:
                expense.setAmount(getDoubleInput("Enter new amount: "));
                scanner.nextLine(); // Clear buffer
                break;
            case 3:
                System.out.println("\nAvailable Categories:");
                System.out.println("1. Food & Dining");
                System.out.println("2. Transportation");
                System.out.println("3. Shopping");
                System.out.println("4. Entertainment");
                System.out.println("5. Bills & Utilities");
                System.out.println("6. Healthcare");
                System.out.println("7. Education");
                System.out.println("8. Others");
                int categoryChoice = getIntInput("Select category (1-8): ");
                expense.setCategory(getCategoryName(categoryChoice));
                scanner.nextLine(); // Clear buffer
                break;
            case 4:
                System.out.print("Enter new date (DD-MM-YYYY): ");
                String dateStr = scanner.nextLine().trim();
                try {
                    expense.setDate(DATE_FORMAT.parse(dateStr));
                } catch (ParseException e) {
                    System.out.println("Invalid date format! Date not updated.");
                }
                break;
            case 5:
                System.out.print("Enter new description: ");
                expense.setDescription(scanner.nextLine().trim());
                expense.setAmount(getDoubleInput("Enter new amount: "));
                scanner.nextLine(); // Clear buffer
                System.out.println("\nAvailable Categories:");
                System.out.println("1. Food & Dining");
                System.out.println("2. Transportation");
                System.out.println("3. Shopping");
                System.out.println("4. Entertainment");
                System.out.println("5. Bills & Utilities");
                System.out.println("6. Healthcare");
                System.out.println("7. Education");
                System.out.println("8. Others");
                int catChoice = getIntInput("Select category (1-8): ");
                expense.setCategory(getCategoryName(catChoice));
                scanner.nextLine(); // Clear buffer
                System.out.print("Enter new date (DD-MM-YYYY): ");
                String newDateStr = scanner.nextLine().trim();
                try {
                    expense.setDate(DATE_FORMAT.parse(newDateStr));
                } catch (ParseException e) {
                    System.out.println("Invalid date format! Date not updated.");
                }
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }

        updateExpenseInDatabase(expense);
        System.out.println("\n✓ Expense updated successfully!");
        System.out.println(expense);
    }

    /**
     * Deletes an expense from the system
     */
    private static void deleteExpense() {
        System.out.println("\n--- Delete Expense ---");

        if (expenseList.isEmpty()) {
            System.out.println("No expenses to delete.");
            return;
        }

        int id = getIntInput("Enter expense ID to delete: ");
        Expense expense = findExpenseById(id);

        if (expense == null) {
            System.out.println("Expense not found with ID: " + id);
            return;
        }

        System.out.println("\nExpense to delete:");
        System.out.println(expense);

        scanner.nextLine(); // Clear buffer
        System.out.print("Are you sure you want to delete this expense? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes") || confirm.equals("y")) {
            deleteExpenseFromDatabase(expense.getId());
            expenseList.remove(expense);
            System.out.println("\n✓ Expense deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    /**
     * Calculates and displays the total of all expenses
     */
    private static void calculateTotalExpenses() {
        System.out.println("\n--- Total Expenses ---");

        if (expenseList.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        double total = expenseList.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        System.out.println("Total number of expenses: " + expenseList.size());
        System.out.printf("Total amount spent: ₹%.2f\n", total);

        double average = total / expenseList.size();
        System.out.printf("Average expense: ₹%.2f\n", average);
    }

    /**
     * Calculates and displays expenses grouped by category
     */
    private static void calculateExpensesByCategory() {
        System.out.println("\n--- Expenses by Category ---");

        if (expenseList.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        Map<String, Double> categoryTotals = expenseList.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));

        System.out.println("\n" + String.format("%-25s %15s", "Category", "Total Amount"));
        System.out.println("=".repeat(40));

        categoryTotals.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(entry ->
                    System.out.printf("%-25s ₹%14.2f\n", entry.getKey(), entry.getValue())
                );

        double grandTotal = categoryTotals.values().stream().mapToDouble(Double::doubleValue).sum();
        System.out.println("=".repeat(40));
        System.out.printf("%-25s ₹%14.2f\n", "TOTAL", grandTotal);
    }

    /**
     * Generates a monthly expense report
     * Shows expenses grouped by month
     */
    private static void generateMonthlyReport() {
        System.out.println("\n--- Monthly Expense Report ---");

        if (expenseList.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        scanner.nextLine(); // Clear buffer
        System.out.print("Enter month (MM): ");
        String monthStr = scanner.nextLine().trim();

        System.out.print("Enter year (YYYY): ");
        String yearStr = scanner.nextLine().trim();

        int month = Integer.parseInt(monthStr);
        int year = Integer.parseInt(yearStr);

        Calendar calendar = Calendar.getInstance();
        List<Expense> monthlyExpenses = expenseList.stream()
                .filter(e -> {
                    calendar.setTime(e.getDate());
                    return calendar.get(Calendar.MONTH) + 1 == month &&
                           calendar.get(Calendar.YEAR) == year;
                })
                .collect(Collectors.toList());

        if (monthlyExpenses.isEmpty()) {
            System.out.println("No expenses found for " + monthStr + "/" + yearStr);
            return;
        }

        System.out.println("\nExpenses for " + monthStr + "/" + yearStr + ":");
        printExpenseTable(monthlyExpenses);

        double monthlyTotal = monthlyExpenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        System.out.println("\n" + "=".repeat(70));
        System.out.printf("Monthly Total: ₹%.2f\n", monthlyTotal);
        System.out.println("Total Transactions: " + monthlyExpenses.size());
    }

    /**
     * Sets a budget limit and checks if expenses exceed it
     */
    private static void setBudgetAlert() {
        System.out.println("\n--- Budget Alert ---");

        double budget = getDoubleInput("Enter your monthly budget: ");

        scanner.nextLine(); // Clear buffer
        System.out.print("Enter month (MM): ");
        String monthStr = scanner.nextLine().trim();

        System.out.print("Enter year (YYYY): ");
        String yearStr = scanner.nextLine().trim();

        int month = Integer.parseInt(monthStr);
        int year = Integer.parseInt(yearStr);

        Calendar calendar = Calendar.getInstance();
        double monthlyTotal = expenseList.stream()
                .filter(e -> {
                    calendar.setTime(e.getDate());
                    return calendar.get(Calendar.MONTH) + 1 == month &&
                           calendar.get(Calendar.YEAR) == year;
                })
                .mapToDouble(Expense::getAmount)
                .sum();

        System.out.printf("\nYour budget: ₹%.2f\n", budget);
        System.out.printf("Current expenses: ₹%.2f\n", monthlyTotal);
        System.out.printf("Remaining budget: ₹%.2f\n", budget - monthlyTotal);

        double percentageUsed = (monthlyTotal / budget) * 100;
        System.out.printf("Budget used: %.2f%%\n", percentageUsed);

        if (monthlyTotal > budget) {
            System.out.println("\n⚠ WARNING: You have exceeded your budget!");
            System.out.printf("Over budget by: ₹%.2f\n", monthlyTotal - budget);
        } else if (percentageUsed >= 80) {
            System.out.println("\n⚠ ALERT: You have used 80% or more of your budget!");
        } else {
            System.out.println("\n✓ You are within your budget!");
        }
    }

    /**
     * Saves expense to Supabase database
     */
    private static void saveExpenseToDatabase(Expense expense) {
        try {
            String url = SUPABASE_URL + "/rest/v1/" + EXPENSES_TABLE;
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);
            conn.setDoOutput(true);

            JSONObject json = new JSONObject();
            json.put("description", expense.getDescription());
            json.put("amount", expense.getAmount());
            json.put("category", expense.getCategory());
            json.put("date", API_DATE_FORMAT.format(expense.getDate()));

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.toString().getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 201) {
                System.out.println("✓ Expense saved to database successfully!");
            } else {
                System.out.println("Error saving to database: HTTP " + responseCode);
            }
            conn.disconnect();
        } catch (Exception e) {
            System.out.println("Error saving to database: " + e.getMessage());
        }
    }

    /**
     * Updates expense in Supabase database
     */
    private static void updateExpenseInDatabase(Expense expense) {
        try {
            String url = SUPABASE_URL + "/rest/v1/" + EXPENSES_TABLE + "?id=eq." + expense.getId();
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("PATCH");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);
            conn.setDoOutput(true);

            JSONObject json = new JSONObject();
            json.put("description", expense.getDescription());
            json.put("amount", expense.getAmount());
            json.put("category", expense.getCategory());
            json.put("date", API_DATE_FORMAT.format(expense.getDate()));

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.toString().getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 204) {
                System.out.println("✓ Expense updated in database successfully!");
            } else {
                System.out.println("Error updating database: HTTP " + responseCode);
            }
            conn.disconnect();
        } catch (Exception e) {
            System.out.println("Error updating database: " + e.getMessage());
        }
    }

    /**
     * Deletes expense from Supabase database
     */
    private static void deleteExpenseFromDatabase(String expenseId) {
        try {
            String url = SUPABASE_URL + "/rest/v1/" + EXPENSES_TABLE + "?id=eq." + expenseId;
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);

            int responseCode = conn.getResponseCode();
            if (responseCode == 204) {
                System.out.println("✓ Expense deleted from database successfully!");
            } else {
                System.out.println("Error deleting from database: HTTP " + responseCode);
            }
            conn.disconnect();
        } catch (Exception e) {
            System.out.println("Error deleting from database: " + e.getMessage());
        }
    }

    /**
     * Loads expenses from Supabase database when application starts
     */
    private static void loadExpensesFromDatabase() {
        try {
            String url = SUPABASE_URL + "/rest/v1/" + EXPENSES_TABLE + "?order=date.desc";
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("apikey", SUPABASE_KEY);
            conn.setRequestProperty("Authorization", "Bearer " + SUPABASE_KEY);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    Expense expense = Expense.fromJSON(jsonObj);
                    if (expense != null) {
                        expenseList.add(expense);
                    }
                }

                System.out.println("✓ Loaded " + expenseList.size() + " expense(s) from Supabase.\n");
            } else {
                System.out.println("Error loading expenses: HTTP " + responseCode);
            }
            conn.disconnect();
        } catch (Exception e) {
            System.out.println("Error loading expenses: " + e.getMessage());
        }
    }

    /**
     * Helper method to print expenses in a formatted table
     */
    private static void printExpenseTable(List<Expense> expenses) {
        System.out.println("\n" + String.format("%-5s %-25s %-15s %-20s %-12s",
                "ID", "Description", "Amount", "Category", "Date"));
        System.out.println("=".repeat(80));

        for (Expense expense : expenses) {
            System.out.printf("%-5d %-25s ₹%-14.2f %-20s %-12s\n",
                    expense.getId(),
                    truncate(expense.getDescription(), 25),
                    expense.getAmount(),
                    expense.getCategory(),
                    DATE_FORMAT.format(expense.getDate()));
        }

        System.out.println("=".repeat(80));
        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
        System.out.printf("Total: ₹%.2f (Count: %d)\n", total, expenses.size());
    }

    /**
     * Helper method to find expense by ID
     */
    private static Expense findExpenseById(int id) {
        return expenseList.stream()
                .filter(e -> e.getId().equals(String.valueOf(id)))
                .findFirst()
                .orElse(null);
    }

    /**
     * Helper method to get category name from choice number
     */
    private static String getCategoryName(int choice) {
        switch (choice) {
            case 1: return "Food & Dining";
            case 2: return "Transportation";
            case 3: return "Shopping";
            case 4: return "Entertainment";
            case 5: return "Bills & Utilities";
            case 6: return "Healthcare";
            case 7: return "Education";
            case 8: return "Others";
            default: return "Others";
        }
    }

    /**
     * Helper method to safely get integer input from user
     */
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    /**
     * Helper method to safely get double input from user
     */
    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return scanner.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid amount.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    /**
     * Helper method to truncate long strings for display
     */
    private static String truncate(String str, int length) {
        if (str.length() <= length) {
            return str;
        }
        return str.substring(0, length - 3) + "...";
    }

    /**
     * Inner class representing an Expense
     * Contains all expense details and methods for JSON serialization
     */
    static class Expense {
        private String id;
        private String description;
        private double amount;
        private String category;
        private Date date;

        public Expense(String id, String description, double amount, String category, Date date) {
            this.id = id;
            this.description = description;
            this.amount = amount;
            this.category = category;
            this.date = date;
        }

        public String getId() { return id; }
        public String getDescription() { return description; }
        public double getAmount() { return amount; }
        public String getCategory() { return category; }
        public Date getDate() { return date; }

        public void setDescription(String description) { this.description = description; }
        public void setAmount(double amount) { this.amount = amount; }
        public void setCategory(String category) { this.category = category; }
        public void setDate(Date date) { this.date = date; }

        /**
         * Creates expense object from JSON
         */
        public static Expense fromJSON(JSONObject json) {
            try {
                String id = json.getString("id");
                String description = json.getString("description");
                double amount = json.getDouble("amount");
                String category = json.getString("category");
                Date date = API_DATE_FORMAT.parse(json.getString("date"));
                return new Expense(id, description, amount, category, date);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public String toString() {
            return String.format("ID: %s | %s | ₹%.2f | %s | %s",
                    id, description, amount, category, DATE_FORMAT.format(date));
        }
    }
}
