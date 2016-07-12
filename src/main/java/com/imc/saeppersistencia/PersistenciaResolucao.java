/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imc.saeppersistencia;

import br.ufg.inf.es.saep.sandbox.dominio.Atributo;
import br.ufg.inf.es.saep.sandbox.dominio.Regra;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.ResolucaoRepository;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matheus
 */
public class PersistenciaResolucao implements ResolucaoRepository {

    MongoClient mongoClient = new MongoClient();
    DB db = mongoClient.getDB("saep");
    DBCollection collResolucao = db.getCollection("Resoluções");
    DBCollection collTipo = db.getCollection("Tipos");

    /**
     *
     */
    public void mostrarTudo() {
        DBCursor cursor = collTipo.find();
        while (cursor.hasNext()) {
            //collResolucao.remove(cursor.next());
            System.out.println(cursor.next());
        }
    }

    /**
     *
     * @param string
     * @return
     */
    @Override
    public Resolucao byId(String string) {
        BasicDBObject query = new BasicDBObject("id", string);
        DBCursor cursor = collResolucao.find(query);
        Resolucao resolucao = null;
        while (cursor.hasNext()) {
            DBObject doc = cursor.next();
            String id = (String) doc.get("id");
            String nome = (String) doc.get("nome");
            String descricao = (String) doc.get("descricao");
            String dataAprovacaoTemp = (String) doc.get("dataAprovacao");
            SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
            Date dataAprovacao = null;
            try {
                dataAprovacao = dt.parse(dataAprovacaoTemp);
            } catch (ParseException ex) {
                Logger.getLogger(PersistenciaResolucao.class.getName()).log(Level.SEVERE, null, ex);
            }
            List<Regra> regras = (ArrayList<Regra>) doc.get("regras");
            Resolucao resolucaoTemp = new Resolucao(id, nome, descricao, dataAprovacao, regras);
            System.out.println(resolucaoTemp.getDataAprovacao());
            resolucao = resolucaoTemp;
        }
        cursor.close();
        return resolucao;
    }

    /**
     *
     * @param rslc
     * @return
     */
    @Override
    public String persiste(Resolucao rslc) {
        BasicDBObject query = new BasicDBObject("id", rslc.getId());
        DBCursor cursor = collResolucao.find(query);
        boolean pode;
        if (cursor.hasNext() == false){
            pode = true;
        }else{
            pode = false;
        }
        if (pode == true) {
            Gson teste = new Gson();
            String json = teste.toJson(rslc);
            SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
            DBObject dbObject = (DBObject) JSON.parse(json);
            collResolucao.insert(dbObject);
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.append("$set", new BasicDBObject().append("dataAprovacao", dt.format(rslc.getDataAprovacao())));
            collResolucao.update(dbObject, newDocument);
            String mensagem = "Resolução salva com sucesso!";
            cursor.close();
            return mensagem;
        }else{
            String mensagem = "Não foi possivel salvar a resolução, pois ja existe uma com esse Id";
            cursor.close();
            return mensagem;
        }
    }

    /**
     *
     * @param string
     * @return
     */
    @Override
    public boolean remove(String string) {
        BasicDBObject query = new BasicDBObject("id", string);
        DBCursor cursor = collResolucao.find(query);
        try {
            while (cursor.hasNext()) {
                DBObject doc = cursor.next();
                collResolucao.remove(doc);
            }
        } finally {
            cursor.close();
        }
        return true;
    }

    /**
     *
     * @return
     */
    @Override
    public List<String> resolucoes() {
        DBCursor cursor = collResolucao.find();
        List<String> resolucoes = new ArrayList<>();
        while (cursor.hasNext()) {
            DBObject doc = cursor.next();
            resolucoes.add((String)doc.get("id"));
        }
        return resolucoes;
    }

    /**
     *
     * @param tipo
     */
    @Override
    public void persisteTipo(Tipo tipo) {
        BasicDBObject query = new BasicDBObject("id", tipo.getId());
        DBCursor cursor = collTipo.find(query);
        boolean pode;
        if (cursor.hasNext() == false){
            pode = true;
        }else{
            pode = false;
        }
        if (pode == true) {
            Gson teste = new Gson();
            String json = teste.toJson(tipo);
            DBObject dbObject = (DBObject) JSON.parse(json);
            collTipo.insert(dbObject);
            cursor.close();
        }else{
            cursor.close();
        }
    }

    /**
     *
     * @param string
     */
    @Override
    public void removeTipo(String string) {
        BasicDBObject query = new BasicDBObject("id", string);
        DBCursor cursor = collTipo.find(query);
        try {
            while (cursor.hasNext()) {
                DBObject doc = cursor.next();
                collTipo.remove(doc);
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
    public Tipo tipoPeloCodigo(String string) {
        BasicDBObject query = new BasicDBObject("id", string);
        DBCursor cursor = collTipo.find(query);
        Tipo tipo = null;
        while (cursor.hasNext()) {
            DBObject doc = cursor.next();
            String id = (String) doc.get("id");
            String nome = (String) doc.get("nome");
            String descricao = (String) doc.get("descricao");
            Set<Atributo> atributos = (HashSet<Atributo>) doc.get("atributos");
            Tipo tipoTemp = new Tipo(id, nome, descricao, atributos);
            tipo = tipoTemp;
        }
        cursor.close();
        return tipo;
    }

    /**
     *
     * @param string
     * @return
     */
    @Override
    public List<Tipo> tiposPeloNome(String string) {
        DBCursor cursor = collResolucao.find();
        List<Tipo> tipos = new ArrayList<>();
        while (cursor.hasNext()) {
            DBObject doc = cursor.next();
            tipos.add((Tipo) doc);
        }
        return tipos;
    }

}
