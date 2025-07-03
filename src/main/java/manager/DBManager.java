package manager;

import model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import model.ProductInputDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

public class DBManager {
    private Connection connection;
    private BufferedReader reader;
    private EntityManagerFactory emf;
    private EntityManager em;

    public DBManager(EntityManager em, EntityManagerFactory emf, BufferedReader reader, Connection connection) {
        this.em = em;
        this.emf = emf;
        this.reader = reader;
        this.connection = connection;
    }

    public void createTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS product(" +
                "id serial primary key, " +
                "name VARCHAR(40) not null, " +
                "price NUMERIC(10,2))");
    }

    public void displayTableJPA() {
        List<Product> products = em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
        if (products.isEmpty()){
            System.out.println("В базе данных нет товаров.");
        }else {
            products.stream()
                    .map(Product::toDTO)
                    .forEach(System.out::println);
        }
    }

    public void addProduct() throws IOException {
        ProductInputDTO dto = new ProductInputDTO();
        System.out.println("Введите название товара");
        dto.setName(reader.readLine());
        System.out.println("Введите стоимость");
        dto.setPrice(BigDecimal.valueOf(Double.parseDouble(reader.readLine())));

        Product newProduct = new Product(dto.getName(), dto.getPrice());

        em.getTransaction().begin();
        em.persist(newProduct);
        em.getTransaction().commit();
    }

    public void displayProductByID() throws IOException, SQLException {
        System.out.println("Введите ID товара для отображения");
        int id = Integer.parseInt(reader.readLine());
        Product required = em.find(Product.class, id);
        if (required == null){
            System.out.println("Товар с указанным ID не найден");
        }else {
            System.out.println(required.toDTO());
        }
    }

    public void removeProductByID() throws IOException, SQLException {
        System.out.println("Введите ID товара для удаления");
        int id = Integer.parseInt(reader.readLine());
        em.getTransaction().begin();
        Product required = em.find(Product.class, id);
        if (required == null){
            System.out.println("Товар с указанным ID не найден");
        }else {
            em.remove(required);
            em.getTransaction().commit();
            System.out.println("Товар с ID=" + id + " успешно удалён");
        }
    }

    public void updateProductByID() throws IOException {
        System.out.println("Введите ID товара для изменения его цены");
        int id = Integer.parseInt(reader.readLine());
        em.getTransaction().begin();
        Product required = em.find(Product.class, id);
        System.out.println("Введите новую цену");
        required.setPrice(BigDecimal.valueOf(Double.parseDouble(reader.readLine())));
        em.getTransaction().commit();
        System.out.println("Цена товара обновлена:");
        System.out.println(em.find(Product.class, id).toDTO());
    }
}

