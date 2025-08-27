
---

### **Beginner/README.md**
```markdown
# Task 1 - Enhanced Console-Based Calculator 

## Goal: 
Create a console calculator supporting basic arithmetic, scientific functions, and unit conversions. 

##  Run Instructions
```bash
javac Calculator.java
java Calculator



``` 
### **Features**

Multi-Functional Calculator

Supports three main types:

Arithmetic Calculator

Scientific Calculator

Unit Conversion Tool

Arithmetic Operations

Addition, Subtraction, Multiplication, Division

Handles division by zero errors gracefully

Validates numeric input

Scientific Operations

Square root, Cube root, Power

Trigonometric functions: Sine, Cosine, Tangent

Inverse trigonometric functions: arcsin, arccos, arctan

Logarithmic operations: Natural log, Base-10 log, Log with any base

Exponential function 
𝑒
𝑥
e
x

Input in degrees for trigonometric functions

Validates numeric input

Unit Conversion

Length: meters ↔ kilometers, kilometers ↔ miles, miles ↔ kilometers

Temperature: Celsius ↔ Fahrenheit

Weight: kilograms ↔ pounds

Easy-to-use prompts and accurate conversion formulas

User-Friendly Interface

Interactive menu for selecting calculator type

Input validation to prevent incorrect data

Option to exit gracefully

Error Handling

Handles invalid menu choices

Handles invalid numeric input

Handles arithmetic exceptions like division by zero

### **Expected Output**
1. Arithmetic Calculator
Choose Calculator Type:
A - Arithmetic Calculator
S - Scientific Calculator
U - Unit Conversion
E - Exit
> A
Enter two numbers:
> 10
> 5
Select operation:
1 - Addition
2 - Subtraction
3 - Multiplication
4 - Division
> 4
Division: 2
2. Scientific Calculator
Choose Calculator Type:
> S
Select operation:
5 - Square root
6 - Cube root
7 - Power
...
Enter number:
> 25
Square root: 5.0
3. Unit Conversion
Choose Calculator Type:
> U
Select conversion:
18 - Meters to Kilometers
19 - Kilometers to Miles
...
Enter meters:
> 1500
Result: 1.5 km
4. Invalid Input Handling
Choose Calculator Type:
> A
Enter two numbers:
> ten
Invalid input! Please enter numbers only.


# Task 2 - Simple Contact Management System  

## Goal: 
 Build a CRUD app that manages contacts (name, phone, email) using arrays or ArrayLists.

## 🏃 Run Instructions
```bash
javac Crud_operations.java
java Crud_operations



``` 
### **Features**

Features

Add Contact

Add a new contact with Name, Phone Number, and Email.

Validates phone number (must be 10 digits).

Validates email format (must contain @ and a valid domain).

Prevents duplicate entries based on name or phone number.

View Contacts

Display all contacts in alphabetical order by name.

Shows Name, Phone, and Email for each contact.

Update Contact

Update phone number and email of an existing contact by providing the name.

Delete Contact

Delete a contact by name.

Provides feedback if the contact does not exist.

Search Contact

Search contacts by name, phone number, or email.

Case-insensitive search for easier matching.

Export Contacts to File

Export all contacts to a contacts.txt file.

File path is displayed to the user.

User-Friendly Interface

Interactive menu-driven console application.

Handles invalid inputs gracefully.

Exit option to close the application.

### **Expected Output**

1. Adding a Contact

Enter name: John Doe
Enter phone (10 digits only): 9876543210
Enter email: john@example.com
Contact added!

2. Viewing Contacts

Name: Alice Smith | Phone: 9123456780 | Email: alice@mail.com
Name: John Doe   | Phone: 9876543210 | Email: john@example.com

3. Updating a Contact

Enter name of contact to update: John Doe
Enter new phone: 9998887776
Enter new email: john.doe@mail.com
Contact updated!

4. Deleting a Contact

Enter name of contact to delete: Alice Smith
Contact deleted!

5. Searching a Contact

Enter keyword (name/phone/email): john
Name: John Doe | Phone: 9998887776 | Email: john.doe@mail.com

6. Exporting Contacts

File is saved in path: C:\Users\User\contacts.txt
Contacts exported to contacts.txt

7. Handling Invalid Inputs

Enter phone (10 digits only): 12345
Invalid phone number! Please enter exactly 10 digits.

```