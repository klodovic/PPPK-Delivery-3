/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.viewmodel;

import hr.algebra.model.Shoes;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Next Design
 */
public class ShoesViewModel {
    
    private final Shoes shoes;

    public ShoesViewModel(Shoes shoes) {
        if (shoes  == null) {
            shoes = new Shoes(0, "", 0);
        }
        this.shoes = shoes;
    }

    public Shoes getShoes() {
        return shoes;
    }
    
    public IntegerProperty getIdShoesProperty() {
        return new SimpleIntegerProperty(shoes.getIDShoes());
    }

    public StringProperty getBrandProperty() {
        return new SimpleStringProperty(shoes.getBrand());
    }

    public IntegerProperty getSizeProperty() {
        return new SimpleIntegerProperty(shoes.getSize());
    }
    
    public IntegerProperty getPersonIdProperty() {
        return new SimpleIntegerProperty(shoes.getPersonID().getIDPerson());
    }

    public ObjectProperty<byte[]> getShoesPictureProperty() {
        return new SimpleObjectProperty<>(shoes.getShoesPicture());
    }
    
}
