/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.es.saep.sandbox.saeppersistencia;

import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Matheus
 */
public class PersistenciaResolucaoTest {

    ObjectCreator objeto = new ObjectCreator();
    PersistenciaResolucao persistencia = new PersistenciaResolucao();

    public PersistenciaResolucaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of persiste method, of class PersistenciaResolucao.
     */
    @Test
    public void testPersiste() throws ParseException {
        System.out.println("persiste radoc");
        Resolucao resolucao = objeto.criaResolucao(UUID.randomUUID().toString());
        String id = persistencia.persiste(resolucao);
        Assert.assertNotNull("Resolução não foi salva.", id);
        Resolucao resolucaoSalva = persistencia.byId(id);
        Assert.assertNotNull("Resolução não foi encontrada.",
                resolucaoSalva);
        Assert.assertEquals("As resolução não está sendo salva.", resolucao,
                resolucaoSalva);
        boolean removido = persistencia.remove(id);
        Assert.assertTrue("Não foi possivel remover a resolução", removido);
    }

    /**
     * Test of remove method, of class PersistenciaResolucao.
     */
    @Test
    public void testRemove() throws ParseException {
        System.out.println("remove");
        Resolucao resolucao = objeto.criaResolucao(UUID.randomUUID().toString());
        String id = persistencia.persiste(resolucao);
        Assert.assertNotNull("Resolução não foi salva.", id);
        Resolucao resolucaoSalva = persistencia.byId(id);
        Assert.assertNotNull("Resolução não foi encontrada.",
                resolucaoSalva);
        Assert.assertEquals("As resolução não está sendo salva.", resolucao,
                resolucaoSalva);
        boolean removido = persistencia.remove(id);
        Assert.assertTrue("Não foi possivel remover a resolução", removido);
    }

    /**
     * Test of resolucoes method, of class PersistenciaResolucao.
     */
    @Test
    public void testResolucoes() throws ParseException {
        System.out.println("resolucoes");
        List<String> idsSalvos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Resolucao resolucao = objeto.criaResolucao(String.valueOf(i));
            String id = persistencia.persiste(resolucao);
            idsSalvos.add(id);
        }
    }

    /**
     * Test of persisteTipo method, of class PersistenciaResolucao.
     */
    @Test
    public void testPersisteTipo() {
        System.out.println("persisteTipo");
        String idTipo = UUID.randomUUID().toString();
        Tipo tipo = objeto.criarTipo(idTipo, "tipo");
        persistencia.persisteTipo(tipo);
        Tipo tipoSalvo = persistencia.tipoPeloCodigo(idTipo);
        Assert.assertNotNull("Tipo não foi encontrado", tipoSalvo);
        Assert.assertEquals("O tipo não está sendo salvo.", tipo, tipoSalvo);
        persistencia.removeTipo(idTipo);
        Tipo tipoRemovido = persistencia.tipoPeloCodigo(idTipo);
        Assert.assertNull("Tipo não foi removido com sucesso.", tipoRemovido);
    }

    /**
     * Test of tiposPeloNome method, of class PersistenciaResolucao.
     */
    @Test
    public void testTiposPeloNome() {
        System.out.println("tiposPeloNome");
        for (int i = 0; i < 5; i++) {
            String idTipo = UUID.randomUUID().toString();
            Tipo tipo = objeto.criarTipo(idTipo, "tipo");
            persistencia.persisteTipo(tipo);
        }
        List<Tipo> tipos = persistencia.tiposPeloNome("tipo");
        Assert.assertNotNull("Tipos não foram encontrados.", tipos);
        Assert.assertTrue("Não foram encontrados tipos.", tipos.size() > 0);
    }

}
