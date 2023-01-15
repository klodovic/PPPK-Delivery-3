/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import hr.algebra.dao.sql.HibernateFactory;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Next Design
 */
@Entity
@Table(name = "Shoes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = HibernateFactory.SELECT_SHOES, query = "SELECT s FROM Shoes s")
    , @NamedQuery(name = "Shoes.findByIDShoes", query = "SELECT s FROM Shoes s WHERE s.iDShoes = :iDShoes")
    , @NamedQuery(name = "Shoes.findByBrand", query = "SELECT s FROM Shoes s WHERE s.brand = :brand")
    , @NamedQuery(name = "Shoes.findBySize", query = "SELECT s FROM Shoes s WHERE s.size = :size")})
public class Shoes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDShoes")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer iDShoes;
    @Basic(optional = false)
    @Column(name = "Brand")
    private String brand;
    @Basic(optional = false)
    @Column(name = "Size")
    private int size;
    @Lob
    @Column(name = "ShoesPicture")
    private byte[] shoesPicture;
    @JoinColumn(name = "PersonID", referencedColumnName = "IDPerson")
    @ManyToOne
    private Person personID;

    public Shoes() {
    }

    public Shoes(Integer iDShoes) {
        this.iDShoes = iDShoes;
    }

    public Shoes(Integer iDShoes, String brand, int size) {
        this.iDShoes = iDShoes;
        this.brand = brand;
        this.size = size;
    }

    public Integer getIDShoes() {
        return iDShoes;
    }

    public void setIDShoes(Integer iDShoes) {
        this.iDShoes = iDShoes;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getShoesPicture() {
        return shoesPicture;
    }

    public void setShoesPicture(byte[] shoesPicture) {
        this.shoesPicture = shoesPicture;
    }

    public Person getPersonID() {
        return personID;
    }

    public void setPersonID(Person personID) {
        this.personID = personID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDShoes != null ? iDShoes.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Shoes)) {
            return false;
        }
        Shoes other = (Shoes) object;
        if ((this.iDShoes == null && other.iDShoes != null) || (this.iDShoes != null && !this.iDShoes.equals(other.iDShoes))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hr.algebra.model.Shoes[ iDShoes=" + iDShoes + " ]";
    }
    
    public Shoes(Shoes data) {
        updateShoesData(data);
    }

    public void updateShoesData(Shoes data) {
        brand = data.brand;
        size = data.size;
        shoesPicture = data.shoesPicture;
        personID = data.personID;
    }
    
    
}
