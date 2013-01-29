/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufjf.mmc.jynacore.tests.metamodel;

import br.ufjf.mmc.jynacore.JynaSimulationData;
import br.ufjf.mmc.jynacore.JynaSimulationProfile;
import br.ufjf.mmc.jynacore.JynaValued;
import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultNameExpression;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationData;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationProfile;
import br.ufjf.mmc.jynacore.metamodel.MetaModel;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassAuxiliary;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModel;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelClassAuxiliary;
import br.ufjf.mmc.jynacore.metamodel.instance.MetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.impl.DefaultMetaModelInstance;
import br.ufjf.mmc.jynacore.metamodel.simulator.MetaModelInstanceSimulation;
import br.ufjf.mmc.jynacore.metamodel.simulator.MetaModelInstanceSimulationMethod;
import br.ufjf.mmc.jynacore.metamodel.simulator.impl.DefaultMetaModelInstanceEulerMethod;
import br.ufjf.mmc.jynacore.metamodel.simulator.impl.DefaultMetaModelInstanceSimulation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author igor
 */
public class SystemConstantsTest {
   private static final String cteDtName = "DT";
   private static final String cteTimeName = "TIME";
   private static final String cteStepName = "STEP";
   private static final String TIME_STEP = "_TIME_STEP_";
   private static final String STEP = "_STEP_";
   public static final String TIME = "_TIME_";

   public SystemConstantsTest() {
   }

   @BeforeClass
   public static void setUpClass() {
   }

   @AfterClass
   public static void tearDownClass() {
   }

   @Before
   public void setUp() {
   }

   @After
   public void tearDown() {
   }

   // TODO add test methods here.
   // The methods must be annotated with annotation @Test. For example:
   //
   // @Test
   // public void hello() {}
   @Test
   public void testDtConstant() throws Exception {
      MetaModel metaModel = new DefaultMetaModel();
      metaModel.setName("Auxiliary only model");
      MetaModelClass mmClass = new DefaultMetaModelClass();
      mmClass.setName("TestClass");

      MetaModelClassAuxiliary mmAuxTimeStep = new DefaultMetaModelClassAuxiliary();
      mmAuxTimeStep.setName(cteDtName);
      Expression expression = new DefaultNameExpression(TIME_STEP);
      mmAuxTimeStep.setExpression(expression);
      mmClass.put(mmAuxTimeStep);

      MetaModelClassAuxiliary mmAuxTime = new DefaultMetaModelClassAuxiliary();
      mmAuxTime.setName(cteTimeName);
      Expression expression2 = new DefaultNameExpression(TIME);
      mmAuxTime.setExpression(expression2);
      mmClass.put(mmAuxTime);

      MetaModelClassAuxiliary mmAuxStep = new DefaultMetaModelClassAuxiliary();
      mmAuxStep.setName(cteStepName);
      Expression expression3 = new DefaultNameExpression(STEP);
      mmAuxStep.setExpression(expression3);
      mmClass.put(mmAuxStep);

      assertEquals(3, mmClass.size());
      assertTrue(mmClass.containsKey(cteTimeName));
      assertTrue(mmClass.containsKey(cteDtName));
      assertTrue(mmClass.containsKey(cteStepName));

      metaModel.put(mmClass);

      MetaModelInstance mmInstance = new DefaultMetaModelInstance();
      mmInstance.setMetaModel(metaModel);
      mmInstance.addNewClassInstance("TestInstance", "TestClass");

      MetaModelInstanceSimulation simulator = new DefaultMetaModelInstanceSimulation();
      JynaSimulationProfile profile = new DefaultSimulationProfile();
      profile.setTimeLimits(0.0, 100.0, 0.5);
      simulator.setProfile(profile);
      MetaModelInstanceSimulationMethod method = new DefaultMetaModelInstanceEulerMethod();
      simulator.setMethod(method);
      simulator.setModel(mmInstance);
      JynaSimulationData data = new DefaultSimulationData();
      simulator.setSimulationData(data);
      data.add((JynaValued) mmInstance.getClassInstances().get("TestInstance").get(cteDtName));
      data.add((JynaValued) mmInstance.getClassInstances().get("TestInstance").get(cteTimeName));
      data.add((JynaValued) mmInstance.getClassInstances().get("TestInstance").get(cteStepName));

      assertEquals(3, (long) data.getWatchedCount());

      simulator.reset();

      simulator.register();
      assertEquals(0.0, (Double)     data.getTime(0), 0.001);
      assertEquals(0.5, (Double) data.getValue(0, 0), 0.001);
      assertEquals(0.0, (Double) data.getValue(1, 0), 0.001);
      assertEquals(0.0, (Double) data.getValue(2, 0), 0.001);

      simulator.step();
      simulator.register();

      assertEquals(0.5,     (Double) data.getTime(1), 0.001);
      assertEquals(0.5, (Double) data.getValue(0, 1), 0.001);
      assertEquals(0.5, (Double) data.getValue(1, 1), 0.001);
      assertEquals(1.0, (Double) data.getValue(2, 1), 0.001);

      simulator.step();
      simulator.register();

      assertEquals(1.0, (Double)     data.getTime(2), 0.001);
      assertEquals(0.5, (Double) data.getValue(0, 2), 0.001);
      assertEquals(1.0, (Double) data.getValue(1, 2), 0.001);
      assertEquals(2.0, (Double) data.getValue(2, 2), 0.001);
   }
}
