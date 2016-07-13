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
     * Adiciona nota ao parecer. Caso a nota a ser acrescentada
     * se refira a um item {@link Avaliavel} para o qual já
     * exista uma nota, então a corrente substitui a anterior.
     *
     * @throws IdentificadorDesconhecido Caso o identificador
     * fornecido não identifique um parecer existente.
     *
     * @param id O identificador único do parecer.
     *
     * @param nota A alteração a ser acrescentada ao
     * pareder.
     */
    @Override
    public void adicionaNota(String string, Nota nota) {
        String json = gson.toJson(nota);
        DBObject dbObject = (DBObject) JSON.parse(json);

        BasicDBObject listItem = new BasicDBObject().append("notas", dbObject);

        BasicDBObject query = new BasicDBObject("id", string);
        DBCursor cursor = collParecer.find(query);
        DBObject doc = cursor.next();
        //DBObject updateQuery = new BasicDBObject("$push", listItem);
        //collParecer.update(doc, updateQuery);
    }

    /**
     * Acrescenta o parecer ao repositório.
     *
     * @throws IdentificadorExistente Caso o
     * identificador seja empregado por parecer
     * existente (já persistido).
     *
     * @param parecer O parecer a ser persistido.
     *
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
            String json = gson.toJson(prcr);
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
     * Altera a fundamentação do parecer.
     *
     * <p>Fundamentação é o texto propriamente dito do
     * parecer. Não confunda com as alterações de
     * valores (dados de relatos ou de pontuações).
     *
     * <p>Após a chamada a esse método, o parecer alterado
     * pode ser recuperado pelo método {@link #byId(String)}.
     * Observe que uma instância disponível antes dessa chamada
     * torna-se "inválida".
     *
     * @throws IdentificadorDesconhecido Caso o identificador
     * fornecido não identifique um parecer.
     *
     * @param parecer O identificador único do parecer.
     * @param fundamentacao Novo texto da fundamentação do parecer.
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
     * Recupera o parecer pelo identificador.
     *
     * @param id O identificador do parecer.
     *
     * @return O parecer recuperado ou o valor {@code null},
     * caso o identificador não defina um parecer.
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
     * Remove o parecer.
     *
     * <p>Se o identificador fornecido é inválido
     * ou não correspondente a um parecer existente,
     * nenhuma situação excepcional é gerada.
     *
     * @param id O identificador único do parecer.
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
     * Recupera o RADOC identificado pelo argumento.
     *
     * @param identificador O identificador único do
     *                      RADOC.
     *
     * @return O {@code Radoc} correspondente ao
     * identificador fornecido.
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
     * Conjunto de relatos de atividades e produtos
     * associados a um docente.
     *
     * <p>Um conjunto de relatos é extraído de fonte
     * externa de informação. Uma cópia é mantida pelo
     * SAEP para consistência de pareceres efetuados ao
     * longo do tempo. Convém ressaltar que informações
     * desses relatórios podem ser alteradas continuamente.
     *
     * @throws IdentificadorExistente Caso o identificador
     * do objeto a ser persistido seja empregado por
     * RADOC existente.
     *
     * @param radoc O conjunto de relatos a ser persistido.
     *
     * @return O identificador único do RADOC.
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
     * <p>Após essa operação o RADOC correspondente não
     * estará disponível para consulta.
     *
     * <p>Não é permitida a remoção de um RADOC para o qual
     * há pelo menos um parecer referenciando-o.
     *
     * @throws ExisteParecerReferenciandoRadoc Caso exista pelo
     * menos um parecer que faz referência para o RADOC cuja
     * remoção foi requisitada.
     *
     * @param identificador O identificador do RADOC.
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
                }
                
                else {
                    System.out.println("Radoc n pode ser removido");
                    break;
                }
            }
        } finally {
            cursor.close();
        }
    }

    /**
     * Remove a nota cujo item {@link Avaliavel} original é
     * fornedido.
     *
     * @param id O identificador único do parecer.
     * @param original Instância de {@link Avaliavel} que participa
     *                 da {@link Nota} a ser removida como origem.
     *
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
                for (int i = 0; i < notas.size(); i++) {
                    Nota nota = notas.get(i);
                    if (avlvl.equals(nota.getItemOriginal())) {
                        doc.removeField("notas");
                        notas.remove(i);
                        for (int p = 0; p < notas.size(); p++) {
                            adicionaNota(string, notas.get(p));
                        }
                        
                    } else {
                        break;
                    }
                }

            }
        } finally {
            cursor.close();
        }
    }

    public void mostrarTudo() {
        DBCursor cursor = collParecer.find();
        while (cursor.hasNext()) {
            //collParecer.remove(cursor.next());
            System.out.println(cursor.next());
        }
    }
}
