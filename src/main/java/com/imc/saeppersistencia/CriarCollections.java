package com.imc.saeppersistencia;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

/**
 *
 * @author Matheus
 */
public class CriarCollections {

    private String nome;
    private MongoClient mongoClient = new MongoClient();
    private DB db = mongoClient.getDB("saep");
    private DBCollection collPersist; 

    /**
     *
     * @param nome
     */
    public CriarCollections(String nome) {
        this.nome = nome;
        criarColl();
    }

    private void criarColl(){
        DBCollection coll = db.getCollection(nome);
        BasicDBObject dbName = new BasicDBObject("Collection", nome);
        coll.insert(dbName);
        collPersist = coll;
    }
    
    /**
     *
     * @return
     */
    public String getNome() {
        return nome;
    }

    /**
     *
     * @param nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     *
     * @return
     */
    public DBCollection getColl() {
        return collPersist;
    }
    
}
