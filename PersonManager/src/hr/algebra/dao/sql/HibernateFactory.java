/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao.sql;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Next Design
 */
public class HibernateFactory {
    
    public static final String SELECT_PEOPLE = "Person.findAll";
    public static final String SELECT_SHOES = "Shoes.findAll";

    public HibernateFactory() {
    }
    
    
    private static final String PERSISTENCE_UNIT = "PersonManagerPU";
    
    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    
    
    public static EntityManagerWrapper getEntityManger() {
        return new EntityManagerWrapper(EMF.createEntityManager());
    }
    
    
    public static void release() {
        EMF.close();
    }  
    
}
