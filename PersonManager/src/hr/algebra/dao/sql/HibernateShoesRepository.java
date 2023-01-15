/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao.sql;

import hr.algebra.dao.ShoesRepository;
import hr.algebra.model.Person;
import hr.algebra.model.Shoes;
import java.util.List;
import javax.persistence.EntityManager;


public class HibernateShoesRepository implements ShoesRepository {

    @Override
    public void release() throws Exception {
        HibernateFactory.release();
    }
    
    

    @Override
    public int addShoes(Shoes data) throws Exception {
        try(EntityManagerWrapper emw = HibernateFactory.getEntityManger()){
            EntityManager em = emw.get();
            em.getTransaction().begin();
            Shoes shoes = new  Shoes(data);
            em.persist(shoes);
            em.getTransaction().commit();
            return shoes.getIDShoes();
        }
    }

    @Override
    public void updateShoes(Shoes shoes) throws Exception {
        try(EntityManagerWrapper emw = HibernateFactory.getEntityManger()){
            EntityManager em = emw.get();
            em.find(Shoes.class, shoes.getIDShoes()).updateShoesData(shoes);
        }
    }

    @Override
    public void deleteShoes(Shoes shoes) throws Exception {
        try(EntityManagerWrapper emw = HibernateFactory.getEntityManger()){
            EntityManager em = emw.get();
            em.getTransaction().begin();
            em.remove(em.contains(shoes) ? shoes : em.merge(shoes));
            em.getTransaction().commit();
        }
    }

    @Override
    public Shoes getShoes(int idShoes) throws Exception {
        try(EntityManagerWrapper emw = HibernateFactory.getEntityManger()){
            EntityManager em = emw.get();
            return em.find(Shoes.class, idShoes);
        }
    }

    @Override
    public List<Shoes> getAllShoes(int idPerson) throws Exception {
        try(EntityManagerWrapper emw = HibernateFactory.getEntityManger()){
            EntityManager em = emw.get();
            return em.createNamedQuery(HibernateFactory.SELECT_SHOES).getResultList();
        }
    }
    
}
