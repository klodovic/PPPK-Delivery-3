/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao;

import hr.algebra.dao.sql.HibernateRepository;

/**
 *
 * @author Next Design
 */
public class RepositoryFactory {

    public RepositoryFactory() {
    }
    
    private final static Repository REPOSITORY = new HibernateRepository();

    public static Repository getRepository() {
        return REPOSITORY;
    }
    
    
}
