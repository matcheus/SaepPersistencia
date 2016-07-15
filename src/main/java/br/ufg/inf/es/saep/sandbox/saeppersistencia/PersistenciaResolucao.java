package br.ufg.inf.es.saep.sandbox.saeppersistencia;

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
import java.util.List;
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
     * Recupera a instância de {@code Resolucao} correspondente ao
     * identificador.
     *
     * @param id O identificador único da resolução a ser recuperada.
     *
     * @return {@code Resolucao} identificada por {@code id}. O retorno
     * {@code null} indica que não existe resolução com o identificador
     * fornecido.
     *
     * @see #persiste(Resolucao)
     */
    @Override
    public Resolucao byId(String id) {
        BasicDBObject query = new BasicDBObject("id", id);
        DBCursor cursor = collResolucao.find(query);
        Resolucao resolucao = null;
        while (cursor.hasNext()) {
            DBObject doc = cursor.next();
            String idRadoc = (String) doc.get("id");
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
            Resolucao resolucaoTemp = new Resolucao(idRadoc, nome, descricao, dataAprovacao, regras);
            resolucao = resolucaoTemp;
        }
        cursor.close();
        return resolucao;
    }

    /**
     * Persiste uma resolução.
     *
     * @throws CampoExigidoNaoFornecido Caso o identificador não seja fornecido.
     *
     * @throws IdentificadorExistente Caso uma resolução com identificador igual
     * àquele fornecido já exista.
     *
     * @param resolucao A resolução a ser persistida.
     *
     * @return O identificador único da resolução, conforme fornecido em
     * propriedade do objeto fornecido. Observe que o método retorna
     * {@code null} para indicar que a operação não foi realizada de forma
     * satisfatória, possivelmente por já existir resolução com identificador
     * semelhante.
     *
     * @see #byId(String)
     * @see #remove(String)
     */
    @Override
    public String persiste(Resolucao resolucao) {
        BasicDBObject query = new BasicDBObject("id", resolucao.getId());
        DBCursor cursor = collResolucao.find(query);
        boolean pode;
        if (cursor.hasNext() == false) {
            pode = true;
        } else {
            pode = false;
        }
        if (pode == true) {
            Gson gson = new Gson();
            String json = gson.toJson(resolucao);
            SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
            DBObject dbObject = (DBObject) JSON.parse(json);
            collResolucao.insert(dbObject);
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.append("$set", new BasicDBObject().append("dataAprovacao", dt.format(resolucao.getDataAprovacao())));
            collResolucao.update(dbObject, newDocument);
            String mensagem = resolucao.getId();
            cursor.close();
            return mensagem;
        } else {
            String mensagem = resolucao.getId();
            cursor.close();
            return mensagem;
        }
    }

    /**
     * Remove a resolução com o identificador fornecido.
     *
     * @see #persiste(Resolucao)
     *
     * @param identificador O identificador único da resolução a ser removida.
     *
     * @return O valor {@code true} se a operação foi executada de forma
     * satisfatória e {@code false}, caso contrário.
     */
    @Override
    public boolean remove(String identificador) {
        BasicDBObject query = new BasicDBObject("id", identificador);
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
     * Recupera a lista dos identificadores das resoluções disponíveis.
     *
     * @return Identificadores das resoluções disponíveis.
     */
    @Override
    public List<String> resolucoes() {
        DBCursor cursor = collResolucao.find();
        List<String> resolucoes = new ArrayList<>();
        while (cursor.hasNext()) {
            DBObject doc = cursor.next();
            resolucoes.add((String) doc.get("id"));
        }
        cursor.close();
        return resolucoes;
    }

    /**
     * Persiste o tipo fornecido.
     *
     * @throws IdentificadorExistente Caso o tipo já esteja persistido no
     * repositório.
     *
     * @param tipo O objeto a ser persistido.
     */
    @Override
    public void persisteTipo(Tipo tipo) {
        BasicDBObject query = new BasicDBObject("id", tipo.getId());
        DBCursor cursor = collTipo.find(query);
        boolean pode;
        if (cursor.hasNext() == false) {
            pode = true;
        } else {
            pode = false;
        }

        if (pode == true) {
            Gson gson = new Gson();
            String json = gson.toJson(tipo);
            DBObject dbObject = (DBObject) JSON.parse(json);
            collTipo.insert(dbObject);
            cursor.close();
        } else {
            cursor.close();
        }
    }

    /**
     * Remove o tipo.
     *
     * @throws ResolucaoUsaTipoException O tipo é empregado por pelo menos uma
     * resolução.
     *
     * @param codigo O identificador do tipo a ser removido.
     */
    @Override
    public void removeTipo(String codigo) {
        BasicDBObject query = new BasicDBObject("id", codigo);
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
     * Recupera o tipo com o código fornecido.
     *
     * @param codigo O código único do tipo.
     *
     * @return A instância de {@link Tipo} cujo código único é fornecido.
     * Retorna {@code null} caso não exista tipo com o código indicado.
     */
    @Override
    public Tipo tipoPeloCodigo(String codigo) {
        BasicDBObject query = new BasicDBObject("id", codigo);
        DBCursor cursor = collTipo.find(query);
        Gson gson = new Gson();
        Tipo tipo = null;
        while (cursor.hasNext()) {
            DBObject doc = cursor.next();
            String json = doc.toString();
            Tipo tipo2 = gson.fromJson(json, Tipo.class);
            tipo = tipo2;
        }
        cursor.close();
        return tipo;
    }

    /**
     * Recupera a lista de tipos cujos nomes são similares àquele fornecido. Um
     * nome é similar àquele do tipo caso contenha o argumento fornecido. Por
     * exemplo, para o nome "casa" temos que "asa" é similar.
     *
     * Um nome é dito similar se contém a sequência indicada.
     *
     * @param nome Sequência que será empregada para localizar tipos por nome.
     *
     * @return A coleção de tipos cujos nomes satisfazem um padrão de semelhança
     * com a sequência indicada.
     */
    @Override
    public List<Tipo> tiposPeloNome(String nome) {
        BasicDBObject query = new BasicDBObject("nome", nome);
        DBCursor cursor = collTipo.find(query);
        List<Tipo> tipos = new ArrayList<>();
        Gson gson = new Gson();
        while (cursor.hasNext()) {
            DBObject doc = cursor.next();
            String json = doc.toString();
            Tipo tipo = gson.fromJson(json, Tipo.class);
            tipos.add(tipo);
        }
        cursor.close();
        return tipos;
    }
}
