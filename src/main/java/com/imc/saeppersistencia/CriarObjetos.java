package com.imc.saeppersistencia;

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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Matheus
 */
public class CriarObjetos {

    public Radoc criarRadoc(String id, int anoBase, List<Relato> relatos) {
        Radoc radoc = new Radoc(id, anoBase, relatos);
        return radoc;
    }

    public Parecer criarParecer(String id, String resolucaoId, List<String> radocsIds, List<Pontuacao> pontuacoes, String fundamentacao, List<Nota> notas) {
        Parecer parecer = new Parecer(id, resolucaoId, radocsIds, pontuacoes, fundamentacao, notas);
        return parecer;
    }
    
    public Parecer criarParecerVazio() {
        Parecer parecer = new Parecer();
        return parecer;
    }

    public Relato criarRelato(String tipo, Map<String, Valor> valores) {
        Relato relato = new Relato(tipo, valores);
        return relato;
    }

    public Nota criarNota(Avaliavel origem, Avaliavel destino, String justificativa) {
        Nota nota = new Nota(origem, destino, justificativa);
        return nota;
    }

    public Resolucao criarResolucao(String id, String nome, String descricao, Date dataAprovacao, List<Regra> regras) {
        Resolucao resolucao = new Resolucao(id, nome, descricao, dataAprovacao, regras);
        return resolucao;
    }

    public Tipo criarTipo(String id, String nome, String descricao, Set<Atributo> atributos) {
        Tipo tipo = new Tipo(id, nome, descricao, atributos);
        return tipo;
    }

    public Atributo criarAtributo(String nome, String descricao, int tipo) {
        Atributo atributo = new Atributo(nome, descricao, tipo);
        return atributo;
    }

    /*public AvaliadorService criarAvaliadorService() {

    }

    public Grupo criarGrupo() {

    }*/

    public Pontuacao criarPontuacao(String nome, Valor valor) {
        Pontuacao pontuacao = new Pontuacao(nome, valor);
        return pontuacao;
    }

}
