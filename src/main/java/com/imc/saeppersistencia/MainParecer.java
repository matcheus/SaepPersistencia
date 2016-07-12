/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imc.saeppersistencia;

import br.ufg.inf.es.saep.sandbox.dominio.Avaliavel;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import br.ufg.inf.es.saep.sandbox.dominio.Relato;
import br.ufg.inf.es.saep.sandbox.dominio.Valor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Matheus
 */
public class MainParecer {

    private static PersistenciaParecer persiste = new PersistenciaParecer();
    private static List<Relato> relatos = new ArrayList<>();
    private static List<String> radocs = new ArrayList<>();
    private static List<Pontuacao> pontuacoes = new ArrayList<>();

    public static void main(String[] args) {
        //salvarRelato();        
        //salvarRadoc();
        //salvarPontuacao();
        //salvarParecer();
        //adicionarNota();
        //removerRadoc();
        removerNota();
        //pesquisarRadocById();
        persiste.mostrarTudo();
    }

    private static Parecer pesquisarParecerById() {
        String id = "6";
        return persiste.byId(id);
    }

    private static void criarColl(String nome) {
        CriarCollections novaColl = new CriarCollections(nome);
        System.out.println("Collection " + novaColl.getColl().getName() + " criada com sucesso");
    }

    private static void salvarParecer() {
        List<Nota> notas = new ArrayList<>();
        String fundamentacao = "pq eu quis esse parecer";
        for (int i = 50; i < 52; i++) {
            String id = Integer.toString(i);
            persiste.persisteParecer(new Parecer(id, "Id da Resolução", radocs, pontuacoes, fundamentacao, notas));
        }
    }

    private static void salvarRadoc() {
        int anoBase = 2016;
        for (int i = 1080; i < 1083; i++) {
            String id = Integer.toString(i);
            Radoc radoc = new Radoc(id, anoBase, relatos);
            persiste.persisteRadoc(radoc);
            radocs.add(radoc.getId());
        }
    }

    private static void salvarRelato() {
        Map<String, Valor> valores = new HashMap(1);
        valores.put("ano", new Valor(2016));
        relatos.add(new Relato("relato1", valores));
        relatos.add(new Relato("relato2", valores));
        relatos.add(new Relato("relato3", valores));
    }

    private static void salvarPontuacao() {
        for (int i = 0; i < 3; i++) {
            String atributo = "Atributo" + i;
            pontuacoes.add(new Pontuacao(atributo, relatos.get(i).get("ano")));
        }
    }

    private static void adicionarNota() {
        String justificativa = "pq eu quis essa nota";
        Avaliavel origem = relatos.get(0);
        Avaliavel destino = relatos.get(1);
        persiste.adicionaNota("7", new Nota(origem, destino, justificativa));
    }

    private static Radoc pesquisarRadocById() {
        return persiste.radocById("1080");
    }
    
    private static void removerRadoc() {
        String id = "1044";
        persiste.removeRadoc(id);
    }
    
    private static void removerNota() {
        String id = "7";
        Map<String, Valor> valores = new HashMap(1);
        valores.put("ano", new Valor(2016));
        persiste.removeNota(id, new Relato("relato1", valores));
    }
}
