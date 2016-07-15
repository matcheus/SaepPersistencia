/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RepositoryTestSuite;

import br.ufg.inf.es.saep.sandbox.saeppersistencia.PersistenciaParecerTest;
import br.ufg.inf.es.saep.sandbox.saeppersistencia.PersistenciaResolucaoTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Matheus
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    PersistenciaResolucaoTest.class,
    PersistenciaParecerTest.class})

public class RepositoryTestSuite {
    
}
