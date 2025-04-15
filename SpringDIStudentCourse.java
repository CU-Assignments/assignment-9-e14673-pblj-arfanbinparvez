import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

// Model class: Course
class Course {
    private String courseName;
    private int duration; // in weeks

    public Course(String courseName, int duration) {
        this.courseName = courseName;
        this.duration = duration;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getDuration() {
        return duration;
    }
}

// Model class: Student
class Student {
    private String name;
    private Course course;

    public Student(String name, Course course) {
        this.name = name;
        this.course = course;
    }

    public void displayDetails() {
        System.out.println("Student Name: " + name);
        System.out.println("Course Name: " + course.getCourseName());
        System.out.println("Duration (weeks): " + course.getDuration());
    }
}

// Configuration class using Java-based Spring configuration
@Configuration
class AppConfig {

    @Bean
    public Course course() {
        return new Course("Spring Boot", 6);
    }

    @Bean
    public Student student() {
        return new Student("Alice", course());
    }
}

// Main class
public class SpringDIStudentCourse {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Student student = context.getBean(Student.class);
        student.displayDetails();
    }
}
