import java.time.LocalDate;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentInfoSystem {
    private static final ArrayList<Student> students = new ArrayList<>();
    private static final String fileName = "students.txt";

    public static void loadStudentsFromFile() {
        students.clear();

        File file = new File(fileName);
        System.out.println("Trying to load from: " + file.getAbsolutePath());
        if (!file.exists()) {
            System.out.println("  > No file found; starting with empty list.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Student current = null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("Student: ")) {
                    String name = line.substring(9).trim();
                    current = new Student(name);
                    students.add(current);
                } else if ("---".equals(line)) {
                    current = null;
                } else if (current != null && line.contains(" - ")) {
                    String[] parts = line.split(" - ", 2);
                    LocalDate date = LocalDate.parse(parts[0].trim());
                    String location = parts[1].trim();
                    current.getExamSchedule().addExam(date, location);
                    current.getExamSchedule().removePastExams();
                }
            }
            System.out.println("Finished loading. Total students: " + students.size());
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean addStudent(Student s) {
        if (findStudentByName(s.getName()) != null) {
            System.out.println("Student already exists in memory: " + s.getName());
            return false;
        }
        students.add(s);
        System.out.println("Added new student: " + s.getName());
        saveStudentsToFile();
        return true;
    }

    public static Student findStudentByName(String name) {
        name = name.trim();
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    public static void saveStudentsToFile() {
        File file = new File(fileName);
        System.out.println("Saving to: " + file.getAbsolutePath());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Student s : students) {
                writer.write("Student: " + s.getName());
                writer.newLine();
                ExamNode cursor = s.getExamSchedule().getHead();
                while (cursor != null) {
                    writer.write(cursor.examDate + " - " + cursor.location);
                    writer.newLine();
                    cursor = cursor.next;
                }
                writer.write("---");
                writer.newLine();
            }
            System.out.println("Save complete. Students in file: " + students.size());
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean removeStudent(String name) {
        Student toRemove = findStudentByName(name);
        if (toRemove != null) {
            students.remove(toRemove);
            saveStudentsToFile();
            System.out.println("Student removed: " + name);
            return true;
        } else {
            System.out.println("Student not found: " + name);
            return false;
        }
    }

    public static List<Student> getAllStudents() {
        return students;
    }

}
