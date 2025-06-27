import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        StudentInfoSystem.loadStudentsFromFile();
        for (Student s : StudentInfoSystem.getAllStudents()) {
            s.getExamSchedule().removePastExams(); // Remove expired exams after loading
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Students currently loaded:");
        for (Student s : StudentInfoSystem.getAllStudents()) {
            System.out.println(" - " + s.getName());
        }

        while (true) {
            try {
                System.out.println("\nOptions:");
                System.out.println("1. Add Student");
                System.out.println("2. Add Exam");
                System.out.println("3. View Next Exam");
                System.out.println("4. View Previous Exam");
                System.out.println("5. View Student Schedule");
                System.out.println("6. Remove Student");
                System.out.println("7. Remove an Exam");
                System.out.println("8. Sort Exams for a Student");
                System.out.println("9. Print Days Until Next Exam");
                System.out.println("10. Exit");
                System.out.print("Enter your choice: ");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        System.out.print("Enter student name: ");
                        String studentName = scanner.nextLine();
                        Student student = new Student(studentName);
                        StudentInfoSystem.addStudent(student);
                        break;
                    case 2:
                        System.out.print("Enter student name: ");
                        String nameForExam = scanner.nextLine();
                        Student studentForExam = StudentInfoSystem.findStudentByName(nameForExam);
                        if (studentForExam != null) {
                            System.out.print("Enter exam date (yyyy-MM-dd): ");
                            String examDate = scanner.nextLine();
                            LocalDate date;
                            try {
                                date = LocalDate.parse(examDate, DATE_FORMAT);
                            } catch (DateTimeParseException e) {
                                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                                continue;
                            }
                            System.out.print("Enter exam location (e.g., Room 101): ");
                            String examLocation = scanner.nextLine();
                            studentForExam.getExamSchedule().addExam(date, examLocation);
                            StudentInfoSystem.saveStudentsToFile();
                        } else {
                            System.out.println("Student not found.");
                        }
                        break;
                    case 3:
                        System.out.print("Enter student name: ");
                        String nameForNextExam = scanner.nextLine();
                        Student studentForNextExam = StudentInfoSystem.findStudentByName(nameForNextExam);
                        if (studentForNextExam != null) {
                            studentForNextExam.getExamSchedule().viewNextExam();
                        } else {
                            System.out.println("Student not found.");
                        }
                        break;
                    case 4:
                        System.out.print("Enter student name: ");
                        String nameForPreviousExam = scanner.nextLine();
                        Student studentForPreviousExam = StudentInfoSystem.findStudentByName(nameForPreviousExam);
                        if (studentForPreviousExam != null) {
                            studentForPreviousExam.getExamSchedule().viewPreviousExam();
                        } else {
                            System.out.println("Student not found.");
                        }
                        break;
                    case 5:
                        System.out.print("Enter student name: ");
                        String nameForSchedule = scanner.nextLine();
                        Student studentForSchedule = StudentInfoSystem.findStudentByName(nameForSchedule);
                        if (studentForSchedule != null) {
                            studentForSchedule.getExamSchedule().viewAllExamSchedule();
                        } else {
                            System.out.println("Student not found.");
                        }
                        break;
                    case 6:
                        System.out.print("Enter student name to remove: ");
                        String nameToRemove = scanner.nextLine();
                        StudentInfoSystem.removeStudent(nameToRemove);
                        break;
                    case 7:
                        System.out.print("Enter student name: ");
                        String nameForExamRemoval = scanner.nextLine();
                        Student s = StudentInfoSystem.findStudentByName(nameForExamRemoval);
                        if (s != null) {
                            System.out.print("Enter exam date to remove (yyyy-MM-dd): ");
                            String dateStr = scanner.nextLine();
                            try {
                                LocalDate date = LocalDate.parse(dateStr);
                                s.getExamSchedule().removeExam(date);
                                StudentInfoSystem.saveStudentsToFile();
                            } catch (DateTimeParseException e) {
                                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                            }
                        } else {
                            System.out.println("Student not found.");
                        }
                        break;
                    case 8:
                        System.out.println("Enter student name");
                        String sortName = scanner.nextLine();
                        Student sortStudent = StudentInfoSystem.findStudentByName(sortName);
                        if (sortStudent != null) {
                            sortStudent.getExamSchedule().sortExams();
                            StudentInfoSystem.saveStudentsToFile();
                        } else {
                            System.out.println("Student not found.");
                        }
                        break;
                    case 9:
                        System.out.print("Enter student name: ");
                        String upcomingName = scanner.nextLine();
                        Student upcomingStudent = StudentInfoSystem.findStudentByName(upcomingName);
                        if (upcomingStudent != null) {
                            upcomingStudent.getExamSchedule().printDaysUntilNextExam();
                        } else {
                            System.out.println("Student not found.");
                        }
                        break;

                    case 10:
                        System.out.println("Exiting the system. Goodbye!");
                        StudentInfoSystem.saveStudentsToFile();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }
}
