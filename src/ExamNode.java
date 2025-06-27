import java.time.LocalDate;

public class ExamNode {
    @Override
    public String toString() {
        return examDate.toString() + " - " + location;
    }

    LocalDate examDate;
    String location;
    ExamNode next;
    ExamNode prev;

    public ExamNode(LocalDate examDate, String location) {
        this.examDate = examDate;
        this.location = location;
        this.next = null;
        this.prev = null;
    }
}
