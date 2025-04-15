//SpringHibernateBankApp.java
import javax.persistence.*;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.*;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import java.util.Properties;

@Entity
class Account {
    @Id @GeneratedValue
    private int id;
    private String holder;
    private double balance;

    public Account() {}
    public Account(String holder, double balance) {
        this.holder = holder;
        this.balance = balance;
    }
    public int getId() { return id; }
    public String getHolder() { return holder; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}

interface BankService {
    void transfer(int fromId, int toId, double amount);
}

class BankServiceImpl implements BankService {
    private SessionFactory sessionFactory;
    public BankServiceImpl(SessionFactory sf) { this.sessionFactory = sf; }

    @Transactional
    public void transfer(int fromId, int toId, double amount) {
        Session session = sessionFactory.getCurrentSession();

        Account from = session.get(Account.class, fromId);
        Account to = session.get(Account.class, toId);

        if (from.getBalance() < amount)
            throw new RuntimeException("Insufficient funds");

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        session.update(from);
        session.update(to);
    }
}

@Configuration
@EnableTransactionManagement
class AppConfig {
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        factory.setAnnotatedClasses(Account.class);
        Properties props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        props.put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        props.put("hibernate.connection.url", "jdbc:mysql://localhost:3306/testdb");
        props.put("hibernate.connection.username", "root");
        props.put("hibernate.connection.password", "yourpassword");
        props.put("hibernate.hbm2ddl.auto", "update");
        factory.setHibernateProperties(props);
        return factory;
    }

    @Bean
    public PlatformTransactionManager txManager(SessionFactory sf) {
        return new HibernateTransactionManager(sf);
    }

    @Bean
    public BankService bankService(SessionFactory sf) {
        return new BankServiceImpl(sf);
    }
}

public class SpringHibernateBankApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        SessionFactory sf = ctx.getBean(SessionFactory.class);

        // Create accounts
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(new Account("Alice", 5000));
        session.save(new Account("Bob", 3000));
        session.getTransaction().commit();
        session.close();

        // Successful transaction
        BankService service = ctx.getBean(BankService.class);
        try {
            service.transfer(1, 2, 1000);
            System.out.println("Transfer successful");
        } catch (Exception e) {
            System.out.println("Failed: " + e.getMessage());
        }

        // Failed transaction (insufficient funds)
        try {
            service.transfer(1, 2, 10000); // should fail
        } catch (Exception e) {
            System.out.println("Rollback occurred: " + e.getMessage());
        }

        ctx.close();
    }
}


//HibernateStudent.java
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import javax.persistence.*;

@Entity
@Table(name = "student")
class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int age;

    public Student() {}
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
}

public class HibernateStudentApp {
    private static SessionFactory factory;

    public static void main(String[] args) {
        factory = new Configuration().configure().addAnnotatedClass(Student.class).buildSessionFactory();

        Student s1 = new Student("Alice", 20);
        Student s2 = new Student("Bob", 22);

        createStudent(s1);
        createStudent(s2);
        readStudents();
        updateStudent(1, "Alicia", 21);
        deleteStudent(2);

        factory.close();
    }

    public static void createStudent(Student student) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(student);
        tx.commit();
        session.close();
        System.out.println("Saved: " + student.getName());
    }

    public static void readStudents() {
        Session session = factory.openSession();
        var students = session.createQuery("FROM Student", Student.class).list();
        for (Student s : students)
            System.out.println(s.getId() + " " + s.getName() + " " + s.getAge());
        session.close();
    }

    public static void updateStudent(int id, String name, int age) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        Student s = session.get(Student.class, id);
        s.setName(name);
        s.setAge(age);
        session.update(s);
        tx.commit();
        session.close();
        System.out.println("Updated student " + id);
    }

    public static void deleteStudent(int id) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        Student s = session.get(Student.class, id);
        session.delete(s);
        tx.commit();
        session.close();
        System.out.println("Deleted student " + id);
    }
}



// hibernate.cfg.xml
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
"-//Hibernate/Hibernate Configuration DTD 3.0//EN"
"http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
  <session-factory>
    <property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
    <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/testdb</property>
    <property name="hibernate.connection.username">root</property>
    <property name="hibernate.connection.password">yourpassword</property>
    <property name="hibernate.dialect">org.hibernate.dialect.MySQL8Dialect</property>
    <property name="hibernate.hbm2ddl.auto">update</property>
    <property name="show_sql">true</property>

    <mapping class="Student"/>
  </session-factory>
</hibernate-configuration>
