package org.example;

import org.apache.jmeter.gui.JMeterGUIComponent;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.collections.HashTree;
import us.abstracta.jmeter.javadsl.core.BuildTreeContext;
import us.abstracta.jmeter.javadsl.core.engines.JmeterEnvironment;
import us.abstracta.jmeter.javadsl.core.listeners.DslViewResultsTree;
import us.abstracta.jmeter.javadsl.core.listeners.DslVisualizer;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ResultsConsoleVisualizer extends DslVisualizer {

    public static ResultsConsoleVisualizer consoleVisualizer() {
        return new ResultsConsoleVisualizer("name", SampleDataLogger.class);
    }

    public ResultsConsoleVisualizer(String name, Class<? extends JMeterGUIComponent> guiClass) {
        super(name, guiClass);
    }

    @Override
    protected TestElement buildTestElement() {
        return new ResultCollector();
    }

    @Override
    public HashTree buildTreeUnder(HashTree parent, BuildTreeContext context) {
        TestElement testElement = buildConfiguredTestElement();
        HashTree ret = parent.add(testElement);
        context.addVisualizer(this, () -> buildTestElementGui(testElement));
        return ret;
    }
}
