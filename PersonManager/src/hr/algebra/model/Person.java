/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import hr.algebra.dao.sql.HibernateFactory;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Next Design
 */
@Entity
@Table(name = "Person")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = HibernateFactory.SELECT_PEOPLE, query = "SELECT p FROM Person p")
    , @NamedQuery(name = "Person.findByIDPerson", query = "SELECT p FROM Person p WHERE p.iDPerson = :iDPerson")
    , @NamedQuery(name = "Person.findByFirstName", query = "SELECT p FROM Person p WHERE p.firstName = :firstName")
    , @NamedQuery(name = "Person.findByLastName", query = "SELECT p FROM Person p WHERE p.lastName = :lastName")
    , @NamedQuery(name = "Person.findByAge", query = "SELECT p FROM Person p WHERE p.age = :age")
    , @NamedQuery(name = "Person.findByEmail", query = "SELECT p FROM Person p WHERE p.email = :email")})
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDPerson")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer iDPerson;
    @Basic(optional = false)
    @Column(name = "FirstName")
    private String firstName;
    @Basic(optional = false)
    @Column(name = "LastName")
    private String lastName;
    @Basic(optional = false)
    @Column(name = "Age")
    private int age;
    @Basic(optional = false)
    @Column(name = "Email")
    private String email;
    @Lob
    @Column(name = "Picture")
    private byte[] picture;
    @OneToMany(mappedBy = "personID")
    private Collection<Shoes> shoesCollection;

    public Person() {
    }

    public Person(Integer iDPerson) {
        this.iDPerson = iDPerson;
    }

    public Person(Integer iDPerson, String firstName, String lastName, int age, String email) {
        this.iDPerson = iDPerson;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
    }

    public Integer getIDPerson() {
        return iDPerson;
    }

    public void setIDPerson(Integer iDPerson) {
        this.iDPerson = iDPerson;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    @XmlTransient
    public Collection<Shoes> getShoesCollection() {
        return shoesCollection;
    }

    public void setShoesCollection(Collection<Shoes> shoesCollection) {
        this.shoesCollection = shoesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDPerson != null ? iDPerson.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.iDPerson == null && other.iDPerson != null) || (this.iDPerson != null && !this.iDPerson.equals(other.iDPerson))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hr.algebra.model.Person[ iDPerson=" + iDPerson + " ]";
    }
    
    public Person(Person data) {
        updateData(data);
    }

    public void updateData(Person data) {
        firstName = data.firstName;
        lastName = data.lastName;
        age = data.age;
        email = data.email;
        picture = data.picture;
    }
    
}