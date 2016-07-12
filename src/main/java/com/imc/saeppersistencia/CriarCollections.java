package com.imc.saeppersistencia;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

/**
 *
 * @author Matheus
 */
public class CriarCollections {

    private String nome;
    private MongoClient mongoClient = new MongoClient();
    private DB db = mongoClient.getDB("saep");
    private DBCollection collPersist; 

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
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public DBCollection getColl() {
        return collPersist;
    }
    
}
