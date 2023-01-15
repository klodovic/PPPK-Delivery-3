/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao.sql;

import hr.algebra.dao.Repository;
import hr.algebra.model.Person;
import java.util.List;
import javax.persistence.EntityManager;


public class HibernateRepository implements Repository {

    @Override
    public void release() throws Exception {
        HibernateFactory.release();
    }
    
    

    @Override
    public int addPerson(Person data) throws Exception {
        try(EntityManagerWrapper emw = HibernateFactory.getEntityManger()){
            EntityManager em = emw.get();
            em.getTransaction().begin();
            Person person = new Person(data);
            em.persist(person);
            em.getTransaction().commit();
            return person.getIDPerson();
        }
    }

    @Override
    public void updatePerson(Person person) throws Exception {
        try(EntityManagerWrapper emw = HibernateFactory.getEntityManger()){
            EntityManager em = emw.get();
            em.find(Person.class, person.getIDPerson()).updateData(person);
        }
    }

    @Override
    public void deletePerson(Person person) throws Exception {
        try(EntityManagerWrapper emw = HibernateFactory.getEntityManger()){
            EntityManager em = emw.get();
            em.getTransaction().begin();
            em.remove(em.contains(person) ? person : em.merge(person));
            em.getTransaction().commit();
        }
    }

    @Override
    public Person getPerson(int idPerson) throws Exception {
        try(EntityManagerWrapper emw = HibernateFactory.getEntityManger()){
            EntityManager em = emw.get();
            return em.find(Person.class, idPerson);
        }
    }

    @Override
    public List<Person> getPeople() throws Exception {
        try(EntityManagerWrapper emw = HibernateFactory.getEntityManger()){
            EntityManager em = emw.get();
            return em.createNamedQuery(HibernateFactory.SELECT_PEOPLE).getResultList();
        }
    }
    
}
