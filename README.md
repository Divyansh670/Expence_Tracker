# Expense Tracker (Java + Supabase)

A robust, console-based expense tracking system built with Core Java and powered by Supabase for secure, cloud-backed data persistence. All amounts are displayed in Indian Rupees (₹).

---

## Overview

**Author:** Divyansh Srivastav  
**Version:** 2.0 (Supabase Integration)

This application helps you record, analyze, and report personal expenses from the command line while keeping your data synced in real time with a Supabase PostgreSQL database. It is designed to be lightweight, reliable, and easy to extend.

---

## Features

- **Add New Expense**  
  Record expenses with description, amount, category, and date, instantly persisted to Supabase.

- **View All Expenses**  
  Display all stored expenses in a clean, formatted console table.

- **Filter by Category**  
  View expenses belonging to a specific category to understand focused spending patterns.

- **Filter by Date Range**  
  Retrieve expenses within a custom time period for detailed period-based analysis.

- **Search by Description**  
  Search expenses by keyword in the description to quickly locate specific transactions.

- **Edit Expense**  
  Update existing expense details (description, amount, category, date) with full cloud synchronization.

- **Delete Expense**  
  Remove expenses from the system and Supabase with safe, controlled operations.

- **Calculate Total Expenses**  
  Compute total spending across all records or within filters using efficient Stream API operations.

- **Category Analysis**  
  View spending breakdown by category to identify where you spend the most.

- **Monthly Reports**  
  Generate detailed monthly expense summaries and statistics.

- **Budget Alerts**  
  Set budgets and receive alerts when spending approaches or exceeds defined limits.

- **Cloud Database (Supabase)**  
  All operations (add/edit/delete/view) are synced with a Supabase table using its REST API.

- **Currency Support**  
  All amounts are shown in Indian Rupees (₹), tailored for Indian users.

- **Demo Data**  
  Supabase comes pre-loaded with 20 sample expenses for quick testing and exploration.

---

## Categories

The following categories are supported out of the box:

- Food & Dining  
- Transportation  
- Shopping  
- Entertainment  
- Bills & Utilities  
- Healthcare  
- Education  
- Others  

These categories are used for filtering, analysis, and reporting.

---

## Architecture & Tech Stack

- **Language:** Core Java (console application)
- **Cloud Backend:** Supabase (PostgreSQL + REST API) [web:17][web:23]
- **Data Format:** JSON (`org.json` library) [web:18][web:20]
- **Integration:** Supabase REST endpoints, authenticated via API keys
- **Core Concepts:**
  - JSON serialization and deserialization
  - Real-time cloud synchronization on each CRUD operation
  - Java Stream API for efficient filtering, aggregation, and reporting
  - Input validation and error handling for robust console interaction

---

## Database Schema (Supabase)

The application uses a single table named `expenses` in your Supabase project.

**Table:** `expenses`

| Column      | Type       | Description                               |
|------------|------------|-------------------------------------------|
| `id`       | UUID       | Primary key for each expense record       |
| `description` | text    | Expense description                       |
| `amount`   | numeric    | Amount spent (in Rupees ₹)                |
| `category` | text       | Expense category                          |
| `date`     | timestamp  | Date and time of the expense              |
| `created_at` | timestamp| Timestamp when the record was created     |

Make sure your Supabase table matches this schema before running the application. [web:17][web:19]

---

## Prerequisites

Before you start, ensure that you have:

- Java Development Kit (**JDK 8 or higher**) installed  
- A **Supabase project** with:
  - A PostgreSQL database
  - REST API enabled
  - An `expenses` table configured as above  
- Supabase **API URL** and **API key** (anon/service) configured in the application [web:17]  
- Internet connectivity for Supabase requests

---

## Setup & Installation

### 1. Configure Supabase

In your Java code (e.g., `ExpenseTracker.java`), configure:

- Supabase base URL (e.g., `https://<project>.supabase.co/rest/v1`)
- Supabase API key (`apikey` or `Authorization: Bearer <token>`)
- Table name: `expenses`

It is recommended to load these values from environment variables or a configuration file instead of hard-coding them for production use. [web:17][web:19]

### 2. Download JSON Library

The project uses `org.json` for JSON parsing and serialization. Download the JAR from Maven Central:

```bash
wget https://repo1.maven.org/maven2/org/json/json/20231013/json-20231013.jar
```

This fetches `json-20231013.jar`, which should be placed alongside your Java source code. [web:18][web:20]

### 3. Compile

Compile the application with the JSON library on the classpath:

```bash
javac -cp json-20231013.jar ExpenseTracker.java
```

### 4. Run

On Linux/macOS:

```bash
java -cp json-20231013.jar:. ExpenseTracker
```

On Windows:

```bash
java -cp json-20231013.jar;. ExpenseTracker
```

---

## Usage

When the application starts:

1. It loads existing expenses from the Supabase `expenses` table, including any demo data.
2. A menu of numbered options is displayed (add, view, filter, edit, delete, reports, etc.).
3. You interact via the keyboard by selecting the corresponding option number.

### Example Workflow

1. Start the application.  
2. Choose option `1` to **Add a new expense**.  
3. Enter:
   - Description (e.g., `Lunch at café`)
   - Amount (e.g., `350`)
   - Category (e.g., `Food & Dining`)
   - Date (e.g., `2026-07-05`)  
4. Choose option `2` to **View all expenses** and verify the new record.  
5. Choose option `8` to **Calculate total expenses**.  
6. Choose the exit option (e.g., `13`) to close the application; all changes remain synced in Supabase.

---

## Demo Data

The Supabase database is pre-seeded with **20 sample expenses** covering common scenarios:

- Coffee & meals (₹45 – ₹450)  
- Transport (₹135 – ₹5000)  
- Shopping (₹1250 – ₹2150)  
- Entertainment (₹250 – ₹1500)  
- Bills & utilities (₹299 – ₹2500)  
- Healthcare (₹500 – ₹1200)  
- Education (₹450 – ₹999)  

This allows you to immediately test filters, category analysis, and reporting without manual data entry.

---

## Error Handling & Validation

The application includes:

- Validation for numeric amounts and supported categories  
- Date format checks for safe parsing  
- Graceful handling of network/API errors (e.g., Supabase unreachable, invalid keys)  
- Defensive checks for missing or malformed JSON responses  

These measures help keep the console experience predictable and robust, even when working with a remote backend.

---

## Roadmap / Future Enhancements

Potential enhancements planned for future versions:

- Supabase Auth integration for user-specific expense tracking [web:17]  
- Export reports to CSV/PDF  
- Advanced analytics (weekly trends, savings projections)  
- Multi-currency support and simple FX conversion  
- Optional web or desktop UI on top of the existing backend

---

## Author

**Name:** Divyansh Srivastav  
**Location:** Gorakhpur, Uttar Pradesh, India  

If you find this project useful or have ideas for improvement, feel free to open an issue or submit a pull request.

---

## License

Add your preferred license here (for example, MIT):

```text
MIT License
```

Specifying a license clearly communicates how others can use, modify, and contribute to this project.
