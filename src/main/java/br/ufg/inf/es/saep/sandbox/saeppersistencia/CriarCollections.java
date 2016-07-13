package br.ufg.inf.es.saep.sandbox.saeppersistencia;

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
     * O contrutor recebe o nome da nova Collection
     * Chama o método que cria a nova Collection no Banco de dados
     * @param nome
     */
    public CriarCollections(String nome) {
        this.nome = nome;
        criarColl();
    }
    
    /**
     * Cria uma nova Collection no BD
     * Inicializa a nova Collection
     * @param
     */
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
