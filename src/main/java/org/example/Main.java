package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import manager.DBManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {

        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost/postgres",
                "postgres", "postgres");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Map<String,String> props= new HashMap<>();
        props.put("jakarta.persistence.provider", "org.hibernate.jpa.HibernatePersistenceProvider");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyPersistenceUnit",
                props);
        EntityManager em = emf.createEntityManager();

        DBManager manager = new DBManager(em, emf, reader, connection);
        manager.createTable();

        System.out.println("Напишите в консоль номер действия: \n" +
                "1. Вывести список товаров \n" +
                "2. Добавление товара в базу данных \n" +
                "3. Вывод информации о товаре по id \n" +
                "4. Удаление товара по id \n" +
                "5. Обновление цены товара \n" +
                "Для завершения работы введите 0");


        while (true) {
            try {
                byte numberOfAction = Byte.parseByte(reader.readLine());
                if (numberOfAction == 1) {
                    manager.displayTableJPA();
                } else if (numberOfAction == 2) {
                    manager.addProduct();
                } else if (numberOfAction == 3) {
                    manager.displayProductByID();
                } else if (numberOfAction == 4) {
                    manager.removeProductByID();
                } else if (numberOfAction == 5) {
                    manager.updateProductByID();
                } else if (numberOfAction == 0) {
                    em.close();
                    break;
                } else {
                    System.out.println("Введите число от 0 до 5");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Введены неверные данные");
            }
        }
    }
}