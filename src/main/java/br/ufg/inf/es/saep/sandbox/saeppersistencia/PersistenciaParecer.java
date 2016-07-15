package br.ufg.inf.es.saep.sandbox.saeppersistencia;

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
     * Adiciona nota ao parecer. Caso a nota a ser acrescentada se refira a um
     * item {@link Avaliavel} para o qual jÃ¡ exista uma nota, entÃ£o a corrente
     * substitui a anterior.
     *
     * @throws IdentificadorDesconhecido Caso o identificador fornecido nÃ£o
     * identifique um parecer existente.
     *
     * @param id O identificador Ãºnico do parecer.
     *
     * @param nota A alteraÃ§Ã£o a ser acrescentada ao pareder.
     */
    @Override
    public void adicionaNota(String id, Nota nota) {
        String json = gson.toJson(nota);
        DBObject dbObject = (DBObject) JSON.parse(json);

        BasicDBObject listItem = new BasicDBObject().append("notas", dbObject);

        BasicDBObject query = new BasicDBObject("id", id);
        DBCursor cursor = collParecer.find(query);
        DBObject doc = cursor.next();
        DBObject updateQuery = new BasicDBObject("$push", listItem);
        collParecer.update(doc, updateQuery);
    }

    /**
     * Acrescenta o parecer ao repositÃ³rio.
     *
     * @throws IdentificadorExistente Caso o identificador seja empregado por
     * parecer existente (jÃ¡ persistido).
     *
     * @param parecer O parecer a ser persistido.
     *
     */
    @Override
    public void persisteParecer(Parecer parecer) {
        BasicDBObject query = new BasicDBObject("id", parecer.getId());
        DBCursor cursor = collParecer.find(query);
        boolean pode;
        if (cursor.hasNext() == false) {
            pode = true;
        } 
        
        else {
            pode = false;
        }

        if (pode == true) {
            String json = gson.toJson(parecer);
            DBObject dbObject = (DBObject) JSON.parse(json);
            collParecer.insert(dbObject);
            cursor.close();
        } 
        
        else {
            cursor.close();
        }
    }

    /**
     * Altera a fundamentaÃ§Ã£o do parecer.
     *
     * <p>
     * FundamentaÃ§Ã£o Ã© o texto propriamente dito do parecer. NÃ£o confunda com as
     * alteraÃ§Ãµes de valores (dados de relatos ou de pontuaÃ§Ãµes).
     *
     * <p>
     * ApÃ³s a chamada a esse mÃ©todo, o parecer alterado pode ser recuperado pelo
     * mÃ©todo {@link #byId(String)}. Observe que uma instÃ¢ncia disponÃ­vel antes
     * dessa chamada torna-se "invÃ¡lida".
     *
     * @throws IdentificadorDesconhecido Caso o identificador fornecido nÃ£o
     * identifique um parecer.
     *
     * @param parecer O identificador Ãºnico do parecer.
     * @param fundamentacao Novo texto da fundamentaÃ§Ã£o do parecer.
     */
    @Override
    public void atualizaFundamentacao(String parecer, String fundamentacao) {
        BasicDBObject novaFundamentacao = new BasicDBObject();
        novaFundamentacao.append("$set", new BasicDBObject().append("fundamentacao", fundamentacao));
        BasicDBObject query = new BasicDBObject("id", parecer);
        DBCursor cursor = collParecer.find(query);
        BasicDBObject doc = new BasicDBObject().append("id", parecer);
        collParecer.update(doc, novaFundamentacao);
        cursor.close();
    }

    /**
     * Recupera o parecer pelo identificador.
     *
     * @param id O identificador do parecer.
     *
     * @return O parecer recuperado ou o valor {@code null}, caso o
     * identificador nÃ£o defina um parecer.
     */
    @Override
    public Parecer byId(String id) {
        BasicDBObject query = new BasicDBObject("id", id);
        DBCursor cursor = collParecer.find(query);
        Parecer parecer = null;
        try {
            while (cursor.hasNext()) {
                DBObject doc = cursor.next();
                String json = doc.toString();
                Parecer parecerTemp = gson.fromJson(json, Parecer.class);;
                parecer = parecerTemp;
            }
        } finally {
            cursor.close();
        }
        return parecer;
    }

    /**
     * Remove o parecer.
     *
     * <p>
     * Se o identificador fornecido Ã© invÃ¡lido ou nÃ£o correspondente a um
     * parecer existente, nenhuma situaÃ§Ã£o excepcional Ã© gerada.
     *
     * @param id O identificador Ãºnico do parecer.
     */
    @Override
    public void removeParecer(String id) {
        BasicDBObject query = new BasicDBObject("id", id);
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
     * Recupera o RADOC identificado pelo argumento.
     *
     * @param identificador O identificador Ãºnico do RADOC.
     *
     * @return O {@code Radoc} correspondente ao identificador fornecido.
     */
    @Override
    public Radoc radocById(String identificador) {
        BasicDBObject query = new BasicDBObject("id", identificador);
        DBCursor cursor = collRadoc.find(query);
        Radoc radoc = null;
        try {
            while (cursor.hasNext()) {
                DBObject doc = cursor.next();
                String id = (String) doc.get("id");
                int anoBase = (int) doc.get("anoBase");
                List<Relato> relatos = (ArrayList<Relato>) doc.get("relatos");
                Radoc radocTemp = new Radoc(id, anoBase, relatos);
                radoc = radocTemp;
            }
        } finally {
            cursor.close();
        }
        return radoc;
    }

    /**
     * Conjunto de relatos de atividades e produtos associados a um docente.
     *
     * <p>
     * Um conjunto de relatos Ã© extraÃ­do de fonte externa de informaÃ§Ã£o. Uma
     * cÃ³pia Ã© mantida pelo SAEP para consistÃªncia de pareceres efetuados ao
     * longo do tempo. ConvÃ©m ressaltar que informaÃ§Ãµes desses relatÃ³rios podem
     * ser alteradas continuamente.
     *
     * @throws IdentificadorExistente Caso o identificador do objeto a ser
     * persistido seja empregado por RADOC existente.
     *
     * @param radoc O conjunto de relatos a ser persistido.
     *
     * @return O identificador Ãºnico do RADOC.
     */
    @Override
    public String persisteRadoc(Radoc radoc) {
        String json = gson.toJson(radoc);
        DBObject dbObject = (DBObject) JSON.parse(json);
        collRadoc.insert(dbObject);
        return radoc.getId();
    }

    /**
     * Remove o RADOC.
     *
     * <p>
     * ApÃ³s essa operaÃ§Ã£o o RADOC correspondente nÃ£o estarÃ¡ disponÃ­vel para
     * consulta.
     *
     * <p>
     * NÃ£o Ã© permitida a remoÃ§Ã£o de um RADOC para o qual hÃ¡ pelo menos um
     * parecer referenciando-o.
     *
     * @throws ExisteParecerReferenciandoRadoc Caso exista pelo menos um parecer
     * que faz referÃªncia para o RADOC cuja remoÃ§Ã£o foi requisitada.
     *
     * @param identificador O identificador do RADOC.
     */
    @Override
    public void removeRadoc(String identificador) {
        BasicDBObject query = new BasicDBObject("id", identificador);
        DBCursor cursor = collRadoc.find(query);
        DBCursor cursorParecer = collParecer.find();
        boolean pode = true;
        try {
            while (cursorParecer.hasNext()) {
                DBObject doc = cursorParecer.next();
                List<String> radocsIds = (ArrayList<String>) doc.get("radocs");
                for (int i = 0; i < radocsIds.size(); i++) {
                    if (identificador.equals(radocsIds.get(i))) {
                        pode = false;
                        break;
                    }
                }

            }
            cursorParecer.close();

            while (cursor.hasNext()) {

                if (pode == true) {
                    DBObject doc = cursor.next();
                    collRadoc.remove(doc);
                } 
                
                else {
                    break;
                }
            }
        } finally {
            cursor.close();
        }
    }

    /**
     * Remove a nota cujo item {@link Avaliavel} original Ã© fornedido.
     *
     * @param id O identificador Ãºnico do parecer.
     * @param original InstÃ¢ncia de {@link Avaliavel} que participa da
     * {@link Nota} a ser removida como origem.
     *
     */
    @Override
    public void removeNota(String id, Avaliavel original) {
        BasicDBObject query = new BasicDBObject("id", id);
        DBCursor cursor = collParecer.find(query);
        Pontuacao pont = (Pontuacao) original;
        try {
            while (cursor.hasNext()) {
                DBObject doc = cursor.next();
                String json = doc.toString();
                Parecer pare = gson.fromJson(json, Parecer.class);
                List<Nota> notas = new ArrayList();
                List<Pontuacao> pontuacoes = new ArrayList();
                for (int i = 0; i < pare.getNotas().size(); i++) {
                    notas.add(pare.getNotas().get(i));
                    pontuacoes.add((Pontuacao) notas.get(i).getItemOriginal());
                }
                for (int i = 0; i < notas.size(); i++) {
                    if (pont.getAtributo().equals(pontuacoes.get(i).getAtributo())) {
                        notas.remove(i);
                        json = gson.toJson(notas);
                        DBObject dbObject = (DBObject) JSON.parse(json);
                        BasicDBObject novaNotas = new BasicDBObject();
                        novaNotas.append("$set", new BasicDBObject().append("notas", dbObject));
                        collParecer.update(query, novaNotas);
                    }
                }
            }
        } finally {
            cursor.close();
        }
    }
}
