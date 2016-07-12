/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imc.saeppersistencia;

import br.ufg.inf.es.saep.sandbox.dominio.Atributo;
import br.ufg.inf.es.saep.sandbox.dominio.Regra;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Matheus
 */
public class MainResolucao {

    private static PersistenciaResolucao persiste = new PersistenciaResolucao();
    private static List<Regra> regras = new ArrayList<>();

    /**
     *
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
        //criarRegra();
        //salvarResolucao();
        //resolucaoById();
        //removerResolucao();
        //salvarTipo();
        //removerTipo();
        //tipoPorCodigo();
        //tiposPeloNome();
        //resolucoes();
    }

    private static void salvarResolucao() throws ParseException {
        String id = "1";
        String nome = "Meurelato";
        String descricao = "descricao";
        String data = "15-12-1995";
        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
        Date dataAprovacao = dt.parse(data);
        Resolucao resolucao = new Resolucao(id, nome, descricao, dataAprovacao, regras);
        persiste.persiste(resolucao);
    }

    private static void criarRegra() {
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
    }

    private static void resolucaoById() {
        String id = "1";
        persiste.byId(id);
    }

    private static void salvarTipo() {
        String id = "tipo1";
        String nome = "nomeTipo";
        String descricao = "descrevaTipo";
        Set<Atributo> atributos = new HashSet<Atributo>();
        atributos.add(new Atributo("nome", "descriçao", 1));
        persiste.persisteTipo(new Tipo(id, nome, descricao, atributos));
    }

    private static void removerResolucao() {
        String id = "1";
        persiste.remove(id);
    }

    private static void removerTipo() {
        String id = "tipo1";
        persiste.removeTipo(id);
    }

    private static void tipoPorCodigo() {
        String id = "tipo1";
        System.out.println(persiste.tipoPeloCodigo(id).getId());
    }

    private static void tiposPeloNome() {
        String nome = "nomeTipo";
        persiste.tiposPeloNome(nome);
    }

    private static void resolucoes() {
        persiste.resolucoes();
    }
}
