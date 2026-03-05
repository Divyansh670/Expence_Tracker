/*
  # Expense Tracker Database Schema

  1. New Tables
    - `expenses`
      - `id` (uuid, primary key)
      - `description` (text)
      - `amount` (numeric, stores amount in Rupees)
      - `category` (text, predefined categories)
      - `date` (timestamp)
      - `created_at` (timestamp)

  2. Security
    - Enable RLS on `expenses` table
    - Add policies for data access
*/

CREATE TABLE IF NOT EXISTS expenses (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  description text NOT NULL,
  amount numeric(12,2) NOT NULL,
  category text NOT NULL,
  date timestamp DEFAULT now(),
  created_at timestamp DEFAULT now()
);

ALTER TABLE expenses ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Enable all operations for all users"
  ON expenses
  FOR ALL
  TO anon, authenticated
  USING (true)
  WITH CHECK (true);

-- Insert demo data
INSERT INTO expenses (description, amount, category, date) VALUES
  ('Morning Coffee', 45.00, 'Food & Dining', '2024-02-01 08:30:00'),
  ('Grocery Shopping', 1250.00, 'Shopping', '2024-02-01 15:00:00'),
  ('Uber Ride', 135.00, 'Transportation', '2024-02-02 09:00:00'),
  ('Movie Ticket', 250.00, 'Entertainment', '2024-02-02 18:30:00'),
  ('Electricity Bill', 2500.00, 'Bills & Utilities', '2024-02-03 10:00:00'),
  ('Doctor Consultation', 500.00, 'Healthcare', '2024-02-04 11:00:00'),
  ('Lunch at Restaurant', 380.00, 'Food & Dining', '2024-02-04 13:00:00'),
  ('Online Course', 999.00, 'Education', '2024-02-05 14:30:00'),
  ('Gas Refuel', 800.00, 'Transportation', '2024-02-05 16:00:00'),
  ('Shopping Clothes', 2150.00, 'Shopping', '2024-02-06 12:00:00'),
  ('Pizza Dinner', 450.00, 'Food & Dining', '2024-02-06 19:00:00'),
  ('Internet Bill', 599.00, 'Bills & Utilities', '2024-02-07 09:00:00'),
  ('Gym Membership', 1200.00, 'Healthcare', '2024-02-07 10:30:00'),
  ('Movie with Friends', 300.00, 'Entertainment', '2024-02-08 19:00:00'),
  ('Book Purchase', 450.00, 'Education', '2024-02-08 16:00:00'),
  ('Car Maintenance', 5000.00, 'Transportation', '2024-02-09 10:00:00'),
  ('Breakfast', 120.00, 'Food & Dining', '2024-02-09 08:00:00'),
  ('Mobile Recharge', 299.00, 'Bills & Utilities', '2024-02-10 11:00:00'),
  ('Concert Ticket', 1500.00, 'Entertainment', '2024-02-10 18:00:00'),
  ('Medicine', 850.00, 'Healthcare', '2024-02-11 14:00:00');
