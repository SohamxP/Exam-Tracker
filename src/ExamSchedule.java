import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class ExamSchedule {
    private ExamNode head;
    private ExamNode current;

    public ExamNode getHead() {
        return head;
    }

    public ExamSchedule() {
        this.head = null;
        this.current = null;
    }

    public void addExam(LocalDate date, String location) {
        LocalDate today = LocalDate.now();
        if (date.isBefore(today)) {
            System.out.println("Cannot add exam. The date " + date + " is in the past.");
            return;
        }
        ExamNode newExam = new ExamNode(date, location);
        if (head == null) {
            head = newExam;
            current = newExam;
        } else {
            ExamNode temp = head;
            while (temp.next != null)
                temp = temp.next;
            temp.next = newExam;
            newExam.prev = temp;
        }
    }

    public void viewNextExam() {
        if (current == null)
            System.out.println("No exams available.");
        else {
            System.out.println("Current Exam: " + current);
            if (current.next != null) {
                current = current.next;
                System.out.println("Next Exam: " + current);
                long daysUntilNext = ChronoUnit.DAYS.between(LocalDate.now(), current.examDate);
                System.out.println("Days until next exam: " + daysUntilNext);
            } else
                System.out.println("You have reached the last exam.");
        }
    }

    public void viewPreviousExam() {
        if (current == null)
            System.out.println("No exams available.");
        else {
            System.out.println("Current Exam: " + current);
            if (current.prev != null) {
                current = current.prev;
                System.out.println("Previous Exam: " + current);
            } else
                System.out.println("You are at the first exam.");
        }
    }

    public void viewAllExamSchedule() {
        ExamNode temp = head;
        if (temp == null)
            System.out.println("No exams scheduled.");
        else {
            System.out.println("Exam Schedule:");
            while (temp != null) {
                System.out.println(temp);
                temp = temp.next;
            }
        }
    }

    public void removeExam(LocalDate date) {
        ExamNode temp = head;
        while (temp != null) {
            if (temp.examDate.equals(date)) {
                if (temp.prev != null)
                    temp.prev.next = temp.next;
                if (temp.next != null)
                    temp.next.prev = temp.prev;
                if (temp == head) // If it's the head node
                    head = temp.next;
                if (temp == current) // If it's the current node
                    current = head;
                System.out.println("Exam on " + date + " removed.");
                return;
            }
            temp = temp.next;
        }
        System.out.println("No exam found on date: " + date);
    }

    public void removePastExams() {
        LocalDate today = LocalDate.now();
        ExamNode temp = head;
        while (temp != null) {
            ExamNode next = temp.next;
            if (temp.examDate.isBefore(today)) {
                removeExam(temp.examDate);
            }
            temp = next;
        }
    }

    public void sortExams() {
        if (head == null || head.next == null) {
            return; // No need to sort if list is empty or has one element
        }
        ArrayList<ExamNode> list = new ArrayList<>();
        ExamNode temp = head;
        while (temp != null) {
            list.add(temp);
            temp = temp.next;
        }
        list.sort((a, b) -> a.examDate.compareTo(b.examDate));
        head = list.get(0);
        head.prev = null;
        current = head;
        for (int i = 0; i < list.size() - 1; i++) {
            list.get(i).next = list.get(i + 1);
            list.get(i + 1).prev = list.get(i);
        }
        list.get(list.size() - 1).next = null;
        System.out.println("Exams sorted by date.");
    }

    public void printDaysUntilNextExam() {
        LocalDate today = LocalDate.now();
        ExamNode temp = head;
        ExamNode nextExam = null;
        while (temp != null) {
            if (!temp.examDate.isBefore(today)) {
                if (nextExam == null || temp.examDate.isBefore(nextExam.examDate)) {
                    nextExam = temp;
                }
            }
            temp = temp.next;
        }
        if (nextExam != null) {
            long daysUntilNext = ChronoUnit.DAYS.between(today, nextExam.examDate);
            System.out.println("Next exam is on: " + nextExam.examDate);
            System.out.println("Days until next exam: " + daysUntilNext);
        } else {
            System.out.println("No upcoming exams.");
        }
    }
}