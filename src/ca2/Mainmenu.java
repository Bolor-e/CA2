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
    static ArrayList<Employee> employeeList = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        print("Could you please enter the filename to read: ");
        String filename = scanner.nextLine();

        loadEmployeesFromFile(filename);

        boolean running = true;

        while (running) {
            print("\nSelect an option:");
            for (MenuOption option : MenuOption.values()) {
                print((option.ordinal() + 1) + ". " + option.name());
            }

            print("Your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (MenuOption.values()[choice - 1]) {
                case SORT_EMPLOYEES:
                    recursiveSort(employeeList, employeeList.size());
                    employeeList.stream().limit(20).forEach(System.out::println);
                    break;
                case SEARCH_EMPLOYEES:
                    print("Enter name to search: ");
                    String target = scanner.nextLine();
                    recursiveSort(employeeList, employeeList.size());
                    Employee found = binarySearch(employeeList, target);
                    print(found != null ? found.toString() : "Not found.");
                    break;
                case ADD_EMPLOYEE:
                    addEmployee();
                    break;
                case GENERATE_RANDOM_EMPLOYEE:
                    Employee rand = generateRandomEmployee();
                    employeeList.add(rand);
                    print("Generated: " + rand);
                    break;
                case EXIT:
                    running = false;
                    print("Goodbye.");
                    break;
            }
        }
    }

    public static void loadEmployeesFromFile(String filename) {
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            fileScanner.nextLine();
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");

                if (data.length >= 9) {
                    String fullName = data[0].trim() + " " + data[1].trim();
                    String deptRaw = data[5].trim().toUpperCase().replace(" ", "_");
                    String mgrRaw = data[7].trim().toUpperCase().replace(" ", "_");

                    Department dept;
                    JobPosition mgr;

                    try {
                        dept = Department.valueOf(deptRaw);
                    } catch (Exception e) {
                        dept = Department.SALES;
                    }

                    try {
                        mgr = JobPosition.valueOf(mgrRaw);
                    } catch (Exception e) {
                        /*print("Invalid job posiition for employee " + fullName + ". Skipping employee.");
                        continue; // Skip this employee */
                        mgr = JobPosition.MANAGER;
                    }

                    employeeList.add(new Employee(fullName, mgr, dept));
                }
            }
            print("File read successfully. " + employeeList.size() + " employees loaded.");
        } catch (Exception e) {
            print("Error: " + e.getMessage());
        }
    }

    public static void recursiveSort(ArrayList<Employee> list, int n) {
        if (n <= 1) return;

        recursiveSort(list, n - 1);

        Employee last = list.get(n - 1);
        int j = n - 2;

        while (j >= 0 && list.get(j).getName().compareToIgnoreCase(last.getName()) > 0) {
            list.set(j + 1, list.get(j));
            j--;
        }

        list.set(j + 1, last);
    }

    public static Employee binarySearch(ArrayList<Employee> list, String name) {
        int left = 0, right = list.size() - 1;

        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = list.get(mid).getName().compareToIgnoreCase(name);

            if (cmp == 0) return list.get(mid);
            if (cmp < 0) left = mid + 1;
            else right = mid - 1;
        }

        return null;
    }

    public static void addEmployee() {
        print("Enter name: ");
        String name = scanner.nextLine();

        print("Select Manager Type:");
        for (int i = 0; i < JobPosition.values().length; i++) {
            print((i + 1) + ". " + JobPosition.values()[i]);
        }
        int m = scanner.nextInt();
        scanner.nextLine();

        print("Select Department:");
        for (int i = 0; i < Department.values().length; i++) {
            print((i + 1) + ". " + Department.values()[i]);
        }
        int d = scanner.nextInt();
        scanner.nextLine();

        Employee emp = new Employee(name, JobPosition.values()[m - 1], Department.values()[d - 1]);
        employeeList.add(emp);
        print("Added: " + emp);
    }

    public static Employee generateRandomEmployee() {
        String[] names = {"Liam", "Olivia", "Noah", "Emma", "Lucas", "Mia", "Ethan", "Sophia", "Logan", "Ava"};
        String name = names[new Random().nextInt(names.length)] + " Random";

        JobPosition mgr = JobPosition.values()[new Random().nextInt(JobPosition.values().length)];
        Department dept = Department.values()[new Random().nextInt(Department.values().length)];

        return new Employee(name, mgr, dept);
    }

    public static void print(String text) {
        System.out.println(text);
    }
    
}
