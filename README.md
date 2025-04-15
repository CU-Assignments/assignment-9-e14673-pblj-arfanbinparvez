[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/pwN1GOTN)
Experiment-9
Create Java applications using Spring and Hibernate for dependency injection, CRUD operations, and transaction management.


Easy Level:
Create a simple Spring application that demonstrates Dependency Injection (DI) using Java-based configuration instead of XML. Define a Student class that depends on a Course class. Use Spring’s @Configuration and @Bean annotations to inject dependencies.

Requirements:
1.	Define a Course class with attributes courseName and duration.
2.	Define a Student class with attributes name and a reference to Course.
3.	Use Java-based configuration (@Configuration and @Bean) to configure the beans.
4.	Load the Spring context in the main method and print student details.

Medium Level:
Develop a Hibernate-based application to perform CRUD (Create, Read, Update, Delete) operations on a Student entity using Hibernate ORM with MySQL.
Requirements:
1.	Configure Hibernate using hibernate.cfg.xml.
2.	Create an Entity class (Student.java) with attributes: id, name, and age.
3.	Implement Hibernate SessionFactory to perform CRUD operations.
4.	Test the CRUD functionality with sample data.
Hard Level:
Develop a Spring-based application integrated with Hibernate to manage transactions. Create a banking system where users can transfer money between accounts, ensuring transaction consistency.
Requirements:
1.	Use Spring configuration with Hibernate ORM.
2.	Implement two entity classes (Account.java and Transaction.java).
3.	Use Hibernate Transaction Management to ensure atomic operations.
4.	If a transaction fails, rollback should occur.
5.	Demonstrate successful and failed transactions.

