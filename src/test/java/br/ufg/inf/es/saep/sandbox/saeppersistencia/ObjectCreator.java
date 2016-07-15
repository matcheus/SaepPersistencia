package br.ufg.inf.es.saep.sandbox.saeppersistencia;

import br.ufg.inf.es.saep.sandbox.dominio.Atributo;
import br.ufg.inf.es.saep.sandbox.dominio.Avaliavel;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import br.ufg.inf.es.saep.sandbox.dominio.Regra;
import br.ufg.inf.es.saep.sandbox.dominio.Relato;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
import br.ufg.inf.es.saep.sandbox.dominio.Valor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author Matheus
 */
public class ObjectCreator {

    public static Parecer criarParecer(String identificador) {
        List<String> radocsIDs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            String radocId = UUID.randomUUID().toString();
            radocsIDs.add(radocId);
        }
        List<Pontuacao> pontuacoes = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            String atributo = UUID.randomUUID().toString();
            pontuacoes.add(criarPontuacao(atributo));
        }
        List<Nota> notas = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            String atributo = UUID.randomUUID().toString();
            notas.add(criarNota(atributo));
        }
        return new Parecer(identificador, "resolucao", radocsIDs, pontuacoes, "fundamentacao", notas);
    }

    public static Pontuacao criarPontuacao(String atributo) {
        Valor valor = new Valor(atributo);
        return new Pontuacao(atributo, valor);
    }

    public static Nota criarNota(String atributo) {
        Valor valor = new Valor(atributo);
        Avaliavel original = new Pontuacao(atributo, valor);
        Valor valorBoole = new Valor(true);
        Map<String, Valor> map = new HashMap<>();
        map.put("Chave", valorBoole);
        Avaliavel destino = new Relato(atributo, map);
        return new Nota(original, destino, "descrição da nota");
    }

    public static Nota criarNota(Pontuacao pontuacao) {
        Avaliavel original = pontuacao;
        Valor valorBoole = new Valor(true);
        Map<String, Valor> map = new HashMap<>();
        map.put("Chave", valorBoole);
        String atributo = UUID.randomUUID().toString();
        Avaliavel destino = new Relato(atributo, map);
        return new Nota(original, destino, "descrição da nota");
    }

    public static Resolucao criaResolucao(String id) throws ParseException {
        String data = "15-12-1995";
        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
        Date dataAprovacao = dt.parse(data);
        return new Resolucao(id, "123", "Resolução", dataAprovacao, criarRegra());
    }

    public static List<Regra> criarRegra() {
        List<Regra> regras = new ArrayList<>();
        int tipo = 3;
        String descricao = "descricao";
        float valorMaximo = 10;
        float valorMinimo = 0;
        String variavel = "variavel";
        String expressao = "expressao";
        String entao = "..então";
        String senao = "..senão";
        String tipoRelato = "relatoTipo";
        int pontosPorItem = 1;
        List<String> dependeDe = new ArrayList<>();
        dependeDe.add("Algo");
        regras.add(new Regra(variavel, tipo, descricao, valorMaximo, valorMinimo, expressao, entao, senao, tipoRelato, pontosPorItem, dependeDe));
        return regras;
    }

    public static Radoc criarRadoc(String id) {
        List<Relato> relatos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Map<String, Valor> valores = new HashMap<>();
            for (int j = 0; j < 5; j++) {
                String tipoValor = UUID.randomUUID().toString();
                Valor valor = new Valor(j + 1);
                valores.put(tipoValor, valor);
            }
            Relato relato = new Relato(UUID.randomUUID().toString(), valores);
            relatos.add(relato);
        }
        return new Radoc(id, 2016, relatos);
    }

    public static Tipo criarTipo(String id, String nome) {
        Set<Atributo> atributos = new HashSet<>();
        for (int i = 0; i < 2; i++) {
            atributos.add(new Atributo(String.valueOf(i), "Descrição do atributo", 1));
        }
        String nomeFinal = nome + id;
        System.out.println(nomeFinal);
        return new Tipo(id, nomeFinal, "Descrição do tipo", atributos);
    }
}
