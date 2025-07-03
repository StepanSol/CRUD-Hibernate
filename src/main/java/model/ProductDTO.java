package model;

import java.math.BigDecimal;

public class ProductDTO {
    private int id;
    private String name;
    private String formattedPrice;

    public ProductDTO(int id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.formattedPrice = String.format("%.2f руб.", price);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    @Override
    public String toString(){
        return String.format("ID: %d | Название: %s | Цена: %s", id, name, formattedPrice);
    }
}
