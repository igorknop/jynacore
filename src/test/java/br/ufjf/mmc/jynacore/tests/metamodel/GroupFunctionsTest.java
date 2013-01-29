package br.ufjf.mmc.jynacore.tests.metamodel;

import br.ufjf.mmc.jynacore.JynaSimulationData;
import br.ufjf.mmc.jynacore.JynaSimulationProfile;
import br.ufjf.mmc.jynacore.JynaValued;
import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.NumberOperator;
import br.ufjf.mmc.jynacore.expression.impl.DefaultExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultNameExpression;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationData;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationProfile;
import br.ufjf.mmc.jynacore.metamodel.MetaModel;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassAuxiliary;
import br.ufjf.mmc.jynacore.metamodel.MetaModelClassProperty;
import br.ufjf.mmc.jynacore.metamodel.MetaModelMultiRelation;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModel;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelClass;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelClassAuxiliary;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelClassProperty;
import br.ufjf.mmc.jynacore.metamodel.impl.DefaultMetaModelMultiRelation;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstance;
import br.ufjf.mmc.jynacore.metamodel.instance.ClassInstanceProperty;
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
public class GroupFunctionsTest {
   public static final String GROUP_SUM_AUX = "GroupSumAux";
   public static final String GROUP_CLASS = "Group";
   public static final String AUX_GROUP_SUM = "GroupSumAux";
   public static final String MULTIPLE_RELATION_NAME = "Link";
   public static final String PROP_NAME = "Value";
   public static final String SINGLE_CLASS = "Single";
   
   public GroupFunctionsTest() {
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
    @Test
    public void GroupSumTest() throws Exception{
      MetaModel metaModel = new DefaultMetaModel();
      metaModel.setName("groupsum test model");
      
      MetaModelClass mmGroupClass = new DefaultMetaModelClass();
      mmGroupClass.setName(GROUP_CLASS);
      MetaModelClassAuxiliary mmGroupSumAux = new DefaultMetaModelClassAuxiliary();
      mmGroupSumAux.setName(AUX_GROUP_SUM);
      Expression expression = new DefaultExpression(NumberOperator.GROUPSUM);
      expression.setLeftOperand(new DefaultNameExpression(MULTIPLE_RELATION_NAME));
      expression.setRightOperand(new DefaultNameExpression(PROP_NAME));
      mmGroupSumAux.setExpression(expression);
      mmGroupClass.put(mmGroupSumAux);
      metaModel.put(mmGroupClass);

      MetaModelClass mmSingleClass = new DefaultMetaModelClass();
      mmSingleClass.setName(SINGLE_CLASS);
      MetaModelClassProperty mmValue = new DefaultMetaModelClassProperty();
      mmValue.setName(PROP_NAME);
      mmValue.setDefaultValue(1.0);
      mmSingleClass.put(mmValue);
      metaModel.put(mmSingleClass);


      MetaModelMultiRelation mmmr = new DefaultMetaModelMultiRelation();
      mmmr.setName(MULTIPLE_RELATION_NAME);
      mmmr.setSource(mmGroupClass);
      mmmr.setTarget(mmSingleClass);
      metaModel.put(mmmr);
      
      assertEquals(3, metaModel.size());
      assertEquals(1, mmSingleClass.size());
      assertEquals(1, mmGroupClass.size());
      assertTrue(metaModel.containsKey("Group"));
      assertTrue(metaModel.containsKey("Single"));
      assertTrue(metaModel.containsKey("Link"));


      MetaModelInstance mmInstance = new DefaultMetaModelInstance();
      mmInstance.setMetaModel(metaModel);
      mmInstance.addNewClassInstance("TestInstance", "Group");
      mmInstance.addNewClassInstance("A", "Single");
      mmInstance.addNewClassInstance("B", "Single");
      mmInstance.addNewClassInstance("C", "Single");
      ClassInstance ciGroup = mmInstance.getClassInstances().get("TestInstance");
      ciGroup.setLink("Link", "A");
      ciGroup.setLink("Link", "B");
      ciGroup.setLink("Link", "C");
      
      MetaModelInstanceSimulation simulator = new DefaultMetaModelInstanceSimulation();
      JynaSimulationProfile profile = new DefaultSimulationProfile();
      profile.setTimeLimits(0.0, 100.0, 0.5);
      simulator.setProfile(profile);
      MetaModelInstanceSimulationMethod method = new DefaultMetaModelInstanceEulerMethod();
      simulator.setMethod(method);
      simulator.setModel(mmInstance);
      JynaSimulationData data = new DefaultSimulationData();
      simulator.setSimulationData(data);
      data.add((JynaValued) mmInstance.getClassInstances().get("TestInstance").get(GROUP_SUM_AUX));

      assertEquals(1, (long) data.getWatchedCount());

      simulator.reset();

      simulator.register();
      assertEquals(0.0, (Double)     data.getTime(0), 0.001);
      assertEquals(3.0, (Double) data.getValue(0, 0), 0.001);

      ((ClassInstanceProperty) mmInstance.getClassInstances().get("A").get("Value")).setValue(2.0);
      simulator.step();
      simulator.register();

      assertEquals(0.5,     (Double) data.getTime(1), 0.001);
      assertEquals(4, (Double) data.getValue(0, 1), 0.001);

      ((ClassInstanceProperty) mmInstance.getClassInstances().get("B").get("Value")).setValue(3.5);

      simulator.step();
      simulator.register();

      assertEquals(1.0, (Double)     data.getTime(2), 0.001);
      assertEquals(6.5, (Double) data.getValue(0, 2), 0.001);

    }
}
