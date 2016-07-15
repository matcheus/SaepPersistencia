/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.es.saep.sandbox.saeppersistencia;

import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import java.util.UUID;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Matheus
 */
public class PersistenciaParecerTest {

    ObjectCreator objeto = new ObjectCreator();
    PersistenciaParecer persistencia = new PersistenciaParecer();
    String idParecer;

    public PersistenciaParecerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of adicionaNota method, of class PersistenciaParecer.
     */
    @Test
    public void testAdicionaNota() {
        System.out.println("persisteParecer");
        String id = UUID.randomUUID().toString();
        idParecer = id;
        Parecer parecer = objeto.criarParecer(id);
        persistencia.persisteParecer(parecer);
        Parecer parecerTeste = persistencia.byId(id);
        Assert.assertNotNull("O parecer não foi salvo", parecerTeste);
        String atributo = UUID.randomUUID().toString();
        Pontuacao pontuacao = objeto.criarPontuacao(atributo);
        Nota notaNova = objeto.criarNota(pontuacao);
        persistencia.adicionaNota(id, notaNova);
        Parecer parecerAtualizado = persistencia.byId(id);
        boolean tst;
        if (parecerAtualizado.getNotas().size() == 2) {
            tst = false;
        } 
        
        else {
            tst = true;
        }
        Assert.assertTrue("A nota não está sendo adicionada no parecer.", tst);
    }

    /**
     * Test of persisteParecer method, of class PersistenciaParecer.
     */
    @Test
    public void testPersisteParecer() {
        System.out.println("persisteParecer");
        String id = UUID.randomUUID().toString();
        idParecer = id;
        Parecer parecer = objeto.criarParecer(id);
        persistencia.persisteParecer(parecer);
        Parecer parecerTeste = persistencia.byId(id);
        Assert.assertNotNull("O parecer não foi salvo", parecerTeste);
    }

    /**
     * Test of atualizaFundamentacao method, of class PersistenciaParecer.
     */
    @Test
    public void testAtualizaFundamentacao() {
        System.out.println("atualizaFundamentacao");
        String id = UUID.randomUUID().toString();
        idParecer = id;
        Parecer parecer = objeto.criarParecer(id);
        persistencia.persisteParecer(parecer);
        Parecer parecerTeste = persistencia.byId(id);
        Assert.assertNotNull("O parecer não foi salvo", parecerTeste);
        String Fundamentacao = UUID.randomUUID().toString();
        persistencia.atualizaFundamentacao(id, Fundamentacao);
        Parecer parecerAtualizado = persistencia.byId(id);
        Assert.assertEquals("Fundamentação não foi atualizada.", Fundamentacao, parecerAtualizado.getFundamentacao());
    }

    /**
     * Test of removeParecer method, of class PersistenciaParecer.
     */
    @Test
    public void testRemoveParecer() {
        System.out.println("removeParecer");
        String id = UUID.randomUUID().toString();
        idParecer = id;
        Parecer parecer = objeto.criarParecer(id);
        persistencia.persisteParecer(parecer);
        Parecer parecerTeste = persistencia.byId(id);
        Assert.assertNotNull("O parecer não foi salvo", parecerTeste);
        persistencia.removeParecer(idParecer);
        Parecer parecerRemovido = persistencia.byId(idParecer);
        Assert.assertNull("Parecer não foi removido.", parecerRemovido);
    }

    /**
     * Test of persisteRadoc method, of class PersistenciaParecer.
     */
    @Test
    public void testPersisteRadoc() {
        System.out.println("persisteRadoc");
        String id = UUID.randomUUID().toString();
        Radoc radoc = objeto.criarRadoc(id);
        persistencia.persisteRadoc(radoc);
        Radoc radocSalvo = persistencia.radocById(id);
        Assert.assertNotNull("Radoc não foi salvo.", radocSalvo);
        persistencia.removeRadoc(id);
        Radoc radocRemovido = persistencia.radocById(id);
        Assert.assertNull("Radoc não foi removido com sucesso.", radocRemovido);
    }

    /**
     * Test of removeRadoc method, of class PersistenciaParecer.
     */
    @Test
    public void testRemoveRadoc() {
        System.out.println("removeRadoc");
        String id = UUID.randomUUID().toString();
        Radoc radoc = objeto.criarRadoc(id);
        persistencia.persisteRadoc(radoc);
        Radoc radocSalvo = persistencia.radocById(id);
        Assert.assertNotNull("Radoc não foi salvo.", radocSalvo);
        persistencia.removeRadoc(id);
        Radoc radocRemovido = persistencia.radocById(id);
        Assert.assertNull("Radoc não foi removido com sucesso.", radocRemovido);
    }

    /**
     * Test of removeNota method, of class PersistenciaParecer.
     */
    @Test
    public void testRemoveNota() {
        System.out.println("removeNota");
        String id = UUID.randomUUID().toString();
        idParecer = id;
        Parecer parecer = objeto.criarParecer(id);
        persistencia.persisteParecer(parecer);
        Parecer parecerTeste = persistencia.byId(id);
        Assert.assertNotNull("O parecer não foi salvo", parecerTeste);
        String atributo = UUID.randomUUID().toString();
        Pontuacao pontuacao = objeto.criarPontuacao(atributo);
        Nota notaNova = objeto.criarNota(pontuacao);
        persistencia.adicionaNota(id, notaNova);
        Parecer parecerAtualizado = persistencia.byId(id);
        boolean tst;
        if (parecerAtualizado.getNotas().size() == 2) {
            tst = false;
        } 
        
        else {
            tst = true;
        }
        Assert.assertTrue("A nota não está sendo adicionada no parecer.", tst);
        persistencia.removeNota(id, pontuacao);
        Parecer parecerAtualizado2 = persistencia.byId(id);
        boolean tst2;
        if (parecerAtualizado2.getNotas().size() == 3) {
            tst2 = false;
        } 
        
        else {
            tst2 = true;
        }
        Assert.assertTrue("Nota não está sendo removida.", tst2);
    }

}
