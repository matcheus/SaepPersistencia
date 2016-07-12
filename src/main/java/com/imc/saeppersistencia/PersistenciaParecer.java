package com.imc.saeppersistencia;

import br.ufg.inf.es.saep.sandbox.dominio.Avaliavel;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.ParecerRepository;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import br.ufg.inf.es.saep.sandbox.dominio.Relato;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Matheus
 */
public class PersistenciaParecer implements ParecerRepository {

    MongoClient mongoClient = new MongoClient();
    DB db = mongoClient.getDB("saep");
    DBCollection collParecer = db.getCollection("Pareceres");
    DBCollection collRadoc = db.getCollection("Radocs");
    Gson gson = new GsonBuilder().registerTypeAdapter(Avaliavel.class, new InterfaceAdapter<Avaliavel>())
            .create();

    /**
     *
     * @param string
     * @param nota
     */
    @Override
    public void adicionaNota(String string, Nota nota) {
        String json = gson.toJson(nota);
        DBObject dbObject = (DBObject) JSON.parse(json);

        BasicDBObject listItem = new BasicDBObject().append("notas", dbObject);

        BasicDBObject query = new BasicDBObject("id", string);
        DBCursor cursor = collParecer.find(query);
        DBObject doc = cursor.next();
        DBObject updateQuery = new BasicDBObject("$push", listItem);
        collParecer.update(doc, updateQuery);
    }

    public void mostrarTudo() {
        DBCursor cursor = collParecer.find();
        while (cursor.hasNext()) {
            //collParecer.remove(cursor.next());
            System.out.println(cursor.next());
        }
    }

    /**
     *
     * @param prcr
     */
    @Override
    public void persisteParecer(Parecer prcr) {
        BasicDBObject query = new BasicDBObject("id", prcr.getId());
        DBCursor cursor = collParecer.find(query);
        boolean pode;
        if (cursor.hasNext() == false) {
            pode = true;
        } else {
            pode = false;
        }
        if (pode == true) {
            Gson teste = new Gson();
            String json = teste.toJson(prcr);
            DBObject dbObject = (DBObject) JSON.parse(json);
            collParecer.insert(dbObject);
            cursor.close();
            System.out.println("pode");
        } else {
            cursor.close();
            System.out.println("n pode");
        }

    }

    /**
     *
     * @param string
     * @param string1
     */
    @Override
    public void atualizaFundamentacao(String string, String string1) {
        BasicDBObject novaFundamentacao = new BasicDBObject();
        novaFundamentacao.append("$set", new BasicDBObject().append("fundamentacao", string1));
        BasicDBObject query = new BasicDBObject("id", string);
        DBCursor cursor = collParecer.find(query);
        System.out.println(cursor.next());
        BasicDBObject doc = new BasicDBObject().append("id", string);
        collParecer.update(doc, novaFundamentacao);
    }

    /**
     *
     * @param string
     * @return
     */
    @Override
    public Parecer byId(String string) {
        BasicDBObject query = new BasicDBObject("id", string);
        DBCursor cursor = collParecer.find(query);
        Parecer parecer = null;
        try {
            while (cursor.hasNext()) {
                DBObject doc = cursor.next();
                String json = doc.toString();
                Parecer parecerTemp = gson.fromJson(json, Parecer.class);;
                System.out.println(parecerTemp.getNotas());
                parecer = parecerTemp;
            }
        } finally {
            cursor.close();
        }
        return parecer;
    }

    /**
     *
     * @param string
     */
    @Override
    public void removeParecer(String string) {
        BasicDBObject query = new BasicDBObject("id", string);
        DBCursor cursor = collParecer.find(query);
        try {
            while (cursor.hasNext()) {
                DBObject doc = cursor.next();
                collParecer.remove(doc);
            }
        } finally {
            cursor.close();
        }
    }

    /**
     *
     * @param string
     * @return
     */
    @Override
    public Radoc radocById(String string) {
        BasicDBObject query = new BasicDBObject("id", string);
        DBCursor cursor = collRadoc.find(query);
        Radoc radoc = null;
        try {
            while (cursor.hasNext()) {
                DBObject doc = cursor.next();
                String id = (String) doc.get("id");
                int anoBase = (int) doc.get("anoBase");
                List<Relato> relatos = (ArrayList<Relato>) doc.get("relatos");
                Radoc radocTemp = new Radoc(id, anoBase, relatos);
                System.out.println(radocTemp.getId());
                radoc = radocTemp;
            }
        } finally {
            cursor.close();
        }
        return radoc;
    }

    /**
     *
     * @param radoc
     * @return
     */
    @Override
    public String persisteRadoc(Radoc radoc) {
        String json = gson.toJson(radoc);
        DBObject dbObject = (DBObject) JSON.parse(json);
        collRadoc.insert(dbObject);
        return radoc.getId();
    }

    /**
     *
     * @param string
     */
    @Override
    public void removeRadoc(String string) {
        BasicDBObject query = new BasicDBObject("id", string);
        DBCursor cursor = collRadoc.find(query);
        DBCursor cursorParecer = collParecer.find();
        boolean pode = true;
        try {
            while (cursorParecer.hasNext()) {
                DBObject doc = cursorParecer.next();
                List<String> radocsIds = (ArrayList<String>) doc.get("radocs");
                for (int i = 0; i < radocsIds.size(); i++) {
                    if (string.equals(radocsIds.get(i))) {
                        pode = false;
                        System.out.println("Radoc vinculado ao parecer " + doc.get("id"));
                        break;
                    }
                }
            }
            while (cursor.hasNext()) {

                if (pode == true) {
                    DBObject doc = cursor.next();
                    collRadoc.remove(doc);
                    System.out.println("Radoc " + doc.get("id") + " removido com sucesso");
                } else {
                    System.out.println("Radoc n pode ser removido");
                    break;
                }
            }
        } finally {
            cursor.close();
        }
    }

    /**
     *
     * @param string
     * @param avlvl
     */
    @Override
    public void removeNota(String string, Avaliavel avlvl) {
        BasicDBObject query = new BasicDBObject("id", string);
        DBCursor cursor = collParecer.find(query);
        try {
            while (cursor.hasNext()) {
                DBObject doc = cursor.next();
                String json = doc.toString();
                Parecer pare = gson.fromJson(json, Parecer.class);
                List<Nota> notas = pare.getNotas();
                System.out.println(notas.get(0));
                for (int i = 0; i < notas.size(); i++) {
                    Nota nota = notas.get(i);
                    if (avlvl == nota.getItemOriginal() || avlvl == nota.getItemNovo()) {
                        doc.removeField("notas");
                        notas.remove(i);
                        for (int p = 0; p < notas.size(); p++) {
                            adicionaNota(string, notas.get(p));
                        }
                        System.out.println("Nota removida com sucesso");
                    } else {
                        System.out.println("Nota n encontrada");
                        break;
                    }
                }

            }
        } finally {
            cursor.close();
        }
    }

}
