/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao;

import hr.algebra.model.Shoes;
import java.util.List;

/**
 *
 * @author Next Design
 */
public interface ShoesRepository {
    
    int addShoes(Shoes data) throws Exception;
    void updateShoes(Shoes shoes) throws Exception;
    void deleteShoes(Shoes shoes) throws Exception;
    Shoes getShoes(int idShoes) throws Exception;
    List<Shoes> getAllShoes(int idPerson) throws Exception;
    
    default void release() throws Exception {};
}
