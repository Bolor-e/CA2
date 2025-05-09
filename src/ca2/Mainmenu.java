/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ca2;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.util.Scanner;

/**
 *
 * @author hsboo
 */

public class Mainmenu {
    // Where we keep all the employees
    static ArrayList<Employee> employeeList = new ArrayList<>();
    // For getting user input
    static Scanner scanner = new Scanner(System.in);

    // Main thing that runs when you start the program
    public static void main(String[] args) {
        print("Could you please enter the filename to read: ");
        String filename = scanner.nextLine();

        // Load employees from whatever file user typed
        loadEmployeesFromFile(filename);

        boolean running = true; // To keep the menu looping until user says stop

        while (running) {
            // Show the menu options
            print("\nSelect an option:");
            for (MenuOption option : MenuOption.values()) {
                print((option.ordinal() + 1) + ". " + option.name());
            }

            print("Your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // To clear leftover newline

            // Do whatever the user picked
            switch (MenuOption.values()[choice - 1]) {
                case SORT_EMPLOYEES:
                    //Sort the employees and show the first 20
                    recursiveSort(employeeList, employeeList.size());
                    employeeList.stream().limit(20).forEach(System.out::println);
                    break;
                case SEARCH_EMPLOYEES:
                    // Look for an employee by name
                    print("Enter name to search: ");
                    String target = scanner.nextLine();
                    recursiveSort(employeeList, employeeList.size()); // Have to sort first for binary search
                    Employee found = binarySearch(employeeList, target);
                    print(found != null ? found.toString() : "Not found.");
                    break;
                case ADD_EMPLOYEE:
                    // Add a new person manually
                    addEmployee();
                    break;
                case GENERATE_RANDOM_EMPLOYEE:
                    // Make a random employee
                    Employee rand = generateRandomEmployee();
                    employeeList.add(rand);
                    print("Generated: " + rand);
                    break;
                case EXIT:
                    // Leaving the program
                    running = false;
                    print("Goodbye.");
                    break;
            }
        }
    }

    // Reads employee data from a file
    public static void loadEmployeesFromFile(String filename) {
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            fileScanner.nextLine(); // Skip the first line (Probably headers)
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");

                if (data.length >= 9) { // Check if there's enough info
                    String fullName = data[0].trim() + " " + data[1].trim();
                    String deptRaw = data[5].trim().toUpperCase().replace(" ", "_");
                    String mgrRaw = data[7].trim().toUpperCase().replace(" ", "_");

                    Department dept;
                    JobPosition mgr;

                    // Try matching department, otherwise just call itt Sales
                    try {
                        dept = Department.valueOf(deptRaw);
                    } catch (Exception e) {
                        dept = Department.SALES;
                    }

                    // Try matching jobv position, otherwise call it MANAGER
                    try {
                        mgr = JobPosition.valueOf(mgrRaw);
                    } catch (Exception e) {
                        /*print("Invalid job posiition for employee " + fullName + ". Skipping employee.");
                        continue; // Skip this employee */
                        mgr = JobPosition.MANAGER;
                    }

                    // ADd the employee to the list
                    employeeList.add(new Employee(fullName, mgr, dept));
                }
            }
            print("File read successfully. " + employeeList.size() + " employees loaded.");
        } catch (Exception e) {
            print("Error: " + e.getMessage());
        }
    }

    // Sorts employees by their names using recursion
    public static void recursiveSort(ArrayList<Employee> list, int n) {
        if (n <= 1) return; // If there's 1 or none, nothing to do

        // Sort the first n-1 employees first
        recursiveSort(list, n - 1);

        // Insert the last one into the right spot
        Employee last = list.get(n - 1);
        int j = n - 2;

        while (j >= 0 && list.get(j).getName().compareToIgnoreCase(last.getName()) > 0) {
            list.set(j + 1, list.get(j));
            j--;
        }

        list.set(j + 1, last);
    }

    // Looks for an employee by name using binary search
    public static Employee binarySearch(ArrayList<Employee> list, String name) {
        int left = 0, right = list.size() - 1;

        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = list.get(mid).getName().compareToIgnoreCase(name);

            if (cmp == 0) return list.get(mid); // Found them
            if (cmp < 0) left = mid + 1; // Look in the right half
            else right = mid - 1; // Look in the left half
        }

        return null; // Didn't find anyone
    }

    // Lets the user add a new employee manuallu
    public static void addEmployee() {
        print("Enter name: ");
        String name = scanner.nextLine();

        //Pick a manager type
        print("Choose Manager Type:");
        for (int i = 0; i < JobPosition.values().length; i++) {
            print((i + 1) + ". " + JobPosition.values()[i]);
        }
        int m = scanner.nextInt();
        scanner.nextLine(); // Clear newline

        // Pick a department
        print("Choose Department:");
        for (int i = 0; i < Department.values().length; i++) {
            print((i + 1) + ". " + Department.values()[i]);
        }
        int d = scanner.nextInt();
        scanner.nextLine(); // Clear newline

        // Make a new employee and add them
        Employee emp = new Employee(name, JobPosition.values()[m - 1], Department.values()[d - 1]);
        employeeList.add(emp);
        print("Added: " + emp);
    }

    // Just makes a random employee with random details
    public static Employee generateRandomEmployee() {
        String[] names = {"Liam", "Olivia", "Noah", "Emma", "Lucas", "Mia", "Ethan", "Sophia", "Logan", "Ava"};
        String name = names[new Random().nextInt(names.length)] + " Random";

        JobPosition mgr = JobPosition.values()[new Random().nextInt(JobPosition.values().length)];
        Department dept = Department.values()[new Random().nextInt(Department.values().length)];

        return new Employee(name, mgr, dept);
    }

    // Shortcut for printing text
    public static void print(String text) {
        System.out.println(text);
    }
    
}
