# Expense Tracker Application

**Author:** Divyansh Srivastav
**Version:** 2.0 (Supabase Integration)

A comprehensive console-based expense tracking system built with Core Java and powered by Supabase.

## Features

1. **Add New Expense** - Record expenses with description, amount, category, and date
2. **View All Expenses** - Display all expenses in a formatted table
3. **Filter by Category** - View expenses from specific categories
4. **Filter by Date Range** - View expenses within a time period
5. **Search by Description** - Find expenses by keyword
6. **Edit Expense** - Modify existing expense details
7. **Delete Expense** - Remove expenses from the system
8. **Calculate Total Expenses** - View overall spending statistics
9. **Category Analysis** - See spending breakdown by category
10. **Monthly Reports** - Generate detailed monthly expense reports
11. **Budget Alerts** - Set budgets and track spending limits
12. **Cloud Database** - All data synced with Supabase cloud database
13. **Currency Support** - All amounts displayed in Indian Rupees (₹)
14. **Demo Data** - Pre-loaded with 20 sample expenses for testing

## Categories

- Food & Dining
- Transportation
- Shopping
- Entertainment
- Bills & Utilities
- Healthcare
- Education
- Others

## Database Setup

The application uses **Supabase** as the cloud database backend. A demo database with 20 sample expenses has been created and is ready to use.

### Database Schema
- **Table:** `expenses`
- **Columns:**
  - `id` (UUID, primary key)
  - `description` (text)
  - `amount` (numeric in Rupees)
  - `category` (text)
  - `date` (timestamp)
  - `created_at` (timestamp)

## How to Compile and Run

### Download JSON Library
First, download the JSON library (org.json):
```bash
# Download org.json JAR file
wget https://repo1.maven.org/maven2/org/json/json/20231013/json-20231013.jar
```

### Compile the program:
```bash
javac -cp json-20231013.jar ExpenseTracker.java
```

### Run the program:
```bash
java -cp json-20231013.jar:. ExpenseTracker
```

Or on Windows:
```bash
java -cp json-20231013.jar;. ExpenseTracker
```

## Data Persistence

All expenses are automatically synced with the Supabase cloud database. Changes made in the application are instantly saved to the cloud.

## Demo Data Included

The database comes pre-loaded with 20 sample expenses including:
- Coffee & meals (₹45 - ₹450)
- Transport (₹135 - ₹5000)
- Shopping (₹1250 - ₹2150)
- Entertainment (₹250 - ₹1500)
- Bills & utilities (₹299 - ₹2500)
- Healthcare (₹500 - ₹1200)
- Education (₹450 - ₹999)

## Usage Example

1. Start the application
2. The app loads existing expenses from Supabase (including demo data)
3. Select option 1 to add a new expense
4. Enter expense details (description, amount, category, date)
5. Use option 2 to view all expenses
6. Use option 8 to see your total spending
7. Select option 13 to exit (changes are already synced to Supabase)

## Technical Details

- Core Java implementation
- Supabase REST API integration
- JSON data serialization
- Real-time cloud database synchronization
- Input validation and error handling
- Formatted console output with Rupees currency
- Stream API for efficient data operations
- Automatic data loading on startup
