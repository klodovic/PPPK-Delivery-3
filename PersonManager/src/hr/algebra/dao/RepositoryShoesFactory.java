/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao;

import hr.algebra.dao.sql.HibernateShoesRepository;

/**
 *
 * @author Next Design
 */
public class RepositoryShoesFactory {

    public RepositoryShoesFactory() {
    }
    
    private final static ShoesRepository SHOES_REPOSITORY = new HibernateShoesRepository();

    public static ShoesRepository getShoesRepository() {
        return SHOES_REPOSITORY;
    }
    
    
}
