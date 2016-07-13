/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufg.inf.es.saep.sandbox.saeppersistencia;

import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
import java.util.List;
import java.util.UUID;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Matheus
 */
public class PersistenciaResolucaoTest {
    
    ObjectCreator objeto = new ObjectCreator();
    
    public PersistenciaResolucaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of byId method, of class PersistenciaResolucao.
     */
    @Test
    public void testById() {
        System.out.println("byId");
        String string = "";
        PersistenciaResolucao instance = new PersistenciaResolucao();
        Resolucao expResult = null;
        Resolucao result = instance.byId(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of persiste method, of class PersistenciaResolucao.
     */
    @Test
    public void testPersiste() {
        System.out.println("persiste radoc");
        String id = UUID.randomUUID().toString();
        Parecer parecer = objeto.criarParecer(id);
        Resolucao rslc = null;
        PersistenciaResolucao instance = new PersistenciaResolucao();
        String expResult = "";
        String result = instance.persiste(rslc);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of remove method, of class PersistenciaResolucao.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
        String string = "";
        PersistenciaResolucao instance = new PersistenciaResolucao();
        boolean expResult = false;
        boolean result = instance.remove(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of resolucoes method, of class PersistenciaResolucao.
     */
    @Test
    public void testResolucoes() {
        System.out.println("resolucoes");
        PersistenciaResolucao instance = new PersistenciaResolucao();
        List<String> expResult = null;
        List<String> result = instance.resolucoes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of persisteTipo method, of class PersistenciaResolucao.
     */
    @Test
    public void testPersisteTipo() {
        System.out.println("persisteTipo");
        Tipo tipo = null;
        PersistenciaResolucao instance = new PersistenciaResolucao();
        instance.persisteTipo(tipo);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeTipo method, of class PersistenciaResolucao.
     */
    @Test
    public void testRemoveTipo() {
        System.out.println("removeTipo");
        String string = "";
        PersistenciaResolucao instance = new PersistenciaResolucao();
        instance.removeTipo(string);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of tipoPeloCodigo method, of class PersistenciaResolucao.
     */
    @Test
    public void testTipoPeloCodigo() {
        System.out.println("tipoPeloCodigo");
        String string = "";
        PersistenciaResolucao instance = new PersistenciaResolucao();
        Tipo expResult = null;
        Tipo result = instance.tipoPeloCodigo(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of tiposPeloNome method, of class PersistenciaResolucao.
     */
    @Test
    public void testTiposPeloNome() {
        System.out.println("tiposPeloNome");
        String string = "";
        PersistenciaResolucao instance = new PersistenciaResolucao();
        List<Tipo> expResult = null;
        List<Tipo> result = instance.tiposPeloNome(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
