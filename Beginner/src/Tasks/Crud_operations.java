//Beginner level-Task 2:Simple Contact Management System 
package Tasks;
import java.util.*;
import java.io.*;

class Contact {
    private String name;
    private String phone;
    private String email;

    public Contact(String name, String phone, String email) {
        this.name = name.trim();
        this.phone = phone.trim();
        this.email = email.trim();
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    public void setPhone(String phone) { this.phone = phone.trim(); }
    public void setEmail(String email) { this.email = email.trim(); }

    @Override
    public String toString() {
        return "Name: " + name + " | Phone: " + phone + " | Email: " + email;
    }
}

public class Crud_operations {
    private static ArrayList<Contact> contacts = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Contact Management System");
            System.out.println("1-Add Contact\n2-View Contacts\n3-Update Contact\n4-Delete Contact\n5-Search Contact\n6-Export to File\n7-Exit\nChoose an option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: addContact();
                break;
                case 2:viewContacts();
                break;
                case 3:updateContact();
                break;
                case 4:deleteContact();
                break;
                case 5:searchContact();
                break;
                case 6:exportToFile();
                break;
                case 7:{ System.out.println("Exited"); return; }
                default:System.out.println("Invalid choice");
            }
        }
    }

    private static void addContact() {
        System.out.print("Enter name: ");
        String name = sc.nextLine();

        String phone;
        while (true) {
            System.out.print("Enter phone (10 digits only): ");
            phone = sc.nextLine();
            if (phone.matches("\\d{10}")) { 
                break;
            } else {
                System.out.println("Invalid phone number! Please enter exactly 10 digits.");
            }
        }

        String email;
        while (true) {
            System.out.print("Enter email: ");
            email = sc.nextLine();
            if (email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                break;
            } else {
                System.out.println("Invalid email format! Must contain '@' and a valid domain like .com or .in");
            }
        }

        for (Contact c : contacts) {
            if (c.getName().equalsIgnoreCase(name) || c.getPhone().equals(phone)) {
                System.out.println("Contact already exists!");
                return;
            }
        }

        contacts.add(new Contact(name, phone, email));
        System.out.println("Contact added!");
    }

    private static void viewContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }
        contacts.sort(Comparator.comparing(Contact::getName));
        contacts.forEach(System.out::println);
    }

    private static void updateContact() {
        System.out.print("Enter name of contact to update: ");
        String name = sc.nextLine();
        for (Contact c : contacts) {
            if (c.getName().equalsIgnoreCase(name)) {
                System.out.print("Enter new phone: ");
                c.setPhone(sc.nextLine());
                System.out.print("Enter new email: ");
                c.setEmail(sc.nextLine());
                System.out.println("Contact updated!");
                return;
            }
        }
        System.out.println("Contact not found.");
    }

    private static void deleteContact() {
        System.out.print("Enter name of contact to delete: ");
        String name = sc.nextLine();
        Iterator<Contact> it = contacts.iterator();
        while (it.hasNext()) {
            if (it.next().getName().equalsIgnoreCase(name)) {
                it.remove();
                System.out.println("Contact deleted!");
                return;
            }
        }
        System.out.println("Contact not found.");
    }

    private static void searchContact() {
        System.out.print("Enter keyword (name/phone/email): ");
        String keyword = sc.nextLine().toLowerCase();
        boolean found = false;
        for (Contact c : contacts) {
            if (c.getName().toLowerCase().contains(keyword) ||
                c.getPhone().contains(keyword) ||
                c.getEmail().toLowerCase().contains(keyword)) {
                System.out.println(c);
                found = true;
            }
        }
        if (!found) System.out.println("No matching contacts found.");
    }

    private static void exportToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("contacts.txt"))) 
        
{
        	System.out.println("File is saved in path:"+new File("contacts.txt").getAbsolutePath());
            for (Contact c : contacts) {
                bw.write(c.toString());
                bw.newLine();
            }
            System.out.println("Contacts exported to contacts.txt");
        } catch (IOException e) {
            System.out.println("Error exporting contacts: " + e.getMessage());
        }
    }
}
